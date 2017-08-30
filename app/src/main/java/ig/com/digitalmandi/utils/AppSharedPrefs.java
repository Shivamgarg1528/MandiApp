package ig.com.digitalmandi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import ig.com.digitalmandi.beans.request.supplier.SellerCustomerList;
import ig.com.digitalmandi.beans.response.common.LoginResponse;
import ig.com.digitalmandi.beans.response.supplier.SellerProductList;
import ig.com.digitalmandi.beans.response.supplier.SellerUnitList;

public class AppSharedPrefs {

    private static final String DEFAULT = "{\"responseCode\":200,\"success\":true,\"message\":\"Fine\",\"result\":[]}";

    private static final String KEY_LOGIN_USER_MODEL = "keyUserModel";
    private static final String KEY_SELLER_CUSTOMERS = "keySellerCustomers";
    private static final String KEY_SELLER_UNITS = "keySellerUnits";
    private static final String KEY_SELLER_PRODUCTS = "keySellerProducts";

    private static AppSharedPrefs sAppSharedPrefs;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private List<SellerCustomerList.Customer> sellerCustomerList;

    private AppSharedPrefs(Context pContext) {
        mSharedPreferences = pContext.getSharedPreferences("AppPreference", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.apply();
    }

    public static synchronized AppSharedPrefs getInstance(Context pContext) {
        if (sAppSharedPrefs == null) {
            sAppSharedPrefs = new AppSharedPrefs(pContext);
        }
        return sAppSharedPrefs;
    }

    public synchronized LoginResponse.LoginUser getLoginUserModel() {
        String json = mSharedPreferences.getString(KEY_LOGIN_USER_MODEL, null);
        return JsonUtils.objectify(json, LoginResponse.LoginUser.class);
    }

    public synchronized void setLoginUserModel(LoginResponse.LoginUser pLoginUserModel) {
        mEditor.putString(KEY_LOGIN_USER_MODEL, JsonUtils.jsonify(pLoginUserModel));
        mEditor.commit();
    }

    public SellerCustomerList getSellerCustomers() {
        String json = mSharedPreferences.getString(KEY_SELLER_CUSTOMERS, DEFAULT);
        return JsonUtils.objectify(json, SellerCustomerList.class);
    }

    public void setSellerCustomers(SellerCustomerList pSellerCustomerListRes) {
        mEditor.putString(KEY_SELLER_CUSTOMERS, JsonUtils.jsonify(pSellerCustomerListRes));
        mEditor.commit();
    }

    public SellerUnitList getSellerUnits() {
        String json = mSharedPreferences.getString(KEY_SELLER_UNITS, DEFAULT);
        return JsonUtils.objectify(json, SellerUnitList.class);
    }

    public void setSellerUnits(SellerUnitList pSellerUnitList) {
        mEditor.putString(KEY_SELLER_UNITS, JsonUtils.jsonify(pSellerUnitList));
        mEditor.commit();
    }

    public SellerProductList getSellerProducts() {
        String json = mSharedPreferences.getString(KEY_SELLER_PRODUCTS, DEFAULT);
        return JsonUtils.objectify(json, SellerProductList.class);
    }

    public void setSellerProducts(SellerProductList pSellerProductList) {
        mEditor.putString(KEY_SELLER_PRODUCTS, JsonUtils.jsonify(pSellerProductList));
        mEditor.commit();
    }

    public void clear() {
        mEditor.clear();
        mEditor.commit();
    }
}
