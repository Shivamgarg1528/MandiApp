package ig.com.digitalmandi.activity.seller;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.request.seller.RegistrationRequest;
import ig.com.digitalmandi.bean.response.LoginResponse;
import ig.com.digitalmandi.bean.response.seller.SupplierListResponse;
import ig.com.digitalmandi.dialog.ImageDialog;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.toast.ToastMessage;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.AppSharedPrefs;
import ig.com.digitalmandi.util.EditTextVerification;
import ig.com.digitalmandi.util.Utils;
import retrofit2.Call;

public class SupplierCustomerAddActivity extends BaseActivity implements ImageDialog.OnItemSelectedListener {

    private final int CAMERA_REQ_CODE = 100;
    private final int GALLERY_REQ_CODE = 101;
    @BindView(R.id.layout_activity_sign_up_edt_name)
    AppCompatEditText mEditTextName;
    @BindView(R.id.layout_activity_sign_up_edt_phone)
    AppCompatEditText mEditTextPhoneNumber;
    @BindView(R.id.activity_login_edt_email_address)
    AppCompatEditText mEditTextEmail;
    @BindView(R.id.activity_login_edt_password)
    AppCompatEditText mEditTextPassword;
    @BindView(R.id.layout_activity_sign_up_edt_confirm_password)
    AppCompatEditText mEditTextConfirmPassword;
    @BindView(R.id.layout_activity_sign_up_btn_submit)
    AppCompatButton mButtonSignUp;
    Unbinder mUnbinder;
    @BindView(R.id.layout_activity_sign_up_edt_firm_name)
    AppCompatEditText mEditTextFirmName;
    @BindView(R.id.layout_activity_sign_up_edt_tin_number)
    AppCompatEditText mEditTextTinNumber;
    @BindView(R.id.layout_activity_sign_up_edt_land_mark)
    AppCompatEditText mEditTextLandMark;
    @BindView(R.id.layout_activity_sign_up_edt_address)
    AppCompatEditText mEditTextAddress;
    @BindView(R.id.layout_activity_sign_up_btn_user_image)
    CircleImageView mCircleImageViewUser;
    Call<SupplierListResponse> sellerResModelCall;
    private String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String imageString64 = "";
    private ImageDialog mImageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_supplier_customer_add);
        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mUnbinder = ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setTitle(getString(R.string.add_new_customer));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();

        if (sellerResModelCall != null)
            sellerResModelCall.cancel();
    }


    public void signUpCode() {

        if (!validate()) {
            onSubmitEnable();
            return;
        }

        onSubmitDisable();

        String name = mEditTextName.getText().toString();
        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();
        String tinNumber = mEditTextTinNumber.getText().toString();
        String firmName = mEditTextFirmName.getText().toString();
        String address = mEditTextAddress.getText().toString();
        String landMark = mEditTextLandMark.getText().toString();
        String phoneNo = mEditTextPhoneNumber.getText().toString();


        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUserName(name);
        registrationRequest.setUserPassword(Utils.getBase64String(password));
        registrationRequest.setDeviceType(AppConstant.ANDROID_DEVICE);
        registrationRequest.setDeviceId(Utils.getDeviceId(mBaseActivity));
        registrationRequest.setDeviceToken("xyz");
        registrationRequest.setUserAddress(address);
        registrationRequest.setUserFirmName(firmName);
        registrationRequest.setUserTinNumber(tinNumber);
        registrationRequest.setUserLandMark(landMark);
        registrationRequest.setUserEmailAddress(email);
        registrationRequest.setUserMobileNo(phoneNo);
        registrationRequest.setUserPicBase64(imageString64);
        registrationRequest.setUserType("2");
        registrationRequest.setSellerId(AppSharedPrefs.getInstance(mBaseActivity).getLoginUserModel().getSellerId());


        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().registerUser(registrationRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<LoginResponse>(this, true) {
            @Override
            public void onSuccess(LoginResponse pResponse, BaseActivity pBaseActivity) {
                if (pResponse.isSuccess() && pResponse.getResponseCode() == 200 && pResponse.getResult().size() > 0 && pBaseActivity != null) {
                    //AppSharedPrefs.getInstance(mBaseActivity).s pResponse.getResult().get(0));
                    Toast.makeText(pBaseActivity, R.string.successfully_registered, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, null);
                    finish();
                } else
                    Toast.makeText(pBaseActivity, pResponse.getResponseCode() + pResponse.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(String pErrorMsg) {
                onSubmitEnable();
            }

        });

    }

    public void onSubmitEnable() {
        mButtonSignUp.setEnabled(true);
    }

    public void onSubmitDisable() {
        mButtonSignUp.setEnabled(false);
    }

    public boolean validate() {

        String name = mEditTextName.getText().toString();
        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();
        String cPassword = mEditTextConfirmPassword.getText().toString();
        String tinNumber = mEditTextTinNumber.getText().toString();
        String firmName = mEditTextFirmName.getText().toString();
        String phoneNo = mEditTextPhoneNumber.getText().toString();

        if (!EditTextVerification.isPersonNameOk(name, mBaseActivity))
            return false;

        if (!EditTextVerification.isPhoneNoOk(phoneNo, mBaseActivity))
            return false;

        if (!EditTextVerification.isEmailAddressOk(email, mBaseActivity))
            return false;

        if (!EditTextVerification.isPasswordOk(password, mBaseActivity))
            return false;

        if (!EditTextVerification.isPasswordOk(cPassword, mBaseActivity))
            return false;

        if (!cPassword.equals(password)) {
            showToast(ToastMessage.PASSWORD_MATCH);
            return false;
        }

        if (!EditTextVerification.isTinNoOk(tinNumber, mBaseActivity))
            return false;

        return EditTextVerification.isFirmOk(firmName, mBaseActivity);
    }

    @OnClick({R.id.layout_activity_sign_up_btn_user_image, R.id.layout_activity_sign_up_btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.layout_activity_sign_up_btn_user_image:
                mImageDialog = new ImageDialog(this, this, mCircleImageViewUser.getWidth(), mCircleImageViewUser.getHeight());
                mImageDialog.show();
                break;

            case R.id.layout_activity_sign_up_btn_submit:
                signUpCode();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImageDialog.REQUEST_CODE_IMAGE) {
            mImageDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRemoveItemTap() {
        mCircleImageViewUser.setImageResource(R.drawable.ic_user_default);
        imageString64 = "";
    }

    @Override
    public void onCancelItemTap() {

    }

    @Override
    public void onImageReceived(Bitmap pBitmap) {
        imageString64 = Utils.getStringImage(pBitmap);
        mCircleImageViewUser.setImageBitmap(pBitmap);
    }
}
