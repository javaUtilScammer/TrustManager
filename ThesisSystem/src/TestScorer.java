
public class TestScorer extends Scorer{
    
    private final double neutralScore;

    public TestScorer(ClientInterface in){
        super(in);
        neutralScore = (rating_scale/2.0)+0.1;
    }

    public void calculateScore(){
    	//do nothing
    }

    public void calculateScore(Evaluation ev, Contribution cont){
    	// System.out.println("validating");
        Evaluation eval = ev;
        Contribution con = cont;
        Account acc = eval.getCreatedBy();
        double accRating = acc.getTrustRating();
        double evalScore = eval.getRating();
        double conScore = con.getContributionScore();
        System.out.println("validating: "+accRating+" "+evalScore+" "+conScore);
        double offset = (accRating-neutralScore)*(evalScore-neutralScore);
        System.out.println("validating: "+offset);
        conScore += offset;
        if(conScore>rating_scale) conScore = rating_scale;
        System.out.println("validating: "+conScore);
//        con.setContributionRating(conScore,conn);
//        acc.
    }
}
