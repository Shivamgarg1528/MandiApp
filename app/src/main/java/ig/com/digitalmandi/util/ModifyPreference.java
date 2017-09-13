package ig.com.digitalmandi.util;

import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.request.seller.CustomerResponse;
import ig.com.digitalmandi.bean.request.seller.SellerProductUnitCustomerRequest;
import ig.com.digitalmandi.bean.response.LoginResponse;
import ig.com.digitalmandi.bean.response.seller.ProductResponse;
import ig.com.digitalmandi.bean.response.seller.UnitResponse;
import ig.com.digitalmandi.callback.ApiCallback;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitClient;

public class ModifyPreference {

    private final BaseActivity mBaseActivity;
    private final ApiCallback mListener;
    private final LoginResponse.LoginUser mLoginUser;

    public ModifyPreference(BaseActivity pBaseActivity, ApiCallback pApiCallback) {
        this.mBaseActivity = pBaseActivity;
        this.mListener = pApiCallback;
        this.mLoginUser = AppSharedPrefs.getInstance(mBaseActivity).getLoginUserModel();
    }

    public void addOrUpdateSellerCustomers() {

        SellerProductUnitCustomerRequest sellerProductUnitCustomerRequest = new SellerProductUnitCustomerRequest();
        sellerProductUnitCustomerRequest.setSellerId(mLoginUser.getSellerId());

        mBaseActivity.mApiEnqueueObject = RetrofitClient.getInstance().getInterface().supplierCustomerList(sellerProductUnitCustomerRequest);
        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<CustomerResponse>(mBaseActivity, false) {

            @Override
            public void onResponse(CustomerResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, true)) {
                    AppSharedPrefs.getInstance(mBaseActivity).setSellerCustomers(pResponse);
                }
                if (mListener != null) {
                    mListener.onApiResponse();
                }
            }
        });
    }

    public void addOrUpdateSellerUnits() {

        SellerProductUnitCustomerRequest sellerProductUnitCustomerRequest = new SellerProductUnitCustomerRequest();
        sellerProductUnitCustomerRequest.setSellerId(mLoginUser.getSellerId());

        mBaseActivity.mApiEnqueueObject = RetrofitClient.getInstance().getInterface().supplierUnitList(sellerProductUnitCustomerRequest);
        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<UnitResponse>(mBaseActivity, false) {

            @Override
            public void onResponse(UnitResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    AppSharedPrefs.getInstance(mBaseActivity).setSellerUnits(pResponse);
                }
                if (mListener != null) {
                    mListener.onApiResponse();
                }
            }
        });
    }

    public void addOrUpdateSellerProducts() {

        SellerProductUnitCustomerRequest sellerProductUnitCustomerRequest = new SellerProductUnitCustomerRequest();
        sellerProductUnitCustomerRequest.setSellerId(mLoginUser.getSellerId());

        mBaseActivity.mApiEnqueueObject = RetrofitClient.getInstance().getInterface().supplierProductList(sellerProductUnitCustomerRequest);
        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<ProductResponse>(mBaseActivity, false) {

            @Override
            public void onResponse(ProductResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, true)) {
                    AppSharedPrefs.getInstance(mBaseActivity).setSellerProducts(pResponse);
                }
                if (mListener != null) {
                    mListener.onApiResponse();
                }
            }
        });
    }
}
