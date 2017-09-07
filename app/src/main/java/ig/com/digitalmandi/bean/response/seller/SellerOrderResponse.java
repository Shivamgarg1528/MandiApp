package ig.com.digitalmandi.bean.response.seller;

import java.io.Serializable;

import ig.com.digitalmandi.bean.AbstractResponse;
import ig.com.digitalmandi.util.Helper;

public class SellerOrderResponse extends AbstractResponse<SellerOrderResponse.Order> {

    public static class Order implements Serializable {

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
        private String sumOfProductInKg;

        // Locally Added
        private String stockQtyInKg = "0";
        private String localSoldQtyInKg = "0";


        public String getSumOfProductInKg() {
            return sumOfProductInKg;
        }

        public void setSumOfProductInKg(String sumOfProductInKg) {
            if (Helper.isEmpty(sumOfProductInKg)) {
                this.sumOfProductInKg = "0";
            }
            this.stockQtyInKg = String.valueOf(Float.parseFloat(productInKg) - Float.parseFloat(this.sumOfProductInKg));
        }

        public String getLeftQty() {
            return String.valueOf(Float.parseFloat(stockQtyInKg) - Float.parseFloat(localSoldQtyInKg));
        }

        public String getLocalSoldQtyInKg() {
            return localSoldQtyInKg;
        }

        public void setLocalSoldQtyInKg(String localSoldQtyInKg) {
            this.localSoldQtyInKg = localSoldQtyInKg;
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
    }
}
