package ig.com.digitalmandi.beans.request.supplier;

public class SupplierProductModifyRequest {
    private String productOperation;
    private String productId;
    private String productName;
    private String productStatus;
    private String productImage;
    private String productQty;
    private String productQtySold;
    private String productImageBase64;
    private String sellerId;

    public void setProductOperation(String productOperation) {
        this.productOperation = productOperation;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public void setProductQtySold(String productQtySold) {
        this.productQtySold = productQtySold;
    }

    public void setProductImageBase64(String productImageBase64) {
        this.productImageBase64 = productImageBase64;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }
}
