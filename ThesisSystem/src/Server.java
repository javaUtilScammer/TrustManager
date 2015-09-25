/*
* Server program for Trust Manager
*/

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    
    private ArrayList<ClientInterface> clients;
    HttpServer httpserver;
    private String username, password;
    ClientConfigurator config;
    
    public static void main(String[] args) throws Exception {
        new Server("root", "root");
    }
    
    public Server(String user, String pass){
        clients = new ArrayList<ClientInterface>();
        username = user;
        password = pass;
        try{
             httpserver = HttpServer.create(new InetSocketAddress(8000), 0);
        }
        catch(IOException e){ e.printStackTrace();}
        
        config = new ClientConfigurator(this,user,pass);
        httpserver.createContext("/create", config);
        httpserver.setExecutor(null); // creates a default executor
        httpserver.start();
        System.out.println("Server started");
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