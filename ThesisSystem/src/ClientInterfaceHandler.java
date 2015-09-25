
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
        System.out.println("Method2: "+req);
        Scanner sc = new Scanner(t.getRequestBody());
        StringBuilder sb = new StringBuilder();
        while(sc.hasNextLine()) sb.append(sc.nextLine());
        System.out.println(sb);
        int resp = intrface.compFactory.create(sb.toString());
        String response = "ERROR";
        if(resp==1) response = "Account Created";
        else if(resp==2) response = "Contribution Created";
        else if(resp==3) response = "Evaluation Created";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
        System.out.println(response);
    }
}
