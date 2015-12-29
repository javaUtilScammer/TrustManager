class EvaluationTest{
    int account_id, contribution_id;
    double rating, trust_rating;
    String created_at, type, name;

    public EvaluationTest(int ai, int ci, double r){
        account_id = ai;
        contribution_id = ci;
        rating = r;
        type = "evaluation";
    }

    public EvaluationTest(String ai, String ci, String r){
        account_id = Integer.parseInt(ai);
        contribution_id = Integer.parseInt(ci);
        rating = Double.parseDouble(r);
        type = "evaluation";
    }

    public EvaluationTest(String n, int ai, int ci, double r, double tr){
        name = n;
        account_id = ai;
        contribution_id = ci;
        rating = r;
        trust_rating = tr;
        type = "evaluation";
    }
}