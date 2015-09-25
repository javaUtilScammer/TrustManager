/*
* Server program for Trust Manager
*/

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    
    ArrayList<ClientInterface> clients;
    HttpServer server;
    
    public static void main(String[] args) throws Exception {
        new Server("root", "root");
    }
    
    public Server(String username, String password){
        clients = new ArrayList<ClientInterface>();
        try{
             server = HttpServer.create(new InetSocketAddress(8000), 0);
        }
        catch(IOException e){ e.printStackTrace();}
        
        server.createContext("/create", new ClientConfigurator(this,username,password));
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started");
    }
}