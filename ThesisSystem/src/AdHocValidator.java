/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *Separate Validator class to recompute threshold and check whether or not a Contribution has reached this threshold
 * @author hadrianang
 */
import java.sql.Connection;
import java.util.*;
public class AdHocValidator extends Validator {
    
    /**
     * Constructor simply calls on superclass constructor
     * @param in ClientInterface necessary for Validator to work passed on to superclass
     */
    public AdHocValidator(ClientInterface in)
    {
        super(in); 
    }
    
    /**
     * Validate method recomputes threshold of Contribution pointed to by Evaluation passed on to it. 
     * @param ev new Evaluation that was just used to compute the new score of a Contribution
     * @return whether or not Contribution has reached the threshold necessary for integration
     */
    public double computeTrustRating(double accepted, double rejected, double total)
    {
        double ret = 0.50; 
        double mult = (accepted-rejected)/total; 
        ret += (0.50 * mult);
        return ret; 
    }
    public boolean validate(Contribution cont)
    {
        double score = cont.getContributionScore(); 
        double active = Math.max(intrface.getActiveCount(),5);
        double denom = Math.log(active) / Math.log(Math.E); 
        denom = Math.pow(denom,degree_of_strictness);
        double threshold = active/denom; 
        System.out.println("Threshold for "+cont.getId()+" "+threshold);
        //If accepted, modify the scores of Evaluators that Evaluated this accepted function
        if(score>=threshold)
        {
            Connection conn = intrface.getConnection(); 
            ArrayList<Evaluation> evals = cont.getEvaluations();
            for(int i=0;i<evals.size();i++)
            {
                Evaluation temp = evals.get(i);
                
                double rating = temp.getRating(); 
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
                
                Account sub = temp.getCreatedBy(); 
                double acc = sub.getAccepted(); 
                double rej = sub.getRejected();
                double total = sub.getTotal(); 
                
                //increment number of Accepted or Rejected and Total number of Contributions by half a point
                if(scaled>=0)
                    sub.incAccepted(acc+(scaled*0.50));
                else
                    sub.incRejected(rej+(scaled*0.50)); 
                sub.incTotal(total+0.50); 
                
                double newTrust = computeTrustRating(sub.getAccepted(), sub.getRejected(), sub.getTotal()); 
                sub.setTrustConfidence(newTrust,conn); 
                //update the database
                sub.updateDB(conn); 
            }
            
            //Contributor gets trust rating score modified also
            Account user = cont.getContributor();
            user.incAccepted(user.getAccepted()+1); 
            user.incTotal(user.getTotal()+1);
            user.updateDB(conn);
            double newTrust = computeTrustRating(user.getAccepted(), user.getRejected(), user.getTotal()); 
            user.setTrustConfidence(newTrust,conn); 
            intrface.returnConnection(conn);
            return true; 
        }else return false; 
    }
    
}
