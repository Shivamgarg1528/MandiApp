package ig.com.digitalmandi.beans.request.supplier;

/**
 * Created by shivam.garg on 25-10-2016.
 */

public class SupplierPurchasePaymentReq {

    @Override
    public String toString() {
        return "SupplierPurchasePaymentReq{" +
                ", id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                ", flag='" + flag + '\'' +
                ", interestAmt='" + interestAmt + '\'' +
                ", interestPaid=" + interestPaid +
                ", interestRate='" + interestRate + '\'' +
                ", paymentType='" + paymentType + '\'' +
                '}';
    }

    /**
     * paymentId : 10
     * id : 12
     * date : 2016-10-12
     * amount : 2000
     * flag : 1
     * interestAmt : 20
     * interestPaid : true
     * interestRate : 1.0
     * paymentType : Cash
     */
    private String id;
    private String date;
    private String amount;
    private String flag;
    private String interestAmt;
    private String interestPaid;
    private String interestRate;
    private String paymentType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getInterestAmt() {
        return interestAmt;
    }

    public void setInterestAmt(String interestAmt) {
        this.interestAmt = interestAmt;
    }

    public String isInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(String interestPaid) {
        this.interestPaid = interestPaid;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
