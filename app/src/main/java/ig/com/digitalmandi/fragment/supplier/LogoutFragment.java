package ig.com.digitalmandi.fragment.supplier;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.activity.SplashActivity;
import ig.com.digitalmandi.bean.request.LogoutRequest;
import ig.com.digitalmandi.bean.response.EmptyResponse;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.util.AppSharedPrefs;
import ig.com.digitalmandi.util.Helper;

public class LogoutFragment extends EmptyFragment {

    @Override
    public void onResume() {
        super.onResume();

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setDeviceId(Helper.getDeviceId(mBaseActivity));

        mBaseActivity.mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().logoutUser(logoutRequest);
        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mBaseActivity) {

            @Override
            public void onResponse(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse)) {
                    AppSharedPrefs.getInstance(mBaseActivity).clear();
                    Helper.onActivityStart(mBaseActivity, true, null, null, SplashActivity.class);
                    mBaseActivity.showToast(getString(R.string.successfully_logout));
                } else {
                    mBaseActivity.showToast(getString(R.string.unsuccesfully_logout));
                }
            }
        });
    }
}
