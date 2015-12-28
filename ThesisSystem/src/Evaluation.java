import java.util.Date.*;
import java.sql.*; 

/**
 * Evaluation class that handles all necessary data with regard to Evaluations in the system
 * @author hadrianang
 */
public class Evaluation extends Component
{
	private int evaluation_id; 
	private double rating, trust_rating;
	private Timestamp created_at;
	private Account created_by;
	private Contribution contribution;
        
        /**
         * 
         * @param evaluation_id system assigned id for this Evaluation
         * @param rating rating submitted with this Evaluation
         * @param created_at timestamp when Evaluation was created
         * @param created_by Account that created this Contribution
         * @param contribution Contribution this Evaluation is rating
         */
	public Evaluation(int evaluation_id, double rating, Timestamp created_at, Account created_by, Contribution contribution)
	{
		this.evaluation_id = evaluation_id;
		this.rating = rating;
		this.created_at = created_at; 
		this.created_by = created_by; 
		this.contribution = contribution;
		type = 'e';
	}
	
        /**
         * updates the database with the current values of this Evaluation's fields
         * @param conn connection necessary to update the database
         * @return whether or not the database operation was successful 
         */
	public boolean updateDB(Connection conn)
	{
	    try{
		Statement st = conn.createStatement();
		String update = "UPDATE Evaluations"
			+ "SET rating = " + rating
			+ "WHERE evaluation_id = " + evaluation_id; 
		
		return true; 
	    }catch(SQLException e)
	    {
		e.printStackTrace(); 
		return false; 
	    }
	}
        
        /**
         * gets the system assigned ID of this Evaluation 
         * @return the system assigned ID of this Evaluation
         */
	public int getId()
	{
		return evaluation_id; 
	}
        
        /**
         * gets the rating submitted of this Evaluation
         * @return rating of this Evaluation
         */
	public double getRating()
	{
		return rating;
	}
        
        /**
         * gets the timestamp when this Evaluation was created
         * @return timestamp when this Evaluation was created
         */
	public Timestamp getCreatedAt()
	{
		return created_at; 
	}
        
        /**
         * gets the Contribution this Evaluation is rating
         * @return the Contribution this Evaluation is rating
         */
	public Contribution getContribution()
	{
		return contribution; 
	}
        
        /**
         * gets the Account that created this Contribution
         * @return the Account that created this Contribution
         */
	public Account getCreatedBy()
	{
		return created_by; 
	}
        
        /**
         * gets the trust rating of this Evaluation
         * @return trust rating of this Evaluation (how much the system trusts the rating of this Evaluation) 
         */
	public double getTrustRating(){
		return trust_rating;
	}
        
        /**
         * sets the trust rating of this Evaluation to the value passed in the parameter
         * @param tr new value for this Evaluation's trust rating
         */
	public void setTrustRating(double tr){
		trust_rating = tr;
	}
}