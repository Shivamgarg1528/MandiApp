package ig.com.digitalmandi.util;

import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.request.seller.SellerCustomerList;
import ig.com.digitalmandi.bean.request.seller.SellerCustomerListRequest;
import ig.com.digitalmandi.bean.request.seller.SellerProductListRequest;
import ig.com.digitalmandi.bean.request.seller.SellerUnitListRequest;
import ig.com.digitalmandi.bean.response.LoginResponse;
import ig.com.digitalmandi.bean.response.seller.SellerProductList;
import ig.com.digitalmandi.bean.response.seller.SellerUnitList;
import ig.com.digitalmandi.callback.ApiCallback;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;

public class ModifyPreference {

    private final BaseActivity mBaseActivity;
    private final ApiCallback mListener;
    private LoginResponse.LoginUser mLoginUser;

    public ModifyPreference(BaseActivity pBaseActivity, ApiCallback pApiCallback) {
        this.mBaseActivity = pBaseActivity;
        this.mListener = pApiCallback;
        this.mLoginUser = AppSharedPrefs.getInstance(mBaseActivity).getLoginUserModel();
    }

    public void addOrUpdateSellerCustomers() {

        SellerCustomerListRequest sellerCustomerListRequest = new SellerCustomerListRequest();
        sellerCustomerListRequest.setSellerId(mLoginUser.getSellerId());

        mBaseActivity.mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().supplierCustomerList(sellerCustomerListRequest);
        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<SellerCustomerList>(mBaseActivity, false) {

            @Override
            public void onResponse(SellerCustomerList pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, true)) {
                    AppSharedPrefs.getInstance(mBaseActivity).setSellerCustomers(pResponse);
                }
                mListener.onApiResponse();
            }

        });
    }

    public void addOrUpdateSellerUnits() {

        SellerUnitListRequest sellerUnitListRequest = new SellerUnitListRequest();
        sellerUnitListRequest.setSellerId(mLoginUser.getSellerId());


        mBaseActivity.mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().supplierUnitList(sellerUnitListRequest);
        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<SellerUnitList>(mBaseActivity, false) {

            @Override
            public void onResponse(SellerUnitList pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    AppSharedPrefs.getInstance(mBaseActivity).setSellerUnits(pResponse);
                }
                mListener.onApiResponse();
            }

        });
    }

    public void addOrUpdateSellerProducts() {

        SellerProductListRequest sellerProductListRequest = new SellerProductListRequest();
        sellerProductListRequest.setSellerId(mLoginUser.getSellerId());

        mBaseActivity.mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().supplierProductList(sellerProductListRequest);
        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<SellerProductList>(mBaseActivity, false) {

            @Override
            public void onResponse(SellerProductList pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, true)) {
                    AppSharedPrefs.getInstance(mBaseActivity).setSellerProducts(pResponse);
                }
                mListener.onApiResponse();
            }

        });
    }
}
