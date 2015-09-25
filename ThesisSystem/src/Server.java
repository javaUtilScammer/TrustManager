/*
* Server program for Trust Manager
*/

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class Server {
    
    private ArrayList<ClientInterface> clients;
    HttpServer httpserver;
    private String username, password;
    private String url = "jdbc:mysql://localhost/clientservers";
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
    
    public void reloadDB()
    {
	try{
	    Connection conn = config.getConnection(); 
	    Statement st = conn.createStatement();
	    ResultSet rs = st.executeQuery("SELECT * FROM Clients;");
	    while(rs.next())
	    {
		int client_id = rs.getInt(1);
		String client_name = rs.getString(2);
		String client_key = rs.getString(3);
		String validation_type = rs.getString(4); 
		int validation_time = rs.getInt(5);
		double default_score = rs.getDouble(6);
		double rating_scale = rs.getDouble(7); 
		
		Configuration conf = new Configuration(client_name, validation_type, validation_time, default_score, rating_scale);
		ClientInterface inf = new ClientInterface(client_key, conf, url, this ); 
		clients.add(inf);
		inf.loadDB(); 
	    }
	}catch(Exception e)
	{
	    e.printStackTrace();
	}
	
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