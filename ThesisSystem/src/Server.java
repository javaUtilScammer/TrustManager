/*
* Server program for Trust Manager
*/

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    
    ArrayList<ClientInterface> clients;
    HttpServer server;
    
    public static void main(String[] args) throws Exception {
        new Server();
    }
    
    public Server(){
        clients = new ArrayList<ClientInterface>();
        try{
             server = HttpServer.create(new InetSocketAddress(8000), 0);
        }
        catch(IOException e){ e.printStackTrace();}
        server.createContext("/create", new ClientConfig());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started");
    }
    
}

class ClientConfig implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
        String req = t.getRequestMethod();
        System.out.println("Method: "+req);
        Scanner sc = new Scanner(t.getRequestBody());
        StringBuilder sb = new StringBuilder();
        while(sc.hasNextLine()) sb.append(sc.nextLine());
        System.out.println(sb);
        Configuration config = new Gson().fromJson(sb.toString(),Configuration.class);
        System.out.println("test: "+config.getClientName());
        String response = "Response from server";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
        System.out.println(response);
    }
}