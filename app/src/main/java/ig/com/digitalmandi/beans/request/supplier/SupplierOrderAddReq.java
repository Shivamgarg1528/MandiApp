package ig.com.digitalmandi.beans.request.supplier;

import java.util.List;

/**
 * Created by shiva on 10/27/2016.
 */

public class SupplierOrderAddReq {

    /**
     * customerId : 1
     * sellerId : 2
     * orderDate : 2016-12-12
     * orderSubtotalAmt : 0.00
     * orderDaamiAmt :
     * orderLabourAmt :
     * orderBardanaAmt :
     * orderDaamiValue : 5.0
     * orderLabourValue : 2.2
     * orderBardanaValue : 4.0
     * orderTotalAmt : 10254.23
     * driverName : AXS
     * driverNumber : 890564685
     * driverVechileNo : Hr-42454
     * vechileRent : 4500
     * vechileRentValue : 90
     * orderDetails : [{"purchaseId":"1","productId":"1","productName":"2","unitId":"1","unitValue":"42.5","qty":"25","qtyInKg":"100.00","price":"2250.00","totalPrice":"30000.00"},{"purchaseId":"1","productId":"1","productName":"2","unitId":"1","unitValue":"42.5","qty":"25","qtyInKg":"100.00","price":"2250.00","totalPrice":"30000.00"},{"purchaseId":"1","productId":"1","productName":"2","unitId":"1","unitValue":"42.5","qty":"25","qtyInKg":"100.00","price":"2250.00","totalPrice":"30000.00"},{"purchaseId":"1","productId":"1","productName":"2","unitId":"1","unitValue":"42.5","qty":"25","qtyInKg":"100.00","price":"2250.00","totalPrice":"30000.00"}]
     */

    private String customerId;
    private String sellerId;
    private String orderDate;
    private String orderSubtotalAmt;
    private String orderDaamiAmt;
    private String orderLabourAmt;
    private String orderBardanaAmt;
    private String orderDaamiValue;
    private String orderLabourValue;
    private String orderBardanaValue;
    private String orderTotalAmt;
    private String driverName;
    private String driverNumber;
    private String driverVechileNo;
    private String vechileRent;
    private String vechileRentValue;
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

    /**
     * purchaseId : 1
     * productId : 1
     * productName : 2
     * unitId : 1
     * unitValue : 42.5
     * qty : 25
     * qtyInKg : 100.00
     * price : 2250.00
     * totalPrice : 30000.00
     */

    private List<OrderDetailsBean> orderDetails;

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

    public String getOrderSubtotalAmt() {
        return orderSubtotalAmt;
    }

    public void setOrderSubtotalAmt(String orderSubtotalAmt) {
        this.orderSubtotalAmt = orderSubtotalAmt;
    }

    public String getOrderDaamiAmt() {
        return orderDaamiAmt;
    }

    public void setOrderDaamiAmt(String orderDaamiAmt) {
        this.orderDaamiAmt = orderDaamiAmt;
    }

    public String getOrderLabourAmt() {
        return orderLabourAmt;
    }

    public void setOrderLabourAmt(String orderLabourAmt) {
        this.orderLabourAmt = orderLabourAmt;
    }

    public String getOrderBardanaAmt() {
        return orderBardanaAmt;
    }

    public void setOrderBardanaAmt(String orderBardanaAmt) {
        this.orderBardanaAmt = orderBardanaAmt;
    }

    public String getOrderDaamiValue() {
        return orderDaamiValue;
    }

    public void setOrderDaamiValue(String orderDaamiValue) {
        this.orderDaamiValue = orderDaamiValue;
    }

    public String getOrderLabourValue() {
        return orderLabourValue;
    }

    public void setOrderLabourValue(String orderLabourValue) {
        this.orderLabourValue = orderLabourValue;
    }

    public String getOrderBardanaValue() {
        return orderBardanaValue;
    }

    public void setOrderBardanaValue(String orderBardanaValue) {
        this.orderBardanaValue = orderBardanaValue;
    }

    public String getOrderTotalAmt() {
        return orderTotalAmt;
    }

    public void setOrderTotalAmt(String orderTotalAmt) {
        this.orderTotalAmt = orderTotalAmt;
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

    public List<OrderDetailsBean> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailsBean> orderDetails) {
        this.orderDetails = orderDetails;
    }



    public static class OrderDetailsBean {
        private String purchaseId;
        private String productId;
        private String productName;
        private String unitId;
        private String unitValue;
        private String qty;
        private String qtyInKg;
        private String price;
        private String totalPrice;

        public String getPurchaseId() {
            return purchaseId;
        }

        public void setPurchaseId(String purchaseId) {
            this.purchaseId = purchaseId;
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

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getQtyInKg() {
            return qtyInKg;
        }

        public void setQtyInKg(String qtyInKg) {
            this.qtyInKg = qtyInKg;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }
    }
}
