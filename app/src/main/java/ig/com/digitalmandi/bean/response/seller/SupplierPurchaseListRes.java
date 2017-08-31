package ig.com.digitalmandi.bean.response.seller;

import android.os.Parcel;
import android.os.Parcelable;

import ig.com.digitalmandi.bean.AbstractResponse;

/**
 * Created by shiva on 10/23/2016.
 */

public class SupplierPurchaseListRes extends AbstractResponse <SupplierPurchaseListRes.ResultBean> {

    public static class ResultBean implements Parcelable {
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
        private String purchaseId;
        private String daamiCost;
        private String daamiValue;
        private String labourCost;
        private String labourValue;
        private String productId;
        private String productInKg;
        private String productName;
        private String productQty;
        private String unitId;
        private String unitValue;
        private String nameOfPerson;
        private String purchaseAmtAcc40Kg;
        private String purchaseAmtAcc100Kg;
        private String stockStatus;
        private String subTotalAmt;
        private String totalAmount;
        private String purchaseDate;
        private String createdOn;
        private String updatedOn;
        private String sellerId;
        private String stockQty         = "0";
        private String localSoldQty     = "0";
        private String sumOfProductInKg = "0";

        public ResultBean() {
        }

        protected ResultBean(Parcel in) {
            this.purchaseId = in.readString();
            this.daamiCost = in.readString();
            this.daamiValue = in.readString();
            this.labourCost = in.readString();
            this.labourValue = in.readString();
            this.productId = in.readString();
            this.productInKg = in.readString();
            this.productName = in.readString();
            this.productQty = in.readString();
            this.unitId = in.readString();
            this.unitValue = in.readString();
            this.nameOfPerson = in.readString();
            this.purchaseAmtAcc40Kg = in.readString();
            this.purchaseAmtAcc100Kg = in.readString();
            this.stockStatus = in.readString();
            this.subTotalAmt = in.readString();
            this.totalAmount = in.readString();
            this.purchaseDate = in.readString();
            this.createdOn = in.readString();
            this.updatedOn = in.readString();
            this.sellerId = in.readString();
            this.stockQty = in.readString();
            this.localSoldQty = in.readString();
            this.sumOfProductInKg = in.readString();
        }

        public String getSumOfProductInKg() {

            if(sumOfProductInKg == null)
                sumOfProductInKg = "0";

            return sumOfProductInKg;
        }

        public void setSumOfProductInKg(String sumOfProductInKg) {
            if (sumOfProductInKg == null)
                this.sumOfProductInKg = "0";
            this.stockQty = String.valueOf(Float.parseFloat(productInKg) - Float.parseFloat(this.sumOfProductInKg));
        }

        public String onGetLeftQty() {
            return String.valueOf(Float.parseFloat(stockQty) - Float.parseFloat(localSoldQty));
        }

        public String getStockQty() {
            return stockQty;
        }

        public void setStockQty(String stockQty) {
            this.stockQty = stockQty;
        }

        public String getLocalSoldQty() {
            return localSoldQty;
        }

        public void setLocalSoldQty(String localSoldQty) {
            this.localSoldQty = localSoldQty;
        }

        public String getPurchaseId() {
            return purchaseId;
        }

        public void setPurchaseId(String purchaseId) {
            this.purchaseId = purchaseId;
        }

        public String getDaamiCost() {
            return daamiCost;
        }

        public void setDaamiCost(String daamiCost) {
            this.daamiCost = daamiCost;
        }

        public String getDaamiValue() {
            return daamiValue;
        }

        public void setDaamiValue(String daamiValue) {
            this.daamiValue = daamiValue;
        }

        public String getLabourCost() {
            return labourCost;
        }

        public void setLabourCost(String labourCost) {
            this.labourCost = labourCost;
        }

        public String getLabourValue() {
            return labourValue;
        }

        public void setLabourValue(String labourValue) {
            this.labourValue = labourValue;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductInKg() {
            return productInKg;
        }

        public void setProductInKg(String productInKg) {
            this.productInKg = productInKg;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductQty() {
            return productQty;
        }

        public void setProductQty(String productQty) {
            this.productQty = productQty;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        public String getUnitValue() {
            return unitValue;
        }

        public void setUnitValue(String unitValue) {
            this.unitValue = unitValue;
        }

        public String getNameOfPerson() {
            return nameOfPerson;
        }

        public void setNameOfPerson(String nameOfPerson) {
            this.nameOfPerson = nameOfPerson;
        }

        public String getPurchaseAmtAcc40Kg() {
            return purchaseAmtAcc40Kg;
        }

        public void setPurchaseAmtAcc40Kg(String purchaseAmtAcc40Kg) {
            this.purchaseAmtAcc40Kg = purchaseAmtAcc40Kg;
        }

        public String getPurchaseAmtAcc100Kg() {
            return purchaseAmtAcc100Kg;
        }

        public void setPurchaseAmtAcc100Kg(String purchaseAmtAcc100Kg) {
            this.purchaseAmtAcc100Kg = purchaseAmtAcc100Kg;
        }

        public String getStockStatus() {
            return stockStatus;
        }

        public void setStockStatus(String stockStatus) {
            this.stockStatus = stockStatus;
        }

        public String getSubTotalAmt() {
            return subTotalAmt;
        }

        public void setSubTotalAmt(String subTotalAmt) {
            this.subTotalAmt = subTotalAmt;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getPurchaseDate() {
            return purchaseDate;
        }

        public void setPurchaseDate(String purchaseDate) {
            this.purchaseDate = purchaseDate;
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

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.purchaseId);
            dest.writeString(this.daamiCost);
            dest.writeString(this.daamiValue);
            dest.writeString(this.labourCost);
            dest.writeString(this.labourValue);
            dest.writeString(this.productId);
            dest.writeString(this.productInKg);
            dest.writeString(this.productName);
            dest.writeString(this.productQty);
            dest.writeString(this.unitId);
            dest.writeString(this.unitValue);
            dest.writeString(this.nameOfPerson);
            dest.writeString(this.purchaseAmtAcc40Kg);
            dest.writeString(this.purchaseAmtAcc100Kg);
            dest.writeString(this.stockStatus);
            dest.writeString(this.subTotalAmt);
            dest.writeString(this.totalAmount);
            dest.writeString(this.purchaseDate);
            dest.writeString(this.createdOn);
            dest.writeString(this.updatedOn);
            dest.writeString(this.sellerId);
            dest.writeString(this.stockQty);
            dest.writeString(this.localSoldQty);
            dest.writeString(this.sumOfProductInKg);
        }
    }
}
