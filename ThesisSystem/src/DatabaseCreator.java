
import java.sql.Connection;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hadrianang
 */
public class DatabaseCreator {
    Configuration conf; 
    Connection conn; 
    String key; 
    public DatabaseCreator(Configuration conf, Connection conn)
    {
        this.conf = conf; 
        this.conn = conn;
	key = generateKey(); 
    }
    
    public Connection getConnection()
    {
	return conn; 
    }
    
    public String createDatabase()
    {
        try{
            Statement st = conn.createStatement(); 
            st.executeUpdate("CREATE DATABASE " + conf.getClientName()); 
            return key; 
        }catch(Exception e)
        {
            return null; 
        }
    }
    
    public boolean createTables()
    {
	try{
	    Statement st = conn.createStatement(); 
	    String accounts = "CREATE TABLE Accounts(" +
			"	account_id int AUTO_INCREMENT," +
			"	username varchar(15) UNIQUE NOT NULL," +
			"	created_at timestamp NOT NULL," +
			"	last_updated_at timestamp NOT NULL," +
			"	trust_rating double NOT NULL," +
			"	trust_validity double NOT NULL," +
			"	PRIMARY KEY(account_id)" +
			");";
	    
	    st.executeUpdate(accounts); 
	    st = conn.createStatement(); 
	    
	    String contributions = "CREATE TABLE Contributions(" +
		"	contribution_id int AUTO_INCREMENT," +
		"	contributor_id int NOT NULL," +
		"	message varchar(150) NOT NULL," +
		"	contribution_score double NOT NULL," +
		"	score_validity double NOT NULL," +
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
	    
	    st = conn.createStatement();
	    String comments = "CREATE TABLE Comments(" +
			    "	comment_id int AUTO_INCREMENT," +
			    "	message varchar(150) NOT NULL," +
			    "	created_at timestamp NOT NULL," +
			    "	account_id int NOT NULL," +
			    "	evaluation_id int NOT NULL," +
			    "	PRIMARY KEY(comment_id)," +
			    "	FOREIGN KEY(account_id) REFERENCES Accounts(account_id)" +
			    "		ON DELETE CASCADE," +
			    "	FOREIGN KEY(evaluation_id) REFERENCES Evaluations(evaluation_id)" +
			    "		ON DELETE CASCADE" +
			    ");";
	    st.executeUpdate(comments); 
	    return true; 
	}catch(Exception e)
	{
	    return false; 
	}
    }
    
    private String generateKey()
    {
	return genRandomString(25); 
    }
    
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
