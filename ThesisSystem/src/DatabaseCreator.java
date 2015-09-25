
import java.sql.Connection;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hadrianang
 */
public class DatabaseCreator {
    Configuration conf; 
    Connection conn; 
    String key; 
    public DatabaseCreator(Configuration conf, Connection conn)
    {
        this.conf = conf; 
        this.conn = conn;
	key = generateKey(); 
    }
    
    public Connection getConnection()
    {
	return conn; 
    }
    
    public String createDatabase()
    {
        try{
            Statement st = conn.createStatement(); 
            st.executeUpdate("CREATE DATABASE " + conf.getClientName()); 
            return key; 
        }catch(Exception e)
        {
            return null; 
        }
    }
    
    public boolean createTables()
    {
	try{
	    return true; 
	}catch(Exception e)
	{
	    return false; 
	}
    }
    
    private String generateKey()
    {
	return genRandomString(25); 
    }
    
    private static String genRandomString(int length)
    {
	StringBuilder sb = new StringBuilder(); 
	for(int i=0; i<length; i++)
	{
	    double numLett = Math.random(); 
	    if(numLett<.60)
	    {
		double cap = Math.random(); 
		int offset = cap > 0.50 ? (int)'a' : (int)'A';
		int lett = (int)(Math.random()*26); 
		char ins = (char)(lett+offset); 
		sb.append(ins); 
	    }else
	    {
		int offset = (int)(Math.random()*9);
		char ins = (char)('0' + offset); 
		sb.append(ins); 
	    }
	}
	return sb.toString(); 
    }
}
