package ig.com.digitalmandi.beans.response.supplier;

import ig.com.digitalmandi.base_package.AbstractResponse;

/**
 * Created by shivam.garg on 25-10-2016.
 */

public class SupplierPaymentListRes extends AbstractResponse<SupplierPaymentListRes.ResultBean> {

    public static class ResultBean {

        private String paymentId;
        private String id;
        private String date;
        private String amount;
        private String flag;
        private String interestAmt;
        private String interestPaid;
        private String interestRate;
        private String paymentType;

        public String getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }

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

        public String getInterestPaid() {
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
}
