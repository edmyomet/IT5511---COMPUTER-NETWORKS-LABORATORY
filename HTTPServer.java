import java.io.*;
import java.net.*;
public class HTTPServer {
    public static void main(String args[]){
        if(args.length < 1)
            return;
        final String SERVER_IP = args[1];
        final int SERVER_PORT = Integer.parseInt(args[0]);
        try{
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server is active on port: "+SERVER_PORT);
            Socket socket = serverSocket.accept();

            System.out.println("Client accepted from: "+socket.getInetAddress());

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Thread clientThread = new Thread(()->{
                try{
                    String request = input.readLine();
                    System.out.println("Received Request = "+request);
                    String[] requestParts = request.split(" ");
                    String method = requestParts[0];
                    String filePath = requestParts[1].substring(1);

                    if(method.equals("GET")){
                        try{
                            PrintWriter output = new PrintWriter(socket.getOutputStream());
                            File file = new File(filePath);
                            if(file.exists() && !file.isDirectory()){
                                output.println("HTTP/1.1 200 OK");
                                output.println("Content-Type: text/html ");
                                output.println("Content-Length: "+file.length());

                                BufferedReader fileReader = new BufferedReader(new FileReader(file));
                                String line;
                                while((line = fileReader.readLine())!=null){
                                    output.println(line);
                                }
                                fileReader.close();
                            }

                        }catch(IOException E){
                            E.printStackTrace();
                        }
                    }
                }catch(IOException E) {
                    E.printStackTrace();
                }
            });
            clientThread.start();
        }catch(IOException E){
            System.out.println(E.getMessage());
            E.printStackTrace();
        }
    }
    public static void handleGetRequests(String filePath, Socket socket){
        try{
            PrintWriter output = new PrintWriter(socket.getOutputStream());
            File file = new File(filePath);
            if(file.exists() && !file.isDirectory()){
                output.println("HTTP/1.1 200 OK");
                output.println("Content-Type: text/html ");
                output.println("Content-Length: "+file.length());

                BufferedReader fileReader = new BufferedReader(new FileReader(file));
                String line;
                while((line = fileReader.readLine())!=null){
                    output.println(line);
                }
                fileReader.close();
            }

        }catch(IOException E){
            E.printStackTrace();
        }
    }
}
