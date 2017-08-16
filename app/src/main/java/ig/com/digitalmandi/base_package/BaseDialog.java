package ig.com.digitalmandi.base_package;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import ig.com.digitalmandi.R;

public abstract class BaseDialog extends Dialog {

	private View rootView;
	private LayoutInflater layoutInflater;
	protected Context mContext;
	protected ParentActivity mRunningActivity;


	public BaseDialog(Context context, int theme , int width ,int height , boolean isOutSideTouch , boolean isCancelable, int layoutId) {
		super(context, theme);
		mContext       = context;
		mRunningActivity = (ParentActivity)mContext;
		layoutInflater = LayoutInflater.from(context);
		rootView       = layoutInflater.inflate(layoutId, null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(isOutSideTouch);
		setContentView(rootView);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		getWindow().setLayout(width, height);
		setCancelable(isCancelable);
	}

	public BaseDialog(Context context,boolean isOutSideTouch , boolean isCancelable, int layoutId) {
		super(context, R.style.myCoolDialog);
		mContext         = context;
		mRunningActivity = (ParentActivity)mContext;
		layoutInflater   = LayoutInflater.from(context);
		rootView         = layoutInflater.inflate(layoutId, null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(isOutSideTouch);
		setContentView(rootView);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setCancelable(isCancelable);
	}

	@SuppressWarnings("unchecked")
	public <T> T findViewByIdAndCast(int id) {
		return (T) rootView.findViewById(id);
	}

	public ParentActivity getmRunningActivity() {
		return mRunningActivity;
	}
}
