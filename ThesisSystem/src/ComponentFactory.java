
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;

/**
 *
 * @author Migee
 */
public class ComponentFactory {
    
    private Connection conn;
    private AccountBuilder ab;
    private ContributionBuilder cb;
    private EvaluationBuilder eb;
    private Scorer scorer;
    ClientInterface intrface;
    private double default_score;
    private boolean intervalCheck; //if false, will create new timertask for each contribution
    
    public ComponentFactory(Connection con, ClientInterface ci, boolean ic, Scorer s){
        conn = con;
        intrface = ci;
        default_score = intrface.getDefaultScore();
        ab = new AccountBuilder();
        cb = new ContributionBuilder();
        eb = new EvaluationBuilder();
        intervalCheck = ic;
        scorer = s;
    }
    
    public int create(String json){
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = (HashMap<String, Object>)gson.fromJson(json, map.getClass());
        String type = (String) map.get("type");
        System.out.println("type: "+type);
        int ret = -1;
        if(type.equalsIgnoreCase("account")){
            ab.setConnection(conn);
            String username = (String) map.get("username");
            Timestamp created_at = new Timestamp(System.currentTimeMillis());;
            Timestamp last_updated_at = new Timestamp(System.currentTimeMillis());;
            double trust_rating = default_score;
            double trust_validity = default_score;
            Account ac = ab.buildAccount(username, created_at, trust_rating, trust_validity,0,0,0);
            // System.out.println("Account "+ac.getTrustRating());
            intrface.putAccount(ac.getId(), ac);
            intrface.addScorerComponent(ac);
            ab.releaseConnection();
            ret = ac.getId();
        }
        else if(type.equalsIgnoreCase("contribution")){
            try{
                cb.setConnection(conn);
                int id = (int) ((double) map.get("account_id"));
                Account contributor = intrface.getAccount(id);
                double contribution_score = contributor.getTrustRating();
                double score_validity = (double) map.get("score_validity");
                Timestamp created_at = new Timestamp(System.currentTimeMillis());
                int state = (int) ((double) map.get("state"));
                Contribution co = cb.buildContribution(contributor,contribution_score, score_validity, created_at, state);
                // System.out.println("Contribution "+co.getContributionScore());
                double newContScore = scorer.computeInitialScore(co);
                if(newContScore!=contribution_score) co.setContributionScore(newContScore);
                intrface.putContribution(co.getId(), co);
                intrface.addScorerComponent(co);
                cb.releaseConnection();
                ret = co.getId();
                if(!intervalCheck) intrface.addTimerTask(co);
            }
            catch(Exception e){e.printStackTrace();}
        }
        else if(type.equalsIgnoreCase("evaluation")){
            try{
                eb.setConnection(conn);
                int accId = (int)((double) map.get("account_id"));
                int conId = (int)((double) map.get("contribution_id"));
                Account contributor = intrface.getAccount(accId);
                Contribution cont = intrface.getContribution(conId);
                Double rating = (double) map.get("rating");
                Timestamp created_at = new Timestamp(System.currentTimeMillis());;
                if(contributor==null) System.out.println("NO ACC for EVAL "+accId);
                if(cont==null){
                    System.out.println("NO CONT for EVAL");
                    ret = -2;
                    return ret;
                }
                Evaluation ev = eb.buildEvaluation(rating, created_at, contributor, cont);
                // System.out.println("Evaluation "+ev.type);
                intrface.putEvaluation(ev.getId(), ev);
                intrface.addScorerComponent(ev);
                eb.releaseConnection();
                intrface.score(ev, conId);
                ret = ev.getId();
            }
            catch(Exception e){e.printStackTrace();}
        }
        return ret;
    }
    
}
