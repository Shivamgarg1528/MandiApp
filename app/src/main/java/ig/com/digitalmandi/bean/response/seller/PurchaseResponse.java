package ig.com.digitalmandi.bean.response.seller;

import java.io.Serializable;

import ig.com.digitalmandi.bean.AbstractResponse;
import ig.com.digitalmandi.util.Helper;

public class PurchaseResponse extends AbstractResponse<PurchaseResponse.Purchase> {

    public static class Purchase implements Serializable {

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

        public String getDaamiCost() {
            return daamiCost;
        }

        public String getLabourCost() {
            return labourCost;
        }

        public String getProductId() {
            return productId;
        }

        public String getProductInKg() {
            return productInKg;
        }

        public String getProductName() {
            return productName;
        }

        public String getProductQty() {
            return productQty;
        }

        public String getUnitValue() {
            return unitValue;
        }

        public String getNameOfPerson() {
            return nameOfPerson;
        }

        public String getPurchaseAmtAcc100Kg() {
            return purchaseAmtAcc100Kg;
        }

        public String getSubTotalAmt() {
            return subTotalAmt;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public String getPurchaseDate() {
            return purchaseDate;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }
    }
}
