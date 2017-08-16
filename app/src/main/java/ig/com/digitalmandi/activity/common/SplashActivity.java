package ig.com.digitalmandi.activity.common;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.supplier.SupplierHomeActivity;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.MyPrefrences;
import ig.com.digitalmandi.utils.Utils;

public class SplashActivity extends ParentActivity {

    private Handler mHandler;
    private Runnable removeCallbacks = new Runnable() {
        @Override
        public void run() {
            if (MyPrefrences.getBooleanPrefrences(ConstantValues.IS_LOGIN, mRunningActivity))
                Utils.onActivityStart(mRunningActivity, true, new int[]{}, null, SupplierHomeActivity.class);
            else
                Utils.onActivityStart(mRunningActivity, true, new int[]{}, null, LoginActivity.class);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_splash);
        mHandler = new Handler();
        mHandler.postDelayed(removeCallbacks, 1500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandler != null) {
            mHandler.removeCallbacks(removeCallbacks);
        }
    }
}
