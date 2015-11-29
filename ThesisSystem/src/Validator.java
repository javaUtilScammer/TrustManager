import java.sql.Connection;
import java.util.ArrayList;

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

    public abstract boolean validate(Evaluation ev);
}
