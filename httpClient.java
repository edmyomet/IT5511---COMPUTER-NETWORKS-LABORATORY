import java.io.*;
import java.net.*;

public class httpClient {
    public static void main(String[] args) {
        final String SERVER_IP = "127.0.0.1";
        final int SERVER_PORT = 8000;

        try {
            // Create a socket and connect to the server
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to the server");

            // Create an output stream to send HTTP requests
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Send a GET request for a specific file (e.g., "GET /index.html HTTP/1.1")
            String getRequest = "GET /index.html HTTP/1.1";
            out.println(getRequest);
            out.println();

            // Create an input stream to read the server's response
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }

            // Send a POST request with data
            String postData = "name=John&age=30";
            String postRequest = "POST / HTTP/1.1";
            out.println(postRequest);
            out.println("Content-Type: application/x-www-form-urlencoded");
            out.println("Content-Length: " + postData.length());
            out.println();
            out.println(postData);

            // Read and print the server's response to the POST request
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }

            // Close the socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
