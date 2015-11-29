
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Hadrian
 */
public class ContributionBuilder extends ComponentBuilder
{
    public Contribution buildContribution(Account contributor, double contribution_score, double score_confidence, Timestamp created_at, int state) 
    {
	try{
	    Statement st = conn.createStatement(); 
	    String sql = "INSERT INTO Contributions(contributor_id, contribution_score, score_confidence, created_at, state)"
		    + "VALUES(" + contributor.getId() +", "+ contribution_score + ", " + score_confidence + ", \"" + created_at + "\", " + state + ");";
	    st.executeUpdate(sql); 
	    
	    st = conn.createStatement(); 
	    String check = "SELECT LAST_INSERT_ID()"; 
	    ResultSet rs = st.executeQuery(check); 
	    int contribution_id = -1; 
	    rs.next(); 
	    contribution_id = rs.getInt(1); 
	    
	    Contribution temp = new Contribution(contribution_id, contributor, contribution_score, score_confidence, created_at, state);
	    
	    return temp; 
	    
	}catch(Exception e)
	{
	    //System.out.println("Error in AccountBuilder");
	    e.printStackTrace(); 
	    return null; 
	}
    }
}
