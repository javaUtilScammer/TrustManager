import java.util.*;
import java.io.*;
import java.net.*;

public class ClientTest{
	private String serverURL;
	HttpURLConnection connection;

	public static void main(String[] args) throws Exception{
		System.out.println(new ClientTest("http://localhost:8000/create").post("lol"));
	}

	public ClientTest(String url) throws Exception{
		serverURL = url;
		URL obj = new URL(url);
		connection = (HttpURLConnection) obj.openConnection();
	}

	// public String get() throws Exception{
	// 	connection.setRequestMethod("GET");
	// 	BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	// 	String line;
	// 	StringBuilder result = new StringBuilder();
	// 	while ((line = rd.readLine()) != null) {
	// 		result.append(line);
	// 	}
	// 	rd.close();
	// 	return result.toString();
	// }

	public String post(String message) throws Exception{
		connection.setRequestMethod("POST");
		// String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
	    connection.setDoOutput(true);
	    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
	    // wr.writeBytes(urlParameters);
	    wr.writeBytes(message);
	    wr.flush();
	    wr.close();
	    int responseCode = connection.getResponseCode();
	    // System.out.println("\nSending 'POST' request to URL : " + url);
	    // System.out.println("Post parameters : " + urlParameters);
	    // System.out.println("Response Code : " + responseCode);
	    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();
	    while ((inputLine = in.readLine()) != null) {
	        response.append(inputLine);
	    }
	    in.close();
	    return response.toString();
	}
}