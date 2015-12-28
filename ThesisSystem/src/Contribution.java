import java.util.Date.*; 
import java.sql.*; 
import java.util.ArrayList;

public class Contribution extends Component implements Comparable<Contribution>
{
	private int contribution_id, state;
	private ArrayList<Evaluation> evaluations;
	private double contribution_score, score_confidence, lat, lng;
	private Timestamp created_at; 
	private Account contributor; 
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
	
    public ArrayList<Evaluation> getEvaluations()
    {
        return evaluations; 
    }

	public int getId()
	{
            return contribution_id; 
	}

	public Account getContributor()
	{
            return contributor; 
	}

	public double getContributionScore()
	{
            return contribution_score;
	}
        
    public double getScoreConfidence()
    {
        return score_confidence; 
    }

	public double getLat()
	{
		return lat;
	}

	public double getLng()
	{
		return lng; 
	}

	public void setContributionScore(double cs, Connection conn){
		contribution_score = cs;
		updateDB(conn);
	}
	
	public void setContributionScore(double cs){
		contribution_score = cs;
	}

	public int compareTo(Contribution c){
		return Double.compare(contribution_score,c.getContributionScore());
	}
}