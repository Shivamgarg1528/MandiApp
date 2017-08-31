package ig.com.digitalmandi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base.BaseActivity;
import ig.com.digitalmandi.bean.request.seller.RegistrationRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierListRequest;
import ig.com.digitalmandi.bean.response.LoginResponse;
import ig.com.digitalmandi.bean.response.seller.SupplierListResponse;
import ig.com.digitalmandi.dialog.ImageDialog;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.AppSharedPrefs;
import ig.com.digitalmandi.util.EditTextVerification;
import ig.com.digitalmandi.util.Utils;


public class SignUpActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, ImageDialog.OnItemSelectedListener, View.OnClickListener {

    private AppCompatEditText mEditTxtName;
    private AppCompatEditText mEditTxtPhoneNumber;
    private AppCompatEditText mEditTxtEmail;
    private AppCompatEditText mEditTxtPassword;
    private AppCompatEditText mEditTxtConfirmPassword;
    private AppCompatEditText mEditTxtFirmName;
    private AppCompatEditText mEditTxtTinNumber;
    private AppCompatEditText mEditTxtLandMark;
    private AppCompatEditText mEditTxtAddress;
    private AppCompatSpinner mSpinnerSeller;
    private CircleImageView mCircleImageUser;

    private int mIndexOfUserType = 0;
    private String mStringBase64 = "";

    private int mIndexOfSelectedSeller = 0;
    private List<String> mSellerNameList = new ArrayList<>(0);
    private List<SupplierListResponse.ResultBean> mSellerList = new ArrayList<>(0);
    private ArrayAdapter<String> mSellerAdapter;

    private ImageDialog mDialogImagePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_activity_sign_up);
        setToolbar(true);
        setTitle(getString(R.string.string_sign_up));
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

        AppCompatSpinner spinnerUserType = (AppCompatSpinner) findViewById(R.id.layout_activity_sign_up_spinner_user_type);
        spinnerUserType.setOnItemSelectedListener(this);

        mCircleImageUser = (CircleImageView) findViewById(R.id.layout_activity_sign_up_btn_user_image);
        mCircleImageUser.setOnClickListener(this);

        findViewById(R.id.layout_activity_sign_up_btn_submit).setOnClickListener(this);
        findViewById(R.id.layout_activity_sign_up_btn_already_account).setOnClickListener(this);

        mSellerAdapter = new ArrayAdapter(mBaseActivity, android.R.layout.simple_spinner_item, mSellerNameList);
        mSellerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerSeller = (AppCompatSpinner) findViewById(R.id.layout_activity_sign_up_spinner_seller);
        mSpinnerSeller.setAdapter(mSellerAdapter);
        mSpinnerSeller.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {

            case R.id.layout_activity_sign_up_spinner_seller:
                mIndexOfSelectedSeller = Integer.parseInt(mSellerList.get(position).getUserId());
                break;

            case R.id.layout_activity_sign_up_spinner_user_type:
                mIndexOfUserType = position;
                if (mIndexOfUserType < AppConstant.CUSTOMER) {
                    mSpinnerSeller.setVisibility(View.INVISIBLE);
                } else {
                    mIndexOfSelectedSeller = 0;
                    mSpinnerSeller.setVisibility(View.VISIBLE);
                    getSellerList();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
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

            case R.id.layout_activity_sign_up_btn_already_account:
                setResult(RESULT_CANCELED);
                finish();
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
        mStringBase64 = Utils.getStringImage(pBitmap);
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
        registrationRequest.setUserPassword(Utils.getBase64String(password));
        registrationRequest.setDeviceType(AppConstant.ANDROID_DEVICE);
        registrationRequest.setDeviceId(Utils.getDeviceId(mBaseActivity));
        registrationRequest.setDeviceToken("");
        registrationRequest.setUserAddress(address);
        registrationRequest.setUserFirmName(firmName);
        registrationRequest.setUserTinNumber(tinNumber);
        registrationRequest.setUserLandMark(landMark);
        registrationRequest.setUserEmailAddress(email);
        registrationRequest.setUserMobileNo(phoneNo);
        registrationRequest.setUserPicBase64(mStringBase64);
        registrationRequest.setUserType(String.valueOf(mIndexOfUserType));

        if (mIndexOfSelectedSeller <= 0)
            registrationRequest.setSellerId("");
        else
            registrationRequest.setSellerId(String.valueOf(mIndexOfSelectedSeller));

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().registerUser(registrationRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<LoginResponse>(this) {
            @Override
            public void onSuccess(LoginResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, true)) {
                    AppSharedPrefs.getInstance(mBaseActivity).setLoginUserModel(pResponse.getResult().get(0));
                    showToast(getString(R.string.successfully_registered));
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showToast(getString(R.string.string_failed_to_register_new_user));
                }
            }

            @Override
            public void onFailure(String pErrorMsg) {
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

        if (!EditTextVerification.isPersonNameOk(name, mBaseActivity)) {
            return false;
        } else if (!EditTextVerification.isPhoneNoOk(phoneNo, mBaseActivity)) {
            return false;
        } else if (!EditTextVerification.isEmailAddressOk(email, mBaseActivity)) {
            return false;
        } else if (!EditTextVerification.isPasswordOk(password, mBaseActivity)) {
            return false;
        } else if (!EditTextVerification.isPasswordOk(cPassword, mBaseActivity)) {
            return false;
        } else if (!cPassword.equals(password)) {
            showToast(getString(R.string.string_password_match_failed));
            return false;
        } else if (!EditTextVerification.isTinNoOk(tinNumber, mBaseActivity)) {
            return false;
        } else if (!EditTextVerification.isFirmOk(firmName, mBaseActivity)) {
            return false;
        } else if (mIndexOfUserType <= 0) {
            showToast(getString(R.string.string_please_select_appropriate_customer_type));
            return false;
        } else if (AppConstant.CUSTOMER == mIndexOfUserType && mIndexOfSelectedSeller <= 0) {
            showToast(getString(R.string.string_please_select_any_supplier_from_drop_down));
            return false;
        } else {
            return true;
        }
    }

    private void getSellerList() {
        SupplierListRequest supplierListRequest = new SupplierListRequest();
        supplierListRequest.setCustomerType(String.valueOf(AppConstant.SELLER));

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().sellerInfo(supplierListRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<SupplierListResponse>(this) {

            @Override
            public void onSuccess(SupplierListResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, true)) {

                    SupplierListResponse.ResultBean tempItem = new SupplierListResponse.ResultBean();
                    tempItem.setUserId("0");
                    tempItem.setUserName(getString(R.string.string_please_select_any_supplier));

                    mSellerList.clear();
                    mSellerList.addAll(pResponse.getResult());
                    mSellerList.add(0, tempItem);

                    mSellerNameList.clear();
                    for (SupplierListResponse.ResultBean resultBean : mSellerList) {
                        mSellerNameList.add(resultBean.getUserName());
                    }
                    mSellerAdapter.notifyDataSetChanged();
                } else
                    mSpinnerSeller.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String pErrorMsg) {
            }
        });
    }
}
