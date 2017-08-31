package ig.com.digitalmandi.bean.request.seller;

/**
 * Created by shivam.garg on 24-10-2016.
 */

public class SupplierPurchaseListReq {


    /**
     * sellerId : 1
     * startDate : 2016-10-26
     * endDate : 2016-10-27
     * page : 2
     */

    private String sellerId;
    private String startDate;
    private String endDate;
    private String page;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
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
