import java.sql.Connection;
import java.util.*;

public class PageRankScorer extends Scorer{
    
	private final double d = 0.85;
	private final double eps = 1e-6;
	private ArrayList<Account> accList;
	private ArrayList<Contribution> conList;
	private ArrayList<Evaluation> evaList;

    public PageRankScorer(ClientInterface in){
        super(in);
        accList = new ArrayList<Account>();
        conList = new ArrayList<Contribution>();
        evaList = new ArrayList<Evaluation>();
    }

    public void addComponent(Component c){
    	if(c instanceof Account) accList.add((Account) c);
    	else if(c instanceof Contribution) conList.add((Contribution) c);
    	else if(c instanceof Evaluation) evaList.add((Evaluation) c);
    }

    public void calculateScore(Evaluation ev, Contribution cont){
    	int Nc = conList.size(), Na = accList.size();
    	double[][] M = new double[Nc][Na];
    	for(int i=0; i<Nc; i++){
    		Contribution con = conList.get(i);
    		ArrayList<Evaluation> evals = con.getEvaluations();
    		int n = evals.size();
    		if(n==0) M[i][accList.indexOf(con.getContributor())] = 1.0;
    		for(int o=0; o<n; o++){
    			Evaluation eva = evals.get(o);
    			double rating = eva.getRating()/rating_scale;
    			Account acc = eva.getCreatedBy();
    			int ind = accList.indexOf(acc);
    			M[i][ind] = rating/n;
    		}
    	}
    	for(int i=0; i<Nc; i++) System.out.println(Arrays.toString(M[i]));
    	double[][] T = new double[Na][Na];
    	for(int i=0; i<Na; i++){
    		Account acc = accList.get(i);
    		ArrayList<Contribution> conts = acc.getContributions();
    		int n = conts.size();
    		for(int o=0; o<n; o++){
    			Contribution con = conts.get(o);
    			int ind = conList.indexOf(con);
    			for(int u=0; u<Na; u++) T[i][u] += M[ind][u];
    		}
    	}
    	for(int i=0; i<Na; i++) System.out.println(Arrays.toString(T[i]));
    }
}
