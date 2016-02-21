import com.google.gson.Gson;
import java.io.*;
import java.net.*;

public class ClientTest{
    private final String serverURL;
    Configuration config;
    URL obj;
    
    public static void main(String[] args) throws Exception{
        String key = new ClientTest("http://localhost:8000/create").postConfig();
        System.out.println(key);
        ClientTest test = new ClientTest("http://localhost:8000/"+key);
        Gson gson = new Gson();
        AccountTest a1 = new AccountTest("migee");
        AccountTest a2 = new AccountTest("test");
        int accID1 = Integer.parseInt(test.post(gson.toJson(a1)));
        int accID2 = Integer.parseInt(test.post(gson.toJson(a2)));
        System.out.println("Account 1 Creation: "+accID1);
        System.out.println("Account 2 Creation: "+accID2);
        ContributionTest c1 = new ContributionTest(accID1,0.5,2);
        int conID = Integer.parseInt(test.post(gson.toJson(c1)));
        System.out.println("Contribution Creation: "+conID);
        EvaluationTest e1 = new EvaluationTest(accID2,conID,1.0);
        int evalID = Integer.parseInt(test.post(gson.toJson(e1)));
        System.out.println("Evaluation Creation: "+evalID);
    }

    public ClientTest(String url) throws Exception{
        serverURL = url;
        obj = new URL(url);
        // config = new Configuration("testClient","PageRank",10,2.5,5,1.0,1.0,1.0,1.0);
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
    //}
    
    public String postConfig() throws Exception{
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        Gson gson = new Gson();
        wr.writeBytes(gson.toJson(config));
        wr.flush();
        wr.close();
        int responseCode = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        connection.disconnect();
        return response.toString();
    }

    public String post(String message) throws Exception{
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        // String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        // wr.writeBytes(urlParameters);
        wr.writeBytes(message);
        wr.flush();
        wr.close();
         System.out.println("\nSending 'POST' request to URL : " + serverURL);
         System.out.println("Post parameters : " + message);
        // System.out.println("Response Code : " + responseCode);
        int responseCode = connection.getResponseCode();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        connection.disconnect();
        return response.toString();
    }
}