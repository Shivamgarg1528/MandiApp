package ig.com.digitalmandi.bean.request.seller;

/**
 * Created by shivam.garg on 11-11-2016.
 */

public class SupplierOrderBillPrintRequest {
    private String orderId,sellerId;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
