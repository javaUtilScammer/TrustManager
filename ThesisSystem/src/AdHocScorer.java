
import java.sql.Connection;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hadrianang
 */
public class AdHocScorer extends Scorer{

    public AdHocScorer(ClientInterface in){
        super(in); 
    }
    
    public void calculateScore()
    {
    }
    
    public void calculateScore(Evaluation ev, Contribution cont)
    {
        double currScore = cont.getContributionScore(); 
        double rating = ev.getRating(); 
        double scaled;
        if(rating_scale=='1')
        {
            if(rating==0) scaled = -1;
            else scaled = 1; 
        }else
        {
            scaled = -1;
            for(int i=0;i<rating;i++) scaled += 0.33;
            if(1-scaled < (0.000001)) scaled = 1; 
        }
        
        Account submit = ev.getCreatedBy(); 
        double trust_rating = submit.getTrustRating(); 
        double trust_confidence = submit.getTrustConfidence(); 
        currScore += (scaled * trust_rating * trust_confidence); 
        cont.setContributionScore(currScore, intrface.getConnection());
    }
}
