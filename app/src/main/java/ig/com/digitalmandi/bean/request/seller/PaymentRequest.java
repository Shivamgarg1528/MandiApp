package ig.com.digitalmandi.bean.request.seller;

public class PaymentRequest {

    private String id;
    private String date;
    private String amount;
    private String flag;
    private String interestAmt;
    private String interestPaid;
    private String interestRate;
    private String paymentType;

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setInterestAmt(String interestAmt) {
        this.interestAmt = interestAmt;
    }

    public void setInterestPaid(String interestPaid) {
        this.interestPaid = interestPaid;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
