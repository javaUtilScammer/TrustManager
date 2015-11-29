
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet; 

public class ClientInterface {
    private final String client_name, validation_type, key;
    private final int validation_time;
    private final double default_score, rating_scale, score_validity, degree_of_strictness, beta_factor, active_user_time, active_evaluation_time;
    final ConnectionPool pool;
    final ComponentFactory compFactory;
    private Scorer scorer;
    private final Server server;
    private HashMap<Integer,Account> accMap;
    private HashMap<Integer,Contribution> contMap;
    private HashMap<Integer,Evaluation> evalMap;
    private Validator valid;
    private HashSet<Account> active; 
    
    public ClientInterface(String key, Configuration config, String url, Server server){
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
        accMap = new HashMap<Integer, Account>();
        contMap = new HashMap<Integer, Contribution>();
        evalMap = new HashMap<Integer, Evaluation>();
        server.httpserver.createContext("/"+key,new ClientInterfaceHandler(this));
        compFactory = new ComponentFactory(pool.getConnection(),this);
        scorer = new PageRankScorer(this);
        // valid = new TestValidator(this);
    }
    
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
		
		Account acc = new Account(account_id, username, created_at, last_updated_at, trust_rating, trust_confidence); 
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
    
    public int getActiveCount()
    {
        return active.size(); 
    }
    
    public Connection getConnection()
    {
        return pool.getConnection(); 
    }
    
    public void returnConnection(Connection conn)
    {
        pool.returnConnection(conn);
    }
    
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
    	scorer.compList.add(c);
    }
}
