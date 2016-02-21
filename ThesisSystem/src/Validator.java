import java.sql.Connection;
import java.util.ArrayList;

/**
 * abstract Validator class for different Validator functions used by the system
 * @author hadrianang
 */
public abstract class Validator {

    final double rating_scale, score_validity,degree_of_strictness, beta_factor, active_user_time, active_evaluation_time;
    ClientInterface intrface;

    public Validator(ClientInterface in){
        intrface = in;
        rating_scale = intrface.getRatingScale();
        score_validity = intrface.getScoreValidity();
        degree_of_strictness = intrface.getDegreeOfStrictness(); 
        beta_factor = intrface.getBetaFactor();
        active_user_time = intrface.getActiveUserTime();
        active_evaluation_time = intrface.getActiveEvaluationTime(); 
    }
    
    /**
     * main Validation function to be used by this Validator
     * @param ev the Evaluation submitted and was recently used to modify the score of a Contrbution
     * @return whether or not the Contribution rated by the Evaluation has reached the threshold computed
     */
    public abstract boolean validate(Contribution cont);
}
