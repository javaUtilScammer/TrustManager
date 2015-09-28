
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
    ClientInterface intrface;
    
    public ComponentFactory(Connection con, ClientInterface ci){
        conn = con;
        intrface = ci;
        ab = new AccountBuilder();
        cb = new ContributionBuilder();
        eb = new EvaluationBuilder();
    }
    
    public int create(String json){
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = (HashMap<String, Object>)gson.fromJson(json, map.getClass());
        String type = (String) map.get("type");
        System.out.println("type: "+type);
        int ret = -1;
        if(type.equals("account")){
            ab.setConnection(conn);
            String username = (String) map.get("username");
            Timestamp created_at = Timestamp.valueOf((String)map.get("created_at"));
            Timestamp last_updated_at = Timestamp.valueOf((String)map.get("last_updated_at"));
            Double trust_rating = Double.parseDouble((String)map.get("trust_rating"));
            Double trust_validity = Double.parseDouble((String)map.get("trust_validity"));
            Account ac = ab.buildAccount(username, created_at, last_updated_at, trust_rating, trust_validity);
            intrface.putAccount(ac.getId(), ac);
            ab.releaseConnection();
            ret = ac.getId();
        }
        else if(type.equals("contribution")){
            cb.setConnection(conn);
            int id = Integer.parseInt((String)map.get("account_id"));
            Account contributor = intrface.getAccount(id);
            Double contribution_score = Double.parseDouble((String)map.get("contribution_score"));
            Double score_validity = Double.parseDouble((String)map.get("score_validity"));
            Timestamp created_at = Timestamp.valueOf((String)map.get("created_at"));
            int state = Integer.parseInt((String)map.get("state"));
            Contribution co = cb.buildContribution(contributor,contribution_score, score_validity, created_at, state);
            intrface.putContribution(co.getId(), co);
            cb.releaseConnection();
            ret = co.getId();
        }
        else if(type.equals("evaluation")){
            eb.setConnection(conn);
            int accId = Integer.parseInt((String)map.get("account_id"));
            int conId = Integer.parseInt((String)map.get("contribution_id"));
            Account contributor = intrface.getAccount(accId);
            Contribution cont = intrface.getContribution(conId);
            Double rating = Double.parseDouble((String)map.get("rating"));
            Timestamp created_at = Timestamp.valueOf((String)map.get("created_at"));
            Evaluation ev = eb.buildEvaluation(rating, created_at, contributor, cont);
            intrface.putEvaluation(ev.getId(), ev);
            eb.releaseConnection();
            ret = ev.getId();
        }
        return ret;
    }
    
}
