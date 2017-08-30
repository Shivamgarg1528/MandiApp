package ig.com.digitalmandi.activity.common;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.beans.response.common.LoginResponse;
import ig.com.digitalmandi.utils.AppSharedPrefs;
import ig.com.digitalmandi.utils.Utils;

public class SplashActivity extends BaseActivity implements Runnable {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler.postDelayed(this, 1500);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(this);
    }

    @Override
    public void run() {
        LoginResponse.LoginUser loginUserModel = AppSharedPrefs.getInstance(mBaseActivity).getLoginUserModel();
        if (loginUserModel != null)
            Utils.onActivityStart(mBaseActivity, true, null, null, SyncActivity.class);
        else
            Utils.onActivityStart(mBaseActivity, true, null, null, LoginActivity.class);
    }
}
