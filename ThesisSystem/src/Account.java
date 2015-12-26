import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date.*;
import java.sql.Timestamp; 
import java.util.ArrayList;

public class Account extends Component
{
	private int account_id, num_ev;
	private ArrayList<Contribution> contributions;
    private ArrayList<Evaluation> evaluations; 
	private String username;
	private Timestamp created_at, last_updated_at;
	private double trust_rating, trust_confidence;
    private double cacc, crej, ctotal; 

	public Account(int account_id, String username, Timestamp created_at, Timestamp last_updated_at, double trust_rating, double trust_confidence, double cacc, double crej, double ctotal)
	{
		this.account_id = account_id;
		this.username = username; 
		this.created_at = created_at;
		this.last_updated_at = last_updated_at; 
		this.trust_rating = trust_rating;
		this.trust_confidence = trust_confidence;
                evaluations = new ArrayList<Evaluation>(); 
		type = 'a';
		contributions = new ArrayList<Contribution>();
                num_ev = 0;
                for(int i=0;i<contributions.size();i++)
                    num_ev += contributions.get(i).getEvaluations().size(); 
                    
	}
	
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
        
    public void addEv()
    {
        num_ev++; 
    }
    
    public int getNumEv()
    {
        return num_ev; 
    }
    
    public double getAccepted()
    {
        return cacc;
    }
    
    public double getRejected()
    {
        return crej;
    }
    
    public double getTotal()
    {
        return ctotal; 
    }
    
    public void setAccepted(double temp)
    {
        cacc += temp;
        //updateDB(conn);
        
    }
    public void setRejected(double temp)
    {
        crej += temp;
        //updateDB(conn);
        
    }
    public void setTotal(double temp)
    {
        ctotal += temp;
        //updateDB(conn);
        
    }
        
	public int getId()
	{
		return account_id;
	}

	public String getUsername()
	{
		return username;
	}

	public Timestamp getCreatedAt()
	{
		return created_at; 
	}

	public Timestamp getUpdatedAt()
	{
		return last_updated_at; 
	}

	public double getTrustRating()
	{
		return trust_rating;
	}

	public double getTrustConfidence()
	{
		return trust_confidence; 
	}
        
    public ArrayList<Evaluation> getEvaluations()
    {
        return evaluations; 
    }
    
    public ArrayList<Contribution> getContributions()
    {
        return contributions; 
    }
        
	public void setTrustRating(double tr, Connection conn){
            trust_rating = tr;
            updateDB(conn);
	}
        
        public void setTrustConfidence(double tr, Connection conn){
            trust_confidence = tr;
            updateDB(conn);
	}
        
    public void setLastUpdatedAt(Timestamp ts, Connection conn)
    {
        last_updated_at = ts; 
        updateDB(conn); 
    }

	public void setTrustRating(double tr){
            trust_rating = tr;
	}
}