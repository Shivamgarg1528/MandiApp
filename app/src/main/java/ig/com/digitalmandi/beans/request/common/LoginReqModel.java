package ig.com.digitalmandi.beans.request.common;

/**
 * Created by shivam.garg on 06-10-2016.
 */

public class LoginReqModel{


    /**
     * userEmailAddress : shivam@keyss.in
     * userPassword : 123456
     * deviceId : 12343444s
     * deviceType : 1/2/3
     * deviceToken : 2111111111111111113dded-e49320fjwefweeeeeeeeeeeeeeeeeeeeeeeeeeeeeewfwfr83985503443034-40304
     */

    private String userEmailAddress;
    private String userPassword;
    private String deviceId;
    private String deviceType;
    private String deviceToken;

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
