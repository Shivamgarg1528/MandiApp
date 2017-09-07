package ig.com.digitalmandi.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.response.LoginResponse;
import ig.com.digitalmandi.util.AppSharedPrefs;

public abstract class BaseFragment extends Fragment {

    protected BaseActivity mBaseActivity;
    protected View mRootView;
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
}
