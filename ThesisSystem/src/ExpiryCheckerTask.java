import java.util.*;

/**
 * This class calls the ClientInterface to check on a certain Contribution when it expires.
 * @author Migee
 */
public class ExpiryCheckerTask extends TimerTask{
    int contributionID;
    ClientInterface intrface;

    public ExpiryCheckerTask(int cid, ClientInterface ci){
		contributionID = cid;
		intrface = ci;
    }

    public void run(){
    	intrface.checkContribution(contributionID);
    }
}
