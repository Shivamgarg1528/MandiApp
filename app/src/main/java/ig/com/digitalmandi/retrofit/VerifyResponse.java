package ig.com.digitalmandi.retrofit;

import ig.com.digitalmandi.base_package.AbstractResponse;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;

/**
 * Created by shiva on 11/3/2016.
 */

public class VerifyResponse {

    public static boolean isResponseOk(AbstractResponse abstractResponse,boolean checkSize){
      return checkSize == true ? abstractResponse.isSuccess() && abstractResponse.getResponseCode() == 200 && abstractResponse.getResult() != null && abstractResponse.getResult().size() > 0 : abstractResponse.isSuccess() && abstractResponse.getResponseCode() == 200;
    }

    public static boolean isResponseOk(EmptyResponse emptyResponse){
        return emptyResponse.isSuccess() && emptyResponse.getResponseCode() == 200;
    }
}
