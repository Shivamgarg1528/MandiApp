package ig.com.digitalmandi.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.util.Utils;

public class MyAlertDialog {

    public static void onShowAlertDialog(Context pContext, String pMessage, String pPositiveBtnText, String pNegativeBtnText, DialogInterface.OnClickListener pListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
        builder.setTitle(pMessage);
        builder.setPositiveButton(pPositiveBtnText, pListener);
        if (!Utils.isEmpty(pNegativeBtnText)) {
            builder.setNegativeButton(pNegativeBtnText, pListener);
        }
        builder.show();
    }

    public static void onShowAlertDialog(Context pContext, String pMessage, boolean pNegativeBtn, DialogInterface.OnClickListener pListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
        builder.setTitle(pMessage);
        builder.setPositiveButton(pContext.getString(R.string.string_continue), pListener);
        if (pNegativeBtn) {
            builder.setNegativeButton(pContext.getString(R.string.string_leave), pListener);
        }
        builder.show();
    }
}
