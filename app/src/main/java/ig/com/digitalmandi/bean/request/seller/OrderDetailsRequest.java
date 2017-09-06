package ig.com.digitalmandi.bean.request.seller;

import java.io.Serializable;

public class OrderDetailsRequest implements Serializable {

    private String id;
    private String flag;

    /* Locally added*/
    private String message;

    public void setId(String id) {
        this.id = id;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
