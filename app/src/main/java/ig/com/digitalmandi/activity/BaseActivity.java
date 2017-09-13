package ig.com.digitalmandi.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.bean.response.LoginResponse;
import ig.com.digitalmandi.util.AppSharedPrefs;
import ig.com.digitalmandi.util.Helper;
import retrofit2.Call;

public abstract class BaseActivity<T> extends AppCompatActivity {

    protected final List<T> mDataList = new ArrayList<>();
    public Call mApiEnqueueObject;
    protected BaseActivity mBaseActivity;
    protected Toolbar mToolBar;
    protected LoginResponse.LoginUser mLoginUser;
    private AppCompatTextView mTextViewNoInternet;
    private AlertDialog mProgressDialog;

    private Handler mHandler = new Handler();
    private BroadcastReceiver mBroadcastReceiver;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (Helper.isInternetOn(mBaseActivity)) {
                mTextViewNoInternet.setVisibility(View.GONE);
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseActivity = new WeakReference<>(this).get();
        mLoginUser = AppSharedPrefs.getInstance(mBaseActivity).getLoginUserModel();
        registerBroadcast();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (mApiEnqueueObject != null) {
            mApiEnqueueObject.cancel();
        }
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        super.onDestroy();
    }

    private void registerBroadcast() {
        if (mBroadcastReceiver == null) {
            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (intent.getAction()) {
                        case ConnectivityManager.CONNECTIVITY_ACTION: {
                            if (mTextViewNoInternet != null) {
                                if (!Helper.isInternetOn(mBaseActivity)) {
                                    mTextViewNoInternet.setVisibility(View.VISIBLE);
                                    mTextViewNoInternet.setText("Please Check Your Internet Connection");
                                    mTextViewNoInternet.setBackgroundResource(android.R.color.holo_red_dark);
                                } else {
                                    mTextViewNoInternet.setText("Internet Available");
                                    mTextViewNoInternet.setBackgroundResource(android.R.color.holo_green_dark);
                                }
                                mHandler.removeCallbacks(mRunnable);
                                mHandler.postDelayed(mRunnable, 1000);
                            }
                        }
                        break;
                    }
                }
            };
        }
        registerReceiver(mBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void showOrHideProgressBar(boolean pShownOrHide) {
        try {
            ProgressBar progressBar = findViewById(R.id.layout_common_list_progress_bar);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(pShownOrHide ? View.VISIBLE : View.INVISIBLE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showOrHideProgressDialog(boolean pShownOrHide) {
        if (mProgressDialog == null) {
            mProgressDialog = new SpotsDialog(mBaseActivity, R.style.AlertDialog_Transparent);
            mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mProgressDialog.setCancelable(false);
        }
        if (pShownOrHide) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    protected void setToolbar(boolean pHomeUpEnable) {
        mToolBar = findViewById(R.id.toolbar);
        mTextViewNoInternet = findViewById(R.id.toolbar_no_internet);
        setSupportActionBar(mToolBar);
        if (pHomeUpEnable && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void showToast(String pMessage) {
        if (mToolBar != null) {
            Snackbar snackbar = Snackbar.make(mToolBar, pMessage, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
