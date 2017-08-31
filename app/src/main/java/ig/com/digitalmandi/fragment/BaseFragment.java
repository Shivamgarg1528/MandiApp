package ig.com.digitalmandi.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.response.LoginResponse;
import ig.com.digitalmandi.util.AppSharedPrefs;

public abstract class BaseFragment<T> extends Fragment {

    protected BaseActivity mBaseActivity;
    protected View mRootView;

    protected List<T> mDataList = new ArrayList<>(0);
    protected List<T> mBackUpList = new ArrayList<>(0);
    protected LoginResponse.LoginUser mLoginUser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AppCompatActivity)
            mBaseActivity = (BaseActivity) context;

        setRetainInstance(true);
        setHasOptionsMenu(true);
        mLoginUser = AppSharedPrefs.getInstance(mBaseActivity).getLoginUserModel();
    }

    @Override
    public void onDetach() {
        if (mBaseActivity != null && mBaseActivity.mApiEnqueueObject != null) {
            mBaseActivity.mApiEnqueueObject.cancel();
        }
        super.onDetach();
    }

    protected void disableTouchEvent() {
        mBaseActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void enableTouchEvent() {
        mBaseActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
