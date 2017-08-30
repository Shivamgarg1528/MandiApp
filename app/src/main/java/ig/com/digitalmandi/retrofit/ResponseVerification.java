package ig.com.digitalmandi.retrofit;

import ig.com.digitalmandi.base_package.AbstractResponse;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;

public class ResponseVerification {

    public static boolean isResponseOk(AbstractResponse pAbstractResponse, boolean pCheckListSize) {
        return pCheckListSize ? pAbstractResponse.isSuccess() && pAbstractResponse.getResponseCode() == 200 && pAbstractResponse.getResult() != null && pAbstractResponse.getResult().size() > 0 : pAbstractResponse.isSuccess() && pAbstractResponse.getResponseCode() == 200;
    }

    public static boolean isResponseOk(EmptyResponse pEmptyResponse) {
        return pEmptyResponse.isSuccess() && pEmptyResponse.getResponseCode() == 200;
    }
}
