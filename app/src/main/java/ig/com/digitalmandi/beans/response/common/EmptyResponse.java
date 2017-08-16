package ig.com.digitalmandi.beans.response.common;

import java.util.List;

/**
 * Created by shiva on 11/4/2016.
 */

public class EmptyResponse {

    /**
     * responseCode : 200
     * success : true
     * message : Fine
     * result : []
     */

    private int responseCode;
    private boolean success;
    private String message;
    private List<?> result;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<?> getResult() {
        return result;
    }

    public void setResult(List<?> result) {
        this.result = result;
    }
}
