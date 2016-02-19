import java.sql.*;
import java.util.*;
/**
 * ClientInterface objects are in charge of handling the interactions between the server and the clients.
 * @author Migee
*/
public class ClientInterface {
    private final String client_name, validation_type, key;
    private final int validation_time;
    private final double default_score, rating_scale, score_validity, degree_of_strictness, beta_factor, active_user_time, 
        active_evaluation_time;
    final ConnectionPool pool;
    final ComponentFactory compFactory;
    private Scorer scorer;
    private final Server server;
    private HashMap<Integer,Account> accMap;
    private HashMap<Integer,Contribution> contMap;
    private HashMap<Integer,Evaluation> evalMap;
    private Validator valid;
    private HashSet<Account> active; 
    private Timer timer;
    private boolean intervalCheck;
    private int contributionTotal, contributionAccepted, contributionRejected;
    private StringBuilder summary;

    /*
        ClientInterface objects are created by the server; do not instantiate these manually!
        @param key randomized string created by server for the client
        @param config Configuration object containing the settings for the interface
        @param url the url of the database in the server machine
        @param server a reference to the server object
    */
    public ClientInterface(String key, Configuration config, String url, Server server, boolean ic){
        this.key = key;
        pool = new ConnectionPool(url,server.getUsername(),server.getPassword());
        client_name = config.getClientName();
        validation_type = config.getValidationType();
        validation_time = config.getValidationTime();
        default_score = config.getDefaultScore();
        degree_of_strictness = config.getDegreeOfStrictness(); 
        beta_factor = config.getBetaFactor();
        active_user_time = config.getActiveUserTime();
        active_evaluation_time = config.getActiveEvaluationTime(); 
        score_validity = default_score;
        rating_scale = config.getRatingScale();
        active = new HashSet<Account>(); 
        this.server = server;
        intervalCheck = ic;
        contributionTotal = 0;
        contributionAccepted = 0;
        contributionRejected = 0;
        accMap = new HashMap<Integer, Account>();
        contMap = new HashMap<Integer, Contribution>();
        evalMap = new HashMap<Integer, Evaluation>();
        server.httpserver.createContext("/"+key,new ClientInterfaceHandler(this));
        compFactory = new ComponentFactory(pool.getConnection(),this,intervalCheck);
        if(validation_type.equalsIgnoreCase("ad hoc")){
            scorer = new AdHocScorer(this);
            valid = new AdHocValidator(this);
        }
        else if(validation_type.equalsIgnoreCase("pagerank")){
            scorer = new PageRankScorer(this);
            valid = new PageRankValidator(this, rating_scale/2.0);
        }
        computeActive();
        timer = new Timer();
        summary = new StringBuilder();
    }
    
    /*
        The loadDB method loads all objects into memory from the DB in case of a server restart.
    */
    public void loadDB()
    {
	Connection conn = pool.getConnection(); 
	try{
	    Statement st = conn.createStatement(); 
	    
	    String sql = "SELECT * FROM Accounts"; 
	    ResultSet rs = st.executeQuery(sql); 
	    while(rs.next())
	    {
		int account_id = rs.getInt(1); 
		String username = rs.getString(2);
		Timestamp created_at = rs.getTimestamp(3);
		Timestamp last_updated_at = rs.getTimestamp(4); 
		double trust_rating = rs.getDouble(5);
		double trust_confidence = rs.getDouble(6);
                double cacc = rs.getDouble(7);
                double crej = rs.getDouble(7);
                double ctotal = rs.getDouble(7);
		
		Account acc = new Account(account_id, username, created_at, trust_rating, trust_confidence,cacc,crej,ctotal); 
		accMap.put(account_id, acc);
	    }
	    
	    st = conn.createStatement(); 
	    sql = "SELECT * FROM Contributions"; 
	    rs = st.executeQuery(sql); 
	    while(rs.next())
	    {
		int contribution_id = rs.getInt(1);
		int contributor_id = rs.getInt(2); 
		Account acc = accMap.get(contributor_id); 
		double contribution_score = rs.getDouble(3);
		double score_confidence = rs.getDouble(4);
		Timestamp created_at = rs.getTimestamp(5);
		int state = rs.getInt(6); 
		if(state!=0) //If state == 0 validation_phase - should load it
		    continue; 
		
                
		Contribution cont = new Contribution(contribution_id, acc, contribution_score, score_confidence, created_at, state);
                acc.getContributions().add(cont); 
		contMap.put(contribution_id,cont);
	    }
	    
	    st = conn.createStatement(); 
	    sql = "SELECT * FROM Evaluations"; 
	    rs = st.executeQuery(sql); 
	    while(rs.next())
	    {
		int evaluation_id = rs.getInt(1);
		double rating = rs.getDouble(2); 
		Timestamp created_at = rs.getTimestamp(3); 
		int created_by = rs.getInt(4);
		int contribution_id = rs.getInt(5); 
		Account evaluator = accMap.get(created_by);
		Contribution cont = contMap.get(contribution_id); 
		
		Evaluation eval = new Evaluation(evaluation_id, rating, created_at, evaluator, cont); 
                evaluator.getEvaluations().add(eval); 
		evalMap.put(evaluation_id, eval); 
	    }
	    pool.returnConnection(conn); 
	    conn = null; 
	}catch(Exception e)
	{
	    pool.returnConnection(conn); 
	    conn = null; 
	    e.printStackTrace(); 
	}
	
    }
    
