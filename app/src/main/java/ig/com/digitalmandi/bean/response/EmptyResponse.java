package ig.com.digitalmandi.bean.response;

import java.util.List;

public class EmptyResponse {
    private int responseCode;
    private boolean success;
    private String message;
    private List<?> result;

    public int getResponseCode() {
        return responseCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<?> getResult() {
        return result;
    }
}
