package ig.com.digitalmandi.bean.request.seller;

public class RegistrationRequest {

    private String userName;
    private String userFirmName;
    private String userTinNumber;
    private String userEmailAddress;
    private String userPassword;
    private String userMobileNo;
    private String userPicBase64;
    private String userAddress;
    private String userLandMark;
    private String userType;
    private String sellerId;
    private String deviceType;
    private String deviceToken;
    private String deviceId;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserFirmName(String userFirmName) {
        this.userFirmName = userFirmName;
    }

    public void setUserTinNumber(String userTinNumber) {
        this.userTinNumber = userTinNumber;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    public void setUserPicBase64(String userPicBase64) {
        this.userPicBase64 = userPicBase64;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserLandMark(String userLandMark) {
        this.userLandMark = userLandMark;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
