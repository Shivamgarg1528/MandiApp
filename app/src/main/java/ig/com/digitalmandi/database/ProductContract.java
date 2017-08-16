package ig.com.digitalmandi.database;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import ig.com.digitalmandi.beans.response.supplier.SupplierProductListRes;

/**
 * Created by shivam.garg on 20-10-2016.
 */

public class ProductContract <T> extends BaseContract<T> {

    public static final String PATH  = "product";
    private static final String TYPE = ".product_items";

    @Override
    public void insertBulkData(final List<T> dataList, final Uri mUri,final OnInsertBulkDataSuccessFully listener) {

        MyAsyncQueryHandler deleteAsync = new MyAsyncQueryHandler(getContext()){

            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                super.onDeleteComplete(token, cookie, result);

                List<SupplierProductListRes.ResultBean> productList     = (List<SupplierProductListRes.ResultBean>) dataList;
                ArrayList<ContentProviderOperation> batch                = new ArrayList<ContentProviderOperation>(productList.size());
                for(int index = 0 ;index < productList.size() ; index++){
                    SupplierProductListRes.ResultBean product     = productList.get(index);
                    ContentProviderOperation.Builder builder       = ContentProviderOperation.newInsert(mUri);
                    builder.withValue(Product.PRODUCT_ID            ,product.getProductId());
                    builder.withValue(Product.PRODUCT_NAME          ,product.getProductName());
                    builder.withValue(Product.PRODUCT_STATUS        ,product.getProductStatus());
                    builder.withValue(Product.PRODUCT_IMAGE_URL     ,product.getProductImage());
                    builder.withValue(Product.PRODUCT_QTY           ,product.getProductQty());
                    builder.withValue(Product.PRODUCT_QTY_SOLD      ,product.getProductQtySold());
                    batch.add(builder.build());
                }
                applyBatchOperation(batch,listener);
            }
        };

        deleteAsync.startDelete(1,null,mUri,null,null);
    }

    @Override
    public T getSingleObject(Cursor cursor) {
        if(cursor != null && cursor.getCount() > 0){
            for (Cursor iterableCursor : new IterableCursor(cursor)) {
                SupplierProductListRes.ResultBean product = new SupplierProductListRes.ResultBean();
                product.setProductId            (iterableCursor.getString(iterableCursor.getColumnIndex(Product.PRODUCT_ID)));
                product.setProductStatus        (iterableCursor.getString(iterableCursor.getColumnIndex(Product.PRODUCT_STATUS)));
                product.setProductName          (iterableCursor.getString(iterableCursor.getColumnIndex(Product.PRODUCT_NAME)));
                product.setProductImage         (iterableCursor.getString(iterableCursor.getColumnIndex(Product.PRODUCT_IMAGE_URL)));
                product.setProductQty           (iterableCursor.getString(iterableCursor.getColumnIndex(Product.PRODUCT_QTY)));
                product.setProductQtySold       (iterableCursor.getString(iterableCursor.getColumnIndex(Product.PRODUCT_QTY_SOLD)));
                return (T) product;
            }
        }
        else
            return null;
        return null;
    }

    @Override
    public List<T> getListOfObject(Cursor cursor) {
        List<SupplierProductListRes.ResultBean> customerList = new ArrayList<>();
        if(cursor != null && cursor.getCount() > 0){
            for (Cursor iterableCursor : new IterableCursor(cursor)) {
                SupplierProductListRes.ResultBean product = new SupplierProductListRes.ResultBean();
                product.setProductId            (iterableCursor.getString(iterableCursor.getColumnIndex(Product.PRODUCT_ID)));
                product.setProductStatus        (iterableCursor.getString(iterableCursor.getColumnIndex(Product.PRODUCT_STATUS)));
                product.setProductName          (iterableCursor.getString(iterableCursor.getColumnIndex(Product.PRODUCT_NAME)));
                product.setProductImage         (iterableCursor.getString(iterableCursor.getColumnIndex(Product.PRODUCT_IMAGE_URL)));
                product.setProductQty           (iterableCursor.getString(iterableCursor.getColumnIndex(Product.PRODUCT_QTY)));
                product.setProductQtySold       (iterableCursor.getString(iterableCursor.getColumnIndex(Product.PRODUCT_QTY_SOLD)));
                customerList.add(product);
            }
        }
        return (List<T>) customerList;
    }

    public ProductContract(Context mContext) {
        super(mContext);
    }

    public static final class Product {


        public static final String TABLE_NAME            = "ProductInfo";
        public static final String PRODUCT_ID            = "productId";
        public static final String PRODUCT_NAME          = "productName";
        public static final String PRODUCT_STATUS        = "productStatus";
        public static final String PRODUCT_IMAGE_URL     = "productImageUrl";
        public static final String PRODUCT_QTY           = "productQty";
        public static final String PRODUCT_QTY_SOLD      = "productQtySold";

        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI           = Uri.withAppendedPath(AppContentProvider.CONTENT_URI,PATH);
        public static final String CONTENT_TYPE       = ContentResolver.CURSOR_DIR_BASE_TYPE  +"/vnd.com.satguru"  + TYPE;
        public static final String CONTENT_ITEM_TYPE  = ContentResolver.CURSOR_ITEM_BASE_TYPE +"/vnd.com.satguru"  + TYPE;
        public static final String[] PROJECTION_ALL   = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_STATUS, PRODUCT_IMAGE_URL,PRODUCT_QTY,PRODUCT_QTY_SOLD};
        public static final String SORT_ORDER_DEFAULT = PRODUCT_ID + " ASC";
        public static final String CREATE_TABLE       =
                "Create Table "
                        + TABLE_NAME        + " ( "
                        + PRODUCT_ID        + " TEXT, "
                        + PRODUCT_NAME      + " TEXT, "
                        + PRODUCT_STATUS    + " TEXT, "
                        + PRODUCT_IMAGE_URL + " TEXT, "
                        + PRODUCT_QTY       + " TEXT, "
                        + PRODUCT_QTY_SOLD  + " TEXT "
                        +")";
    }
}