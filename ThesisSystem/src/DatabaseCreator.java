
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
    String db_name; 
    Connection conn; 
    public DatabaseCreator(String db_name, Connection conn)
    {
        this.db_name = db_name; 
        this.conn = conn; 
    }
    
    public boolean createDatabase()
    {
        try{
            Statement st = conn.createStatement(); 
            st.executeUpdate("CREATE DATABASE " + db_name); 
            return true; 
        }catch(Exception e)
        {
            return false; 
        }
    }
}
