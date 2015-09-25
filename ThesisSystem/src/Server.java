/*
* Server program for Trust Manager
*/

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    
    private ArrayList<ClientInterface> clients;
    HttpServer server;
    private String username, password;
    
    public static void main(String[] args) throws Exception {
        new Server("root", "root");
    }
    
    public Server(String user, String pass){
        clients = new ArrayList<ClientInterface>();
        username = user;
        password = pass;
        try{
             server = HttpServer.create(new InetSocketAddress(8000), 0);
        }
        catch(IOException e){ e.printStackTrace();}
        
        server.createContext("/create", new ClientConfigurator(this,user,pass));
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started");
    }
    
    public void reloadDB()
    {
    }
    
    public void integrateClientInterface(ClientInterface cf){
        clients.add(cf);
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
}