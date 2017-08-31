package ig.com.digitalmandi.retrofit;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitCallBack<T> implements Callback<T> {

    private BaseActivity mBaseActivity;
    private boolean mProgressDialogShown;

    public RetrofitCallBack(AppCompatActivity pActivity) {
        this(pActivity, true);
    }

    public RetrofitCallBack(AppCompatActivity pActivity, boolean pProgressShown) {
        mProgressDialogShown = pProgressShown;
        mBaseActivity = (BaseActivity) pActivity;
        showOrHideDialog(true);
    }

    public abstract void onSuccess(T pResponse, BaseActivity pBaseActivity);

    public abstract void onFailure(String pErrorMsg);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (mBaseActivity != null && !mBaseActivity.isFinishing()) {
            showOrHideDialog(false);
            if (response == null || response.body() == null) {
                if (response != null) {
                    onFailure(response.errorBody() != null ? response.errorBody().toString() : mBaseActivity.getString(R.string.string_error_in_on_response));
                } else {
                    onFailure(mBaseActivity.getString(R.string.string_error_in_on_response));
                }
            } else {
                onSuccess(response.body(), mBaseActivity);
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (mBaseActivity != null && !mBaseActivity.isFinishing()) {
            showOrHideDialog(false);
            String pErrorMsg = t.getMessage() != null ? t.getMessage() : mBaseActivity.getString(R.string.string_error_in_on_failure);
            pErrorMsg = mBaseActivity.getString(R.string.please_contact_app_admin_for_better_support, pErrorMsg);
            onFailure(pErrorMsg);
            if (mProgressDialogShown) {
                getErrorAlert(mBaseActivity, mBaseActivity.getString(R.string.app_name), pErrorMsg).show();
            }
        }
    }

    private AlertDialog.Builder getErrorAlert(AppCompatActivity pActivity, String pTitle, String pMessage) {
        AlertDialog.Builder errorAlertDialog = new AlertDialog.Builder(pActivity);
        errorAlertDialog.setTitle(pTitle);
        errorAlertDialog.setMessage(pMessage);
        errorAlertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        errorAlertDialog.setPositiveButton(R.string.string_report, null);
        return errorAlertDialog;
    }

    private void showOrHideDialog(boolean pViewShowOrHide) {
        if (!mProgressDialogShown) {
            mBaseActivity.showOrHideProgressBar(pViewShowOrHide);
        } else {
            mBaseActivity.showOrHideProgressDialog(pViewShowOrHide);
        }
    }
}
