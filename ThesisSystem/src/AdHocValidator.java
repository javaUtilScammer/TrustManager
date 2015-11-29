/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hadrianang
 */
import java.sql.Connection;
import java.util.*;
public class AdHocValidator extends Validator {
    
    public AdHocValidator(ClientInterface in)
    {
        super(in); 
    }
    
    public boolean validate(Evaluation ev)
    {
        Contribution cont = ev.getContribution();
        double score = cont.getContributionScore(); 
        double active = intrface.getActiveCount();
        double denom = Math.log(active) / Math.log(Math.E); 
        denom = Math.pow(denom,degree_of_strictness);
        double threshold = active/denom; 
        
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
                
                if(scaled>=0)
                    sub.setAccepted(acc+(scaled*0.50));
                else
                    sub.setRejected(rej+(scaled*0.50)); 
                sub.setTotal(total+0.50); 
                sub.updateDB(conn); 
            }
            
            Account user = ev.getCreatedBy();
            user.setAccepted(user.getAccepted()+1); 
            user.setTotal(user.getTotal()+1);
            user.updateDB(conn);
            intrface.returnConnection(conn);
            return true; 
        }else return false; 
    }
    
}
