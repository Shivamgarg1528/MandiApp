package ig.com.digitalmandi.bean.response;

import ig.com.digitalmandi.base.AbstractResponse;

public class LoginResponse extends AbstractResponse<LoginResponse.LoginUser> {

    public static class LoginUser {
        private String userId;
        private String userName;
        private String userEmailAddress;
        private String userFirmName;
        private String userTinNumber;
        private String userMobileNo;
        private String userAddress;
        private String userLandMark;
        private int userType;
        private String sellerId;
        private String deviceType;
        private String createdOn;
        private String updatedOn;
        private String userImageUrl;

        public String getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public String getUserEmailAddress() {
            return userEmailAddress;
        }

        public String getUserFirmName() {
            return userFirmName;
        }

        public String getUserTinNumber() {
            return userTinNumber;
        }

        public String getUserMobileNo() {
            return userMobileNo;
        }

        public String getUserAddress() {
            return userAddress;
        }

        public String getUserLandMark() {
            return userLandMark;
        }

        public int getUserType() {
            return userType;
        }

        public String getSellerId() {
            return sellerId;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public String getUpdatedOn() {
            return updatedOn;
        }

        public String getUserImageUrl() {
            return userImageUrl;
        }
    }
}
