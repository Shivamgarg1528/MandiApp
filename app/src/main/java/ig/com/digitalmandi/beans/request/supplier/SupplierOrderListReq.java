package ig.com.digitalmandi.beans.request.supplier;

/**
 * Created by Shivam.Garg on 27-10-2016.
 */

public class SupplierOrderListReq {

    /**
     * sellerId : 1
     * customerId : 1
     * startDate : 2016-12-10
     * endDate : 2016-12-10
     * page : 1
     */

    private String sellerId;
    private String customerId;
    private String startDate;
    private String endDate;
    private String page;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
