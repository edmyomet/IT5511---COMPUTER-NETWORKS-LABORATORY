import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class HttpClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8000;

    public static void main(String[] args) {
        // Send a GET request
        String getRequest = "GET / HTTP/1.1\r\nHost: " + SERVER_IP + "\r\n\r\n";
        sendHttpRequest(getRequest);

        // Send a POST request with data
        String postData = "<h1>hello<h1>";
        String postRequest = buildPostRequest(postData);
        sendHttpRequest(postRequest);
    }

    private static void sendHttpRequest(String request) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send the HTTP request
            out.print(request);
            out.flush();

            // Read and print the HTTP response
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildPostRequest(String data) {
        String postRequest = "POST / HTTP/1.1\r\n" +
                "Host: " + SERVER_IP + "\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: " + data.length() + "\r\n\r\n" + data;

        return postRequest;
    }
}

