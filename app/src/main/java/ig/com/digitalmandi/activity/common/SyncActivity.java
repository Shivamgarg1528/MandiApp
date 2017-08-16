package ig.com.digitalmandi.activity.common;

import android.os.Bundle;
import android.os.Handler;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.supplier.SupplierHomeActivity;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.database.BaseContract;
import ig.com.digitalmandi.database.DataBaseOperation;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.MyPrefrences;
import ig.com.digitalmandi.utils.Utils;

public class SyncActivity extends ParentActivity implements BaseContract.OnInsertBulkDataSuccessFully {

    private Handler handler   = new Handler();
    private int totalApiCount = 3;
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            handler.removeCallbacks(this);
            if(totalApiCount == DataBaseOperation.operationCount) {
                DataBaseOperation.operationCount = 0;
                MyPrefrences.setBooleanPrefrences(ConstantValues.IS_SETUP,true,mRunningActivity);
                onStartHome();
            }
            else
                handler.postDelayed(this,500);
        }
    };

    private void onStartHome(){
        if (MyPrefrences.getStringPrefrences(ConstantValues.USER_TYPE, mRunningActivity).equalsIgnoreCase(ConstantValues.SELLER)) {
            Utils.onActivityStart(mRunningActivity, true, new int[]{}, null, SupplierHomeActivity.class);
        }
        else{

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_sync);
        MyPrefrences.setBooleanPrefrences(ConstantValues.IS_LOGIN,true,mRunningActivity);
        if(MyPrefrences.getBooleanPrefrences(ConstantValues.IS_SETUP,mRunningActivity)){
            onStartHome();
        }
        else{
            handler.postDelayed(runnable,500);
            DataBaseOperation.insertIntoCustomerTable((ParentActivity) mRunningActivity,this);
            DataBaseOperation.insertIntoUnitTable((ParentActivity) mRunningActivity,this);
            DataBaseOperation.insertIntoProductTable((ParentActivity) mRunningActivity,this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onInsertBulkDataSuccess() {
        ++DataBaseOperation.operationCount;
    }
}
