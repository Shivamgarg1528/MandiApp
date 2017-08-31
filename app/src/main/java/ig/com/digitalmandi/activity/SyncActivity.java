package ig.com.digitalmandi.activity;

import android.os.Bundle;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.seller.SupplierHomeActivity;
import ig.com.digitalmandi.base.BaseActivity;
import ig.com.digitalmandi.bean.response.LoginResponse;
import ig.com.digitalmandi.callback.ApiCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.AppSharedPrefs;
import ig.com.digitalmandi.util.ModifyPreference;
import ig.com.digitalmandi.util.Utils;

public class SyncActivity extends BaseActivity implements ApiCallback {

    private int mApiCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        setToolbar(false);
        setTitle(getString(R.string.string_syncing));

        ModifyPreference modifyPreference = new ModifyPreference(this, this);
        modifyPreference.addOrUpdateSellerCustomers();
        modifyPreference.addOrUpdateSellerProducts();
        modifyPreference.addOrUpdateSellerUnits();

    }

    public void startAfterSync() {
        LoginResponse.LoginUser loginUserModel = AppSharedPrefs.getInstance(this).getLoginUserModel();
        if (loginUserModel.getUserType() == AppConstant.SELLER) {
            Utils.onActivityStart(mBaseActivity, true, null, null, SupplierHomeActivity.class);
        } else if (loginUserModel.getUserType() == AppConstant.CUSTOMER) {
            //Utils.onActivityStart(mBaseActivity, true, null, null, CustomerHomeActivity.class);
        }
    }

    @Override
    public void onApiResponse() {
        ++mApiCount;
        if (mApiCount == 3) {
            mBaseActivity.showToast(getString(R.string.string_sync_completed));
            startAfterSync();
        }
    }
}
