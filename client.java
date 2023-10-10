import java.io.*;
import java.net.*;

public class client{
    public static void main(String [] args){
        if(args.length <1)
            return;
        final String SERVER_IP = args[1];
        final int SERVER_PORT = Integer.parseInt(args[0]);

        try{
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to the server");
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            Thread serverThread = new Thread(() ->{
                try{
                    String message;
                    while((message = input.readLine()) != null){
                        System.out.println("Server: "+message );
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            });
            serverThread.start();
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while(true){
                userInput = consoleReader.readLine();
                output.println(userInput);
            }

        }catch(UnknownHostException e){
            System.out.println("Server id not valid: "+e.getMessage());
            e.printStackTrace();
        }

        catch(IOException e){
            System.out.println("Error in connection: "+e.getMessage());
            e.printStackTrace();
        }

    }
}