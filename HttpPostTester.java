import java.util.*;
import java.io.*;
import java.net.*;

public class HttpPostTester{
	public static void main(String[] args) throws Exception{
		// String rawData = "id=10";
		// String type = "application/x-www-form-urlencoded";
		// String encodedData = URLEncoder.encode( rawData );
		// String urlToWrite = "http://localhost:8000/test";
		// URL u = new URL(urlToWrite);
		// HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		// conn.setDoOutput(true);
		// conn.setRequestMethod("POST");
		// conn.setRequestProperty( "Content-Type", type );
		// conn.setRequestProperty( "Content-Length", String.valueOf(encodedData.length()));
		// OutputStream os = conn.getOutputStream();
		// os.write(encodedData.getBytes()); // HTTP POST request
   
	    String url = "http://localhost:8000/test";
	    URL obj = new URL(url);
	    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	    //add reuqest header
	    con.setRequestMethod("POST");
	    con.setRequestProperty("User-Agent", "USER_AGENT");
	    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

	    String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

	    // Send post request
	    con.setDoOutput(true);
	    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	    String message = "huehuehue";
	    // wr.writeBytes(message);
	    wr.writeBytes(urlParameters);
	    wr.flush();
	    wr.close();

	    int responseCode = con.getResponseCode();
	    System.out.println("\nSending 'POST' request to URL : " + url);
	    System.out.println("Post parameters : " + urlParameters);
	    System.out.println("Response Code : " + responseCode);

	    BufferedReader in = new BufferedReader(
	            new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();

	    while ((inputLine = in.readLine()) != null) {
	        response.append(inputLine);
	    }
	    in.close();

	    //print result
	    System.out.println(response.toString());

	}
}