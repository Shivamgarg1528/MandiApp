package ig.com.digitalmandi.bean.response.seller;

import java.io.Serializable;

import ig.com.digitalmandi.bean.AbstractResponse;

public class ProductResponse extends AbstractResponse<ProductResponse.Product> {

    public static class Product implements Serializable {
        private String productId;
        private String productName;
        private String productStatus;
        private String productImage;
        private String productQty;
        private String productQtySold;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductStatus() {
            return productStatus;
        }

        public void setProductStatus(String productStatus) {
            this.productStatus = productStatus;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public String getProductQty() {
            return productQty;
        }

        public void setProductQty(String productQty) {
            this.productQty = productQty;
        }

        public String getProductQtySold() {
            return productQtySold;
        }

        public void setProductQtySold(String productQtySold) {
            this.productQtySold = productQtySold;
        }
    }
}
