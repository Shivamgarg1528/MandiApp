package ig.com.digitalmandi.activity.supplier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierProductModifyReq;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierProductListRes;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.MyPrefrences;
import ig.com.digitalmandi.utils.Utils;

public class SupplierProductModifyActivity extends ParentActivity {

    public static final String PRODUCT_OBJECT_KEY        = "productKey";
    public static final String PRODUCT_OBJECT_KEY_UPDATE = "productUpdate";
    public static final int REQUEST_CODE_ADD_UPDATE_PRODUCT = 1001;

    @BindView(R.id.mImageViewProduct)
    AppCompatImageView mImageViewProduct;
    @BindView(R.id.mEditTextProductName)
    AppCompatEditText mEditTextProductName;
    @BindView(R.id.mEditTextProductQty)
    AppCompatEditText mEditTextProductQty;
    @BindView(R.id.mButtonProductAdd)
    AppCompatButton mButtonProductAdd;
    private SupplierProductListRes.ResultBean productObject;
    private boolean isUpdateTrue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_product_modify);
        ButterKnife.bind(this);

        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();

        if (intent.getBooleanExtra(PRODUCT_OBJECT_KEY_UPDATE, false)) {

            if (intent == null || (SupplierProductListRes.ResultBean) intent.getParcelableExtra(PRODUCT_OBJECT_KEY) == null) {
                Toast.makeText(mRunningActivity, R.string.product_result, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            isUpdateTrue  = true;
            productObject = (SupplierProductListRes.ResultBean) intent.getParcelableExtra(PRODUCT_OBJECT_KEY);
            mEditTextProductName.setText(productObject.getProductName());
            mEditTextProductQty .setText(productObject.getProductQty());
            Picasso.with(this).load("http://www.aiob.in/shivam/product/201610111330154088.png").into(mImageViewProduct);
            setTitle(getString(R.string.update_product_string,productObject.getProductName()));
            return;
        }
        setTitle(getString(R.string.add_new_product));
    }

    public boolean validate() {
        String productName  = mEditTextProductName.getText().toString();
        String productQty   = mEditTextProductQty.getText().toString();

        if (productName.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_product_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        /*if (productQty.isEmpty() || Long.parseLong(productQty) <=  0) {
            Toast.makeText(this, R.string.qty_must_be_greater_than_zero, Toast.LENGTH_SHORT).show();
            return false;
        }*/
        return true;
    }

    @OnClick({R.id.mImageViewProduct, R.id.mButtonProductAdd})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mImageViewProduct:
                break;

            case R.id.mButtonProductAdd:
                Utils.onHideSoftKeyBoard(this,mEditTextProductName);

                if(!validate()){
                    return;
                }

                SupplierProductModifyReq reqModel = new SupplierProductModifyReq();
                //reqModel.setProductQty(mEditTextProductQty.getText().toString());
                reqModel.setProductQty(String.valueOf(0));
                reqModel.setProductName(mEditTextProductName.getText().toString());
                reqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID,mRunningActivity));
                reqModel.setProductImageBase64("");

                if(isUpdateTrue){
                    reqModel.setProductId(productObject.getProductId());
                    reqModel.setProductStatus(productObject.getProductStatus());
                    reqModel.setProductOperation(ConstantValues.UPDATE);

                }
                else{
                    reqModel.setProductOperation(ConstantValues.ADD);
                    reqModel.setProductStatus(ConstantValues.ENABLE);
                }

                apiEnqueueObject = RetrofitWebService.getInstance().getInterface().modifiedProduct(reqModel);
                apiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(this,true) {

                    @Override
                    public void yesCall(EmptyResponse response, ParentActivity weakRef) {
                       if(VerifyResponse.isResponseOk(response)){
                            setResult(RESULT_OK,null);
                            finish();
                        }
                        else
                            Toast.makeText(mRunningActivity, R.string.please_try_again, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void noCall(Throwable error) {

                    }
                });

                break;
        }
    }
}
