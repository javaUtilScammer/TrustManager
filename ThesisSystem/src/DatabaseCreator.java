
import java.sql.Connection;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Class contains necessary methods to create a new database for each new client system
 * @author hadrianang
 */
public class DatabaseCreator {
    Configuration conf; 
    Connection conn; 
    String key; 
    
    /**
     * 
     * @param conf configuration file for the new client system
     * @param conn connection necessary to create a database for the client system
     */
    public DatabaseCreator(Configuration conf, Connection conn)
    {
        this.conf = conf; 
        this.conn = conn;
	key = generateKey(); 
    }
    
    /**
     * gets the Connection used by this DatabaseCreator
     * @return Connection used by this DatabaseCreator
     */
    public Connection getConnection()
    {
	return conn; 
    }
    
    /**
     * creates a database for the client system based on the Configuration
     * @return a randomly generated key to be used by the client for future HTTP POSTS
     */
    public String createDatabase()
    {
        try{
			Statement st = conn.createStatement(); 
			st.executeUpdate("CREATE DATABASE " + conf.getClientName()); 
			st = conn.createStatement();
			String sql = "INSERT INTO Clients(client_key, client_name, validation_time, validation_type, default_score, rating_scale, degree_of_strictness, beta_factor, active_user_time, active_evaluation_time)"
			+ "VALUES(\"" + key +"\", \"" + conf.getClientName()+"\", "+conf.getValidationTime() +", \""+conf.getValidationType()+"\", "+ conf.getDefaultScore()+", " + conf.getRatingScale() +", " + conf.getDegreeOfStrictness() + ", " + conf.getBetaFactor() + ", " + conf.getActiveUserTime() + ", " + conf.getActiveEvaluationTime() +");"; 
			// System.out.println(sql);
            st.executeUpdate(sql);
			return key; 
        }
        catch(Exception e){
			e.printStackTrace();
			return null; 
        }
    }
    
    /**
     * gets the key generated for use by the client system
     * @return key for use by the client system 
     */
    public String getKey()
    {
	return key; 
    }
    
    /**
     * creates database tables for Accounts, Contributions and Evaluations
     * @param conn connection necessary for database operations
     * @return whether or not the database operation was successful 
     */
    public boolean createTables(Connection conn)
    {
	try{
	    Statement st = conn.createStatement(); 
	    String accounts = "CREATE TABLE Accounts(" +
                "	account_id int AUTO_INCREMENT," +
                "	username varchar(15) UNIQUE NOT NULL," +
                "	created_at timestamp NOT NULL," +
                "	last_updated_at timestamp NOT NULL," +
                "	trust_rating double NOT NULL," +
                "	trust_confidence double NOT NULL," +
                "       contributions_accepted double NOT NULL," +
                "       contributions_rejected double NOT NULL," +
                "       contributions_total double NOT NULL," +
                "	PRIMARY KEY(account_id)" +
                ");";
	    
	    st.executeUpdate(accounts); 
	    st = conn.createStatement(); 
	    
	    String contributions = "CREATE TABLE Contributions(" +
		"	contribution_id int AUTO_INCREMENT," +
		"	contributor_id int NOT NULL," +
		"	contribution_score double NOT NULL," +
		"	score_confidence double NOT NULL," +
		"	created_at timestamp NOT NULL," +
		"	state int(4) NOT NULL," +
		"	PRIMARY KEY(contribution_id)," +
		"	FOREIGN KEY(contributor_id) REFERENCES Accounts(account_id)" +
		"		ON DELETE CASCADE" +
		");";
	    
	    st.executeUpdate(contributions); 
	    
	    st = conn.createStatement(); 
	    String evaluations = "CREATE TABLE Evaluations(" +
                "	evaluation_id int AUTO_INCREMENT," +
                "	rating double NOT NULL," +
                "	created_at timestamp NOT NULL," +
                "	created_by int NOT NULL," +
                "	contribution_id int NOT NULL, " +
                "	PRIMARY KEY(evaluation_id)," +
                "	FOREIGN KEY(created_by) REFERENCES Accounts(account_id)" +
                "		ON DELETE CASCADE," +
                "	FOREIGN KEY(contribution_id) REFERENCES Contributions(contribution_id)" +
                "		ON DELETE CASCADE" +
                ");";
	    
	    st.executeUpdate(evaluations); 
	   
	    return true; 
	}catch(Exception e)
	{
	    e.printStackTrace(); 
	    return false; 
	}
    }
    
    /**
     * generates a key 
     * @return key that can be used by a client system 
     */
    private String generateKey()
    {
	return genRandomString(25); 
    }
    
    
    /**
     * generates a random string
     * @param length length of string to be generated
     * @return string generated
     */
    private static String genRandomString(int length)
    {
	StringBuilder sb = new StringBuilder(); 
	for(int i=0; i<length; i++)
	{
	    double numLett = Math.random(); 
	    if(numLett<.60)
	    {
		double cap = Math.random(); 
		int offset = cap > 0.50 ? (int)'a' : (int)'A';
		int lett = (int)(Math.random()*26); 
		char ins = (char)(lett+offset); 
		sb.append(ins); 
	    }else
	    {
		int offset = (int)(Math.random()*9);
		char ins = (char)('0' + offset); 
		sb.append(ins); 
	    }
	}
	return sb.toString(); 
    }
}
