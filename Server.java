import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.*;

public class Server {

  public static void main(String[] args) throws Exception {
    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
    server.createContext("/test", new HttpGetHandler());
    server.setExecutor(null); // creates a default executor
    server.start();
    System.out.println("Server started");
  }

  static class HttpGetHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
      String req = t.getRequestMethod();
      Scanner sc = new Scanner(t.getRequestBody());
      while(sc.hasNextLine()){
        System.out.println(sc.nextLine());
      }
      System.out.println("Input Read");
      String response = "hellooooo";
      t.sendResponseHeaders(200, response.length());
      OutputStream os = t.getResponseBody();
      os.write(response.getBytes());
      os.close();
      System.out.println(response);
    }
  }
}