
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
    }
    
    public int create(String json){
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = (HashMap<String, Object>)gson.fromJson(json, map.getClass());
        String type = (String) map.get("type");
        System.out.println("type: "+type);
        System.out.println(type.equals("account"));
        int ret = -1;
        if(type.equals("account")){
            System.out.println("troll");
            ab.setConnection(conn);
            System.out.println("hooooy");
            String username = (String) map.get("username");
            System.out.println("hooooy1");
            Timestamp created_at = Timestamp.valueOf((String)map.get("created_at"));
            System.out.println("hooooy2");
            Timestamp last_updated_at = Timestamp.valueOf((String)map.get("last_updated_at"));
            System.out.println("hooooy3");
            double trust_rating = (double) map.get("trust_rating");
            double trust_validity = (double) map.get("trust_validity");
            Account ac = ab.buildAccount(username, created_at, last_updated_at, trust_rating, trust_validity);
            intrface.putAccount(ac.getId(), ac);
            ab.releaseConnection();
            ret = 1;
        }
        else if(type.equals("contribution")){
            cb.setConnection(conn);
            int id = (int) map.get("id");
            Account contributor = intrface.getAccount(id);
            double contribution_score = (double) map.get("contribution_score");
            double score_validity = (double) map.get("score_validity");
            Timestamp created_at = Timestamp.valueOf((String)map.get("created_at"));
            int state = (int) map.get("state");
            Contribution co = cb.buildContribution(contributor,contribution_score, score_validity, created_at, state);
            intrface.putContribution(co.getId(), co);
            cb.releaseConnection();
            ret = 2;
        }
        else if(type.equals("evaluation")){
            eb.setConnection(conn);
            int accId = (int) map.get("accId");
            int conId = (int) map.get("conId");
            Account contributor = intrface.getAccount(accId);
            Contribution cont = intrface.getContribution(conId);
            double rating = (double) map.get("rating");
            Timestamp created_at = Timestamp.valueOf((String)map.get("created_at"));
            int state = (int) map.get("state");
            Evaluation ev = eb.buildEvaluation(rating, created_at, contributor, cont);
            intrface.putEvaluation(ev.getId(), ev);
            eb.releaseConnection();
            ret = 3;
        }
        System.out.println("RET: "+ret);
        return ret;
    }
    
}
