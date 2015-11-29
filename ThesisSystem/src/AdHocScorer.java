
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
    
    public double computeInitialScore(Contribution cont)
    {
        double active = intrface.getActiveCount();
        double denom = Math.log(active) / Math.log(Math.E); 
        denom = Math.pow(denom,degree_of_strictness);
        double threshold = active/denom; 
        
        Account contributor = cont.getContributor(); 
        return threshold * contributor.getTrustRating() * contributor.getTrustConfidence(); 
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
            scaled += (0.33*rating);
            if(1-scaled < (0.000001)) scaled = 1; 
        }
        
        Account submit = ev.getCreatedBy(); 
        double trust_rating = submit.getTrustRating(); 
        double trust_confidence = submit.getTrustConfidence(); 
        currScore += (scaled * trust_rating * trust_confidence); 
        Connection conn = intrface.getConnection(); 
        cont.setContributionScore(currScore,conn);
        Account contributor = cont.getContributor(); 

            double conf = contributor.getTrustConfidence(); 
            double active = intrface.getActiveCount();
            double num = Math.log(active)/Math.log(Math.E); 
            num = Math.pow(num,beta_factor); 
            num/=active; 
            num*=contributor.getNumEv(); 
            conf = Math.min(1, 0.50 + 0.50*num );
                    
        contributor.setTrustConfidence(conf,conn); 
        intrface.returnConnection(conn);
        
    }
}
