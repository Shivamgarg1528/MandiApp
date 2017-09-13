package ig.com.digitalmandi.activity.seller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.request.seller.SupplierProductModifyRequest;
import ig.com.digitalmandi.bean.response.EmptyResponse;
import ig.com.digitalmandi.bean.response.seller.ProductResponse;
import ig.com.digitalmandi.dialog.ImageDialog;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class SupplierProductModifyActivity extends BaseActivity implements View.OnClickListener, ImageDialog.OnItemSelectedListener {

    private AppCompatEditText mEditTextProductName;

    private ProductResponse.Product mProductObject;
    private AppCompatImageView mImageViewProduct;
    private ImageDialog mDialogImagePicker;
    private String mStringBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_modify);
        setToolbar(true);

        mEditTextProductName = findViewById(R.id.activity_product_modify_edt_product_name);
        findViewById(R.id.activity_product_modify_btn_submit).setOnClickListener(this);

        mImageViewProduct = findViewById(R.id.activity_product_modify_iv_product_image);
        mImageViewProduct.setOnClickListener(this);

        Intent intent = getIntent();
        mProductObject = (ProductResponse.Product) intent.getSerializableExtra(AppConstant.KEY_OBJECT);

        if (mProductObject != null) {
            mEditTextProductName.setText(mProductObject.getProductName());
            Helper.setImage(this, AppConstant.END_POINT.concat("product/201610111330154088.png"), mImageViewProduct);
            setTitle(getString(R.string.update_product_string, mProductObject.getProductName()));
            return;
        }
        setTitle(getString(R.string.add_new_product));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.activity_product_modify_iv_product_image:
                mDialogImagePicker = new ImageDialog(mBaseActivity, this, mImageViewProduct.getWidth(), mImageViewProduct.getHeight());
                mDialogImagePicker.show();
                break;

            case R.id.activity_product_modify_btn_submit:

                Helper.hideSoftKeyBoard(this, mEditTextProductName);

                String productName = mEditTextProductName.getText().toString();
                if (Helper.isEmpty(productName)) {
                    mBaseActivity.showToast(getString(R.string.please_enter_product_name));
                    return;
                }

                SupplierProductModifyRequest supplierProductModifyRequest = new SupplierProductModifyRequest();
                supplierProductModifyRequest.setProductQty(String.valueOf(0));
                supplierProductModifyRequest.setProductName(mEditTextProductName.getText().toString());
                supplierProductModifyRequest.setSellerId(mLoginUser.getSellerId());

                if (mProductObject != null) {
                    //mStringBase64 = Helper.getStringImage(((BitmapDrawable) mImageViewProduct.getDrawable()).getBitmap());
                    supplierProductModifyRequest.setProductImageBase64(mStringBase64);
                    supplierProductModifyRequest.setProductId(mProductObject.getProductId());
                    supplierProductModifyRequest.setProductOperation(AppConstant.UPDATE);
                    supplierProductModifyRequest.setProductStatus(mProductObject.getProductStatus());
                } else {
                    supplierProductModifyRequest.setProductOperation(AppConstant.ADD);
                    supplierProductModifyRequest.setProductStatus(AppConstant.ENABLE);
                    supplierProductModifyRequest.setProductImageBase64(mStringBase64);
                }

                mApiEnqueueObject = RetrofitClient.getInstance().getInterface().modifiedProduct(supplierProductModifyRequest);
                mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(this) {
                    @Override
                    public void onResponse(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                        if (ResponseVerification.isResponseOk(pResponse)) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            mBaseActivity.showToast(getString(R.string.please_try_again));
                        }
                    }

                });
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
        mImageViewProduct.setImageBitmap(null);
        mStringBase64 = "";

    }

    @Override
    public void onCancelItemTap() {

    }

    @Override
    public void onImageReceived(Bitmap pBitmap) {
        mImageViewProduct.setImageBitmap(pBitmap);
        mStringBase64 = Helper.getStringImage(pBitmap);
    }
}
