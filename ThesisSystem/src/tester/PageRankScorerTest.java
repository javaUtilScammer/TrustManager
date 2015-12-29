import java.util.*;
import java.io.*;

public class PageRankScorerTest{

	BufferedReader in;
	StringBuilder sb;
	int N;
	boolean flag;
	double default_score, rating_scale;
	ArrayList<AccountTest> accList;
	ArrayList<ContributionTest> conList;
	ArrayList<EvaluationTest> evaList;

	public static void main(String[] args) throws Exception{
		new PageRankScorerTest();
	}

	public PageRankScorerTest() throws Exception{
		in = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		readConfig();
		accList = new ArrayList<AccountTest>();
		conList = new ArrayList<ContributionTest>();
		evaList = new ArrayList<EvaluationTest>();
		N = 0;
		flag = true;
		while(flag) readInput();
	}

	public void readConfig() throws Exception{
		String[] g = in.readLine().split(" ");
		default_score = Double.parseDouble(g[0]);
		rating_scale = Double.parseDouble(g[1]);
	}

	public void readInput() throws Exception{
		String[] g = in.readLine().split(" ");
		char f = g[0].charAt(0);
		if(f=='a'){
			accList.add(new AccountTest(g[1], default_score));
			N++;
		}
		else if(f=='c'){
			int ind = Integer.parseInt(g[2]);
			conList.add(new ContributionTest(g[1], ind, accList.get(ind-1).rating));
			N++;
		}
		else if(f=='e'){
			int ind = Integer.parseInt(g[2]);
			evaList.add(new EvaluationTest(g[1],ind,Integer.parseInt(g[3]),Double.parseDouble(g[4]), default_score));
			accList.get(ind-1).list.add(evaList.size());
			N++;
			calculateScores();
		}
		else flag = false;
	}

	public void calculateScores(){
		double[] scores = new double[N];
		double[][] matrix = new double[N][N];
		int n = 0;
		int A = accList.size(), C = conList.size(), E = evaList.size();
		for(int i=0;i<A; i++) scores[n++] = accList.get(i).rating;
		for(int i=0;i<C; i++) scores[n++] = conList.get(i).score_validity;
		for(int i=0;i<E; i++) scores[n++] = evaList.get(i).trust_rating;
		System.out.println(Arrays.toString(scores));
		int offset = A+C;
		for(int i=0; i<A; i++){
			AccountTest as = accList.get(i);
			for(int o=0; o<as.list.size(); o++){
				int m = as.list.get(o)+offset;
				matrix[i][m-1] = 1.0/as.list.size();
			}
		}
		offset = A;
		for(int i=0; i<C; i++){
			ContributionTest cs = conList.get(i);
			matrix[offset+i][cs.account_id-1] = 1.0;
		}
		offset = A+C;
		for(int i=0; i<E; i++){
			EvaluationTest es = evaList.get(i);
			matrix[offset+i][A+es.contribution_id-1] = 1.0;
		}
		// System.out.println();
		// for(int i=0; i<N; i++) System.out.println(Arrays.toString(matrix[i]));
		// System.out.println();
		System.out.println(Arrays.toString(matrixMult(matrix, scores)));
		System.out.println();
	}

	public double[] matrixMult(double[][] mat, double[] vec){
		int n = mat.length;
		// System.out.println(n);
		double[] ret = new double[vec.length];
		for(int i=0; i<n; i++){
			double sum = 0;
			for(int o=0; o<n; o++)sum+=(mat[i][o]*vec[o]);
			ret[i] = sum;
		}
		return ret;
	}
}