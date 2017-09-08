package ig.com.digitalmandi.bean.request.seller;

import java.util.List;

import ig.com.digitalmandi.bean.response.seller.OrderDetailResponse;

public class SupplierOrderAddRequest {

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
    private List<OrderDetailResponse.OrderDetail> orderDetails;

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderSubtotalAmt(String orderSubtotalAmt) {
        this.orderSubtotalAmt = orderSubtotalAmt;
    }

    public void setOrderDaamiAmt(String orderDaamiAmt) {
        this.orderDaamiAmt = orderDaamiAmt;
    }

    public void setOrderLabourAmt(String orderLabourAmt) {
        this.orderLabourAmt = orderLabourAmt;
    }

    public void setOrderBardanaAmt(String orderBardanaAmt) {
        this.orderBardanaAmt = orderBardanaAmt;
    }

    public void setOrderDaamiValue(String orderDaamiValue) {
        this.orderDaamiValue = orderDaamiValue;
    }

    public void setOrderLabourValue(String orderLabourValue) {
        this.orderLabourValue = orderLabourValue;
    }

    public void setOrderBardanaValue(String orderBardanaValue) {
        this.orderBardanaValue = orderBardanaValue;
    }

    public void setOrderTotalAmt(String orderTotalAmt) {
        this.orderTotalAmt = orderTotalAmt;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public void setDriverVechileNo(String driverVechileNo) {
        this.driverVechileNo = driverVechileNo;
    }

    public void setVechileRent(String vechileRent) {
        this.vechileRent = vechileRent;
    }

    public void setVechileRentValue(String vechileRentValue) {
        this.vechileRentValue = vechileRentValue;
    }

    public void setOrderTotalNag(String orderTotalNag) {
        this.orderTotalNag = orderTotalNag;
    }

    public void setOrderTotalQuintal(String orderTotalQuintal) {
        this.orderTotalQuintal = orderTotalQuintal;
    }

    public void setOrderDetails(List<OrderDetailResponse.OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
