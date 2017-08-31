package ig.com.digitalmandi.bean.response.seller;

import ig.com.digitalmandi.base.AbstractResponse;

public class SupplierOrderDetailListResponse extends AbstractResponse<SupplierOrderDetailListResponse.OrderDetail> {

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

        public String getOrderId() {
            return orderId;
        }

        public String getPurchaseId() {
            return purchaseId;
        }

        public String getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public String getUnitId() {
            return unitId;
        }

        public String getUnitValue() {
            return unitValue;
        }

        public String getQty() {
            return qty;
        }

        public String getQtyInKg() {
            return qtyInKg;
        }

        public String getPrice() {
            return price;
        }

        public String getTotalPrice() {
            return totalPrice;
        }
    }
}