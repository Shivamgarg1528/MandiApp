package ig.com.digitalmandi.beans.response.supplier;

import ig.com.digitalmandi.base_package.AbstractResponse;

/**
 * Created by shiva on 10/9/2016.
 */

public class SupplierListRes extends AbstractResponse<SupplierListRes.ResultBean> {

    public static class ResultBean {
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
