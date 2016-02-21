/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *Separate Validator class to recompute threshold and check whether or not a Contribution has reached this threshold
 * @author migee
 */
import java.sql.Connection;
import java.util.*;

public class PageRankValidator extends Validator {
    private double threshold;
    /**
     * Constructor simply calls on superclass constructor
     * @param in ClientInterface necessary for Validator to work passed on to superclass
     */
    public PageRankValidator(ClientInterface in, double t){
        super(in);
        threshold = t;
    }
    
    /**
     * Validate method recomputes threshold of Contribution pointed to by Evaluation passed on to it. 
     * @param ev new Evaluation that was just used to compute the new score of a Contribution
     * @return whether or not Contribution has reached the threshold necessary for integration
     */
    public boolean validate(Contribution cont){
        boolean b = true;
        if(cont.getContributionScore()<=threshold) b = false;
        return b;
    }
    
}
