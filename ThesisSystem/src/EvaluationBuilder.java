
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
public class EvaluationBuilder extends ComponentBuilder
{
    public Evaluation buildEvaluation(double rating, Timestamp created_at, Account created_by, Contribution contribution) 
    {
	try{
	    Statement st = conn.createStatement(); 
	    String sql = "INSERT INTO Evaluations(rating, created_at, created_by, contribution_id)"
		    + "VALUES(" + rating+", \""+ created_at + "\", " + created_by.getId() + ", " + contribution.getId() + ");";
	    st.executeUpdate(sql); 
	    
	    st = conn.createStatement(); 
	    String check = "SELECT LAST_INSERT_ID()"; 
	    ResultSet rs = st.executeQuery(check); 
	    int evaluation_id = -1; 
	    rs.next(); 
	    evaluation_id = rs.getInt(1); 
	    Evaluation temp = new Evaluation(evaluation_id, rating, created_at, created_by, contribution); 
	    created_by.getEvaluations().add(temp); 
            created_by.setLastUpdatedAt(created_at,conn); 
            created_by.incNumEv(); 
            contribution.getEvaluations().add(temp);
	    return temp; 
	    
	}catch(Exception e)
	{
	    //System.out.println("Error in AccountBuilder");
	    e.printStackTrace(); 
	    return null; 
	}
    }
}
