
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Scanner;

/**
 *
 * @author Migee
 */
public class ClientInterfaceHandler implements HttpHandler {
    
    private Gson gson;
    private Connection conn;
    private Server server;
    private String url;
    
    public ClientInterfaceHandler(){
        
    }
    
    public void handle(HttpExchange t) throws IOException {
        String req = t.getRequestMethod();
        System.out.println("Method: "+req);
        Scanner sc = new Scanner(t.getRequestBody());
        StringBuilder sb = new StringBuilder();
        while(sc.hasNextLine()) sb.append(sc.nextLine());
        System.out.println(sb);
        String key = "";
        t.sendResponseHeaders(200, key.length());
        OutputStream os = t.getResponseBody();
        os.write(key.getBytes());
        os.close();
        System.out.println(key);
    }
}
