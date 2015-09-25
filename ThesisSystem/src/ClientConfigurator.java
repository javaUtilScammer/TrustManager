
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Migee
 */
public class ClientConfigurator implements HttpHandler {
    
    Gson gson;
    Connection conn;
    Server server;
    
    public ClientConfigurator(Server server, String username, String password){
        this.server = server;
        gson = new Gson();
        try {
            String url = "jdbc:mysql://localhost/?user="+username+"&password="+password;
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) { e.printStackTrace();}
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
        ClientInterface clientInt = new ClientInterface(config);
        String response = "Response from server";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
        System.out.println(response);
    }
}
