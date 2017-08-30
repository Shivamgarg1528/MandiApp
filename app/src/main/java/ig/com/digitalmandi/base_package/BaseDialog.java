package ig.com.digitalmandi.base_package;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import ig.com.digitalmandi.R;

public abstract class BaseDialog extends Dialog {

    protected BaseActivity mBaseActivity;
    private View mRootView;

    public BaseDialog(Context context, boolean isOutSideTouch, boolean isCancelable, int layoutId) {
        super(context, R.style.AlertDialog_Slide);
        mBaseActivity = (BaseActivity) context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mRootView = layoutInflater.inflate(layoutId, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(isOutSideTouch);
        setContentView(mRootView);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCancelable(isCancelable);
    }

    public BaseDialog(BaseActivity pBaseActivity) {
        super(pBaseActivity, R.style.AlertDialog_Slide);
        mBaseActivity = pBaseActivity;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public BaseActivity getBaseActivity() {
        return mBaseActivity;
    }
}
