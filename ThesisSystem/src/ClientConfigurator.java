
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
 *
 * @author Migee
 */
public class ClientConfigurator implements HttpHandler {
    
    private Gson gson;
    private Connection conn;
    private Server server;
    private String root = "jdbc:mysql://localhost/";
    private String url = "jdbc:mysql://localhost/clientservers";
    
    public ClientConfigurator(Server server, String username, String password){
        this.server = server;
        gson = new Gson();
        try {
            conn = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) { e.printStackTrace();}
    }
    
    public Connection getConnection()
    {
	return conn; 
    }
    
    public void handle(HttpExchange t) throws IOException {
        String req = t.getRequestMethod();
        System.out.println("Method: "+req);
        Scanner sc = new Scanner(t.getRequestBody());
        StringBuilder sb = new StringBuilder();
        while(sc.hasNextLine()) sb.append(sc.nextLine());
        System.out.println(sb);
        Configuration config = new Gson().fromJson(sb.toString(),Configuration.class);
//        System.out.println("test: "+config.getClientName());
        DatabaseCreator dbc = new DatabaseCreator(config,conn);
        String key = dbc.createDatabase();
        ClientInterface clientInt = new ClientInterface(key,config,root+config.getClientName(),server);
        Connection connector = clientInt.pool.getConnection();
        dbc.createTables(connector);
        clientInt.pool.returnConnection((connector));
        t.sendResponseHeaders(200, key.length());
        OutputStream os = t.getResponseBody();
        os.write(key.getBytes());
        os.close();
        System.out.println(key);
    }
}
