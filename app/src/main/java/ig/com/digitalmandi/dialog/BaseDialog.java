package ig.com.digitalmandi.dialog;

import android.app.Dialog;
import android.view.View;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.callback.EventCallback;

abstract class BaseDialog extends Dialog implements View.OnClickListener {

    protected final BaseActivity mBaseActivity;
    protected final EventCallback mEventCallback;

    BaseDialog(BaseActivity pBaseActivity, EventCallback pEventCallback) {
        super(pBaseActivity, R.style.AlertDialog_Slide);
        mBaseActivity = pBaseActivity;
        mEventCallback = pEventCallback;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
