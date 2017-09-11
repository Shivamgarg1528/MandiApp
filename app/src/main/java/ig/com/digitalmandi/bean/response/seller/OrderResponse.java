package ig.com.digitalmandi.bean.response.seller;

import java.io.Serializable;

import ig.com.digitalmandi.bean.AbstractResponse;

public class OrderResponse extends AbstractResponse<OrderResponse.Order> {

    public static class Order implements Serializable {

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

        public String getOrderId() {
            return orderId;
        }

        public String getSellerId() {
            return sellerId;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public String getOrderTotalAmt() {
            return orderTotalAmt;
        }

        public String getOrderSubTotalAmt() {
            return orderSubTotalAmt;
        }

        public String getOrderDaamiAmt() {
            return orderDaamiAmt;
        }

        public String getOrderLabourAmt() {
            return orderLabourAmt;
        }

        public String getVechileRent() {
            return vechileRent;
        }

        public String getDriverNumber() {
            return driverNumber;
        }

        public String getOrderBardanaAmt() {
            return orderBardanaAmt;
        }

        public String getOrderTotalNag() {
            return orderTotalNag;
        }

        public String getOrderTotalQuintal() {
            return orderTotalQuintal;
        }

        public String getDriverName() {
            return driverName;
        }
    }
}
