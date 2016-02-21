/**
 * Main class for testing and results gathering.
 * @author Migee
 */
import java.io.*;
import java.util.*;
import java.net.*;
import com.google.gson.Gson;
import com.sun.net.httpserver.*;

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
	private HttpServer httpserver;
	private ArrayList<AccountJSON> accounts;
	// private ArrayList<ContributionJSON> contributions;
	// private ArrayList<EvaluationJSON> evaluations;
	private HashMap<Integer, ContributionJSON> contMap;
	// private HashMap<Integer, EvaluationJSON> evalMap;
	private StringBuilder summary;
	private long sysTime, interval;

	public static void main(String[] args) throws FileNotFoundException, IOException{
		new SystemTester("tester.in");
	}

    public SystemTester(String file) throws FileNotFoundException, IOException{
		in = new BufferedReader(new FileReader(file));
		gson = new Gson();
		accounts = new ArrayList<AccountJSON>();
		accounts.add(new AccountJSON(0+"",0,0,0,0));
		contMap = new HashMap<Integer, ContributionJSON>();
		summary = new StringBuilder();
		try{
             httpserver = HttpServer.create(new InetSocketAddress(8002), 0);
        }
        catch(IOException e){ e.printStackTrace();}
        httpserver.createContext("/reject", new ClientSideHandler(this));
        httpserver.setExecutor(null); // creates a default executor
        httpserver.start();
        System.out.println("ClientSideServer started");
		try{
			readConfig();
			postConfig();
        	readInput();
		}
		catch(IOException e){}
		int[] times = {60000,120000,180000};
		int index = 0;
		sysTime = System.currentTimeMillis();
		long startTime = sysTime;
		while(index!=times.length){
			long curTime = System.currentTimeMillis();
			if((curTime-sysTime)<interval){
				continue;
			}
			simulate();
			if((curTime-startTime)>=times[index]){
				printSummary();
				postPrint();
				index++;
			}
			sysTime = curTime;
		}
		printSummary();
		postPrint();
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
            Double.parseDouble(default_score), Double.parseDouble(rating_scale), Double.parseDouble(degree_strict),
            Double.parseDouble(beta_factor), Double.parseDouble(active_time), Double.parseDouble(eval_time),"http://localhost:8002/reject");
    	interval = Long.parseLong(in.readLine());
    	in.readLine();
    	if(logging){
    		StringBuilder log = new StringBuilder();
    		log.append("Configuration Details:\n");
    		log.append(clientName+", "+valid_type+", "+valid_time+"\n");
    		log.append(default_score+", "+rating_scale+", "+degree_strict+"\n");
    		log.append(beta_factor+", "+active_time+", "+eval_time+"\n");
    		log.append(interval+"\n");
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

    private int randInRange(int min, int max){
    	return (int)(Math.random() * (max - min) + min);
    }

    private void readInput() throws IOException{
    	int good = Integer.parseInt(in.readLine());
    	int bad = Integer.parseInt(in.readLine());
    	int neut = Integer.parseInt(in.readLine());
    	summary.append(good+"\n");
    	summary.append(bad+"\n");
    	summary.append(neut+"\n");
    	summary.append(degree_strict+"\n");
    	summary.append(beta_factor+"\n");
    	for(int i=0; i<good; i++){
    		int chance = randInRange(80,100);
    		int con = randInRange(20, 25);
    		int eva = randInRange(60, 70);
    		AccountJSON acc = new AccountJSON(accounts.size()+"", con, eva, chance, chance);
			String temp = post(gson.toJson(acc));
			System.out.println("Account "+acc.username+" created, ID: "+temp);
			accounts.add(acc);
    	}
    	for(int i=0; i<bad; i++){
    		int chance = randInRange(10,30);
    		int con = randInRange(20, 25);
    		int eva = randInRange(60, 70);
    		AccountJSON acc = new AccountJSON(accounts.size()+"", con, eva, chance, chance);
			String temp = post(gson.toJson(acc));
			System.out.println("Account "+acc.username+" created, ID: "+temp);
			accounts.add(acc);
		}
		for(int i=0; i<neut; i++){
    		int chance = randInRange(40,60);
    		int con = randInRange(20, 25);
    		int eva = randInRange(60, 70);
    		AccountJSON acc = new AccountJSON(accounts.size()+"", con, eva, chance, chance);
			String temp = post(gson.toJson(acc));
			System.out.println("Account "+acc.username+" created, ID: "+temp);
			accounts.add(acc);
		}
    }

    public void simulate() throws IOException{
    	for(int i=1; i<accounts.size(); i++){
    		// try {
      //           Thread.sleep(1000);
      //       } catch (InterruptedException ex) {
      //       }
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
    			// contributions.add(cont);
    			cont.id = Integer.parseInt(post(gson.toJson(cont)));
    			acc.contributions.add(cont.id);
    			if(logging) System.out.println("Contribution created "+ cont.id+" "+correct);
    			summary.append(cont.id+" "+correct+"\n");
    			contMap.put(cont.id, cont);
    		}
    		else{ //making an evaluation
    			int n = accounts.size();
    			int id = pickRandomID(n);
    			while(id==i) id = pickRandomID(n);
    			EvaluationJSON eval = null;
    			AccountJSON acc2 = accounts.get(id);
    			int nums = acc2.contributions.size();
    			if(nums==0) continue;
    			int cont_id = acc2.contributions.get(pickRandomID(nums));
    			while(acc.sent.contains(cont_id)){
    				try{
    					cont_id = acc2.contributions.get(pickRandomID(nums));
    				} catch( Exception e){e.printStackTrace(); continue;}
    			}
    			ContributionJSON cont = contMap.get(cont_id);
    			boolean correct = RNG(acc.chance_Correct_Eval);
    			double score = 1.0;
    			if(correct^cont.correct) score = 0.0;
    			eval = new EvaluationJSON(i, cont_id, score);
    			eval.id = Integer.parseInt(post(gson.toJson(eval)));
    			acc.sent.add(cont_id);
    			if(logging) System.out.println("Evaluation created "+eval.id+" "+correct);
    		}
    	}
    }

    public void removeContribution(int id){
    	try{
	    	ContributionJSON  cont = contMap.get(id);
	    	AccountJSON acc = accounts.get(cont.account_id);
	    	acc.contributions.remove((Integer)id);
	    	contMap.remove(id);
	    	System.out.println("Received reject "+id);
    	}
    	catch(Exception e){e.printStackTrace();}
    }

    public void printSummary(){
    	try{
	    	PrintWriter out = new PrintWriter(new FileWriter("clientLog.txt",false));
	    	out.print(summary);
	    	// out.println("******");
	    	out.flush();		
    	}
    	catch(Exception e){e.printStackTrace();}
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
	int chance_Cont;
	// int id;
	HashSet<Integer> sent;
	ArrayList<Integer> contributions;
	// double trust_rating;

	public AccountJSON(String n, int cc, int ce, int ccc, int cce){
		username = n;
		chance_Contribute = cc;
		chance_Evaluate = ce;
		chance_Correct_Cont = ccc;
		chance_Correct_Eval = cce;
		chance_Cont = chance_Contribute+chance_Evaluate;
		contributions = new ArrayList<Integer>();
		sent = new HashSet<Integer>();
		type = "Account";
	}

	public int move(){
		int roll = (int) (Math.random()*100)+1;
		int ret = 0;
		if(roll<=chance_Evaluate) ret = 1; //make a contribution
		else if(roll<=chance_Cont) ret = 2; //make an evaluation
		// System.out.println(roll+" "+chance_Eval+" "+ret);
		return ret; //0 = do nothing
	}
}

class ContributionJSON extends ComponentJSON{
	int account_id, state;
	int id;
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
	int id;

	public EvaluationJSON(int a, int c, double tr){
		account_id = a;
		contribution_id = c;
		rating = tr;
		type = "Evaluation";
	}
}

class ClientSideHandler implements HttpHandler {
	SystemTester system;

	public ClientSideHandler(SystemTester s){
		system = s;
	}

	public void handle(HttpExchange t){
        try {
            String req = t.getRequestMethod();
            Scanner sc = new Scanner(t.getRequestBody());
            StringBuilder sb = new StringBuilder();
            while(sc.hasNextLine()) sb.append(sc.nextLine());
            String response = "";
            int resp = 0;
            system.removeContribution(Integer.parseInt(sb.toString()));
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            // System.out.println("Index: "+response);
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
    }
}