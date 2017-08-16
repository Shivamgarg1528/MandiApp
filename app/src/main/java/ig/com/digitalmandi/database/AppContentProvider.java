package ig.com.digitalmandi.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by shivam.garg on 18-10-2016.
 */

public class AppContentProvider extends ContentProvider {


    public static final String AUTHORITY = "com.satguru.appcontentprovider";
    public static final String URL = "content://" + AUTHORITY;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    private DbHelper dbHelper;
    private static SQLiteDatabase sqLiteDatabase;
    private static final int CUSTOMER_LIST = 1;
    private static final int CUSTOMER_ID = 2;
    private static final int UNIT_LIST = 3;
    private static final int UNIT_ID = 4;
    private static final int PRODUCT_LIST = 5;
    private static final int PRODUCT_ID = 6;
    private static final int PURCHASE_LIST = 7;
    private static final int PURCHASE_ID = 8;
    private static final UriMatcher URI_MATCHER;

    // prepare the UriMatcher
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, CustomerContract.PATH, CUSTOMER_LIST);
        URI_MATCHER.addURI(AUTHORITY, CustomerContract.PATH + "/#", CUSTOMER_ID);
        URI_MATCHER.addURI(AUTHORITY, UnitContract.PATH, UNIT_LIST);
        URI_MATCHER.addURI(AUTHORITY, UnitContract.PATH + "/#", UNIT_ID);
        URI_MATCHER.addURI(AUTHORITY, ProductContract.PATH, PRODUCT_LIST);
        URI_MATCHER.addURI(AUTHORITY, ProductContract.PATH + "/#", PRODUCT_ID);
        URI_MATCHER.addURI(AUTHORITY, PurchaseContract.PATH, PURCHASE_LIST);
        URI_MATCHER.addURI(AUTHORITY, PurchaseContract.PATH + "/#", PURCHASE_ID);
    }

    public AppContentProvider() {
    }

    @Override
    public String getType(Uri uri) {

        try {
            switch (URI_MATCHER.match(uri)) {

                case CUSTOMER_ID:
                    return CustomerContract.Customer.CONTENT_ITEM_TYPE;

                case CUSTOMER_LIST:
                    return CustomerContract.Customer.CONTENT_TYPE;

                case UNIT_ID:
                    return UnitContract.Unit.CONTENT_ITEM_TYPE;

                case UNIT_LIST:
                    return UnitContract.Unit.CONTENT_TYPE;

                case PRODUCT_ID:
                    return ProductContract.Product.CONTENT_ITEM_TYPE;

                case PRODUCT_LIST:
                    return ProductContract.Product.CONTENT_TYPE;

                case PURCHASE_ID:
                    return PurchaseContract.Purchase.CONTENT_ITEM_TYPE;

                case PURCHASE_LIST:
                    return PurchaseContract.Purchase.CONTENT_TYPE;

                default:
                    return "";
            }
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            return "";
        }
    }

    private Uri insertIntoDb(SQLiteDatabase db, Uri uri, String tableName, ContentValues values) throws Exception {
        long id = db.insert(tableName, null, values);
        Uri itemUri = ContentUris.withAppendedId(uri, id);
        return itemUri;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newUri = null;
        dbHelper.openDataBase();
        try {
            switch (URI_MATCHER.match(uri)) {

                case CUSTOMER_LIST:
                    newUri = insertIntoDb(sqLiteDatabase, uri, CustomerContract.Customer.TABLE_NAME, values);
                    break;

                case UNIT_LIST:
                    newUri = insertIntoDb(sqLiteDatabase, uri, UnitContract.Unit.TABLE_NAME, values);
                    break;

                case PRODUCT_LIST:
                    newUri = insertIntoDb(sqLiteDatabase, uri, ProductContract.Product.TABLE_NAME, values);
                    break;

                case PURCHASE_LIST:
                    newUri = insertIntoDb(sqLiteDatabase, uri, PurchaseContract.Purchase.TABLE_NAME, values);
                    break;

                default:
                    newUri = uri;
            }
        } catch (Exception e) {
            newUri = uri;
            e.printStackTrace();
        } finally {
            dbHelper.closeDataBase();
            return newUri;
        }
    }

    @Override
    public boolean onCreate() {
        Context mContext = getContext();
        if (dbHelper == null) {
            dbHelper = new AppContentProvider.DbHelper(mContext);
            dbHelper.openDataBase();
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        dbHelper.openDataBase();
        Cursor cursor = null;
        try {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            switch (URI_MATCHER.match(uri)) {

                case CUSTOMER_LIST:
                    builder.setTables(CustomerContract.Customer.TABLE_NAME);
                    if (TextUtils.isEmpty(sortOrder)) {
                        sortOrder = CustomerContract.Customer.SORT_ORDER_DEFAULT;
                    }
                    break;

                case CUSTOMER_ID:
                    builder.setTables(CustomerContract.Customer.TABLE_NAME);
                    builder.appendWhere(CustomerContract.Customer.USER_ID + " = " + uri.getLastPathSegment());
                    break;

                case UNIT_LIST:
                    builder.setTables(UnitContract.Unit.TABLE_NAME);
                    if (TextUtils.isEmpty(sortOrder)) {
                        sortOrder = UnitContract.Unit.SORT_ORDER_DEFAULT;
                    }
                    break;

                case UNIT_ID:
                    builder.setTables(UnitContract.Unit.TABLE_NAME);
                    builder.appendWhere(UnitContract.Unit.UNIT_ID + " = " + uri.getLastPathSegment());
                    break;

                case PRODUCT_LIST:
                    builder.setTables(ProductContract.Product.TABLE_NAME);
                    if (TextUtils.isEmpty(sortOrder)) {
                        sortOrder = ProductContract.Product.SORT_ORDER_DEFAULT;
                    }
                    Log.d("AppContentProvider", "My Turn");
                    break;

                case PRODUCT_ID:
                    builder.setTables(ProductContract.Product.TABLE_NAME);
                    builder.appendWhere(ProductContract.Product.PRODUCT_ID + " = " + uri.getLastPathSegment());
                    break;

                case PURCHASE_LIST:
                    builder.setTables(PurchaseContract.Purchase.TABLE_NAME);
                    if (TextUtils.isEmpty(sortOrder)) {
                        sortOrder = PurchaseContract.Purchase.SORT_ORDER_DEFAULT;
                    }
                    Log.d("AppContentProvider", "My Turn");
                    break;

                case PURCHASE_ID:
                    builder.setTables(PurchaseContract.Purchase.TABLE_NAME);
                    builder.appendWhere(PurchaseContract.Purchase.PRODUCT_ID + " = " + uri.getLastPathSegment());
                    break;

            }
            cursor = builder.query(sqLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeDataBase();
            return cursor;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        dbHelper.openDataBase();
        int updateCount = 0;
        try {
            switch (URI_MATCHER.match(uri)) {
                case CUSTOMER_LIST:
                    updateCount = sqLiteDatabase.update(CustomerContract.Customer.TABLE_NAME, values, selection, selectionArgs);
                    break;

                case CUSTOMER_ID:
                    String idStr = uri.getLastPathSegment();
                    String where = CustomerContract.Customer.USER_ID + " = " + idStr;
                    if (!TextUtils.isEmpty(selection)) {
                        where += " AND " + selection;
                    }
                    updateCount = sqLiteDatabase.update(CustomerContract.Customer.TABLE_NAME, values, where, selectionArgs);
                    break;

                case UNIT_LIST:
                    updateCount = sqLiteDatabase.update(UnitContract.Unit.TABLE_NAME, values, selection, selectionArgs);
                    break;

                case UNIT_ID:
                    String idStr1 = uri.getLastPathSegment();
                    String where1 = UnitContract.Unit.UNIT_ID + " = " + idStr1;
                    if (!TextUtils.isEmpty(selection)) {
                        where1 += " AND " + selection;
                    }
                    updateCount = sqLiteDatabase.update(UnitContract.Unit.TABLE_NAME, values, where1, selectionArgs);
                    break;

                case PRODUCT_LIST:
                    updateCount = sqLiteDatabase.update(ProductContract.Product.TABLE_NAME, values, selection, selectionArgs);
                    break;

                case PRODUCT_ID:
                    String idStr11 = uri.getLastPathSegment();
                    String where11 = ProductContract.Product.PRODUCT_ID + " = " + idStr11;
                    if (!TextUtils.isEmpty(selection)) {
                        where11 += " AND " + selection;
                    }
                    updateCount = sqLiteDatabase.update(ProductContract.Product.TABLE_NAME, values, where11, selectionArgs);
                    break;

                case PURCHASE_LIST:
                    updateCount = sqLiteDatabase.update(PurchaseContract.Purchase.TABLE_NAME, values, selection, selectionArgs);
                    break;

                case PURCHASE_ID:
                    String idStr111 = uri.getLastPathSegment();
                    String where111 = PurchaseContract.Purchase.PRODUCT_ID + " = " + idStr111;
                    if (!TextUtils.isEmpty(selection)) {
                        where111 += " AND " + selection;
                    }
                    updateCount = sqLiteDatabase.update(PurchaseContract.Purchase.TABLE_NAME, values, where111, selectionArgs);
                    break;

                default:
                    updateCount = 0;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeDataBase();
        }
        return updateCount;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        dbHelper.openDataBase();
        int deleteCount = 0;

        try {
            switch (URI_MATCHER.match(uri)) {

                case CUSTOMER_LIST:
                    deleteCount = sqLiteDatabase.delete(CustomerContract.Customer.TABLE_NAME, selection, selectionArgs);
                    break;

                case CUSTOMER_ID:
                    String idStr = uri.getLastPathSegment();
                    String where = CustomerContract.Customer.USER_ID + " = " + idStr;
                    if (!TextUtils.isEmpty(selection)) {
                        where += " AND " + selection;
                    }
                    deleteCount = sqLiteDatabase.delete(CustomerContract.Customer.TABLE_NAME, where, selectionArgs);
                    break;

                case UNIT_LIST:
                    deleteCount = sqLiteDatabase.delete(UnitContract.Unit.TABLE_NAME, selection, selectionArgs);
                    break;

                case UNIT_ID:
                    String idStr1 = uri.getLastPathSegment();
                    String where1 = UnitContract.Unit.UNIT_ID + " = " + idStr1;
                    if (!TextUtils.isEmpty(selection)) {
                        where1 += " AND " + selection;
                    }
                    deleteCount = sqLiteDatabase.delete(UnitContract.Unit.TABLE_NAME, where1, selectionArgs);
                    break;

                case PRODUCT_LIST:
                    deleteCount = sqLiteDatabase.delete(ProductContract.Product.TABLE_NAME, selection, selectionArgs);
                    break;

                case PRODUCT_ID:
                    String idStr11 = uri.getLastPathSegment();
                    String where11 = ProductContract.Product.PRODUCT_ID + " = " + idStr11;
                    if (!TextUtils.isEmpty(selection)) {
                        where11 += " AND " + selection;
                    }
                    deleteCount = sqLiteDatabase.delete(ProductContract.Product.TABLE_NAME, where11, selectionArgs);
                    break;

                case PURCHASE_LIST:
                    deleteCount = sqLiteDatabase.delete(PurchaseContract.Purchase.TABLE_NAME, selection, selectionArgs);
                    break;

                case PURCHASE_ID:
                    String idStr111 = uri.getLastPathSegment();
                    String where111 = PurchaseContract.Purchase.PRODUCT_ID + " = " + idStr111;
                    if (!TextUtils.isEmpty(selection)) {
                        where111 += " AND " + selection;
                    }
                    deleteCount = sqLiteDatabase.delete(PurchaseContract.Purchase.TABLE_NAME, where111, selectionArgs);
                    break;

                default:
                    deleteCount = 0;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeDataBase();
        }
        return deleteCount;
    }

    public static class DbHelper extends SQLiteOpenHelper {

        static final String DB_NAME = "DigitalMandi.db";
        static int count = 0;

        public DbHelper(Context context) {
            super(context, DB_NAME, null, 0x1);
        }

        private void dropTableIfExist(String tableName, SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("Drop Table " + tableName);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CustomerContract.Customer.CREATE_TABLE);
            sqLiteDatabase.execSQL(UnitContract.Unit.CREATE_TABLE);
            sqLiteDatabase.execSQL(ProductContract.Product.CREATE_TABLE);
            sqLiteDatabase.execSQL(PurchaseContract.Purchase.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            dropTableIfExist(CustomerContract.Customer.TABLE_NAME, sqLiteDatabase);
            dropTableIfExist(UnitContract.Unit.TABLE_NAME, sqLiteDatabase);
            dropTableIfExist(ProductContract.Product.TABLE_NAME, sqLiteDatabase);
            dropTableIfExist(PurchaseContract.Purchase.TABLE_NAME, sqLiteDatabase);
            onCreate(sqLiteDatabase);
        }

        public synchronized void openDataBase() {
            count++;
            if(sqLiteDatabase == null)
                sqLiteDatabase = this.getWritableDatabase();
        }

        public synchronized void closeDataBase() {
            count--;
            if (count == 0 && sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
                sqLiteDatabase = null;
            }
        }
    }
}
