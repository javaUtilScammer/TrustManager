
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *  The ConnectionPool simply pre initializes and stores the Connection objects for later use.
 *  @author Migee
 */
public class ConnectionPool {
    String url;
    ArrayDeque<Connection> pool;
    final int capacity = 50;
    
    /*
        @param url the url of the database
        @param user the username for the mysql account in the server machine
        @param pass the password for the mysql account in the server machine
    */
    public ConnectionPool(String url,String user, String pass){
        this.url = url;
        pool = new ArrayDeque<Connection>();
        try{
            for(int i=0; i<capacity; i++) pool.offer(DriverManager.getConnection(url,user,pass));
        }
        catch(Exception e){ e.printStackTrace();}
    }
    
    /*
        @return a Connection object from the pool
    */
    public Connection getConnection(){
        Connection ret = null;
        if(pool.size()!= 0) ret = pool.poll();
        else {
            try{
                ret = DriverManager.getConnection(url);
            }
            catch(SQLException e){ e.printStackTrace();}
        }
        return ret;
    }
    
    /*
        @param con the Connection object to be returned to the pool
    */
    public void returnConnection(Connection con){
        pool.offer(con);
    }
}
