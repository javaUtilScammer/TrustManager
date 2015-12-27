
import java.sql.Connection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Abstract class for different Builders used throughout the system
 * @author Hadrian
 */
public abstract class ComponentBuilder
{
    Connection conn;
    /**
     * sets Connection object to be used by Builder
     * @param conn Connection to be used
     */
    public void setConnection(Connection conn)
    {
	this.conn = conn;
    }
    
    /**
     * releases Connection so it can be returned to the connection pool
     * @return connection that was released for returning to the pool
     */
    public Connection releaseConnection()
    {
	Connection temp = conn; 
	conn = null;
	return temp; 
    }
}
