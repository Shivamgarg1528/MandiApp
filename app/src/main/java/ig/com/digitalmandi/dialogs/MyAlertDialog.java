package ig.com.digitalmandi.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import ig.com.digitalmandi.interfaces.OnAlertDialogCallBack;

/**
 * Created by Shivam.Garg on 27-10-2016.
 */

public class MyAlertDialog {

    public static void onShowAlertDialog(Context mHostActivity, String message, String veText, String negativeTxt, boolean showNegative, final OnAlertDialogCallBack listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mHostActivity);
        builder.setTitle(message);
        if (showNegative) {
            builder.setNegativeButton(negativeTxt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listener.onNegative(dialogInterface,i);
                }
            });
        }

        builder.setPositiveButton(veText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onPositive(dialogInterface,i);
            }
        });

        builder.show();
    }

}
