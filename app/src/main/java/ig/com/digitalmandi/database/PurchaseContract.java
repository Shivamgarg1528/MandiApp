package ig.com.digitalmandi.database;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import ig.com.digitalmandi.bean.response.seller.SellerOrderResponse;

/**
 * Created by shiva on 10/23/2016.
 */

public class PurchaseContract <T> extends BaseContract<T> {

    public static final String PATH  = "purchase";
    private static final String TYPE = ".purchase_items";

    public PurchaseContract(Context mContext) {
        super(mContext);
    }

    @Override
    public void insertBulkData(final List<T> dataList, final Uri mUri, final OnInsertBulkDataSuccessFully listener) {

        MyAsyncQueryHandler deleteAsync = new MyAsyncQueryHandler(getContext()){

            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                super.onDeleteComplete(token, cookie, result);

                List<SellerOrderResponse.Order> purchaseList = (List<SellerOrderResponse.Order>) dataList;
                ArrayList<ContentProviderOperation> batch             = new ArrayList<ContentProviderOperation>(purchaseList.size());
                for(int index = 0 ;index < purchaseList.size() ; index++){
                    SellerOrderResponse.Order purchase = purchaseList.get(index);
                    ContentProviderOperation.Builder builder        = ContentProviderOperation.newInsert(mUri);

                    builder.withValue(Purchase.PRODUCT_ID            ,purchase.getProductId());
                    builder.withValue(Purchase.PRODUCT_NAME          ,purchase.getProductName());
                    builder.withValue(Purchase.PRODUCT_IN_KG         ,purchase.getProductInKg());
                    builder.withValue(Purchase.PRODUCT_QTY           ,purchase.getProductQty());
                    builder.withValue(Purchase.PURCHASE_AMT_IN_40    ,purchase.getPurchaseAmtAcc40Kg());
                    builder.withValue(Purchase.PURCHASE_AMT_IN_100   ,purchase.getPurchaseAmtAcc100Kg());
                    builder.withValue(Purchase.UNIT_ID               ,purchase.getUnitId());
                    builder.withValue(Purchase.UNIT_VALUE            ,purchase.getUnitValue());
                    builder.withValue(Purchase.TOTAL_AMOUNT          ,purchase.getTotalAmount());
                    builder.withValue(Purchase.PURCHASE_DATE         ,purchase.getPurchaseDate());
                    builder.withValue(Purchase.NAME_OF_PERSON        ,purchase.getNameOfPerson());
                    builder.withValue(Purchase._ID                   ,purchase.getPurchaseId());
                    builder.withValue(Purchase.DAAMI_VALUE           ,purchase.getDaamiValue());
                    builder.withValue(Purchase.DAAMI_COST            ,purchase.getDaamiCost());
                    builder.withValue(Purchase.LABOUR_VALUE          ,purchase.getLabourValue());
                    builder.withValue(Purchase.LABOUR_COST           ,purchase.getLabourCost());
                    builder.withValue(Purchase.SUBTOTAL_AMOUNT       ,purchase.getSubTotalAmt());
                    builder.withValue(Purchase.STOCK_STATUS          ,purchase.getStockStatus());
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
                SellerOrderResponse.Order product = new SellerOrderResponse.Order();
                product.setPurchaseId           (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase._ID)));
                product.setProductId            (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PRODUCT_ID)));
                product.setProductName          (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PRODUCT_NAME)));
                product.setProductInKg          (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PRODUCT_IN_KG)));
                product.setProductQty           (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PRODUCT_QTY)));
                product.setPurchaseDate         (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PURCHASE_DATE)));
                product.setUnitId               (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.UNIT_ID)));
                product.setUnitValue            (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.UNIT_VALUE)));
                product.setPurchaseAmtAcc40Kg   (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PURCHASE_AMT_IN_40)));
                product.setPurchaseAmtAcc100Kg  (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PURCHASE_AMT_IN_100)));
                product.setNameOfPerson         (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.NAME_OF_PERSON)));
                product.setDaamiCost            (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.DAAMI_COST)));
                product.setDaamiValue           (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.DAAMI_VALUE)));
                product.setLabourCost           (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.LABOUR_COST)));
                product.setLabourValue          (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.LABOUR_VALUE)));
                product.setStockStatus          (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.STOCK_STATUS)));
                product.setSubTotalAmt          (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.SUBTOTAL_AMOUNT)));
                product.setTotalAmount          (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.TOTAL_AMOUNT)));
                return (T) product;
            }
        }
        else
            return null;
        return null;
    }

    @Override
    public List<T> getListOfObject(Cursor cursor) {
        List<SellerOrderResponse.Order> purchaseList = new ArrayList<>();
        if(cursor != null && cursor.getCount() > 0){
            for (Cursor iterableCursor : new IterableCursor(cursor)) {
                SellerOrderResponse.Order product = new SellerOrderResponse.Order();
                product.setPurchaseId(iterableCursor.getString(iterableCursor.getColumnIndex(Purchase._ID)));
                product.setProductId            (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PRODUCT_ID)));
                product.setProductName          (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PRODUCT_NAME)));
                product.setProductInKg          (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PRODUCT_IN_KG)));
                product.setProductQty           (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PRODUCT_QTY)));
                product.setPurchaseDate         (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PURCHASE_DATE)));
                product.setUnitId               (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.UNIT_ID)));
                product.setUnitValue            (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.UNIT_VALUE)));
                product.setPurchaseAmtAcc40Kg   (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PURCHASE_AMT_IN_40)));
                product.setPurchaseAmtAcc100Kg  (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.PURCHASE_AMT_IN_100)));
                product.setNameOfPerson         (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.NAME_OF_PERSON)));
                product.setDaamiCost            (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.DAAMI_COST)));
                product.setDaamiValue           (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.DAAMI_VALUE)));
                product.setLabourCost           (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.LABOUR_COST)));
                product.setLabourValue          (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.LABOUR_VALUE)));
                product.setStockStatus          (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.STOCK_STATUS)));
                product.setSubTotalAmt          (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.SUBTOTAL_AMOUNT)));
                product.setTotalAmount          (iterableCursor.getString(iterableCursor.getColumnIndex(Purchase.TOTAL_AMOUNT)));
                purchaseList.add(product);
            }
        }
        return (List<T>) purchaseList;
    }

    public static final class Purchase {

        public static final String TABLE_NAME               = "PurchaseInfo";

        public static final String _ID                      = "_Id";
        public static final String PRODUCT_ID               = "productId";
        public static final String PRODUCT_NAME             = "productName";
        public static final String PRODUCT_IN_KG            = "productInKg";
        public static final String NAME_OF_PERSON           = "nameOfPerson";
        public static final String PRODUCT_QTY              = "productQty";
        public static final String PURCHASE_AMT_IN_40       = "purchaseAmtAcc40Kg";
        public static final String PURCHASE_AMT_IN_100      = "purchaseAmtAcc100Kg";
        public static final String PURCHASE_DATE            = "purchaseDate";
        public static final String UNIT_ID                  = "unitId";
        public static final String UNIT_VALUE               = "unitValue";
        public static final String TOTAL_AMOUNT             = "totalAmount";
        public static final String DAAMI_COST               = "daamiCost";
        public static final String DAAMI_VALUE              = "daamiValue";
        public static final String LABOUR_COST              = "labourCost";
        public static final String LABOUR_VALUE             = "labourValue";
        public static final String STOCK_STATUS             = "stockStatus";
        public static final String SUBTOTAL_AMOUNT          = "subTotalAmt";

        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI           = Uri.withAppendedPath(AppContentProvider.CONTENT_URI,PATH);
        public static final String CONTENT_TYPE       = ContentResolver.CURSOR_DIR_BASE_TYPE  +"/vnd.com.satguru"  + TYPE;
        public static final String CONTENT_ITEM_TYPE  = ContentResolver.CURSOR_ITEM_BASE_TYPE +"/vnd.com.satguru"  + TYPE;
        public static final String SORT_ORDER_DEFAULT = PRODUCT_ID + " ASC";
        public static final String CREATE_TABLE       =
                "Create Table "
                        + TABLE_NAME          + " ( "
                        + PRODUCT_ID          + " TEXT, "
                        + PRODUCT_NAME        + " TEXT, "
                        + PRODUCT_QTY         + " TEXT, "
                        + PRODUCT_IN_KG       + " TEXT, "
                        + NAME_OF_PERSON      + " TEXT, "
                        + PURCHASE_AMT_IN_40  + " TEXT, "
                        + PURCHASE_AMT_IN_100 + " TEXT, "
                        + PURCHASE_DATE       + " TEXT, "
                        + UNIT_ID             + " TEXT, "
                        + UNIT_VALUE          + " TEXT, "
                        + DAAMI_COST          + " TEXT, "
                        + DAAMI_VALUE         + " TEXT, "
                        + LABOUR_COST         + " TEXT, "
                        + LABOUR_VALUE        + " TEXT, "
                        + STOCK_STATUS        + " TEXT, "
                        + SUBTOTAL_AMOUNT     + " TEXT, "
                        + _ID                 + " TEXT, "
                        + TOTAL_AMOUNT        + " TEXT "
                        +")";
    }
}