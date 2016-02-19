
import java.sql.Connection;
import java.util.ArrayList;

/**
 * abstract scorer as basis of other Scorers to be used by the system
 * @author hadrianang
 */
public abstract class Scorer {

    final double rating_scale, score_validity,degree_of_strictness, beta_factor, active_user_time, active_evaluation_time;
    ClientInterface intrface;
    ArrayList<Component> compList;
    Connection conn;
    
    /**
     * 
     * @param in ClientInterface necessary for the scorer to work (gets multiple required fields from this interface)  
     */
    public Scorer(ClientInterface in){
            intrface = in;
            rating_scale = intrface.getRatingScale();
            score_validity = intrface.getScoreValidity();
            conn = intrface.pool.getConnection();
            compList = new ArrayList<Component>();
            degree_of_strictness = intrface.getDegreeOfStrictness(); 
            beta_factor = intrface.getBetaFactor();
            active_user_time = intrface.getActiveUserTime();
            active_evaluation_time = intrface.getActiveEvaluationTime(); 
    }
    
    /**
     * Add a component to this Scorer's component list
     * @param c Component to be addded to this Scorer's component list
     */
    public void addComponent(Component c){
        compList.add(c);
    }

    /**
     * main scoring function used by this Scorer
     * @param ev new Evaluation submitted
     * @param cont the to be scored, the Contribution the Evaluation is rating
     */
    abstract void calculateScore(Evaluation ev, Contribution cont);
        
}