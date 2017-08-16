package ig.com.digitalmandi.utils;

import android.text.TextUtils;
import android.util.Patterns;

import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.toast.ToastMessage;

/**
 * Created by shiva on 11/3/2016.
 */

public class EditTextVerification {

    public static boolean isEmailAddressOk(String email, ParentActivity mContext){
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mContext.showToast(ToastMessage.EMAIL_ADDRESS);
            return false;
        }
        return true;
    }

    public static boolean isPasswordOk(String password, ParentActivity mContext){
        if (password.isEmpty() || password.length() < 6) {
            mContext.showToast(ToastMessage.PASSWORD_SIX_);
            return false;
        }
        return true;
    }

    public static boolean isTinNoOk(String tinNumber, ParentActivity mContext){
        if (TextUtils.isEmpty(tinNumber)) {
            mContext.showToast(ToastMessage.FIRM_TIN_NUMBER);
            return false;
        }
        return true;
    }

    public static boolean isFirmOk(String firmName, ParentActivity mContext){
        if (TextUtils.isEmpty(firmName)) {
            mContext.showToast(ToastMessage.FIRM_NAME);
            return false;
        }
        return true;
    }


    public static boolean isPhoneNoOk(String phoneNo, ParentActivity mContext){
        if(phoneNo.isEmpty() || phoneNo.length() != 10){
            mContext.showToast(ToastMessage.PHONE_NUMBER);
            return false;
        }
        return true;
    }

    public static boolean isPersonNameOk(String name, ParentActivity mContext){
        if (name.isEmpty() || name.length() < 3) {
            mContext.showToast(ToastMessage.NAME);
            return false;
        }
        return true;
    }
}
