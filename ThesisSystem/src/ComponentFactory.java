
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Migee
 */
public class ComponentFactory {
    
    private Connection conn;
    private AccountBuilder ab;
    private ContributionBuilder cb;
    private EvaluationBuilder eb;
    
    public ComponentFactory(Connection con){
        conn = con;
    }
    
    public void create(String json){
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = (HashMap<String, Object>)gson.fromJson(json, map.getClass());
        String type = (String) map.get("type");
        if(type.equals("account")){
            ab.setConnection(conn);
            String username = (String) map.get("username");
            Timestamp created_at = (Timestamp) map.get("created_at");
            Timestamp last_updated_at = (Timestamp) map.get("last_updated_at");
            double trust_rating = (double) map.get("trust_rating");
            double trust_validity = (double) map.get("trust_validity");
            ab.buildAccount(username, created_at, last_updated_at, trust_rating, trust_validity);
            ab.releaseConnection();
        }
        else if(type.equals("contribution")){
            
        }
        else if(type.equals("evaluation")){
            
        }
    }
    
}
