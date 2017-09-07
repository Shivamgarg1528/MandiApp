package ig.com.digitalmandi.dialog;

import android.app.Dialog;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;

abstract class BaseDialog extends Dialog {

    final BaseActivity mBaseActivity;

    BaseDialog(BaseActivity pBaseActivity) {
        super(pBaseActivity, R.style.AlertDialog_Slide);
        mBaseActivity = pBaseActivity;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

}
