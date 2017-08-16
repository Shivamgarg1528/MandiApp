package ig.com.digitalmandi.utils;

/**
 * Created by Shivam.Garg on 09-11-2016.
 */

public class CheckForFloat {
    public static boolean onCheckFloat(String floatValue){
        try {
            if(floatValue.isEmpty())
                return false;

            Float.parseFloat(floatValue);
            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
