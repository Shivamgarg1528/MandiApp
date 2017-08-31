package ig.com.digitalmandi.bean.request.seller;

/**
 * Created by shiva on 10/22/2016.
 */

public class SupplierPurchaseAddReq {

    private String productId,productName,purchaseDate,unitId,unitValue,nameOfPerson,productInKg,stockStatus, purchaseOperation;
    private int productQty;
    private float purchaseAmtAcc40Kg,purchaseAmtAcc100kg,totalAmount,labourValue,labourCost,daamiValue,daamiCost,subTotalAmt;
    private String sellerId;

    @Override
    public String toString() {
        return "SupplierPurchaseAddReq{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", purchaseDate='" + purchaseDate + '\'' +
                ", unitId='" + unitId + '\'' +
                ", unitValue='" + unitValue + '\'' +
                ", nameOfPerson='" + nameOfPerson + '\'' +
                ", productInKg='" + productInKg + '\'' +
                ", stockStatus='" + stockStatus + '\'' +
                ", productQty=" + productQty +
                ", purchaseAmtAcc40Kg=" + purchaseAmtAcc40Kg +
                ", purchaseAmtAcc100kg=" + purchaseAmtAcc100kg +
                ", totalAmount=" + totalAmount +
                ", labourValue=" + labourValue +
                ", labourCost=" + labourCost +
                ", daamiValue=" + daamiValue +
                ", daamiCost=" + daamiCost +
                ", subTotalAmt=" + subTotalAmt +
                '}';
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public float getSubTotalAmt() {
        return subTotalAmt;
    }

    public void setSubTotalAmt(float subTotalAmt) {
        this.subTotalAmt = subTotalAmt;
    }

    public float getLabourValue() {
        return labourValue;
    }

    public void setLabourValue(float labourValue) {
        this.labourValue = labourValue;
    }

    public float getLabourCost() {
        return labourCost;
    }

    public void setLabourCost(float labourCost) {
        this.labourCost = labourCost;
    }

    public float getDaamiValue() {
        return daamiValue;
    }

    public void setDaamiValue(float daamiValue) {
        this.daamiValue = daamiValue;
    }

    public float getDaamiCost() {
        return daamiCost;
    }

    public void setDaamiCost(float daamiCost) {
        this.daamiCost = daamiCost;
    }

    public String getProductInKg() {
        return productInKg;
    }


    public void setProductInKg(String productInKg) {
        this.productInKg = productInKg;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public float getPurchaseAmtAcc40Kg() {
        return purchaseAmtAcc40Kg;
    }

    public void setPurchaseAmtAcc40Kg(float purchaseAmtAcc40Kg) {
        this.purchaseAmtAcc40Kg = purchaseAmtAcc40Kg;
    }

    public float getPurchaseAmtAcc100kg() {
        return purchaseAmtAcc100kg;
    }

    public void setPurchaseAmtAcc100kg(float purchaseAmtAcc100kg) {
        this.purchaseAmtAcc100kg = purchaseAmtAcc100kg;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPurchaseOperation() {
        return purchaseOperation;
    }

    public void setPurchaseOperation(String purchaseOperation) {
        this.purchaseOperation = purchaseOperation;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
