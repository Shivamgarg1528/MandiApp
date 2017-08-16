package ig.com.digitalmandi.retrofit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.ParentActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shivam.garg on 29-08-2016.
 */

public abstract class RetrofitCallBack<T> implements Callback<T> {

    private String TAG = this.getClass().getCanonicalName();
    private ParentActivity weakRef;
    private AlertDialog dialog;
    private boolean needToShow = true;
    private static android.support.v7.app.AlertDialog errorDialog;

    public abstract void yesCall(T response, ParentActivity weakRef);

    public abstract void noCall(Throwable error);


    private static void getAlertSingleInstance(AppCompatActivity reference, String message, String title) {

        if (errorDialog == null) {
            errorDialog = new android.support.v7.app.AlertDialog.Builder(reference).create();
        }
        errorDialog.setTitle(title);
        errorDialog.setMessage(message);
        errorDialog.setIcon(android.R.drawable.ic_dialog_alert);
        errorDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "Report", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }

    public RetrofitCallBack(AppCompatActivity activity) {
        this(activity, true);
    }

    public RetrofitCallBack(AppCompatActivity activity, boolean shown) {
        weakRef = (ParentActivity) activity;
        dialog = new SpotsDialog(weakRef, R.style.Custom);
        needToShow = shown;
        showProgressBar();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    private void hideProgressBar() {

        if (!needToShow)
            weakRef.onShowOrHideBar(false);

        if (needToShow && dialog != null && dialog.isShowing())
            dialog.dismiss();

    }

    private void showProgressBar() {

        if (!needToShow)
            weakRef.onShowOrHideBar(true);

        if (needToShow && dialog != null && !dialog.isShowing())
            dialog.show();
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (weakRef != null) {
            hideProgressBar();
            if (response.body() != null)
                yesCall(response.body(), weakRef);
            else {
                try {
                    noCall(new Throwable(response.errorBody().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (weakRef != null) {
            weakRef.showToast(weakRef.getString(R.string.please_contact_app_admin_for_better_support, t.getMessage()));
            hideProgressBar();
            noCall(new Throwable(weakRef.getString(R.string.please_contact_app_admin_for_better_support, t.getMessage())));
            getAlertSingleInstance(weakRef, weakRef.getString(R.string.please_contact_app_admin_for_better_support, t.getMessage()), weakRef.getString(R.string.app_name));
            if (errorDialog != null && !errorDialog.isShowing() && needToShow) {
                errorDialog.show();
            }
        }
    }
}
