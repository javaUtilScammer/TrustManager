/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Hadrian
 */
public class Configuration {
    
    private int validation_time;
    private String client_name, validation_type;
    private double default_score, rating_scale, degree_of_strictness, beta_factor, active_user_time, active_evaluation_time; 

    public Configuration(String client_name, String validation_type, int validation_time, double default_score, double rating_scale, double degree_of_strictness, double beta_factor, double active_user_time, double active_evaluation_time)
    {
        this.client_name = client_name;
        this.validation_type = validation_type;
        this.validation_time = validation_time; 
    	this.default_score = default_score; 
    	this.rating_scale = rating_scale; 
        this.degree_of_strictness = degree_of_strictness;
        this.beta_factor = beta_factor;
        this.active_evaluation_time = active_evaluation_time; 
        this.active_user_time = active_user_time; 
    }
    
    public String getClientName()
    {
	   return client_name; 
    }
    
    public String getValidationType()
    {
	   return validation_type; 
    }
    
    public int getValidationTime()
    {
	   return validation_time; 
    }
    
    public double getDefaultScore()
    {
	   return default_score; 
    }
    
    public double getRatingScale()
    {
	   return rating_scale; 
    }
    
    public double getDegreeOfStrictness()
    {
        return degree_of_strictness;
    }
    
    public double getBetaFactor()
    {
        return beta_factor; 
    }
    
    public double getActiveUserTime()
    {
        return active_user_time; 
    }
    
    public double getActiveEvaluationTime()
    {
        return active_evaluation_time; 
    }
}
