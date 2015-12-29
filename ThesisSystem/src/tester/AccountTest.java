import java.util.ArrayList;

class AccountTest{
    String username, type;
    double rating;
    ArrayList<Integer> list;

    public AccountTest(String u){
        username = u;
        type = "account";
    }

    public AccountTest(String u, double r){
        username = u;
        rating = r;
      	list = new ArrayList<Integer>();
        type = "account";
    }
}