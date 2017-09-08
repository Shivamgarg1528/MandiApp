package ig.com.digitalmandi.bean.response.seller;

import ig.com.digitalmandi.bean.AbstractResponse;

public class Payments extends AbstractResponse<Payments.Payment> {

    public static class Payment {

        private String paymentId;
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

        public String getDate() {
            return date;
        }

        public String getAmount() {
            return amount;
        }

        public String getInterestAmt() {
            return interestAmt;
        }

        public String getInterestPaid() {
            return interestPaid;
        }

        public String getInterestRate() {
            return interestRate;
        }

        public String getPaymentType() {
            return paymentType;
        }
    }
}
