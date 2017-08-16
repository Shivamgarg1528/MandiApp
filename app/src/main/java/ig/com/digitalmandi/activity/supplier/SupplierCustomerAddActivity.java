package ig.com.digitalmandi.activity.supplier;

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
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.RegistrationReqModel;
import ig.com.digitalmandi.beans.response.common.LoginResModel;
import ig.com.digitalmandi.beans.response.supplier.SupplierListRes;
import ig.com.digitalmandi.dialogs.ImageDialog;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitConstant;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.services_parsing.Parsing;
import ig.com.digitalmandi.toast.ToastMessage;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.EditTextVerification;
import ig.com.digitalmandi.utils.MyPrefrences;
import ig.com.digitalmandi.utils.Utils;
import retrofit2.Call;

public class SupplierCustomerAddActivity extends ParentActivity implements ImageDialog.OnItemSelectedListener {

    @BindView(R.id.mEditTextName)
    AppCompatEditText mEditTextName;
    @BindView(R.id.mEditTextPhoneNumber)
    AppCompatEditText mEditTextPhoneNumber;
    @BindView(R.id.mEditTextEmail)
    AppCompatEditText mEditTextEmail;
    @BindView(R.id.mEditTextPassword)
    AppCompatEditText mEditTextPassword;
    @BindView(R.id.inputConfirmPassword)
    AppCompatEditText mEditTextConfirmPassword;
    @BindView(R.id.mButtonSignUp)
    AppCompatButton mButtonSignUp;
    Unbinder mUnbinder;
    @BindView(R.id.mEditTextFirmName)
    AppCompatEditText mEditTextFirmName;
    @BindView(R.id.mEditTextTinNumber)
    AppCompatEditText mEditTextTinNumber;
    @BindView(R.id.mEditTextLandMark)
    AppCompatEditText mEditTextLandMark;
    @BindView(R.id.mEditTextAddress)
    AppCompatEditText mEditTextAddress;
    @BindView(R.id.mCircleImageViewUser)
    CircleImageView mCircleImageViewUser;
    private String[] permission  = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String imageString64 = "";
    Call<SupplierListRes> sellerResModelCall;
    private final int CAMERA_REQ_CODE = 100;
    private final int GALLERY_REQ_CODE = 101;
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

        if(sellerResModelCall != null)
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


        RegistrationReqModel registrationReqModel = new RegistrationReqModel();
        registrationReqModel.setUserName(name);
        registrationReqModel.setUserPassword(Utils.onConvertIntoBase64(password));
        registrationReqModel.setDeviceType(RetrofitConstant.ANDROID_DEVICE);
        registrationReqModel.setDeviceId(Utils.getDeviceId(mRunningActivity));
        registrationReqModel.setDeviceToken("xyz");
        registrationReqModel.setUserAddress(address);
        registrationReqModel.setUserFirmName(firmName);
        registrationReqModel.setUserTinNumber(tinNumber);
        registrationReqModel.setUserLandMark(landMark);
        registrationReqModel.setUserEmailAddress(email);
        registrationReqModel.setUserMobileNo(phoneNo);
        registrationReqModel.setUserPicBase64(imageString64);
        registrationReqModel.setUserType("2");
        registrationReqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID,mRunningActivity));


        apiEnqueueObject = RetrofitWebService.getInstance().getInterface().registerUser(registrationReqModel);
        apiEnqueueObject.enqueue(new RetrofitCallBack<LoginResModel>(this,true) {
            @Override
            public void yesCall(LoginResModel response, ParentActivity weakRef) {
                if (response.isSuccess() && response.getResponseCode() == 200 && response.getResult().size() > 0 && weakRef != null) {
                    Parsing.parseLoginUserData(mRunningActivity, response.getResult().get(0));
                    Toast.makeText(weakRef, R.string.successfully_registered, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, null);
                    finish();
                } else
                    Toast.makeText(weakRef, response.getResponseCode() + response.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void noCall(Throwable error) {
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

        if (!EditTextVerification.isPersonNameOk(name, (ParentActivity) mRunningActivity))
            return false;

        if (!EditTextVerification.isPhoneNoOk(phoneNo, (ParentActivity) mRunningActivity))
            return false;

        if (!EditTextVerification.isEmailAddressOk(email, (ParentActivity) mRunningActivity))
            return false;

        if (!EditTextVerification.isPasswordOk(password, (ParentActivity) mRunningActivity))
            return false;

        if (!EditTextVerification.isPasswordOk(cPassword, (ParentActivity) mRunningActivity))
            return false;

        if (!cPassword.equals(password)) {
            showToast(ToastMessage.PASSWORD_MATCH);
            return false;
        }

        if (!EditTextVerification.isTinNoOk(tinNumber, (ParentActivity) mRunningActivity))
            return false;

        if (!EditTextVerification.isFirmOk(firmName, (ParentActivity) mRunningActivity))
            return false;
        return true;
    }

    @OnClick({R.id.mCircleImageViewUser, R.id.mButtonSignUp})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mCircleImageViewUser:
                mImageDialog = new ImageDialog(this, this, mCircleImageViewUser.getWidth(), mCircleImageViewUser.getHeight());
                mImageDialog.onShowDialog();
                break;

            case R.id.mButtonSignUp:
                signUpCode();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImageDialog.CAMERA_REQ_CODE || resultCode == ImageDialog.GALLERY_REQ_CODE) {
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
    public void onImageReceived(Bitmap bitmap) {
        imageString64 = Utils.getStringImage(bitmap);
        mCircleImageViewUser.setImageBitmap(bitmap);
    }
}
