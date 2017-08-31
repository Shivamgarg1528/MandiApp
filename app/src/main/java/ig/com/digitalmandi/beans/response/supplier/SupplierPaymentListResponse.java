package ig.com.digitalmandi.beans.response.supplier;

import ig.com.digitalmandi.base_package.AbstractResponse;

public class SupplierPaymentListResponse extends AbstractResponse<SupplierPaymentListResponse.Payment> {

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

        public String getPaymentId() {
            return paymentId;
        }

        public String getId() {
            return id;
        }

        public String getDate() {
            return date;
        }

        public String getAmount() {
            return amount;
        }

        public String getFlag() {
            return flag;
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
