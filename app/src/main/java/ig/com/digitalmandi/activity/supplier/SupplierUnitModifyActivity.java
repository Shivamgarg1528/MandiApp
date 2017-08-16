package ig.com.digitalmandi.activity.supplier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierUnitModifyReq;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierUnitListRes;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.utils.CheckForFloat;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.MyPrefrences;
import ig.com.digitalmandi.utils.Utils;

public class SupplierUnitModifyActivity extends ParentActivity {

    public static final String UNIT_OBJECT_KEY = "unitKey";
    public static final String UNIT_OBJECT_KEY_UPDATE = "unitUpdate";
    public static final int REQUEST_CODE_ADD_UPDATE_UNIT = 1001;

    @BindView(R.id.mEditTextUnitName)
    AppCompatEditText mEditTextUnitName;
    @BindView(R.id.mEditTextUnitValue)
    AppCompatEditText mEditTextUnitValue;
    @BindView(R.id.mButtonUnitAdd)
    AppCompatButton mButtonUnitAdd;
    private SupplierUnitListRes.ResultBean unitObject;
    private boolean isUpdateTrue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_unit_modify);
        ButterKnife.bind(this);

        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();

        if (intent.getBooleanExtra(UNIT_OBJECT_KEY_UPDATE, false)) {

            if (intent == null || (SupplierUnitListRes.ResultBean) intent.getParcelableExtra(UNIT_OBJECT_KEY) == null) {
                Toast.makeText(mRunningActivity, R.string.no_result_found, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            isUpdateTrue = true;
            unitObject = (SupplierUnitListRes.ResultBean) intent.getParcelableExtra(UNIT_OBJECT_KEY);
            mEditTextUnitName.setText(unitObject.getUnitName());
            mEditTextUnitValue.setText(unitObject.getKgValue());
            setTitle(getString(R.string.update_unit, unitObject.getUnitName()));
            return;
        }
        setTitle(getString(R.string.add_new_unit));
    }

    public boolean validate() {
        String unitName = mEditTextUnitName.getText().toString();
        String unitValue = mEditTextUnitValue.getText().toString();

        if (unitName.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_unit_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!CheckForFloat.onCheckFloat(unitValue)) {
            Toast.makeText(this,  R.string.unit_value_must_be_greater_than_zero, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick({R.id.mButtonUnitAdd})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mButtonUnitAdd:

                Utils.onHideSoftKeyBoard(this, mEditTextUnitValue);
                if (!validate()) {
                    return;
                }

                final SupplierUnitModifyReq reqModel = new SupplierUnitModifyReq();
                reqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mRunningActivity));
                reqModel.setUnitName(mEditTextUnitName.getText().toString().trim());
                reqModel.setKgValue(mEditTextUnitValue.getText().toString().trim());

                if (isUpdateTrue) {
                    reqModel.setOperation(ConstantValues.UPDATE);
                    reqModel.setUnitId(unitObject.getUnitId());
                    reqModel.setUnitStatus(unitObject.getUnitStatus());
                } else {
                    reqModel.setUnitStatus(ConstantValues.ENABLE);
                    reqModel.setOperation(ConstantValues.ADD);
                }

                apiEnqueueObject = RetrofitWebService.getInstance().getInterface().modifiedUnit(reqModel);
                apiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mRunningActivity) {

                    @Override
                    public void yesCall(EmptyResponse response, ParentActivity weakRef) {
                        if (VerifyResponse.isResponseOk(response)) {
                            setResult(RESULT_OK, null);
                            finish();
                        } else
                            Toast.makeText(mRunningActivity, R.string.please_try_again, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void noCall(Throwable error) {}
                });

        }
    }
}
