package ig.com.digitalmandi.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Patterns;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.BaseDialog;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.common.ForgotPasswordReqModel;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.toast.ToastMessage;

/**
 * Created by shiva on 10/10/2016.
 */

public class ForgotPasswordDialog extends BaseDialog {

    @BindView(R.id.mEditTextEmail)
    AppCompatEditText mEditTextEmail;
    @BindView(R.id.mButtonSubmit)
    AppCompatButton mButtonSubmit;

    public ForgotPasswordDialog(Context context, boolean isOutSideTouch, boolean isCancelable, int layoutId) {
        super(context,isOutSideTouch, isCancelable, layoutId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forgot_password);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.mButtonSubmit)
    public void onClick() {
        String emailAddress = mEditTextEmail.getText().toString().trim();
        if (emailAddress.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            getmRunningActivity().showToast(ToastMessage.EMAIL_ADDRESS);
            return;
        }

        ForgotPasswordReqModel forgotPasswordReqModel = new ForgotPasswordReqModel();
        forgotPasswordReqModel.setUserEmailAddress(emailAddress);

        getmRunningActivity().apiEnqueueObject = RetrofitWebService.getInstance().getInterface().forgotPassword(forgotPasswordReqModel);
        getmRunningActivity().apiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(getmRunningActivity()) {


            @Override
            public void yesCall(EmptyResponse response, ParentActivity weakRef) {
                if (VerifyResponse.isResponseOk(response)) {
                    dismiss();
                    getmRunningActivity().showToast(ToastMessage.PASSWORD_SENT_);
                }
                else
                    getmRunningActivity().showToast(ToastMessage.EMAIL_ADDRESS);
            }

            @Override
            public void noCall(Throwable error) {}
        });
    }
}
