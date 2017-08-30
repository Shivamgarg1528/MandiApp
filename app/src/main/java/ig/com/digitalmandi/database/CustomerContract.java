package ig.com.digitalmandi.database;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import ig.com.digitalmandi.beans.request.supplier.SellerCustomerList;

/**
 * Created by shivam.garg on 18-10-2016.
 */

public class CustomerContract<T> extends BaseContract<T> {

    public  static final String PATH = "customer";
    private static final String TYPE = ".customer_items";

    public CustomerContract(Context mContext) {
        super(mContext);
    }

    @Override
    public void insertBulkData(final List<T> dataList, final Uri mUri,final OnInsertBulkDataSuccessFully listener) {

        MyAsyncQueryHandler deleteAsync = new MyAsyncQueryHandler(getContext()){

            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                super.onDeleteComplete(token, cookie, result);

                List<SellerCustomerList.Customer> customerList = (List<SellerCustomerList.Customer>) dataList;
                ArrayList<ContentProviderOperation> batch              = new ArrayList<ContentProviderOperation>(customerList.size());
                for(int index = 0 ;index < customerList.size() ; index++){
                    SellerCustomerList.Customer customer = customerList.get(index);
                    ContentProviderOperation.Builder builder     = ContentProviderOperation.newInsert(mUri);
                    builder.withValue(CustomerContract.Customer.USER_ID, customer.getUserId());
                    builder.withValue(CustomerContract.Customer.USER_NAME, customer.getUserName());
                    builder.withValue(CustomerContract.Customer.USER_EMAIL_ADDRESS, customer.getUserEmailAddress());
                    builder.withValue(CustomerContract.Customer.USER_FIRM_NAME, customer.getUserFirmName());
                    builder.withValue(CustomerContract.Customer.USER_TIN_NUMBER, customer.getUserTinNumber());
                    builder.withValue(CustomerContract.Customer.USER_MOBILE_NO, customer.getUserMobileNo());
                    builder.withValue(CustomerContract.Customer.USER_ADDRESS, customer.getUserAddress());
                    builder.withValue(CustomerContract.Customer.USER_LANDMARK, customer.getUserLandMark());
                    builder.withValue(CustomerContract.Customer.USER_TYPE, customer.getUserType());
                    builder.withValue(CustomerContract.Customer.USER_SELLER_ID, customer.getSellerId());
                    builder.withValue(CustomerContract.Customer.USER_DEVICE_TYPE, customer.getDeviceType());
                    builder.withValue(CustomerContract.Customer.USER_CREATED_ON, customer.getCreatedOn());
                    builder.withValue(CustomerContract.Customer.USER_UPDATED_ON, customer.getUpdatedOn());
                    builder.withValue(CustomerContract.Customer.USER_IMAGE_URL, customer.getUserImageUrl());
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
                SellerCustomerList.Customer customer = new SellerCustomerList.Customer();
                customer.setUserId(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_ID)));
                customer.setCreatedOn(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_CREATED_ON)));
                customer.setDeviceType(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_DEVICE_TYPE)));
                customer.setSellerId(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_SELLER_ID)));
                customer.setUpdatedOn(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_UPDATED_ON)));
                customer.setUserAddress(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_ADDRESS)));
                customer.setUserEmailAddress(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_EMAIL_ADDRESS)));
                customer.setUserFirmName(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_FIRM_NAME)));
                customer.setUserImageUrl(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_IMAGE_URL)));
                customer.setUserLandMark(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_LANDMARK)));
                customer.setUserMobileNo(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_MOBILE_NO)));
                customer.setUserName(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_NAME)));
                customer.setUserTinNumber(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_TIN_NUMBER)));
                customer.setUserType(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_TYPE)));
                return (T) customer;
            }
        }
        else
            return null;
        return null;
    }

    @Override
    public List<T> getListOfObject(Cursor cursor) {
        List<SellerCustomerList.Customer> customerList = new ArrayList<>();
        if(cursor != null && cursor.getCount() > 0){
            for (Cursor iterableCursor : new IterableCursor(cursor)) {
                SellerCustomerList.Customer customer = new SellerCustomerList.Customer();
                customer.setUserId(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_ID)));
                customer.setCreatedOn(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_CREATED_ON)));
                customer.setDeviceType(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_DEVICE_TYPE)));
                customer.setSellerId(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_SELLER_ID)));
                customer.setUpdatedOn(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_UPDATED_ON)));
                customer.setUserAddress(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_ADDRESS)));
                customer.setUserEmailAddress(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_EMAIL_ADDRESS)));
                customer.setUserFirmName(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_FIRM_NAME)));
                customer.setUserImageUrl(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_IMAGE_URL)));
                customer.setUserLandMark(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_LANDMARK)));
                customer.setUserMobileNo(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_MOBILE_NO)));
                customer.setUserName(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_NAME)));
                customer.setUserTinNumber(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_TIN_NUMBER)));
                customer.setUserType(iterableCursor.getString(iterableCursor.getColumnIndex(CustomerContract.Customer.USER_TYPE)));
                customerList.add(customer);
            }
        }
        return (List<T>) customerList;
    }

    public static final class Customer {

        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AppContentProvider.CONTENT_URI, PATH);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.satguru" + TYPE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.satguru" + TYPE;
        static final String TABLE_NAME         = "CustomerInfo";
        static final String USER_ID            = "userId";
        public static final String SORT_ORDER_DEFAULT = USER_ID + " ASC";
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
        public static final String[] PROJECTION_ALL   = {USER_ID, USER_NAME, USER_EMAIL_ADDRESS, USER_FIRM_NAME, USER_TIN_NUMBER,USER_MOBILE_NO,USER_ADDRESS,USER_LANDMARK,USER_TYPE,USER_SELLER_ID,USER_DEVICE_TYPE,USER_CREATED_ON,USER_UPDATED_ON,USER_IMAGE_URL};
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