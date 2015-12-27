import java.sql.Connection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *Scorer using an ad-hoc method based on the thesis paper 
 * @author hadrianang
 */
public class AdHocScorer extends Scorer{
    
    /**
     * constructor simply uses the superclass constructor
     * @param in ClientInterface necessary for the Scorer to operate (passed onto superclass constructor)  
     */
    public AdHocScorer(ClientInterface in){
        super(in); 
    }
    
    /**
     * Not used by this type of scorer 
     */
    public void calculateScore()
    {
    }
    
    /**
     * function called to compute initial scores of contributions
     * @param cont Contribution to be scored
     * @return initial score of the Contribution passed as a parameter
     */
    public double computeInitialScore(Contribution cont)
    {
        //first compute for the threshold
        //get number of active users
        double active = intrface.getActiveCount();
        //compute for denominator ln(active) ^ degree_of_strictness
        double denom = Math.log(active) / Math.log(Math.E); 
        denom = Math.pow(denom,degree_of_strictness);
        double threshold = active/denom; 
        
        //score is a percentage of the threshold based on trust rating of contributor
        Account contributor = cont.getContributor(); 
        return threshold * contributor.getTrustRating() * contributor.getTrustConfidence(); 
    }
    
    /**
     * Main scoring method of the ad-hoc scorer
     * @param ev new Evaluation of the given Contribution
     * @param cont Contribution to be scored
     */
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
