package ig.com.digitalmandi.bean.response.seller;

import ig.com.digitalmandi.bean.AbstractResponse;

public class SellerResponse extends AbstractResponse<SellerResponse.Seller> {

    public static class Seller {

        private String userName;
        private String userId;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
