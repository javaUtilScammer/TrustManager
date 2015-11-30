
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
public class AccountBuilder extends ComponentBuilder
{
    public Account buildAccount(String username, Timestamp created_at, Timestamp last_updated_at, double trust_rating, double trust_confidence, double cacc, double crej, double ctotal) 
    {
	try{
	    Statement st = conn.createStatement(); 
	    String sql = "INSERT INTO Accounts(username, created_at, last_updated_at, trust_rating, trust_confidence, contributions_accepted, contributions_rejected, contributions_total)"
		    + "VALUES(\"" + username + "\", \"" + created_at + "\", \"" + last_updated_at + "\", " + trust_rating + ", " + trust_confidence + ", " + cacc + ", " + crej + ", " + ctotal + ");";
	    st.executeUpdate(sql); 
	    
	    st = conn.createStatement(); 
	    String check = "SELECT LAST_INSERT_ID()"; 
	    ResultSet rs = st.executeQuery(check); 
	    int account_id = -1; 
	    rs.next(); 
	    account_id = rs.getInt(1); 
	    
	    Account temp = new Account(account_id, username, created_at, last_updated_at, trust_rating, trust_confidence, cacc, crej, ctotal); 
	    return temp; 
	    
	}catch(Exception e)
	{
	    //System.out.println("Error in AccountBuilder");
	    e.printStackTrace(); 
	    return null; 
	}
    }
}
