package ig.com.digitalmandi.activity.supplier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierUnitModifyRequest;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.beans.response.supplier.SellerUnitList;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.utils.AppConstant;
import ig.com.digitalmandi.utils.Utils;

public class SellerUnitModifyActivity extends BaseActivity implements View.OnClickListener {

    private AppCompatEditText mEditTxtUnitName;
    private AppCompatEditText mEditTxtUnitValue;

    private SellerUnitList.Unit mUnitObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_modify);
        setToolbar(true);

        findViewById(R.id.activity_unit_modify_btn_add).setOnClickListener(this);
        mEditTxtUnitName = (AppCompatEditText) findViewById(R.id.activity_unit_modify_edt_unit_name);
        mEditTxtUnitValue = (AppCompatEditText) findViewById(R.id.activity_unit_modify_edt_unit_value);

        Intent intent = getIntent();
        mUnitObject = (SellerUnitList.Unit) intent.getSerializableExtra(AppConstant.KEY_OBJECT);

        if (mUnitObject != null) {
            mEditTxtUnitName.setText(mUnitObject.getUnitName());
            mEditTxtUnitValue.setText(mUnitObject.getKgValue());
            setTitle(getString(R.string.update_unit, mUnitObject.getUnitName()));
            return;
        }

        setTitle(getString(R.string.add_new_unit));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.activity_unit_modify_btn_add:
                Utils.onHideSoftKeyBoard(this, mEditTxtUnitValue);

                String unitName = mEditTxtUnitName.getText().toString();
                String unitValue = mEditTxtUnitValue.getText().toString();

                if (Utils.isEmpty(unitName)) {
                    mBaseActivity.showToast(getString(R.string.please_enter_unit_name));
                    return;
                } else if (Utils.isEmpty(unitValue) || !Utils.isFloat(unitValue)) {
                    mBaseActivity.showToast(getString(R.string.unit_value_must_be_greater_than_zero));
                    return;
                } else if (mUnitObject != null) {
                    if (unitName.equalsIgnoreCase(mUnitObject.getUnitName())
                            && unitValue.equalsIgnoreCase(mUnitObject.getKgValue())) {
                        mBaseActivity.showToast(getString(R.string.string_no_modification_done));
                        return;
                    }
                }

                SupplierUnitModifyRequest supplierUnitModifyRequest = new SupplierUnitModifyRequest();
                supplierUnitModifyRequest.setSellerId(mLoginUser.getSellerId());
                supplierUnitModifyRequest.setUnitName(unitName);
                supplierUnitModifyRequest.setKgValue(unitValue);

                if (mUnitObject != null) {
                    supplierUnitModifyRequest.setUnitId(mUnitObject.getUnitId());
                    supplierUnitModifyRequest.setUnitStatus(mUnitObject.getUnitStatus());
                    supplierUnitModifyRequest.setOperation(AppConstant.UPDATE);
                } else {
                    supplierUnitModifyRequest.setUnitStatus(AppConstant.ENABLE);
                    supplierUnitModifyRequest.setOperation(AppConstant.ADD);
                }

                mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().modifiedUnit(supplierUnitModifyRequest);
                mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mBaseActivity) {
                    @Override
                    public void onSuccess(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                        if (ResponseVerification.isResponseOk(pResponse)) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            mBaseActivity.showToast(getString(R.string.please_try_again));
                        }
                    }

                    @Override
                    public void onFailure(String pErrorMsg) {
                    }
                });
        }
    }
}
