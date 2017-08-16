package ig.com.digitalmandi.fragment.supplier;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.common.SplashActivity;
import ig.com.digitalmandi.base_package.BaseFragment;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.common.LogoutReqModel;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.MyPrefrences;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by shivam.garg on 13-10-2016.
 */

public class LogoutFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();

        disableTouchEvent();
        LogoutReqModel logoutReqModel = new LogoutReqModel();
        logoutReqModel.setDeviceId(Utils.getDeviceId(mHostActivity));

        mHostActivity.apiEnqueueObject = RetrofitWebService.getInstance().getInterface().logoutUser(logoutReqModel);
        mHostActivity.apiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mHostActivity, false) {

            @Override
            public void yesCall(EmptyResponse response, ParentActivity weakRef) {
                enableTouchEvent();
                if (VerifyResponse.isResponseOk(response)) {
                    Toast.makeText(mHostActivity, R.string.successfully_logout, Toast.LENGTH_SHORT).show();
                    MyPrefrences.setBooleanPrefrences(ConstantValues.IS_SETUP, false, mHostActivity);
                    MyPrefrences.setBooleanPrefrences(ConstantValues.IS_LOGIN, false, mHostActivity);
                    Utils.onActivityStart(mHostActivity, true, new int[]{}, null, SplashActivity.class);
                } else
                    Toast.makeText(mHostActivity, R.string.failed_to_logout_uer, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void noCall(Throwable error) {
                enableTouchEvent();
            }
        });
    }
}
