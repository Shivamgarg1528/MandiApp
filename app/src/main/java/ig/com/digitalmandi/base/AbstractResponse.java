package ig.com.digitalmandi.base;

import java.util.List;

public abstract class AbstractResponse<T> {

    private int responseCode;
    private boolean success;
    private String message;
    private List<T> result;

    public int getResponseCode() {
        return responseCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getResult() {
        return result;
    }
}
