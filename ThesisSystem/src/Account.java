import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date.*;
import java.sql.Timestamp; 
import java.util.ArrayList;

/**
 * Account class to hold data of Accounts made by users of the client system. Necessary methods to get and modify fields are included.
 * @author hadrianang
 */
public class Account extends Component
{
    private int account_id, num_ev;
    private ArrayList<Contribution> contributions;
    private ArrayList<Evaluation> evaluations; 
    private String username;
    private Timestamp created_at, last_updated_at;
    private double trust_rating, trust_confidence;
    private double cacc, crej, ctotal; 
    
    /**
     * Constructor for Account objects which hold data of Accounts made by users of the client system 
     * @param account_id system generated id number
     * @param username username of this account
     * @param created_at timestamp when this account was created
     * @param trust_rating rating of how much the system trusts the given account
     * @param trust_confidence rating of how sure the system is that it has given a correct trust rating
     * @param cacc number of contributions by this account that were accepted
     * @param crej number of contributions by this account that were rejected
     * @param ctotal total number of contributions submitted by this account
     */
    public Account(int account_id, String username, Timestamp created_at, double trust_rating, double trust_confidence, double cacc, double crej, double ctotal)
    {
            this.account_id = account_id;
            this.username = username; 
            this.created_at = created_at;
            this.last_updated_at = created_at; 
            this.trust_rating = trust_rating;
            this.trust_confidence = trust_confidence;
            evaluations = new ArrayList<Evaluation>(); 
            type = 'a';
            contributions = new ArrayList<Contribution>();
            num_ev = 0;
            for(int i=0;i<contributions.size();i++)
                num_ev += contributions.get(i).getEvaluations().size(); 

    }
    
    /**
     * updates database using account_id with the current values of this object's fields 
     * @param conn Connection object required to update database
     * @return whether update operation was successful or not
     */
    public boolean updateDB(Connection conn)
    {
        try{
            Statement st = conn.createStatement();
            String update = "UPDATE Accounts"
                    + "SET last_updated_at = \"" + last_updated_at + "\", trust_rating = " + trust_rating + ", trust_confidence = " + trust_confidence  + ", contributions_accepted = " + cacc + ", contributions_rejected = " + crej +   ", contributions_total = " + ctotal
                    + " WHERE account_id = " + account_id; 

            return true; 
        }catch(SQLException e)
        {
            e.printStackTrace(); 
            return false; 
        }
    }
      
    /**
     * increments the total number of Evaluations of all Contributions submitted by this Account
     */
    public void incNumEv()
    {
        num_ev++; 
    }
    
    /**
     * gets total number of Evaluations across all Contributions submitted by this Account
     * @return total number of Evaluations of all Contributions submitted by this Account
     */
    public int getNumEv()
    {
        return num_ev; 
    }
    
    /**
     * gets count of accepted Contributions, which may further be modified by different Scorers
     * @return number of accepted Contributions
     */
    public double getAccepted()
    {
        return cacc;
    }
    
    /**
     * gets count of rejected Contributions, which may further be modified by different Scorers
     * @return number of rejected Contributions
     */
    public double getRejected()
    {
        return crej;
    }
    
    /**
     * gets total Contributions submitted by this Account, which may further be modified by different Scorers
     * @return total number of Contributions
     */
    public double getTotal()
    {
        return ctotal; 
    }
    
    /**
     * increments the number of accepted Contributions by the given parameter
     * @param temp amount to increment the number of accepted Contributions by 
     */
    public void incAccepted(double temp)
    {
        cacc += temp;
        //updateDB(conn);
    }
    
    /**
     * increments number of rejected Contributions by given parameter
     * @param temp amount to increment number of rejected Contributions by
     */
    public void incRejected(double temp)
    {
        crej += temp;
        //updateDB(conn);
    }
    
    /**
     * increments total number of Contributions by a given parameter
     * @param temp amount to increment total Contributions by
     */
    public void incTotal(double temp)
    {
        ctotal += temp;
        //updateDB(conn);
    }
    
    /**
     * gets the id of this Account object
     * @return id of this Account object
     */
    public int getId()
    {
            return account_id;
    }
    
    /**
     * gets the username of this Account object
     * @return username of this Account object
     */
    public String getUsername()
    {
        return username;
    }
    
    /**
     * gets the timestamp this Account object was created at 
     * @return timestamp this Account object was created at
     */
    public Timestamp getCreatedAt()
    {
        return created_at; 
    }
    
    /**
     * gets the Timestamp this Account object was last updated at (submitting Contributions or Evaluations constituting as updated)
     * @return timestamp this Account object was last updated at 
     */
    public Timestamp getUpdatedAt()
    {
        return last_updated_at; 
    }
    
    /**
     * gets the computed trust rating of this Account object
     * @return trust rating of this Account
     */
    public double getTrustRating()
    {
        return trust_rating;
    }

    /**
     * gets computed trust confidence of this Account
     * @return trust confidence of this Account 
     */
    public double getTrustConfidence()
    {
        return trust_confidence; 
    }
    
    /**
     * gets the list of Evaluations submitted by this Account
     * @return list of Evaluations submitted by this Account
     */
    public ArrayList<Evaluation> getEvaluations()
    {
        return evaluations; 
    }
    
    /**
     * gets list of Contributions submitted by this Account
     * @return list of Contributions submitted by this Account
     */
    public ArrayList<Contribution> getContributions()
    {
        return contributions; 
    }
    
    /**
     * sets the trust rating of this Account and also updates the database
     * @param tr new trust rating to set the current value to
     * @param conn connection required to update the database
     */
    public void setTrustRating(double tr, Connection conn)
    {
        trust_rating = tr;
        updateDB(conn);
    }
    
    /**
     * sets the trust confidence of this Account and also updates the database
     * @param tc new trust confidence to set the current value to
     * @param conn connection required to update the database
     */
    public void setTrustConfidence(double tc, Connection conn)
    {
        trust_confidence = tc;
        updateDB(conn);
    }
        
    /**
     * sets the last updated timestamp of this Account to the given parameter and also updates the database
     * @param ts when this Account was last updated
     * @param conn connection required to update the database
     */
    public void setLastUpdatedAt(Timestamp ts, Connection conn)
    {
        last_updated_at = ts; 
        updateDB(conn); 
    }

    /**
     * sets the trust rating of this Account to the given parameter
     * @param tr new trust rating value
     */
    public void setTrustRating(double tr)
    {
        trust_rating = tr;
    }
}