    /*
        The computeActive method is used for the ad hoc method. It shows the percentage of users that have been actively recently.
    */
    public void computeActive()
    {
        active = new HashSet<Account>(); 
        for(Integer key: accMap.keySet())
        {
            Account acc = accMap.get(key); 
            Timestamp ts = acc.getUpdatedAt(); 
            long hold = ts.getTime(); 
            long currTime = System.currentTimeMillis(); 
            double diff = currTime - hold; 
            double check = diff / 3600000; 
            if(check<=active_user_time) active.add(acc); 
        }
    }
    
    /*

    */
    public int getActiveCount()
    {
        return active.size(); 
    }
    
    /*
        Returns a Connection object from the connetion pool.
    */
    public Connection getConnection()
    {
        return pool.getConnection(); 
    }
    
    /*
        Method used to return a connection object to the connection pool.
    */
    public void returnConnection(Connection conn)
    {
        pool.returnConnection(conn);
    }
    
    /*

    */
    public Account getAccount(int id){
        if(!accMap.containsKey(id)) return null;
        return accMap.get(id);
    }
    
    public Contribution getContribution(int id){
        if(!contMap.containsKey(id)) return null;
        return contMap.get(id);
    }
    
    public Evaluation getEvaluation(int id){
        if(!evalMap.containsKey(id)) return null;
        return evalMap.get(id);
    }
    
    public void putAccount(int id, Account ac){
        accMap.put(id, ac);
    }
    
    public void putContribution(int id, Contribution co){
        contMap.put(id, co);
        contributionTotal++;
    }
    
    public void putEvaluation(int id, Evaluation ev){
        evalMap.put(id, ev);
    }
    
    public void score(Evaluation ev, int ci){
        Contribution cont = contMap.get(ci); 
        if(cont==null || ev==null) return; 
        scorer.calculateScore(ev, cont);
    }

    public double getRatingScale(){
    	return rating_scale;
    }

    public double getScoreValidity(){
    	return score_validity;
    }

    public double getDefaultScore(){
    	return default_score;
    }
    
    public double getDegreeOfStrictness()
    {
        return degree_of_strictness; 
    }
    
    public double getBetaFactor()
    {
        return beta_factor; 
    }
    
    public double getActiveUserTime()
    {
        return active_user_time;
    }
    
    public double getActiveEvaluationTime()
    {
        return active_evaluation_time; 
    }
    
    public void addScorerComponent(Component c){
    	scorer.addComponent(c);
    }

    /*
        When called, it will create a scheduled TimerTask to call the checkContribution method when the 
        evaluation period of a Contribution ends.
        @param n the id of the contribution
    */
    public void addTimerTask(int n){
        timer.schedule(new ExpiryCheckerTask(n,this),validation_time*1000);
    }
    
    /*
        This method checks if a certain contribution must be accepted or rejected by the system
        @param n the id of the contribution
    */
    public void checkContribution(int n){
        boolean accept = valid.checkContribution(n);
        if(accept){
            scorer.acceptContribution(n);
            contributionAccepted++;
            summary.append(n+" Accepted\n");
        }
        else{
            scorer.rejectContribution(n);
            contributionRejected++;
            summary.append(n+" Rejected\n");
        }
    }
    /* 
        This method is called to integrate a contribution to the system.
        @param contribution_id the id of the contribution to be accepted
    */
    public void acceptContribution(int contribution_id){
        //scorer.acceptContribution(contribution_id);
        //make scorer remove contribution and evaluations
        Contribution cont = contMap.get(contribution_id);
        // updateContributionDBStatus(contribution_id, "Accepted");
        updateContributionDBStatus(contribution_id, 1);
        ArrayList<Evaluation> evals = cont.getEvaluations();
        for(int i=0; i<evals.size(); i++){
            Evaluation ev = evals.get(i);
            int id = ev.getId();
            evalMap.remove(id);
        }
        contMap.remove(cont.getId());
    }

    /*
        This method returns the top rated contributions in the system. This makes it easier for the admins of the client system to find the good contributions.
        @param n the number of contributions to be returned
    */
    public String getTopContributions(int n){
        ArrayList<Contribution> list = new ArrayList<Contribution>();
        Iterator<Integer> iter = contMap.keySet().iterator();
        while(iter.hasNext()){
            int id = iter.next();
            list.add(contMap.get(id));
        }
        Collections.sort(list);
        StringBuilder results = new StringBuilder();
        results.append("Top "+n+" Contributions: \n");
        for(int i=0; i<n; i++) {
            Contribution contr = list.get(i);
            results.append(contr.getId()+" "+contr.getContributionScore()+"\n");
        }
        return results.toString();
    }

    /*
        This method is called to remove a contribution from the system.
        @param contribution_id the id of the contribution to be rejected
    */
    public void rejectContribution(int contribution_id){
        //scorer.rejectContribution(contribution_id);
        //make scorer remove contribution and evaluations
        Contribution cont = contMap.get(contribution_id);
        // updateContributionDBStatus(contribution_id, "Rejected");
        updateContributionDBStatus(contribution_id, 2);
        ArrayList<Evaluation> evals = cont.getEvaluations();
        for(int i=0; i<evals.size(); i++){
            Evaluation ev = evals.get(i);
            int id = ev.getId();
            evalMap.remove(id);
        }
        contMap.remove(cont.getId());
    }
    
    private void updateContributionDBStatus(int contribution_id, int status){
        Connection conn = pool.getConnection();
        try{
            Statement st = conn.createStatement();
            String update = "UPDATE Contributions"
                    + "SET state = " + status
                    + "WHERE contribution_id = " + contribution_id;  
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void printSummary(){
        System.out.println(summary);
        StringBuilder sb = new StringBuilder();
        sb.append("Total: "+contributionTotal+"\n");
        sb.append("Accepted: "+contributionAccepted+"\n");
        sb.append("Rejected: "+contributionRejected+"\n");
        System.out.println(sb);
    }
}
