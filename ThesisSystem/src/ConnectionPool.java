
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
 *
 * @author Migee
 */
public class ConnectionPool {
    String url;
    ArrayDeque<Connection> pool;
    final int capacity = 50;
    
    public ConnectionPool(String url){
        this.url = url;
        pool = new ArrayDeque<Connection>();
        try{
            for(int i=0; i<capacity; i++) pool.offer(DriverManager.getConnection(url));
        }
        catch(SQLException e){ e.printStackTrace();}
    }
    
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
    
    public void returnConnection(Connection con){
        pool.offer(con);
    }
}
