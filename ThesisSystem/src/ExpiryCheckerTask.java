import java.util.*;

/**
 * This class calls the ClientInterface to check on a certain Contribution when it expires.
 * @author Migee
 */
public class ExpiryCheckerTask extends TimerTask{
    Contribution contribution;
    ClientInterface intrface;

    public ExpiryCheckerTask(Contribution c, ClientInterface ci){
		contribution = c;
		intrface = ci;
    }

    public void run(){
    	if(contribution.getState()==0) intrface.rejectContribution(contribution);
    }
}
