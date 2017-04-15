package exceptions;

/**
 * Created by Palash on 4/15/2017.
 */
public class DoesNotMeetMinCriteriaException extends Exception {
    public DoesNotMeetMinCriteriaException(String msg){
        super(msg);
    }
}
