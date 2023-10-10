
import java.lang.Integer;
import java.util.ArrayList;
import java.io.*;
import java.net.*;

class ServerThread extends Thread{
    private Socket socket;
    private ArrayList<ServerThread> threads;
    private PrintWriter output;
    public ServerThread(Socket socket,ArrayList<ServerThread>threads){
        this.socket = socket;
        this.threads = threads;
    }

    @Override
    public void run(){
        try{
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream());
            while(true){
                String outputString = input.readLine();
                if(outputString.equals("exit"))
                    break;
                printToAllClients(outputString);
                System.out.println("Server received: "+ outputString);
            }


        }catch(Exception e){
            System.out.println("Exception has occured: "+e.getMessage());
            e.printStackTrace();
        }
    }
    private void printToAllClients(String outputString){
        for(ServerThread st: threads){
            st.output.println(outputString);
        }
    }
}
public class mcServer{
    public static void main(String[] args){
        ArrayList<ServerThread> threads = new ArrayList<>();
        try{
            final String SERVER_IP = args[0];
            final int PORT = Integer.parseInt(args[1]);
            ServerSocket serverSocket = new ServerSocket(PORT);
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket, threads);
            threads.add(serverThread);
            serverThread.start();


        }catch(Exception e){
            System.out.println("unhandled exception: "+e.getMessage());
            e.printStackTrace();
        }
    }
}