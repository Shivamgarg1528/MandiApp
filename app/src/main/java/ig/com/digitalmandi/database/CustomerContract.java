package ig.com.digitalmandi.database;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import ig.com.digitalmandi.beans.request.supplier.SupplierCustomerListRes;

/**
 * Created by shivam.garg on 18-10-2016.
 */

public class CustomerContract<T> extends BaseContract<T> {

    public  static final String PATH = "customer";
    private static final String TYPE = ".customer_items";

    @Override
    public void insertBulkData(final List<T> dataList, final Uri mUri,final OnInsertBulkDataSuccessFully listener) {

        MyAsyncQueryHandler deleteAsync = new MyAsyncQueryHandler(getContext()){

            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                super.onDeleteComplete(token, cookie, result);

                List<SupplierCustomerListRes.ResultBean> customerList = (List<SupplierCustomerListRes.ResultBean>) dataList;
                ArrayList<ContentProviderOperation> batch              = new ArrayList<ContentProviderOperation>(customerList.size());
                for(int index = 0 ;index < customerList.size() ; index++){
                    SupplierCustomerListRes.ResultBean customer = customerList.get(index);
                    ContentProviderOperation.Builder builder     = ContentProviderOperation.newInsert(mUri);
                    builder.withValue(Customer.USER_ID            ,customer.getUserId());
                    builder.withValue(Customer.USER_NAME          ,customer.getUserName());
                    builder.withValue(Customer.USER_EMAIL_ADDRESS ,customer.getUserEmailAddress());
                    builder.withValue(Customer.USER_FIRM_NAME     ,customer.getUserFirmName());
                    builder.withValue(Customer.USER_TIN_NUMBER    ,customer.getUserTinNumber());
                    builder.withValue(Customer.USER_MOBILE_NO     ,customer.getUserMobileNo());
                    builder.withValue(Customer.USER_ADDRESS       ,customer.getUserAddress());
                    builder.withValue(Customer.USER_LANDMARK      ,customer.getUserLandMark());
                    builder.withValue(Customer.USER_TYPE          ,customer.getUserType());
                    builder.withValue(Customer.USER_SELLER_ID     ,customer.getSellerId());
                    builder.withValue(Customer.USER_DEVICE_TYPE   ,customer.getDeviceType());
                    builder.withValue(Customer.USER_CREATED_ON    ,customer.getCreatedOn());
                    builder.withValue(Customer.USER_UPDATED_ON    ,customer.getUpdatedOn());
                    builder.withValue(Customer.USER_IMAGE_URL     ,customer.getUserImageUrl());
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
                SupplierCustomerListRes.ResultBean customer = new SupplierCustomerListRes.ResultBean();
                customer.setUserId           (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_ID)));
                customer.setCreatedOn        (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_CREATED_ON)));
                customer.setDeviceType       (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_DEVICE_TYPE)));
                customer.setSellerId         (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_SELLER_ID)));
                customer.setUpdatedOn        (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_UPDATED_ON)));
                customer.setUserAddress      (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_ADDRESS)));
                customer.setUserEmailAddress (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_EMAIL_ADDRESS)));
                customer.setUserFirmName     (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_FIRM_NAME)));
                customer.setUserImageUrl     (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_IMAGE_URL)));
                customer.setUserLandMark     (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_LANDMARK)));
                customer.setUserMobileNo     (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_MOBILE_NO)));
                customer.setUserName         (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_NAME)));
                customer.setUserTinNumber    (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_TIN_NUMBER)));
                customer.setUserType         (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_TYPE)));
                return (T) customer;
            }
        }
        else
            return null;
        return null;
    }

    @Override
    public List<T> getListOfObject(Cursor cursor) {
        List<SupplierCustomerListRes.ResultBean> customerList = new ArrayList<>();
        if(cursor != null && cursor.getCount() > 0){
            for (Cursor iterableCursor : new IterableCursor(cursor)) {
                SupplierCustomerListRes.ResultBean customer = new SupplierCustomerListRes.ResultBean();
                customer.setUserId           (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_ID)));
                customer.setCreatedOn        (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_CREATED_ON)));
                customer.setDeviceType       (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_DEVICE_TYPE)));
                customer.setSellerId         (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_SELLER_ID)));
                customer.setUpdatedOn        (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_UPDATED_ON)));
                customer.setUserAddress      (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_ADDRESS)));
                customer.setUserEmailAddress (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_EMAIL_ADDRESS)));
                customer.setUserFirmName     (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_FIRM_NAME)));
                customer.setUserImageUrl     (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_IMAGE_URL)));
                customer.setUserLandMark     (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_LANDMARK)));
                customer.setUserMobileNo     (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_MOBILE_NO)));
                customer.setUserName         (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_NAME)));
                customer.setUserTinNumber    (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_TIN_NUMBER)));
                customer.setUserType         (iterableCursor.getString(iterableCursor.getColumnIndex(Customer.USER_TYPE)));
                customerList.add(customer);
            }
        }
        return (List<T>) customerList;
    }

    public CustomerContract(Context mContext) {
        super(mContext);
    }

    public static final class Customer {

        static final String TABLE_NAME         = "CustomerInfo";
        static final String USER_ID            = "userId";
        static final String USER_NAME          = "userName";
        static final String USER_EMAIL_ADDRESS = "userEmailAddress";
        static final String USER_FIRM_NAME     = "userFirmName";
        static final String USER_TIN_NUMBER    = "userTinNumber";
        static final String USER_MOBILE_NO     = "userMobileNo";
        static final String USER_ADDRESS       = "userAddress";
        static final String USER_LANDMARK      = "userLandMark";
        static final String USER_TYPE          = "userType";
        static final String USER_SELLER_ID     = "sellerId";
        static final String USER_DEVICE_TYPE   = "deviceType";
        static final String USER_CREATED_ON    = "createdOn";
        static final String USER_UPDATED_ON    = "updatedOn";
        static final String USER_IMAGE_URL     = "userImageUrl";

        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI           = Uri.withAppendedPath(AppContentProvider.CONTENT_URI,PATH);
        public static final String CONTENT_TYPE       = ContentResolver.CURSOR_DIR_BASE_TYPE +"/vnd.com.satguru"  + TYPE;
        public static final String CONTENT_ITEM_TYPE  = ContentResolver.CURSOR_ITEM_BASE_TYPE +"/vnd.com.satguru" + TYPE;
        public static final String[] PROJECTION_ALL   = {USER_ID, USER_NAME, USER_EMAIL_ADDRESS, USER_FIRM_NAME, USER_TIN_NUMBER,USER_MOBILE_NO,USER_ADDRESS,USER_LANDMARK,USER_TYPE,USER_SELLER_ID,USER_DEVICE_TYPE,USER_CREATED_ON,USER_UPDATED_ON,USER_IMAGE_URL};
        public static final String SORT_ORDER_DEFAULT = USER_ID + " ASC";

        public static final String CREATE_TABLE = "Create Table "
                + TABLE_NAME          + " ( "
                + USER_ID             + " TEXT, "
                + USER_NAME           + " TEXT, "
                + USER_EMAIL_ADDRESS  + " TEXT, "
                + USER_FIRM_NAME      + " TEXT, "
                + USER_TIN_NUMBER     + " TEXT, "
                + USER_MOBILE_NO      + " TEXT, "
                + USER_ADDRESS        + " TEXT, "
                + USER_LANDMARK       + " TEXT, "
                + USER_TYPE           + " TEXT, "
                + USER_SELLER_ID      + " TEXT, "
                + USER_DEVICE_TYPE    + " TEXT, "
                + USER_CREATED_ON     + " TEXT, "
                + USER_UPDATED_ON     + " TEXT, "
                + USER_IMAGE_URL      + " TEXT "
                +")";
    }
}