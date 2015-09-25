
import com.google.gson.Gson;
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
    
    private Gson gson;
    ClientInterface intrface;
    int count = 0;
    
    public ClientInterfaceHandler(ClientInterface ci){
        gson = new Gson();
        intrface = ci;
    }
    
    public void handle(HttpExchange t) throws IOException {
        String req = t.getRequestMethod();
        System.out.println("handler object = " + this);
        System.out.println("called by thread = " + Thread.currentThread());
        System.out.println("Method2: "+req);
        Scanner sc = new Scanner(t.getRequestBody());
        StringBuilder sb = new StringBuilder();
        while(sc.hasNextLine()) sb.append(sc.nextLine());
        System.out.println(sb);
        System.out.println(count++);
        System.out.println("lol");
        int resp = intrface.compFactory.create(sb.toString());
        System.out.println("lol2");
        System.out.println("resp = "+resp);
        String response = "haha";
        if(resp==1) response = "Account Created";
        else if(resp==2) response = "Contribution Created";
        else if(resp==3) response = "Evaluation Created";
        else if(resp==-1) response = "ERROR";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
        System.out.println(response);
    }
}
