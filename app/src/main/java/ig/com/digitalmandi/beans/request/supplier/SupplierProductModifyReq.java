package ig.com.digitalmandi.beans.request.supplier;

/**
 * Created by Shivam.Garg on 12-10-2016.
 */

public class SupplierProductModifyReq {

    /**
     * productOperation : add/delete/update
     * productId : 1
     * productName : product
     * productStatus : 1
     * productImage : http://www.aiob.in/shivam/product/201610110424509258.png
     * productQty : 5
     * productQtySold : 0
     * productImageBase64 :
     * sellerId : 1
     */

    private String productOperation;
    private String productId;
    private String productName;
    private String productStatus;
    private String productImage;
    private String productQty;
    private String productQtySold;
    private String productImageBase64;
    private String sellerId;

    public String getProductOperation() {
        return productOperation;
    }

    public void setProductOperation(String productOperation) {
        this.productOperation = productOperation;
    }

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

    public String getProductImageBase64() {
        return productImageBase64;
    }

    public void setProductImageBase64(String productImageBase64) {
        this.productImageBase64 = productImageBase64;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
