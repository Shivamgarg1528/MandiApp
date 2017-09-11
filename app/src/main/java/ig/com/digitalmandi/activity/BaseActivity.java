package ig.com.digitalmandi.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import retrofit2.Call;

public abstract class BaseActivity<T> extends AppCompatActivity {

    protected final List<T> mDataList = new ArrayList<>();
    public Call mApiEnqueueObject;
    protected BaseActivity mBaseActivity;
    protected Toolbar mToolBar;
    protected LoginResponse.LoginUser mLoginUser;
    private AlertDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseActivity = new WeakReference<>(this).get();
        mLoginUser = AppSharedPrefs.getInstance(mBaseActivity).getLoginUserModel();
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
        super.onDestroy();
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
