import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class httpServer {
    public static void main(String[] args) {
        final int PORT = 8000;

        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("HTTP server is listening on port " + PORT);

            while (true) {
                // Accept client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());

                // Read the HTTP request from the client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String request = in.readLine();
                System.out.println("Received HTTP request: " + request);

                // Extract the request method and requested file path
                String[] requestParts = request.split(" ");
                String method = requestParts[0];
                String filePath = requestParts[1].substring(1); // Remove the leading '/'

                if (method.equals("GET")) {
                    // Handle GET requests for files
                    handleGetRequest(filePath, clientSocket);
                } else if (method.equals("POST")) {
                    // Handle POST requests
                    handlePostRequest(clientSocket);
                } else {
                    // Unsupported HTTP method
                    sendMethodNotAllowedResponse(clientSocket);
                }

                // Close the client socket
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle GET requests for files
    private static void handleGetRequest(String filePath, Socket clientSocket) {
        try {
            // Create an output stream to send the HTTP response
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Check if the requested file exists
            File file = new File(filePath);
            if (file.exists() && !file.isDirectory()) {
                // Send an HTTP response header
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html");
                out.println("Content-Length: " + file.length());
                out.println();

                // Read and send the file content as the HTTP response
                BufferedReader fileReader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = fileReader.readLine()) != null) {
                    out.println(line);
                }
                fileReader.close();
            } else {
                // Send a 404 Not Found response if the file does not exist
                sendNotFoundResponse(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle POST requests
    private static void handlePostRequest(Socket clientSocket) {
        try {
            // Create an input stream to read the POST data from the client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Read the POST data
            StringBuilder postData = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                postData.append(line);
            }

            // Parse and process the POST data (in this example, we just print it)
            System.out.println("Received POST data:\n" + postData.toString());

            // Send an HTTP response indicating success
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();
            out.println("<html><body><h1>POST request received successfully</h1></body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Send a 404 Not Found response
    private static void sendNotFoundResponse(PrintWriter out) {
        out.println("HTTP/1.1 404 Not Found");
        out.println("Content-Type: text/html");
        out.println();
        out.println("<html><body><h1>404 Not Found</h1></body></html>");
    }

    // Send a 405 Method Not Allowed response
    private static void sendMethodNotAllowedResponse(Socket clientSocket) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("HTTP/1.1 405 Method Not Allowed");
            out.println("Content-Type: text/html");
            out.println();
            out.println("<html><body><h1>405 Method Not Allowed</h1></body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
