package ig.com.digitalmandi.bean.request;

public class LoginRequest {

    private String userEmailAddress;
    private String userPassword;
    private String deviceId;
    private String deviceType;
    private String deviceToken;

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
