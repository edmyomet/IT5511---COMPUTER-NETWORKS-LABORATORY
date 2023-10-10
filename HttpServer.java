import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class HttpServer {
    private static final int PORT = 8000;
    private static final String FILE_PATH = "index.html"; // Path to the text file
    private static final ExecutorService clientThreadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("HTTP server is listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());
                clientThreadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Read the HTTP request
                StringBuilder request = new StringBuilder();
                String line;
                while (!(line = in.readLine()).isEmpty()) {
                    request.append(line).append("\r\n");
                }

                System.out.println("Received HTTP request:\n" + request.toString());

                // Process the request and generate an HTTP response
                String response = processRequest(request.toString());

                // Send the HTTP response
                out.print(response);
                out.flush();

                // Close the client socket
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String processRequest(String request) {
            if (request.startsWith("GET")) {
                // Handle GET requests (retrieve text from the file)
                String fileContent = readFileContents(FILE_PATH);
                return "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n" + fileContent;
            } else if (request.startsWith("POST")) {
                // Handle POST requests (append text to the file)
                String postData = request.substring(request.lastIndexOf("\r\n\r\n")+4);
                boolean success = appendToFile(FILE_PATH, postData);
                if (success) {
                    return "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\nData appended to the file.";
                } else {
                    return "HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/plain\r\n\r\nError appending data.";
                }
            } else {
                // Unsupported HTTP method
                return "HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/plain\r\n\r\nMethod Not Allowed";
            }
        }

        private String readFileContents(String filePath) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();
                return content.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "Error reading file.";
            }
        }

        private boolean appendToFile(String filePath, String data) {
            try {
                FileWriter writer = new FileWriter(filePath, true);
                writer.write(data);
                writer.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}

