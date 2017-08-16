package ig.com.digitalmandi.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.common.LoginReqModel;
import ig.com.digitalmandi.beans.response.common.LoginResModel;
import ig.com.digitalmandi.dialogs.ForgotPasswordDialog;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitConstant;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.services_parsing.Parsing;
import ig.com.digitalmandi.toast.ToastMessage;
import ig.com.digitalmandi.utils.Utils;

public class LoginActivity extends ParentActivity {

    private static final int REQUEST_SIGN_UP = 0;
    @BindView(R.id.mEditTextEmail)
    AppCompatEditText inputEmail;
    @BindView(R.id.mEditTextPassword)
    AppCompatEditText inputPassword;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.forgotPassword)
    AppCompatTextView linkSignUp;
    @BindView(R.id.signUpButton)
    ImageView signUpButton;
    Unbinder unbinderw;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (unbinderw != null)
            unbinderw.unbind();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_login);
        unbinderw = ButterKnife.bind(this);
        setTitle(getString(R.string.login_title));
    }

    public void doLogin() {

        Utils.onHideSoftKeyBoard(mRunningActivity, inputEmail);
        Utils.onHideSoftKeyBoard(mRunningActivity, inputPassword);

        if (!isInputOk()) {
            return;
        }

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString().trim();

        LoginReqModel loginModel = new LoginReqModel();
        loginModel.setUserEmailAddress(email);
        loginModel.setUserPassword(Utils.onConvertIntoBase64(password));
        loginModel.setDeviceId(Utils.getDeviceId(mRunningActivity));
        loginModel.setDeviceType(RetrofitConstant.ANDROID_DEVICE);
        loginModel.setDeviceToken("");

        apiEnqueueObject = RetrofitWebService.getInstance().getInterface().loginUser(loginModel);
        apiEnqueueObject.enqueue(new RetrofitCallBack<LoginResModel>(mRunningActivity) {

            @Override
            public void yesCall(LoginResModel response, ParentActivity weakRef) {
                if (VerifyResponse.isResponseOk(response, true)) {
                    Parsing.parseLoginUserData(weakRef, response.getResult().get(0));
                    onActivityResult(REQUEST_SIGN_UP, RESULT_OK, null);
                } else
                    weakRef.showToast(ToastMessage.LOGIN_FAILED);
            }

            @Override
            public void noCall(Throwable error) {}
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SIGN_UP) {
                Utils.onActivityStart(mRunningActivity, true, new int[]{}, null, SyncActivity.class);
            }
        }
    }

    public boolean isInputOk() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast(ToastMessage.EMAIL_ADDRESS);
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            showToast(ToastMessage.PASSWORD_SIX_);
            return false;
        }
        return true;
    }

    @OnClick({R.id.btn_login, R.id.forgotPassword, R.id.signUpButton})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_login:
                doLogin();
                break;

            case R.id.forgotPassword:
                ForgotPasswordDialog dialog = new ForgotPasswordDialog(mRunningActivity, true, true, R.layout.layout_forgot_password);
                dialog.show();
                break;

            case R.id.signUpButton:
                Utils.onHideSoftKeyBoard(mRunningActivity, inputEmail);
                Utils.onHideSoftKeyBoard(mRunningActivity, inputPassword);
                Utils.onActivityStartForResult(mRunningActivity, false, new int[]{}, null, SignUpActivity.class, REQUEST_SIGN_UP);
                break;
        }
    }
}

