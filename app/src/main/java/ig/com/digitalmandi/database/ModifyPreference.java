package ig.com.digitalmandi.database;

import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.beans.request.supplier.SellerCustomerList;
import ig.com.digitalmandi.beans.request.supplier.SellerCustomerListRequest;
import ig.com.digitalmandi.beans.request.supplier.SellerProductListRequest;
import ig.com.digitalmandi.beans.request.supplier.SellerUnitListRequest;
import ig.com.digitalmandi.beans.response.common.LoginResponse;
import ig.com.digitalmandi.beans.response.supplier.SellerProductList;
import ig.com.digitalmandi.beans.response.supplier.SellerUnitList;
import ig.com.digitalmandi.interfaces.ApiCallback;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.utils.AppSharedPrefs;

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
            public void onSuccess(SellerCustomerList pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, true)) {
                    AppSharedPrefs.getInstance(mBaseActivity).setSellerCustomers(pResponse);
                }
                mListener.onApiResponse();
            }

            @Override
            public void onFailure(String pErrorMsg) {
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
            public void onSuccess(SellerUnitList pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    AppSharedPrefs.getInstance(mBaseActivity).setSellerUnits(pResponse);
                }
                mListener.onApiResponse();
            }

            @Override
            public void onFailure(String pErrorMsg) {
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
            public void onSuccess(SellerProductList pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, true)) {
                    AppSharedPrefs.getInstance(mBaseActivity).setSellerProducts(pResponse);
                }
                mListener.onApiResponse();
            }

            @Override
            public void onFailure(String pErrorMsg) {
                mListener.onApiResponse();
            }
        });
    }
}
