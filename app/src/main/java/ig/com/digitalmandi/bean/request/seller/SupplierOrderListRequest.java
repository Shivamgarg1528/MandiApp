package ig.com.digitalmandi.bean.request.seller;

public class SupplierOrderListRequest {

    private String sellerId;
    private String customerId;
    private String startDate;
    private String endDate;
    private String page;

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
