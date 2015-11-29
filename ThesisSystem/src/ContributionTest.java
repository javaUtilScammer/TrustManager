class ContributionTest{
    int account_id, state;
    double score_validity;
    String created_at, type, name;

    public ContributionTest(int id, double sv, int s){
        account_id = id;
        score_validity = sv;
        state = s;
        type = "contribution";
    }

    public ContributionTest(String id, String sv, String s){
        account_id = Integer.parseInt(id);
        score_validity = Double.parseDouble(sv);
        state = Integer.parseInt(s);
        type = "contribution";
    }

    public ContributionTest(String n, int id, double sv){
    	name = n;
    	account_id = id;
        score_validity = sv;
        state = 1;
        type = "contribution";
    }
}