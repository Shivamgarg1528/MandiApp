package ig.com.digitalmandi.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Patterns;
import android.view.View;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.beans.request.common.LoginRequest;
import ig.com.digitalmandi.beans.response.common.LoginResponse;
import ig.com.digitalmandi.dialogs.ForgotPasswordDialog;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.utils.AppConstant;
import ig.com.digitalmandi.utils.AppSharedPrefs;
import ig.com.digitalmandi.utils.Utils;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_SIGN_UP = 0;

    private AppCompatEditText mEdtTxtEmail;
    private AppCompatEditText mEdtTxtPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setToolbar(false);

        setTitle(getString(R.string.login_title));

        mEdtTxtEmail = (AppCompatEditText) findViewById(R.id.activity_login_edt_email_address);
        mEdtTxtPassword = (AppCompatEditText) findViewById(R.id.activity_login_edt_password);

        findViewById(R.id.activity_login_btn_login).setOnClickListener(this);
        findViewById(R.id.activity_login_btn_forgot_password).setOnClickListener(this);
        findViewById(R.id.activity_login_btn_signup).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SIGN_UP) {
                Utils.onActivityStart(mBaseActivity, true, null, null, SyncActivity.class);
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_login_btn_login:
                appLogin();
                break;

            case R.id.activity_login_btn_forgot_password:
                ForgotPasswordDialog dialog = new ForgotPasswordDialog(this);
                dialog.show();
                break;

            case R.id.activity_login_btn_signup:
                Utils.onHideSoftKeyBoard(mBaseActivity, mEdtTxtEmail);
                Utils.onHideSoftKeyBoard(mBaseActivity, mEdtTxtPassword);
                Utils.onActivityStartForResult(mBaseActivity, false, null, null, SignUpActivity.class, REQUEST_CODE_SIGN_UP);
                break;
        }
    }

    private void appLogin() {

        Utils.onHideSoftKeyBoard(this, mEdtTxtEmail);
        Utils.onHideSoftKeyBoard(this, mEdtTxtPassword);

        String emailAddress = mEdtTxtEmail.getText().toString();
        String password = mEdtTxtPassword.getText().toString();

        if (Utils.isEmpty(emailAddress) || !Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            showToast(getString(R.string.string_enter_valid_email_address));
            return;
        }
        if (Utils.isEmpty(password) || password.length() < 6) {
            showToast(getString(R.string.string_password_at_least_six_char_long));
            return;
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserEmailAddress(emailAddress);
        loginRequest.setUserPassword(Utils.getBase64String(password));
        loginRequest.setDeviceId(Utils.getDeviceId(this));
        loginRequest.setDeviceType(AppConstant.ANDROID_DEVICE);
        loginRequest.setDeviceToken("");

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().loginUser(loginRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<LoginResponse>(this) {
            @Override
            public void onSuccess(LoginResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, true)) {
                    AppSharedPrefs.getInstance(mBaseActivity).setLoginUserModel(pResponse.getResult().get(0));
                    onActivityResult(REQUEST_CODE_SIGN_UP, RESULT_OK, null);
                } else
                    showToast(getString(R.string.string_login_failed));
            }

            @Override
            public void onFailure(String pErrorMsg) {
            }
        });
    }
}

