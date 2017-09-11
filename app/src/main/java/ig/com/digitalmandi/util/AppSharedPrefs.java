package ig.com.digitalmandi.util;

import android.content.Context;
import android.content.SharedPreferences;

import ig.com.digitalmandi.bean.request.seller.CustomerResponse;
import ig.com.digitalmandi.bean.response.LoginResponse;
import ig.com.digitalmandi.bean.response.seller.ProductResponse;
import ig.com.digitalmandi.bean.response.seller.UnitResponse;

public class AppSharedPrefs {

    private static final String DEFAULT = "{\"responseCode\":200,\"success\":true,\"message\":\"Fine\",\"result\":[]}";

    private static final String KEY_LOGIN_USER_MODEL = "keyUserModel";
    private static final String KEY_SELLER_CUSTOMERS = "keySellerCustomers";
    private static final String KEY_SELLER_UNITS = "keySellerUnits";
    private static final String KEY_SELLER_PRODUCTS = "keySellerProducts";

    private static AppSharedPrefs sAppSharedPrefs;
    private final SharedPreferences mSharedPreferences;
    private final SharedPreferences.Editor mEditor;

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

    public CustomerResponse getSellerCustomers() {
        String json = mSharedPreferences.getString(KEY_SELLER_CUSTOMERS, DEFAULT);
        return JsonUtils.objectify(json, CustomerResponse.class);
    }

    void setSellerCustomers(CustomerResponse pCustomerResponseRes) {
        mEditor.putString(KEY_SELLER_CUSTOMERS, JsonUtils.jsonify(pCustomerResponseRes));
        mEditor.commit();
    }

    public UnitResponse getSellerUnits() {
        String json = mSharedPreferences.getString(KEY_SELLER_UNITS, DEFAULT);
        return JsonUtils.objectify(json, UnitResponse.class);
    }

    void setSellerUnits(UnitResponse pUnitResponse) {
        mEditor.putString(KEY_SELLER_UNITS, JsonUtils.jsonify(pUnitResponse));
        mEditor.commit();
    }

    public ProductResponse getSellerProducts() {
        String json = mSharedPreferences.getString(KEY_SELLER_PRODUCTS, DEFAULT);
        return JsonUtils.objectify(json, ProductResponse.class);
    }

    void setSellerProducts(ProductResponse pProductResponse) {
        mEditor.putString(KEY_SELLER_PRODUCTS, JsonUtils.jsonify(pProductResponse));
        mEditor.commit();
    }

    public void clear() {
        mEditor.clear();
        mEditor.commit();
    }
}
