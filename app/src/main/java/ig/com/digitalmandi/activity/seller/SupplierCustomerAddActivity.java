package ig.com.digitalmandi.activity.seller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.WindowManager;

import de.hdodenhof.circleimageview.CircleImageView;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.request.seller.RegistrationRequest;
import ig.com.digitalmandi.bean.response.LoginResponse;
import ig.com.digitalmandi.dialog.ImageDialog;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class SupplierCustomerAddActivity extends BaseActivity implements ImageDialog.OnItemSelectedListener, View.OnClickListener {

    private AppCompatEditText mEditTxtName;
    private AppCompatEditText mEditTxtPhoneNumber;
    private AppCompatEditText mEditTxtEmail;
    private AppCompatEditText mEditTxtPassword;
    private AppCompatEditText mEditTxtConfirmPassword;
    private AppCompatEditText mEditTxtFirmName;
    private AppCompatEditText mEditTxtTinNumber;
    private AppCompatEditText mEditTxtLandMark;
    private AppCompatEditText mEditTxtAddress;
    private CircleImageView mCircleImageUser;

    private String mStringBase64 = "";
    private ImageDialog mDialogImagePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_activity_customer_add);
        setToolbar(true);
        setTitle(getString(R.string.add_new_customer));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mEditTxtName = (AppCompatEditText) findViewById(R.id.layout_activity_sign_up_edt_name);
        mEditTxtPhoneNumber = (AppCompatEditText) findViewById(R.id.layout_activity_sign_up_edt_phone);
        mEditTxtEmail = (AppCompatEditText) findViewById(R.id.layout_activity_sign_up_edt_email_address);
        mEditTxtPassword = (AppCompatEditText) findViewById(R.id.layout_activity_sign_up_edt_password);
        mEditTxtConfirmPassword = (AppCompatEditText) findViewById(R.id.layout_activity_sign_up_edt_confirm_password);
        mEditTxtFirmName = (AppCompatEditText) findViewById(R.id.layout_activity_sign_up_edt_firm_name);
        mEditTxtTinNumber = (AppCompatEditText) findViewById(R.id.layout_activity_sign_up_edt_tin_number);
        mEditTxtLandMark = (AppCompatEditText) findViewById(R.id.layout_activity_sign_up_edt_land_mark);
        mEditTxtAddress = (AppCompatEditText) findViewById(R.id.layout_activity_sign_up_edt_address);

        mCircleImageUser = (CircleImageView) findViewById(R.id.layout_activity_sign_up_btn_user_image);
        mCircleImageUser.setOnClickListener(this);

        findViewById(R.id.layout_activity_sign_up_btn_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.layout_activity_sign_up_btn_user_image:
                mDialogImagePicker = new ImageDialog(this, this, mCircleImageUser.getWidth(), mCircleImageUser.getHeight());
                mDialogImagePicker.show();
                break;

            case R.id.layout_activity_sign_up_btn_submit:
                doSignUp();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDialogImagePicker.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRemoveItemTap() {
        mCircleImageUser.setImageResource(R.drawable.ic_user_default);
        mStringBase64 = "";
    }

    @Override
    public void onCancelItemTap() {

    }

    @Override
    public void onImageReceived(Bitmap pBitmap) {
        mStringBase64 = Helper.getStringImage(pBitmap);
        mCircleImageUser.setImageBitmap(pBitmap);
    }

    private void doSignUp() {
        if (!checkInput()) {
            return;
        }
        String name = mEditTxtName.getText().toString();
        String email = mEditTxtEmail.getText().toString();
        String password = mEditTxtPassword.getText().toString();
        String tinNumber = mEditTxtTinNumber.getText().toString();
        String firmName = mEditTxtFirmName.getText().toString();
        String address = mEditTxtAddress.getText().toString();
        String landMark = mEditTxtLandMark.getText().toString();
        String phoneNo = mEditTxtPhoneNumber.getText().toString();

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUserName(name);
        registrationRequest.setUserPassword(Helper.getBase64String(password));
        registrationRequest.setDeviceType(AppConstant.ANDROID_DEVICE);
        registrationRequest.setDeviceId(Helper.getDeviceId(mBaseActivity));
        registrationRequest.setDeviceToken("");
        registrationRequest.setUserAddress(address);
        registrationRequest.setUserFirmName(firmName);
        registrationRequest.setUserTinNumber(tinNumber);
        registrationRequest.setUserLandMark(landMark);
        registrationRequest.setUserEmailAddress(email);
        registrationRequest.setUserMobileNo(phoneNo);
        registrationRequest.setUserPicBase64(mStringBase64);
        registrationRequest.setUserType(String.valueOf(AppConstant.CUSTOMER));
        registrationRequest.setSellerId(mLoginUser.getSellerId());

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().registerUser(registrationRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<LoginResponse>(this) {
            @Override
            public void onResponse(LoginResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, true)) {
                    showToast(getString(R.string.successfully_registered));
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showToast(getString(R.string.string_failed_to_register_new_user));
                }
            }
        });
    }

    private boolean checkInput() {

        String name = mEditTxtName.getText().toString();
        String phoneNo = mEditTxtPhoneNumber.getText().toString();
        String email = mEditTxtEmail.getText().toString();
        String password = mEditTxtPassword.getText().toString();
        String cPassword = mEditTxtConfirmPassword.getText().toString();
        String tinNumber = mEditTxtTinNumber.getText().toString();
        String firmName = mEditTxtFirmName.getText().toString();

        if (!Helper.isPersonNameOk(name, mBaseActivity)) {
            return false;
        } else if (!Helper.isPhoneNoOk(phoneNo, mBaseActivity)) {
            return false;
        } else if (!Helper.isEmailAddressOk(email, mBaseActivity)) {
            return false;
        } else if (!Helper.isPasswordOk(password, mBaseActivity)) {
            return false;
        } else if (!Helper.isPasswordOk(cPassword, mBaseActivity)) {
            return false;
        } else if (!cPassword.equals(password)) {
            showToast(getString(R.string.string_password_match_failed));
            return false;
        } else
            return Helper.isTinNoOk(tinNumber, mBaseActivity) && Helper.isFirmOk(firmName, mBaseActivity);
    }
}
