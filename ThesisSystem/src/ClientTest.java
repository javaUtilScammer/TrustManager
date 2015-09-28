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
           String created_at = gson.toJson("1995-10-10 10:10:10");
           String json = "{\"username\":\"migee\",\"created_at\":";
           json+=created_at+",\"last_updated_at\":";
           String updated_at = gson.toJson("2015-09-26 12:23:10");
           json+=updated_at+",\"trust_rating\":\"3.0\",\"trust_validity\":\"2.5\",\"type\":\"account";
           json+="\"}";
           System.out.println("teeest");
           int accId = Integer.parseInt(test.post(json));
           created_at = gson.toJson("1995-02-29 01:02:03");
           json = "{\"username\":\"test\",\"created_at\":";
           json+=created_at+",\"last_updated_at\":";
           updated_at = gson.toJson("2015-09-27 04:05:06");
           json+=updated_at+",\"trust_rating\":\"4.0\",\"trust_validity\":\"1.5\",\"type\":\"account";
           json+="\"}";
           int accId2 = Integer.parseInt(test.post(json));
           System.out.println("Account create: "+accId2);
           json = "{\"account_id\":\""+accId+"\",\"contribution_score\":";
           json+= "\"2.0\",\"score_validity\":\"0.5\",\"created_at\":";
           created_at = gson.toJson("2015-09-28 01:02:03");
           json+= created_at+",\"state\":\"2\",\"type\":\"contribution\"}";
           int conId = Integer.parseInt(test.post(json));
           System.out.println("Contribution create: "+conId);
           json = "{\"rating\":\"1.0\",\"created_at\":";
           created_at = gson.toJson("2015-09-30 02:02:02");
           json+=created_at+",\"account_id\":\""+accId2+"\",";
           json+="\"contribution_id\":\""+conId+"\",\"type\":\"evaluation\"}";
           int evaId = Integer.parseInt(test.post(json));
           System.out.println("Evaluation create: "+evaId);
    }

    public ClientTest(String url) throws Exception{
            serverURL = url;
            obj = new URL(url);
            config = new Configuration("testClient","testValidation",10,50,5);
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
        // String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        // wr.writeBytes(urlParameters);
        Gson gson = new Gson();
        wr.writeBytes(gson.toJson(config));
        wr.flush();
        wr.close();
        int responseCode = connection.getResponseCode();
        // System.out.println("\nSending 'POST' request to URL : " + url);
        // System.out.println("Post parameters : " + urlParameters);
        // System.out.println("Response Code : " + responseCode);
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