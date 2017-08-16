package ig.com.digitalmandi.base_package;

import java.util.List;

/**
 * Created by shiva on 11/3/2016.
 */

public abstract class AbstractResponse<T> {
    /**
     * responseCode : 200
     * success : true
     * message : Fine
     * result : []
     */
    protected int responseCode;
    protected boolean success;
    protected String message;

    private List<T> result;

    public  List<T> getResult() {
        return result;
    }
    public void setResult(List<T> result) {
        this.result = result;
    }

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
}
