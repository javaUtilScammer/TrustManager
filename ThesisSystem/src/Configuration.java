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
    private String client_name, validation_type;
    private int validation_time;

    public Configuration(String client_name, String validation_type, int validation_time)
    {
        this.client_name = client_name;
	this.validation_type = validation_type;
        this.validation_time = validation_time; 
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
}
