/**
 * Main class for testing and results gathering.
 * @author Migee
 */
import java.io.*;
import java.util.*;
import java.net.*;
// import java.lang.*;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemTester2{
	private BufferedReader in;
	private String serverURL, postURL;
	private String clientName, valid_type, valid_time;
	private String default_score, rating_scale, degree_strict;
	private String beta_factor, active_time, eval_time;
	private Configuration config;
	private Gson gson;
	private URL obj, objPost;
	private boolean logging = true;
	private ArrayList<AccountJSON2> accounts;
	private ArrayList<ContributionJSON2> contributions;
	private ArrayList<EvaluationJSON2> evaluations;
	private long sysTime, interval;

	public static void main(String[] args) throws FileNotFoundException, IOException{
		new SystemTester2("tester2.in");
	}

    public SystemTester2(String file) throws FileNotFoundException, IOException{
		// System.out.println(System.getProperty("user.dir"));
		in = new BufferedReader(new FileReader(file));
		gson = new Gson();
		accounts = new ArrayList<AccountJSON2>();
		contributions = new ArrayList<ContributionJSON2>();
		evaluations = new ArrayList<EvaluationJSON2>();
		sysTime = System.currentTimeMillis();
		try{
			readConfig();
			postConfig();
        	readInput();
            readScript();
            printSummary();
		}
		catch(IOException e){}
    }

    private void readConfig() throws IOException{
    	serverURL = in.readLine();
    	obj = new URL(serverURL);
    	clientName = in.readLine();
    	valid_type = in.readLine();
    	valid_time = in.readLine();
    	default_score = in.readLine();
    	rating_scale = in.readLine();
    	degree_strict = in.readLine();
    	beta_factor = in.readLine();
    	active_time = in.readLine();
    	eval_time = in.readLine();
    	// config = new Configuration(clientName, valid_type, Integer.parseInt(valid_time), 
            // Double.parseDouble(default_score), Integer.parseInt(rating_scale), Double.parseDouble(degree_strict),
            // Double.parseDouble(beta_factor), Double.parseDouble(active_time), Double.parseDouble(eval_time));
    	interval = Long.parseLong(in.readLine());
    	in.readLine();
    	if(logging){
    		StringBuilder log = new StringBuilder();
    		log.append("Configuration Details:\n");
    		log.append(clientName+", "+valid_type+", "+valid_time+"\n");
    		log.append(default_score+", "+rating_scale+", "+degree_strict+"\n");
    		log.append(beta_factor+", "+active_time+", "+eval_time+"\n");
    		System.out.print(log);
    	}
    }

    private void postConfig() throws ProtocolException, IOException{
    	HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(gson.toJson(config));
        wr.flush();
        wr.close();
        System.out.println("\nSending 'POST' request to URL : " + serverURL);
        int responseCode = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        connection.disconnect();
        postURL = response.toString();
    	objPost = new URL("http://localhost:8000/"+postURL);
    }

    private void readInput() throws IOException{
    	int n = Integer.parseInt(in.readLine());
    	while(n-->0){
    		String[] input = in.readLine().split(" ");
    		if(input[0].equalsIgnoreCase("Account")){
    			double cc = Double.parseDouble(input[1]);
    			double ce = Double.parseDouble(input[2]);
    			double ccc = Double.parseDouble(input[3]);
    			double cce = Double.parseDouble(input[4]);
    			AccountJSON2 acc = new AccountJSON2(accounts.size()+"", cc, ce, ccc, cce);
    			String temp = "";
                try{
                    temp = post(gson.toJson(acc));
                }
                catch(Exception e){e.printStackTrace();}
    			System.out.println("Account "+acc.username+" created, ID: "+temp);
    			accounts.add(acc);
    		}
    	}
    }

    private void readScript() throws IOException{
        int n = Integer.parseInt(in.readLine());
        for(int i=0; i<n; i++){
            String[] g = in.readLine().split(" ");
            String key = g[0];
            // System.out.println(i+" "+key);
            if(key.equals("cont")){
                int a_id = Integer.parseInt(g[1]);
                int state = Integer.parseInt(g[2]);
                double score = Double.parseDouble(g[3]);
                boolean b = g[4].equals("true");
                ContributionJSON2 cj = new ContributionJSON2(a_id,state,score, b);
                String temp = post(gson.toJson(cj));
                System.out.println("Contribution created "+temp);
            }
            else if(key.equals("eval")){
                int a_id = Integer.parseInt(g[1]);
                int c_id = Integer.parseInt(g[2]);
                double trust = Double.parseDouble(g[3]);
                EvaluationJSON2 ej = new EvaluationJSON2(a_id,c_id,trust);
                String temp = post(gson.toJson(ej));
                System.out.println("Evaluation created "+temp);
            }
            else if(key.equals("delay")){
                try {
                    Thread.sleep(Integer.parseInt(g[1])*1000);
                } catch (InterruptedException ex) {
                }
            }
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }
        postPrint();
    }

    public void printSummary(){

    }

    public String post(String message) throws ProtocolException, IOException{
        HttpURLConnection connection = (HttpURLConnection) objPost.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes("create "+message);
        wr.flush();
        wr.close();
		// System.out.println("\nSending 'POST' request to URL : " + serverURL);
		System.out.println("Post parameters : " + message);
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

    public String postPrint() throws ProtocolException, IOException{
        HttpURLConnection connection = (HttpURLConnection) objPost.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes("print");
        wr.flush();
        wr.close();
        // System.out.println("\nSending 'POST' request to URL : " + serverURL);
        // System.out.println("Post parameters : " + message);
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

class AccountJSON2 extends ComponentJSON{
	String username;
	int chance_Contribute, chance_Evaluate, chance_Correct_Cont, chance_Correct_Eval;
	int chance_Eval;
	ArrayList<Integer> contributions;
	// double trust_rating;

	public AccountJSON2(String n, double cc, double ce, double ccc, double cce){
		username = n;
		chance_Contribute = (int) (cc*100);
		chance_Evaluate = (int) (ce*100);
		chance_Correct_Cont = (int) (ccc*100);
		chance_Correct_Eval = (int) (cce*100);
		chance_Eval = chance_Contribute+chance_Evaluate;
		contributions = new ArrayList<Integer>();
		type = "Account";
	}
}

class ContributionJSON2 extends ComponentJSON{
	int account_id, state;
	double score_validity;
	boolean correct;

	public ContributionJSON2(int a, int s, double sv, boolean c){
		account_id = a;
		state = s;
		score_validity = sv;
		correct = c;
		type = "Contribution";
	}
}

class EvaluationJSON2 extends ComponentJSON{
	int account_id, contribution_id;
	double rating;

	public EvaluationJSON2(int a, int c, double tr){
		account_id = a;
		contribution_id = c;
		rating = tr;
		type = "Evaluation";
	}
}