package ig.com.digitalmandi.activity.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.RegistrationReqModel;
import ig.com.digitalmandi.beans.request.supplier.SupplierSellerListReq;
import ig.com.digitalmandi.beans.response.common.LoginResModel;
import ig.com.digitalmandi.beans.response.supplier.SupplierListRes;
import ig.com.digitalmandi.dialogs.ImageDialog;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitConstant;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.services_parsing.Parsing;
import ig.com.digitalmandi.toast.ToastMessage;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.EditTextVerification;
import ig.com.digitalmandi.utils.Utils;


public class SignUpActivity extends ParentActivity implements AdapterView.OnItemSelectedListener, ImageDialog.OnItemSelectedListener {

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
    @BindView(R.id.mSpinnerCustomerType)
    AppCompatSpinner spinnerCustomerType;
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
    @BindView(R.id.mSpinnerSellerName)
    AppCompatSpinner mSpinnerSellerName;
    @BindView(R.id.mTextViewAlreadyHaveAccount)
    AppCompatTextView mTextViewAlreadyHaveAccount;
    private int spinnerItem = 0;
    private String imageString64 = "";
    private String[] sellerName = new String[0];
    private List<SupplierListRes.ResultBean> sellerList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private String supplierId = "-1";
    private ImageDialog mImageDialog;


    private void getSellerList() {
        SupplierSellerListReq sellerReqModel = new SupplierSellerListReq();
        sellerReqModel.setCustomerType(ConstantValues.SELLER);

        apiEnqueueObject = RetrofitWebService.getInstance().getInterface().sellerInfo(sellerReqModel);
        apiEnqueueObject.enqueue(new RetrofitCallBack<SupplierListRes>(this) {

            @Override
            public void yesCall(SupplierListRes response, ParentActivity weakRef) {
                if (VerifyResponse.isResponseOk(response, true)) {
                    sellerList.clear();
                    sellerList.addAll(response.getResult());
                    SupplierListRes.ResultBean temp = new SupplierListRes.ResultBean();
                    temp.setUserId("0");
                    temp.setUserName("Please Select Any Supplier...");
                    sellerList.add(0, temp);
                    sellerName = new String[sellerList.size()];
                    for (int index = 0; index < sellerList.size(); index++) {
                        sellerName[index] = sellerList.get(index).getUserName();
                    }
                    arrayAdapter = new ArrayAdapter(mRunningActivity, android.R.layout.simple_spinner_item, sellerName);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    try {
                        mSpinnerSellerName.setAdapter(arrayAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    mSpinnerSellerName.setVisibility(View.GONE);

            }

            @Override
            public void noCall(Throwable error) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_sign_up);
        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mUnbinder = ButterKnife.bind(this);
        spinnerCustomerType.setOnItemSelectedListener(this);
        mSpinnerSellerName.setOnItemSelectedListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setTitle(getString(R.string.signup_string));
    }

    public void signUpCode() {

        if (!isInputOk()) {
            return;
        }

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
        registrationReqModel.setUserType(String.valueOf(spinnerItem));

        if (Integer.parseInt(supplierId) <= 0)
            registrationReqModel.setSellerId("");
        else
            registrationReqModel.setSellerId(supplierId);


        apiEnqueueObject = RetrofitWebService.getInstance().getInterface().registerUser(registrationReqModel);
        apiEnqueueObject.enqueue(new RetrofitCallBack<LoginResModel>(this) {

            @Override
            public void yesCall(LoginResModel response, ParentActivity weakRef) {
                if (VerifyResponse.isResponseOk(response, true)) {
                    Parsing.parseLoginUserData(mRunningActivity, response.getResult().get(0));
                    showToast(ToastMessage.SUCCESSFULLY_REGISTERED);
                    setResult(RESULT_OK, null);
                    finish();
                } else
                    showToast(ToastMessage.FAILED_REGISTERED);
            }

            @Override
            public void noCall(Throwable error) {}
        });

    }

    public boolean isInputOk() {

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


        if (spinnerItem <= 0) {
            Toast.makeText(mRunningActivity, "Please Select Appropriate CustomerFragment Type From Drop Down", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (String.valueOf(spinnerItem).equalsIgnoreCase(ConstantValues.CUSTOMER) && Integer.parseInt(supplierId) <= 0) {
            Toast.makeText(mRunningActivity, "Please Select Any Supplier From Drop Down", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {

            case R.id.mSpinnerSellerName:
                if (position > 0)
                    supplierId = sellerList.get(position).getUserId();
                break;

            case R.id.mSpinnerCustomerType:
                spinnerItem = position;
                if (spinnerItem == 0 || spinnerItem == 1)
                    mSpinnerSellerName.setVisibility(View.GONE);
                else {
                    mSpinnerSellerName.setVisibility(View.VISIBLE);
                    getSellerList();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @OnClick({R.id.mCircleImageViewUser, R.id.mButtonSignUp, R.id.mTextViewAlreadyHaveAccount})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mCircleImageViewUser:
                mImageDialog = new ImageDialog(this, this, mCircleImageViewUser.getWidth(), mCircleImageViewUser.getHeight());
                mImageDialog.onShowDialog();
                break;

            case R.id.mButtonSignUp:
                signUpCode();
                break;

            case R.id.mTextViewAlreadyHaveAccount:
                setResult(RESULT_CANCELED, null);
                finish();
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
