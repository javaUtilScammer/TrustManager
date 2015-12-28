import java.util.Date.*; 
import java.sql.*; 
import java.util.ArrayList;


/**
 * Contribution class to hold all information with regard to Contributions
 * @author hadrianang
 */
public class Contribution extends Component implements Comparable<Contribution>
{
    private int contribution_id, state;
    private ArrayList<Evaluation> evaluations;
    private double contribution_score, score_confidence;
    private Timestamp created_at; 
    private Account contributor; 
    
    /**
     * 
     * @param id system assigned ID of this Contribution
     * @param contributor Account that submitted this Contribution
     * @param contribution_score score of this Contribution
     * @param score_confidence how confident the system is in the assigned score of this Contribution
     * @param created_at timestamp of when the Contribution was created 
     * @param state state of the Contribution (accepted, rejected, validation phase)
     */
    public Contribution(int id, Account contributor, double contribution_score, double score_confidence, Timestamp created_at, int state)
    {
        this.contribution_id=id; 
        this.contributor = contributor;
        this.contribution_score = contribution_score; 
        this.score_confidence = score_confidence; 
        this.created_at = created_at; 
        this.state = state;
        type = 'c';
        evaluations = new ArrayList<Evaluation>();
    }

    /**
     * updates the database with this Contribution's values
     * @param conn connection required to update database
     * @return whether or not the database operation was successful
     */
    public boolean updateDB(Connection conn)
    {
        try{
            Statement st = conn.createStatement();
            String update = "UPDATE Contributions"
                    + "SET contribution_score = " + contribution_score + ", score_confidence = " + score_confidence + ", state = " + state  
                    + "WHERE contribution_id = " + contribution_id; 


            return true; 
        }catch(SQLException e)
        {
            e.printStackTrace(); 
            return false; 
        }
    }

    /**
     * gets list of Evaluations rating this Contribution
     * @return list of evaluations rating this Contribution
     */
    public ArrayList<Evaluation> getEvaluations()
    {
        return evaluations; 
    }
    
    /**
     * gets the system assigned ID of this Contribution
     * @return ID of this Contribution
     */
    public int getId()
    {
        return contribution_id; 
    }
    
    /**
     * gets the Account that submitted this Contribution
     * @return Account that submitted this Contribution
     */
    public Account getContributor()
    {
        return contributor; 
    }
    
    /**
     * gets the score computed for this Contribution
     * @return score computed for this Contribution
     */
    public double getContributionScore()
    {
        return contribution_score;
    }

    /**
     * gets the score confidence of this Contribution
     * @return score confidence of this Contribution
     */
    public double getScoreConfidence()
    {
        return score_confidence; 
    }
    
    /**
     * sets a new contribution score and updates the database
     * @param cs sets a new score for this Contribution
     * @param conn connection necessary to update the database
     */
    public void setContributionScore(double cs, Connection conn){
            contribution_score = cs;
            updateDB(conn);
    }
    /**
     * sets a new score for this Contribution
     * @param cs new contribution score
     */
    public void setContributionScore(double cs){
            contribution_score = cs;
            
    }
    public int compareTo(Contribution c){
            return Double.compare(contribution_score,c.getContributionScore());
    }
}