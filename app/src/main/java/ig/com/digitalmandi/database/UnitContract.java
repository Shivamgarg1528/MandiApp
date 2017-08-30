package ig.com.digitalmandi.database;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import ig.com.digitalmandi.beans.response.supplier.SellerUnitList;

/**
 * Created by shivam.garg on 18-10-2016.
 */

public class UnitContract<T> extends BaseContract<T> {

    public  static final String PATH = "unit";
    private static final String TYPE = ".unit_items";

    public UnitContract(Context mContext) {
        super(mContext);
    }

    @Override
    public void insertBulkData(final List<T> dataList, final Uri mUri,final OnInsertBulkDataSuccessFully listener) {

        MyAsyncQueryHandler deleteAsync = new MyAsyncQueryHandler(getContext()){

            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                super.onDeleteComplete(token, cookie, result);

                List<SellerUnitList.Unit> unitList = (List<SellerUnitList.Unit>) dataList;
                ArrayList<ContentProviderOperation> batch          = new ArrayList<ContentProviderOperation>(unitList.size());
                for(int index = 0 ;index < unitList.size() ; index++){
                    SellerUnitList.Unit unit = unitList.get(index);
                    ContentProviderOperation.Builder builder       = ContentProviderOperation.newInsert(mUri);
                    builder.withValue(UnitContract.Unit.UNIT_ID, unit.getUnitId());
                    builder.withValue(UnitContract.Unit.UNIT_NAME, unit.getUnitName());
                    builder.withValue(UnitContract.Unit.UNIT_STATUS, unit.getUnitStatus());
                    builder.withValue(UnitContract.Unit.UNIT_KG_VALUE, unit.getKgValue());
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
                SellerUnitList.Unit unit = new SellerUnitList.Unit();
                unit.setUnitId(iterableCursor.getString(iterableCursor.getColumnIndex(UnitContract.Unit.UNIT_ID)));
                unit.setUnitStatus(iterableCursor.getString(iterableCursor.getColumnIndex(UnitContract.Unit.UNIT_STATUS)));
                unit.setUnitName(iterableCursor.getString(iterableCursor.getColumnIndex(UnitContract.Unit.UNIT_NAME)));
                unit.setKgValue(iterableCursor.getString(iterableCursor.getColumnIndex(UnitContract.Unit.UNIT_KG_VALUE)));
                return (T) unit;
            }
            cursor.close();
        }
        else
            return null;
        return null;
    }

    @Override
    public List<T> getListOfObject(Cursor cursor) {
        List<SellerUnitList.Unit> customerList = new ArrayList<>();
        if(cursor != null && cursor.getCount() > 0){
            for (Cursor iterableCursor : new IterableCursor(cursor)) {
                SellerUnitList.Unit unit = new SellerUnitList.Unit();
                unit.setUnitId(iterableCursor.getString(iterableCursor.getColumnIndex(UnitContract.Unit.UNIT_ID)));
                unit.setUnitStatus(iterableCursor.getString(iterableCursor.getColumnIndex(UnitContract.Unit.UNIT_STATUS)));
                unit.setUnitName(iterableCursor.getString(iterableCursor.getColumnIndex(UnitContract.Unit.UNIT_NAME)));
                unit.setKgValue(iterableCursor.getString(iterableCursor.getColumnIndex(UnitContract.Unit.UNIT_KG_VALUE)));
                customerList.add(unit);
            }
        }
        cursor.close();
        return (List<T>) customerList;
    }

    public static final class Unit {


        public static final String TABLE_NAME         = "UnitInfo";
        public static final String UNIT_ID            = "unitId";
        public static final String UNIT_NAME          = "unitName";
        public static final String UNIT_STATUS        = "unitStatus";
        public static final String UNIT_KG_VALUE      = "unitKgValue";

        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI           = Uri.withAppendedPath(AppContentProvider.CONTENT_URI,PATH);
        public static final String CONTENT_TYPE       = ContentResolver.CURSOR_DIR_BASE_TYPE +"/vnd.com.satguru"  + TYPE;
        public static final String CONTENT_ITEM_TYPE  = ContentResolver.CURSOR_ITEM_BASE_TYPE +"/vnd.com.satguru" + TYPE;
        public static final String[] PROJECTION_ALL   = {UNIT_ID, UNIT_NAME, UNIT_STATUS, UNIT_KG_VALUE};
        public static final String SORT_ORDER_DEFAULT = UNIT_ID + " ASC";

        public static final String CREATE_TABLE       =
                "Create Table "
                + TABLE_NAME     + " ( "
                + UNIT_ID        + " TEXT, "
                + UNIT_NAME      + " TEXT, "
                + UNIT_STATUS    + " TEXT, "
                + UNIT_KG_VALUE  + " TEXT "
                +")";
    }
}