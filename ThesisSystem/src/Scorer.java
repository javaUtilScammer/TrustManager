
import java.sql.Connection;
import java.util.ArrayList;

public abstract class Scorer {

    final double rating_scale, score_validity;
    ClientInterface intrface;
    ArrayList<Component> compList;
    Connection conn;

	public Scorer(ClientInterface in){
		intrface = in;
		rating_scale = intrface.getRatingScale();
		score_validity = intrface.getScoreValidity();
		conn = intrface.pool.getConnection();
		compList = new ArrayList<Component>();
	}

	abstract void calculateScore();
	abstract void calculateScore(Evaluation ev, Contribution cont);
        
}