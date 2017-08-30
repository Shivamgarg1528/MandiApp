package ig.com.digitalmandi.fragment.supplier;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.common.SplashActivity;
import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.base_package.BaseFragment;
import ig.com.digitalmandi.beans.request.common.LogoutRequest;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.utils.AppSharedPrefs;
import ig.com.digitalmandi.utils.Utils;

public class LogoutFragment extends BaseFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setDeviceId(Utils.getDeviceId(mBaseActivity));

        mBaseActivity.mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().logoutUser(logoutRequest);
        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mBaseActivity) {

            @Override
            public void onSuccess(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse)) {
                    AppSharedPrefs.getInstance(mBaseActivity).clear();
                    Utils.onActivityStart(mBaseActivity, true, null, null, SplashActivity.class);
                    mBaseActivity.showToast(getString(R.string.successfully_logout));
                } else {
                    mBaseActivity.showToast(getString(R.string.unsuccesfully_logout));
                }
            }

            @Override
            public void onFailure(String pErrorMsg) {
            }
        });
    }
}
