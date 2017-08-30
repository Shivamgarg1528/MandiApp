package ig.com.digitalmandi.beans.response.supplier;

import java.io.Serializable;

import ig.com.digitalmandi.base_package.AbstractResponse;

public class SupplierOrderListResponse extends AbstractResponse<SupplierOrderListResponse.Order> {

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

        public String getCustomerId() {
            return customerId;
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

        public String getOrderDaamiValue() {
            return orderDaamiValue;
        }

        public String getOrderLabourAmt() {
            return orderLabourAmt;
        }

        public String getOrderLabourValue() {
            return orderLabourValue;
        }

        public String getDriverVechileNo() {
            return driverVechileNo;
        }

        public String getVechileRent() {
            return vechileRent;
        }

        public String getVechileRentValue() {
            return vechileRentValue;
        }

        public String getDriverName() {
            return driverName;
        }

        public String getDriverNumber() {
            return driverNumber;
        }

        public String getOrderBardanaAmt() {
            return orderBardanaAmt;
        }

        public String getOrderBardanaValue() {
            return orderBardanaValue;
        }

        public String getOrderTotalNag() {
            return orderTotalNag;
        }

        public String getOrderTotalQuintal() {
            return orderTotalQuintal;
        }
    }
}
