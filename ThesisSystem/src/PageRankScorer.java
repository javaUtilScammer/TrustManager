import java.sql.Connection;
import java.util.*;

public class PageRankScorer extends Scorer{
    
    // private ArrayList<Component> compList;
	// private double[][] grid;
	private final double d = 0.85;
	private final double eps = 1e-6;
	// private Connection conn;

    public PageRankScorer(ClientInterface in){
        super(in);
        // compList = new ArrayList<Component>();
        // grid = new double[10][10];
        // conn = in.pool.getConnection();
    }

    public void addToList(Component c){
    	compList.add(c);
    }

    public void calculateScore(){
    	int N = compList.size();
    	System.out.println("VALIDATING TIME "+N);
    	double v = (1-d)/N;
    	boolean flag = true;
		double[] arr = new double[N];
    	while(true){
    		if(!flag) break;
			double[] arr2 = new double[N];
    		for(int i=0; i<N; i++){
    			Component x = compList.get(i);
    			// System.out.println(i+" "+x.type);
    			if(x instanceof Account){
    				Account a = (Account) x;
    				double temp = 0;
    				for(int o=0; o<a.contributions.size(); o++){
    					System.out.println(a.contributions.get(o).getContributionScore());
    					temp+= a.contributions.get(o).getContributionScore();
    				}
    				// System.out.println("Account temp "+temp+" "+a.contributions.size());
    				temp*=d;
    				double prev = arr[i];
    				if(Math.abs(prev-temp)<=eps) flag = false;
    				else flag = true;
    				arr2[i] = temp;
    			}
    			else if(x instanceof Contribution){
    				Contribution c = (Contribution) x;
    				double temp = 0;
    				for(int o=0; o<c.evaluations.size(); o++){
    					Evaluation e = c.evaluations.get(o);
    					if(e.getTrustRating()==0.0) temp+= e.getRating();
    					else temp+= e.getTrustRating()*e.getRating();
    				}
    				// System.out.println("CONTRIBUTION temp "+temp+" "+c.evaluations.size());
    				temp*=d;
    				double prev = arr[i];
    				if(Math.abs(prev-temp)<=eps) flag = false;
    				else flag = true;
    				arr2[i] = temp;
    			}
    			else if(x instanceof Evaluation){
    				// System.out.println("ERROR e3");
    				Evaluation e = (Evaluation) x;
    				// System.out.println("ERROR e2");
    				double temp = 0;
    				Account a = e.getCreatedBy();
    				temp = a.getTrustRating();
    				temp*=d;
    				// System.out.println("EVALUATION temp "+temp);
    				double prev = arr[i];
    				if(Math.abs(prev-temp)<=eps) flag = false;
    				// else flag = true;
    				// System.out.println("ERROR e");
    				arr2[i] = temp;
    			}
    		}
    		for(int i=0; i<N; i++){
    			Component x = compList.get(i);
    			if(x instanceof Account){
    				Account a = (Account) x;
    				a.setTrustRating(arr2[i]);
    			}
    			else if(x instanceof Contribution){
    				Contribution c = (Contribution) x;
    				c.setContributionScore(arr2[i]);
    			}
    			else{
    				Evaluation e = (Evaluation) x;
    				e.setTrustRating(arr2[i]);
    			}
    		}
    		// arr2[0] = 2.0;
    		// System.out.println(Arrays.toString(arr));
    		// System.out.println(" "+Arrays.toString(arr2));
    		arr = arr2;
    	}
    	for(int i=0; i<N; i++){
			Component x = compList.get(i);
			if(x.type == 'a'){
				Account a = (Account) x;
				a.setTrustRating(a.getTrustRating(),conn);
			}
			else if(x.type == 'a'){
				Contribution c = (Contribution) x;
				c.setContributionScore(c.getContributionScore(),conn);
			}
		}
		System.out.println(Arrays.toString(arr));
    }

    public void calculateScore(Evaluation ev, Contribution cont){
    	calculateScore();
    }
}
