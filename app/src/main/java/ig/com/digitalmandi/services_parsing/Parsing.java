package ig.com.digitalmandi.services_parsing;

import android.content.Context;

import ig.com.digitalmandi.beans.response.common.LoginResModel;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.MyPrefrences;

/**
 * Created by shiva on 10/9/2016.
 */

public class Parsing {

    public static void parseLoginUserData(Context mContext, LoginResModel.ResultBean loginResModel){
        MyPrefrences.setStringPrefrences(ConstantValues.USER_ID,loginResModel.getUserId(),mContext);
        MyPrefrences.setStringPrefrences(ConstantValues.USER_ADDRESS,loginResModel.getUserAddress(),mContext);
        MyPrefrences.setStringPrefrences(ConstantValues.USER_EMAIL_ADDRESS,loginResModel.getUserEmailAddress(),mContext);
        MyPrefrences.setStringPrefrences(ConstantValues.USER_FIRM_NAME,loginResModel.getUserFirmName(),mContext);
        MyPrefrences.setStringPrefrences(ConstantValues.USER_IMAGE_URL,loginResModel.getUserImageUrl(),mContext);
        MyPrefrences.setStringPrefrences(ConstantValues.USER_LANDMARK,loginResModel.getUserLandMark(),mContext);
        MyPrefrences.setStringPrefrences(ConstantValues.USER_MOBILE_NO,loginResModel.getUserMobileNo(),mContext);
        MyPrefrences.setStringPrefrences(ConstantValues.USER_NAME,loginResModel.getUserName(),mContext);
        MyPrefrences.setStringPrefrences(ConstantValues.USER_SELLER_ID,loginResModel.getSellerId(),mContext);
        MyPrefrences.setStringPrefrences(ConstantValues.USER_TIN_NUMBER,loginResModel.getUserTinNumber(),mContext);
        MyPrefrences.setStringPrefrences(ConstantValues.USER_TYPE,loginResModel.getUserType(),mContext);
    }
}
