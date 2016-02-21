/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Configuration class with getter and setter methods 
 * @author Hadrian
 */
public class Configuration {
    
    private int validation_time;
    private String client_name, validation_type;
    private double default_score, rating_scale, degree_of_strictness, beta_factor, active_user_time, active_evaluation_time; 
    private String url;
    /**
     * 
     * @param client_name name of client system
     * @param validation_type validation and scorer method to be used
     * @param validation_time time before a Contribution expires and is rejected
     * @param default_score default trust rating of a user
     * @param rating_scale scale of the rating (1 or 0, 0-5) 
     * @param degree_of_strictness variable for how strict thresholds must be
     * @param beta_factor factor used in score computations
     * @param active_user_time time since last update for a user to still be considered an active user 
     * @param active_evaluation_time time before effects of an evaluation of score decays 
     */
    public Configuration(String client_name, String validation_type, int validation_time, double default_score, double rating_scale, double degree_of_strictness, double beta_factor, double active_user_time, double active_evaluation_time, String url)
    {
        this.client_name = client_name;
        this.validation_type = validation_type;
        this.validation_time = validation_time; 
    	this.default_score = default_score; 
    	this.rating_scale = rating_scale; 
        this.degree_of_strictness = degree_of_strictness;
        this.beta_factor = beta_factor;
        this.active_user_time = active_user_time; 
        this.active_evaluation_time = active_evaluation_time;
        this.url = url;
    }
    
    /**
     * gets name of client system that owns the configuration
     * @return name of client system
     */
    public String getClientName()
    {
	   return client_name; 
    }
    
    /**
     * gets kind of validation method used by client system
     * @return name of validation method used
     */
    public String getValidationType()
    {
	   return validation_type; 
    }
    
    /**
     * gets length of validation time in hours
     * @return validation time in number of hours
     */
    public int getValidationTime()
    {
	   return validation_time; 
    }
    
    /**
     * gets default score of newly created Accounts
     * @return default score of newly created Accounts
     */
    public double getDefaultScore()
    {
	   return default_score; 
    }
    
    /**
     * gets scale of ratings used by validation methods
     * @return maximum possible rating (0 as the assumed minimum)
     */
    public double getRatingScale()
    {
	   return rating_scale; 
    }
    
    /**
     * gets degree of strictness used in calculation of scores and thresholds
     * @return degree of strictness
     */
    public double getDegreeOfStrictness()
    {
        return degree_of_strictness;
    }
    
    /**
     * gets beta factor used in calculation of scores and thresholds
     * @return beta factor
     */
    public double getBetaFactor()
    {
        return beta_factor; 
    }
    
    
    /**
     * gets time since last update for a user to be considered active 
     * @return time since last update for a user to be considered active
     */
    public double getActiveUserTime()
    {
        return active_user_time; 
    }
    
    /**
     * gets time before an Evaluation's effects decay
     * @return time before an Evaluation's effects decay
     */
    public double getActiveEvaluationTime()
    {
        return active_evaluation_time; 
    }

    public String getURL(){
        return url;
    }
}
