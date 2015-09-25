
import java.util.HashMap;



public class ClientInterface {
    private final String client_name, validation_type, key;
    private final int validation_time;
    private final double default_score, rating_scale, score_validity;
    final ConnectionPool pool;
    final ComponentFactory compFactory;
    private final Server server;
    private HashMap<Integer,Account> accMap;
    private HashMap<Integer,Contribution> contMap;
    private HashMap<Integer,Evaluation> evalMap;
    
    public ClientInterface(String key, Configuration config, String url, Server server){
        this.key = key;
        pool = new ConnectionPool(url,server.getUsername(),server.getPassword());
        client_name = config.getClientName();
        validation_type = config.getValidationType();
        validation_time = config.getValidationTime();
        default_score = config.getDefaultScore();
        score_validity = default_score;
        rating_scale = config.getRatingScale();
        this.server = server;
        accMap = new HashMap<Integer, Account>();
        contMap = new HashMap<Integer, Contribution>();
        evalMap = new HashMap<Integer, Evaluation>();
        server.httpserver.createContext("/"+key,new ClientInterfaceHandler(this));
        compFactory = new ComponentFactory(pool.getConnection(),this);
    }
    
    public Account getAccount(int id){
        return accMap.get(id);
    }
    
    public Contribution getContribution(int id){
        return contMap.get(id);
    }
    
    public Evaluation getEvaluation(int id){
        return evalMap.get(id);
    }
    
    public void putAccount(int id, Account ac){
        accMap.put(id, ac);
    }
    
    public void putContribution(int id, Contribution co){
        contMap.put(id, co);
    }
    
    public void putEvaluation(int id, Evaluation ev){
        evalMap.put(id, ev);
    }
}
