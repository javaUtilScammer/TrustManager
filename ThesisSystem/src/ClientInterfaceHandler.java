
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

/**
 *
 * @author Migee
 */
public class ClientInterfaceHandler implements HttpHandler {
    
    ClientInterface intrface;
    
    public ClientInterfaceHandler(ClientInterface ci){
        intrface = ci;
    }
    
    public void handle(HttpExchange t) throws IOException {
        String req = t.getRequestMethod();
        System.out.println("Method: "+req);
        Scanner sc = new Scanner(t.getRequestBody());
        StringBuilder sb = new StringBuilder();
        while(sc.hasNextLine()) sb.append(sc.nextLine());
        System.out.println(sb);
        String[] tokens = sb.toString().split(" ");
        int resp = intrface.compFactory.create(sb.toString());
        // if(tokens[0].equals("create")){
        //     int resp = intrface.compFactory.create(sb.toString());
        // }
        // else if(tokens[0].equals("accept")){
                // intrface.acceptContribution(Integer.parseInt(tokens[1]));
        // }
        // else if(tokens[0].equals("get")){
                // intrface.getTopContibutions(Integer.parseInt(tokens[1]));
        // }
        String response = "";
        if(resp==-1) response = "ERROR";
        else response = resp+"";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
        // System.out.println("Index: "+response);
    }
}
