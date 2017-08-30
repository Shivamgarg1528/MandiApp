package ig.com.digitalmandi.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.util.Patterns;
import android.view.View;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.base_package.BaseDialog;
import ig.com.digitalmandi.beans.request.common.ForgotPasswordRequest;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.utils.Utils;

public class ForgotPasswordDialog extends BaseDialog implements View.OnClickListener {

    private AppCompatEditText mEditTxtEmail;

    public ForgotPasswordDialog(@NonNull BaseActivity pBaseActivity) {
        super(pBaseActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_forgot_password);
        findViewById(R.id.layout_forgot_password_btn_submit).setOnClickListener(this);
        mEditTxtEmail = (AppCompatEditText) findViewById(R.id.layout_forgot_password_edt_email);
    }

    @Override
    public void onClick(View v) {

        String emailAddress = mEditTxtEmail.getText().toString().trim();
        if (Utils.isEmpty(emailAddress) || !Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            mBaseActivity.showToast(mBaseActivity.getString(R.string.enter_valid_email_address));
            return;
        }

        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setUserEmailAddress(emailAddress);

        mBaseActivity.mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().forgotPassword(forgotPasswordRequest);
        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mBaseActivity) {
            @Override
            public void onSuccess(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse)) {
                    dismiss();
                    mBaseActivity.showToast(mBaseActivity.getString((R.string.string_password_has_been_sent)));
                } else {
                    mBaseActivity.showToast(mBaseActivity.getString(R.string.enter_valid_email_address));
                }
            }

            @Override
            public void onFailure(String pErrorMsg) {
            }
        });
    }
}
