package ig.com.digitalmandi.util;

public class CheckForFloat {
    public static boolean onCheckFloat(String floatValue) {
        try {
            if (floatValue.isEmpty())
                return false;

            Float.parseFloat(floatValue);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
