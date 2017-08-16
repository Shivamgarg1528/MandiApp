package ig.com.digitalmandi.interfaces;

import android.content.DialogInterface;

/**
 * Created by Shivam.Garg on 27-10-2016.
 */

public interface OnAlertDialogCallBack {

    public void onNegative(DialogInterface dialogInterface, int i);
    public void onPositive(DialogInterface dialogInterface, int i);

}
