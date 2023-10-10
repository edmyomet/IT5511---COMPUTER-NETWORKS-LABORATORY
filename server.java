import java.io.*;
import java.net.*;
import java.nio.Buffer;

public class server{
    public static void main(String[] args){
        if(args.length < 1)
            return;
        int PORT = Integer.parseInt(args[0]);
        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port: "+ PORT);
            Socket socket = serverSocket.accept();
            System.out.println("Client connected from: "+socket.getInetAddress());
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            Thread clientThread = new Thread(()->{
                while(true){
                    try{
                        String message;
                        while((message = input.readLine()) != null){
                            System.out.println("Client says: "+message);
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                }

            });
            clientThread.start();

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while(true){
                userInput = consoleReader.readLine();
                output.println(userInput);
            }
        }catch(IOException e){
            System.out.println("Error with server: "+e.getMessage());
            e.printStackTrace();
        }
    }
}