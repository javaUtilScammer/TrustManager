
import java.sql.Connection;
import java.util.ArrayList;

public abstract class Scorer {

    final double rating_scale, score_validity,degree_of_strictness, beta_factor, active_user_time, active_evaluation_time;
    ClientInterface intrface;
    ArrayList<Component> compList;
    Connection conn;

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

    abstract void calculateScore();
    abstract void calculateScore(Evaluation ev, Contribution cont);
        
}