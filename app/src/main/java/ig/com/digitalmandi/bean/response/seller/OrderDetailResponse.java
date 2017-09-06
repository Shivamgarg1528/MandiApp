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

        public String getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
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