package ig.com.digitalmandi.beans.response.supplier;

import android.os.Parcel;
import android.os.Parcelable;

import ig.com.digitalmandi.base_package.AbstractResponse;

/**
 * Created by Shivam.Garg on 27-10-2016.
 */

public class SupplierOrderListRes extends AbstractResponse<SupplierOrderListRes.ResultBean> {

    public static class ResultBean implements Parcelable {

        private String orderId;
        private String customerId;
        private String sellerId;
        private String orderDate;
        private String orderTotalAmt;
        private String orderSubTotalAmt;
        private String orderDaamiAmt;
        private String orderDaamiValue;
        private String orderLabourAmt;
        private String orderLabourValue;
        private String driverVechileNo;
        private String vechileRent;
        private String vechileRentValue;
        private String driverName;
        private String driverNumber;
        private String orderBardanaAmt;
        private String orderBardanaValue;
        private String orderTotalNag;
        private String orderTotalQuintal;

        public String getOrderTotalNag() {
            return orderTotalNag;
        }

        public void setOrderTotalNag(String orderTotalNag) {
            this.orderTotalNag = orderTotalNag;
        }

        public String getOrderTotalQuintal() {
            return orderTotalQuintal;
        }

        public void setOrderTotalQuintal(String orderTotalQuintal) {
            this.orderTotalQuintal = orderTotalQuintal;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getOrderTotalAmt() {
            return orderTotalAmt;
        }

        public void setOrderTotalAmt(String orderTotalAmt) {
            this.orderTotalAmt = orderTotalAmt;
        }

        public String getOrderSubTotalAmt() {
            return orderSubTotalAmt;
        }

        public void setOrderSubTotalAmt(String orderSubTotalAmt) {
            this.orderSubTotalAmt = orderSubTotalAmt;
        }

        public String getOrderDaamiAmt() {
            return orderDaamiAmt;
        }

        public void setOrderDaamiAmt(String orderDaamiAmt) {
            this.orderDaamiAmt = orderDaamiAmt;
        }

        public String getOrderDaamiValue() {
            return orderDaamiValue;
        }

        public void setOrderDaamiValue(String orderDaamiValue) {
            this.orderDaamiValue = orderDaamiValue;
        }

        public String getOrderLabourAmt() {
            return orderLabourAmt;
        }

        public void setOrderLabourAmt(String orderLabourAmt) {
            this.orderLabourAmt = orderLabourAmt;
        }

        public String getOrderLabourValue() {
            return orderLabourValue;
        }

        public void setOrderLabourValue(String orderLabourValue) {
            this.orderLabourValue = orderLabourValue;
        }

        public String getDriverVechileNo() {
            return driverVechileNo;
        }

        public void setDriverVechileNo(String driverVechileNo) {
            this.driverVechileNo = driverVechileNo;
        }

        public String getVechileRent() {
            return vechileRent;
        }

        public void setVechileRent(String vechileRent) {
            this.vechileRent = vechileRent;
        }

        public String getVechileRentValue() {
            return vechileRentValue;
        }

        public void setVechileRentValue(String vechileRentValue) {
            this.vechileRentValue = vechileRentValue;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public String getDriverNumber() {
            return driverNumber;
        }

        public void setDriverNumber(String driverNumber) {
            this.driverNumber = driverNumber;
        }

        public String getOrderBardanaAmt() {
            return orderBardanaAmt;
        }

        public void setOrderBardanaAmt(String orderBardanaAmt) {
            this.orderBardanaAmt = orderBardanaAmt;
        }

        public String getOrderBardanaValue() {
            return orderBardanaValue;
        }

        public void setOrderBardanaValue(String orderBardanaValue) {
            this.orderBardanaValue = orderBardanaValue;
        }

        public ResultBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.orderId);
            dest.writeString(this.customerId);
            dest.writeString(this.sellerId);
            dest.writeString(this.orderDate);
            dest.writeString(this.orderTotalAmt);
            dest.writeString(this.orderSubTotalAmt);
            dest.writeString(this.orderDaamiAmt);
            dest.writeString(this.orderDaamiValue);
            dest.writeString(this.orderLabourAmt);
            dest.writeString(this.orderLabourValue);
            dest.writeString(this.driverVechileNo);
            dest.writeString(this.vechileRent);
            dest.writeString(this.vechileRentValue);
            dest.writeString(this.driverName);
            dest.writeString(this.driverNumber);
            dest.writeString(this.orderBardanaAmt);
            dest.writeString(this.orderBardanaValue);
            dest.writeString(this.orderTotalNag);
            dest.writeString(this.orderTotalQuintal);
        }

        protected ResultBean(Parcel in) {
            this.orderId = in.readString();
            this.customerId = in.readString();
            this.sellerId = in.readString();
            this.orderDate = in.readString();
            this.orderTotalAmt = in.readString();
            this.orderSubTotalAmt = in.readString();
            this.orderDaamiAmt = in.readString();
            this.orderDaamiValue = in.readString();
            this.orderLabourAmt = in.readString();
            this.orderLabourValue = in.readString();
            this.driverVechileNo = in.readString();
            this.vechileRent = in.readString();
            this.vechileRentValue = in.readString();
            this.driverName = in.readString();
            this.driverNumber = in.readString();
            this.orderBardanaAmt = in.readString();
            this.orderBardanaValue = in.readString();
            this.orderTotalNag = in.readString();
            this.orderTotalQuintal = in.readString();
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
