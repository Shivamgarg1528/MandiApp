package ig.com.digitalmandi.bean.response.seller;

import ig.com.digitalmandi.bean.AbstractResponse;

/**
 * Created by shivam.garg on 11-11-2016.
 */

public class BillPrintResponse extends AbstractResponse<BillPrintResponse.Bill> {

    public static class Bill {
        private String URL;

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }
    }
}
