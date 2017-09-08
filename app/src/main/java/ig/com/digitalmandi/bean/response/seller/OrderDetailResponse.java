package ig.com.digitalmandi.bean.response.seller;

import ig.com.digitalmandi.bean.AbstractResponse;

public class OrderDetailResponse extends AbstractResponse<OrderDetailResponse.OrderDetail> {

    public static class OrderDetail {

        private String orderDetailsId;
        private String orderId;
        private String purchaseId;
        private String productId;
        private String productName;
        private String orderDate;
        private String unitId;
        private String unitValue;
        private String qty;
        private String qtyInKg;
        private String price;
        private String totalPrice;

        public String getOrderDetailsId() {
            return orderDetailsId;
        }

        public void setOrderDetailsId(String orderDetailsId) {
            this.orderDetailsId = orderDetailsId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

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

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
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