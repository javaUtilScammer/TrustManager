
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *  ClientConfigurator is the class that handles client creation requests for the server.
 *  It is created by the server and is bound to localhost:8000/create
 *  @author Migee
 */
public class ClientConfigurator implements HttpHandler {
    
    private Gson gson;
    private Connection conn;
    private Server server;
    private String root = "jdbc:mysql://localhost/";
    private String url = "jdbc:mysql://localhost/clientservers";
    
    /*
        @param server a reference to the server object
        @param username the username string for the mysql account on the machine
        @param password the password string for the mysql account on the machine
    */
    public ClientConfigurator(Server server, String username, String password){
        this.server = server;
        gson = new Gson();
        try {
            conn = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) { e.printStackTrace();}
    }
    
    /*
        @return returns the Connection object used by the ClientConfigurator
    */
    public Connection getConnection()
    {
	   return conn; 
    }
    
    /*
        Handles the requests sent by HTTP Post.
    */
    public void handle(HttpExchange t) throws IOException {
        String req = t.getRequestMethod();
        // System.out.println("Method: "+req);
        System.out.println("Incoming create client request");
        Scanner sc = new Scanner(t.getRequestBody());
        StringBuilder sb = new StringBuilder();
        while(sc.hasNextLine()) sb.append(sc.nextLine());
        System.out.println(sb);
        Configuration config = new Gson().fromJson(sb.toString(),Configuration.class);
        // System.out.println("test: "+config.getClientName());
        DatabaseCreator dbc = new DatabaseCreator(config,conn);
        String key = dbc.createDatabase();
        ClientInterface clientInt = new ClientInterface(key,config,root+config.getClientName(),server, false, config.getURL());
        Connection connector = clientInt.pool.getConnection();
        dbc.createTables(connector);
        clientInt.pool.returnConnection((connector));
        server.integrateClientInterface(clientInt);
        t.sendResponseHeaders(200, key.length());
        OutputStream os = t.getResponseBody();
        os.write(key.getBytes());
        os.close();
        System.out.println("Created client with key: "+key);
    }
}
