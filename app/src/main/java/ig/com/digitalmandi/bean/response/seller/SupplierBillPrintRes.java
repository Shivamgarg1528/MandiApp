package ig.com.digitalmandi.bean.response.seller;

import ig.com.digitalmandi.base.AbstractResponse;

/**
 * Created by shivam.garg on 11-11-2016.
 */

public class SupplierBillPrintRes extends AbstractResponse<SupplierBillPrintRes.ResultBean> {

    public static class ResultBean {
        private String URL;

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }
    }
}
