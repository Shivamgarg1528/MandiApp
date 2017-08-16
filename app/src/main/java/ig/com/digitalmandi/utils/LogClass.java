package ig.com.digitalmandi.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class LogClass {
    /**
     * set this to true (for testing and debugging) to show the application
     * lodgs.
     */
    public static boolean DEBUG = true;

    public static void i(String tag, String msg) {
        if (DEBUG)
            if (msg != null)
                Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            if (msg != null)
                Log.e(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            if (msg != null)
                Log.d(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG)
            if (msg != null)
                Log.w(tag, msg);
    }

    public static void showToast(Context context, String message) {
        if (DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
