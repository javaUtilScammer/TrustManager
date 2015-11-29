import java.sql.Connection;
import java.util.ArrayList;

public abstract class Validator {

    final double rating_scale, score_validity;
    ClientInterface intrface;

    public Validator(ClientInterface in){
        intrface = in;
        rating_scale = intrface.getRatingScale();
        score_validity = intrface.getScoreValidity();
    }

    public boolean validate(Evaluation ev, double newScore){
    	double threshold = rating_scale/2;//temporary
    	if(newScore<threshold) return false;
    	return true;
    }
}
