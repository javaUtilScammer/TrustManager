import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/*
*   Server program for Trust Manager.
*   Start this program first.
*   @author Migee
*/

public class Server {
    
    private ArrayList<ClientInterface> clients;
    HttpServer httpserver;
    private String username, password;
    private String url = "jdbc:mysql://localhost/clientservers";
    ClientConfigurator config;
    
    /*
        Calls the constructor with a default username and password combination ("root", "root").
    */
    public static void main(String[] args) throws Exception {
        new Server("root", "root");
    }
    
    /*
        @param user the username 
    */
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
    
    /*
        The reloadDB method reloads the client information into memory in case of a server reboot.
    */
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
                double degree_of_strictness = rs.getDouble(8);
                double beta_factor = rs.getDouble(9);
                double active_user_time = rs.getDouble(10);
                double active_evaluation_time = rs.getDouble(11); 
		
		Configuration conf = new Configuration(client_name, validation_type, validation_time, default_score, rating_scale, degree_of_strictness, beta_factor, active_user_time, active_evaluation_time);
		ClientInterface inf = new ClientInterface(client_key, conf, url, this, false); 
		clients.add(inf);
		inf.loadDB(); 
	    }
	}catch(Exception e)
	{
	    e.printStackTrace();
	}
	
    }
    
    /*
        @param cf adds this ClientInterface object to the server
    */
    public void integrateClientInterface(ClientInterface cf){
        clients.add(cf);
    }
    
    /*
        @return the username for the mysql account in the server machine
    */
    public String getUsername(){
        return username;
    }
    
    /*
        @return the username for the mysql account in the server machine
    */
    public String getPassword(){
        return password;
    }
}