
import java.sql.Connection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Hadrian
 */
public abstract class ComponentBuilder
{
    Connection conn;
    public void setConnection(Connection conn)
    {
        System.out.println("WHYYYY");
	this.conn = conn;
        System.out.println("PLEEASE");
    }
    
    public Connection releaseConnection()
    {
	Connection temp = conn; 
	conn = null;
	return temp; 
    }
}
