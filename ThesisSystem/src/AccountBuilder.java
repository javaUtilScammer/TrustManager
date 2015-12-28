
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
 * Class specifically made to create Account objects 
 * @author Hadrian
 */	
public class AccountBuilder extends ComponentBuilder
{
    /**
     * method to create Account objects using the given parameters
     * @param username username of Account to be created
     * @param created_at timestamp of when Account was created (based on the client system)
     * @param trust_rating initial trust rating of Account to be created
     * @param trust_confidence initial trust confidence of Account to be created
     * @param cacc initial number of accepted Contributions of Account to be created
     * @param crej initial number of rejected Contributions of Account to be created
     * @param ctotal initial total number of Contributions of Account to be created
     * @return 
     */
    public Account buildAccount(String username, Timestamp created_at, double trust_rating, double trust_confidence, double cacc, double crej, double ctotal) 
    {
	try{
	    Statement st = conn.createStatement(); 
	    String sql = "INSERT INTO Accounts(username, created_at, last_updated_at, trust_rating, trust_confidence, contributions_accepted, contributions_rejected, contributions_total)"
		    + "VALUES(\"" + username + "\", \"" + created_at + "\", \"" + created_at + "\", " + trust_rating + ", " + trust_confidence + ", " + cacc + ", " + crej + ", " + ctotal + ");";
	    st.executeUpdate(sql); 
	    
	    st = conn.createStatement(); 
	    String check = "SELECT LAST_INSERT_ID()"; 
	    ResultSet rs = st.executeQuery(check); 
	    int account_id = -1; 
	    rs.next(); 
	    account_id = rs.getInt(1); 
	    
	    Account temp = new Account(account_id, username, created_at, trust_rating, trust_confidence, cacc, crej, ctotal); 
	    return temp; 
	    
	}catch(Exception e)
	{
	    //System.out.println("Error in AccountBuilder");
	    e.printStackTrace(); 
	    return null; 
	}
    }
}
