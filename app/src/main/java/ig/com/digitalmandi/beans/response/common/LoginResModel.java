package ig.com.digitalmandi.beans.response.common;

import ig.com.digitalmandi.base_package.AbstractResponse;

/**
 * Created by shiva on 10/8/2016.
 */

public class LoginResModel extends AbstractResponse<LoginResModel.ResultBean> {

    public static class ResultBean {
        private String userId;
        private String userName;
        private String userEmailAddress;
        private String userFirmName;
        private String userTinNumber;
        private String userMobileNo;
        private String userAddress;
        private String userLandMark;
        private String userType;
        private String sellerId;
        private String deviceType;
        private String createdOn;
        private String updatedOn;
        private String userImageUrl;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserEmailAddress() {
            return userEmailAddress;
        }

        public void setUserEmailAddress(String userEmailAddress) {
            this.userEmailAddress = userEmailAddress;
        }

        public String getUserFirmName() {
            return userFirmName;
        }

        public void setUserFirmName(String userFirmName) {
            this.userFirmName = userFirmName;
        }

        public String getUserTinNumber() {
            return userTinNumber;
        }

        public void setUserTinNumber(String userTinNumber) {
            this.userTinNumber = userTinNumber;
        }

        public String getUserMobileNo() {
            return userMobileNo;
        }

        public void setUserMobileNo(String userMobileNo) {
            this.userMobileNo = userMobileNo;
        }

        public String getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(String userAddress) {
            this.userAddress = userAddress;
        }

        public String getUserLandMark() {
            return userLandMark;
        }

        public void setUserLandMark(String userLandMark) {
            this.userLandMark = userLandMark;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public String getUpdatedOn() {
            return updatedOn;
        }

        public void setUpdatedOn(String updatedOn) {
            this.updatedOn = updatedOn;
        }

        public String getUserImageUrl() {
            return userImageUrl;
        }

        public void setUserImageUrl(String userImageUrl) {
            this.userImageUrl = userImageUrl;
        }
    }
}
