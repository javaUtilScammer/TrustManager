import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ClientInterfaceHandler receives HTTP Posts from clients and parses their requests. It then calls the appropriate method in the ClientInterface.
 * @author Migee
 */
public class ClientInterfaceHandler implements HttpHandler {
    
    ClientInterface intrface;
    
    public ClientInterfaceHandler(ClientInterface ci){
        intrface = ci;
    }
    
    public void handle(HttpExchange t){
        try {
            String req = t.getRequestMethod();
            Scanner sc = new Scanner(t.getRequestBody());
            StringBuilder sb = new StringBuilder();
            while(sc.hasNextLine()) sb.append(sc.nextLine());
            System.out.println(sb);
            String[] tokens = sb.toString().split(" ");
            String response = "";
            // int resp = intrface.compFactory.create(sb.toString());
            if(tokens[0].equals("create")){
                int resp = intrface.compFactory.create(tokens[1]);
                if(resp==-1) response = "ERROR";
                else response = resp+"";
            }
            else if(tokens[0].equals("accept")){
                intrface.acceptContribution(intrface.getContribution(Integer.parseInt(tokens[1])));
                response = "ok";
            }
            else if(tokens[0].equals("get")){
                response = intrface.getTopContributions(Integer.parseInt(tokens[1]));
            }
            else if(tokens[0].equals("print")){
                intrface.printSummary();
            }
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            // System.out.println("Index: "+response);
        } catch (IOException ex) {
            Logger.getLogger(ClientInterfaceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
