package ig.com.digitalmandi.beans.response.supplier;

import android.os.Parcel;
import android.os.Parcelable;

import ig.com.digitalmandi.base_package.AbstractResponse;

/**
 * Created by Shivam.Garg on 12-10-2016.
 */

public class SupplierProductListRes extends AbstractResponse <SupplierProductListRes.ResultBean>{

    public static class ResultBean implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.productId);
            dest.writeString(this.productName);
            dest.writeString(this.productStatus);
            dest.writeString(this.productImage);
            dest.writeString(this.productQty);
            dest.writeString(this.productQtySold);
        }

        public ResultBean() {
        }

        protected ResultBean(Parcel in) {
            this.productId = in.readString();
            this.productName = in.readString();
            this.productStatus = in.readString();
            this.productImage = in.readString();
            this.productQty = in.readString();
            this.productQtySold = in.readString();
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            @Override
            public ResultBean createFromParcel(Parcel source) {
                return new ResultBean(source);
            }

            @Override
            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };
    }
}
