package ig.com.digitalmandi.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.bean.response.LoginResponse;
import ig.com.digitalmandi.util.AppSharedPrefs;
import ig.com.digitalmandi.util.Helper;

public class SplashActivity extends BaseActivity implements Runnable {

    private final Handler mHandler = new Handler();

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
            Helper.onActivityStart(mBaseActivity, true, null, null, SyncActivity.class);
        else
            Helper.onActivityStart(mBaseActivity, true, null, null, LoginActivity.class);
    }
}
