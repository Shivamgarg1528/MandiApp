package ig.com.digitalmandi.beans.request.supplier;

import android.os.Parcel;
import android.os.Parcelable;

import ig.com.digitalmandi.base_package.AbstractResponse;

/**
 * Created by shiva on 10/11/2016.
 */

public class SupplierCustomerListRes extends AbstractResponse<SupplierCustomerListRes.ResultBean> {

    public static class ResultBean implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.userId);
            dest.writeString(this.userName);
            dest.writeString(this.userEmailAddress);
            dest.writeString(this.userFirmName);
            dest.writeString(this.userTinNumber);
            dest.writeString(this.userMobileNo);
            dest.writeString(this.userAddress);
            dest.writeString(this.userLandMark);
            dest.writeString(this.userType);
            dest.writeString(this.sellerId);
            dest.writeString(this.deviceType);
            dest.writeString(this.createdOn);
            dest.writeString(this.updatedOn);
            dest.writeString(this.userImageUrl);
        }

        public ResultBean() {
        }

        protected ResultBean(Parcel in) {
            this.userId = in.readString();
            this.userName = in.readString();
            this.userEmailAddress = in.readString();
            this.userFirmName = in.readString();
            this.userTinNumber = in.readString();
            this.userMobileNo = in.readString();
            this.userAddress = in.readString();
            this.userLandMark = in.readString();
            this.userType = in.readString();
            this.sellerId = in.readString();
            this.deviceType = in.readString();
            this.createdOn = in.readString();
            this.updatedOn = in.readString();
            this.userImageUrl = in.readString();
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            @Override
            public ResultBean createFromParcel(Parcel source) {
                return new ResultBean(source);
            }

            @Override
            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };
    }
}
