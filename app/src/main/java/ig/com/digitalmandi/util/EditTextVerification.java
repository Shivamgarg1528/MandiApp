package ig.com.digitalmandi.util;

import android.util.Patterns;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base.BaseActivity;

public class EditTextVerification {

    public static boolean isEmailAddressOk(String pEmailAddress, BaseActivity pBaseActivity) {
        if (Utils.isEmpty(pEmailAddress) || !Patterns.EMAIL_ADDRESS.matcher(pEmailAddress).matches()) {
            pBaseActivity.showToast(pBaseActivity.getString(R.string.enter_valid_email_address));
            return false;
        }
        return true;
    }

    public static boolean isPasswordOk(String pPassword, BaseActivity pBaseActivity) {
        if (Utils.isEmpty(pPassword) || pPassword.length() < 6) {
            pBaseActivity.showToast(pBaseActivity.getString(R.string.string_password_at_least_six_char_long));
            return false;
        }
        return true;
    }

    public static boolean isTinNoOk(String pTinNumber, BaseActivity pBaseActivity) {
        if (Utils.isEmpty(pTinNumber)) {
            pBaseActivity.showToast(pBaseActivity.getString(R.string.string_enter_tin_number));
            return false;
        }
        return true;
    }

    public static boolean isFirmOk(String pFirmName, BaseActivity pBaseActivity) {
        if (Utils.isEmpty(pFirmName)) {
            pBaseActivity.showToast(pBaseActivity.getString(R.string.string_please_enter_firm_name));
            return false;
        }
        return true;
    }


    public static boolean isPhoneNoOk(String pPhoneNo, BaseActivity pBaseActivity) {
        if (Utils.isEmpty(pPhoneNo) || pPhoneNo.length() != 10) {
            pBaseActivity.showToast(pBaseActivity.getString(R.string.string_enter_valid_phone_number));
            return false;
        }
        return true;
    }

    public static boolean isPersonNameOk(String pName, BaseActivity pBaseActivity) {
        if (Utils.isEmpty(pName) || pName.length() < 3) {
            pBaseActivity.showToast(pBaseActivity.getString(R.string.string_name_at_least_three_char_long));
            return false;
        }
        return true;
    }
}
