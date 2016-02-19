/**
 * Main class for testing and results gathering.
 * @author Migee
 */
import java.io.*;
import java.util.*;
import java.net.*;
import com.google.gson.Gson;

public class SystemTester{
	private BufferedReader in;
	private String serverURL, postURL;
	private String clientName, valid_type, valid_time;
	private String default_score, rating_scale, degree_strict;
	private String beta_factor, active_time, eval_time;
	private Configuration config;
	private Gson gson;
	private URL obj, objPost;
	private boolean logging = true;
	private ArrayList<AccountJSON> accounts;
	private ArrayList<ContributionJSON> contributions;
	private ArrayList<EvaluationJSON> evaluations;
	private long sysTime, interval;
	private int cycles;
	private StringBuilder summary;

	public static void main(String[] args) throws FileNotFoundException, IOException{
		new SystemTester("tester.in");
	}

    public SystemTester(String file) throws FileNotFoundException, IOException{
		// System.out.println(System.getProperty("user.dir"));
		in = new BufferedReader(new FileReader(file));
		gson = new Gson();
		accounts = new ArrayList<AccountJSON>();
		contributions = new ArrayList<ContributionJSON>();
		evaluations = new ArrayList<EvaluationJSON>();
		summary = new StringBuilder();
		sysTime = System.currentTimeMillis();
		try{
			readConfig();
			postConfig();
        	readInput();
		}
		catch(IOException e){}
		for(int i=1; i<=cycles; i++){
			long curTime = System.currentTimeMillis();
			if((curTime-sysTime)<interval){
				i--;
				continue;
			}
			sysTime = curTime;
			simulate();
		}
		printSummary();
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
    	config = new Configuration(clientName, valid_type, Integer.parseInt(valid_time), 
            Double.parseDouble(default_score), Integer.parseInt(rating_scale), Double.parseDouble(degree_strict),
            Double.parseDouble(beta_factor), Double.parseDouble(active_time), Double.parseDouble(eval_time));
    	interval = Long.parseLong(in.readLine());
    	cycles = Integer.parseInt(in.readLine());
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
    	System.out.println("test");
    }

    private void readInput() throws IOException{
    	int n = Integer.parseInt(in.readLine());
    	while(n-->0){
    		String[] input = in.readLine().split(" ");
    		if(input[0].equalsIgnoreCase("Account")){
    			int count = Integer.parseInt(input[1]);
    			double cc = Double.parseDouble(input[2]);
    			double ce = Double.parseDouble(input[3]);
    			double ccc = Double.parseDouble(input[4]);
    			double cce = Double.parseDouble(input[5]);
    			for(int i=0; i<count; i++){
	    			AccountJSON acc = new AccountJSON(accounts.size()+"", cc, ce, ccc, cce);
	    			String temp = post(gson.toJson(acc));
	    			System.out.println("Account "+acc.username+" created, ID: "+temp);
	    			accounts.add(acc);
    			}
    		}
    	}
    }

    public void simulate() throws IOException{
    	for(int i=0; i<accounts.size(); i++){
    		AccountJSON acc = accounts.get(i);
    		int roll = acc.move();
    		if(roll==0) continue;
    		else if(roll==1){ //making a contribution
    			ContributionJSON cont = null;
    			boolean correct = RNG(acc.chance_Correct_Cont);
    			if(correct)//will make a correct contribution
    				cont = new ContributionJSON(i, 0, 0.0, true);
    			else
    				cont = new ContributionJSON(i, 0, 0.0, false);
    			if(logging) System.out.println("Contribution created "+ contributions.size()+" "+correct);
    			summary.append(contributions.size()+" "+correct+"\n");
    			acc.contributions.add(contributions.size());
    			contributions.add(cont);
    			post(gson.toJson(cont));
    		}
    		else{ //making an evaluation
    			int n = accounts.size();
    			int id = pickRandomID(n);
    			while(id==i) id = pickRandomID(n);
    			EvaluationJSON eval = null;
    			int cont_id = pickRandomID(accounts.get(id).contributions.size());
    			boolean correct = RNG(acc.chance_Correct_Eval);
    			if(correct) //will make a "correct" evaluation
    				eval = new EvaluationJSON(i, cont_id, 5.0);
    				//also randomize the score
    			else
    				eval = new EvaluationJSON(i, cont_id, 0.0);
    			if(logging) System.out.println("Evaluation created "+evaluations.size()+" "+correct);
    			//score given is not yet randomized
    			//should we add evaluations arraylist per account?
    			evaluations.add(eval);
    			post(gson.toJson(eval));
    		}
    	}
    }

    public void printSummary(){
    	System.out.println(summary);
    }

    public int pickRandomID(int n){
    	return (int)(Math.random() * n);
    }

    public boolean RNG(int chance){
    	int roll = (int)(Math.random() * 100)+1;
    	if(chance<=roll) return true;
    	return false;
    }
    
    public String post(String message) throws ProtocolException, IOException{
        HttpURLConnection connection = (HttpURLConnection) objPost.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(message);
        wr.flush();
        wr.close();
		System.out.println("\nSending 'POST' request to URL : " + serverURL);
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

}

abstract class ComponentJSON{
	String type;
}

class AccountJSON extends ComponentJSON{
	String username;
	int chance_Contribute, chance_Evaluate, chance_Correct_Cont, chance_Correct_Eval;
	int chance_Eval;
	ArrayList<Integer> contributions;
	// double trust_rating;

	public AccountJSON(String n, double cc, double ce, double ccc, double cce){
		username = n;
		chance_Contribute = (int) (cc*100);
		chance_Evaluate = (int) (ce*100);
		chance_Correct_Cont = (int) (ccc*100);
		chance_Correct_Eval = (int) (cce*100);
		chance_Eval = chance_Contribute+chance_Evaluate;
		contributions = new ArrayList<Integer>();
		type = "Account";
	}

	public int move(){
		int roll = (int) (Math.random()*100)+1;
		int ret = 0;
		if(roll<=chance_Contribute) ret = 1; //make a contribution
		else if(roll<=chance_Eval) ret = 2; //make an evaluation
		return ret; //0 = do nothing
	}
}

class ContributionJSON extends ComponentJSON{
	int account_id, state;
	double score_validity;
	boolean correct;

	public ContributionJSON(int a, int s, double sv, boolean c){
		account_id = a;
		state = s;
		score_validity = sv;
		correct = c;
		type = "Contribution";
	}
}

class EvaluationJSON extends ComponentJSON{
	int account_id, contribution_id;
	double rating;

	public EvaluationJSON(int a, int c, double tr){
		account_id = a;
		contribution_id = c;
		rating = tr;
		type = "Evaluation";
	}
}