package ig.com.digitalmandi.database;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivam.garg on 18-10-2016.
 */

public abstract class BaseContract<T> {

    protected ContentResolver mContentResolver;
    protected AppContentProvider.DbHelper mDbHelper;
    private   Context mContext;
    public abstract void insertBulkData(List<T> dataList,Uri mUri, final OnInsertBulkDataSuccessFully listener);
    public abstract T getSingleObject(Cursor cursor);
    public abstract List<T> getListOfObject(Cursor cursor);

    public interface OnInsertBulkDataSuccessFully
    {
        public void onInsertBulkDataSuccess();
    }

    public void applyBatchOperation(final ArrayList<ContentProviderOperation> batch, final OnInsertBulkDataSuccessFully listener){

        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mContentResolver.applyBatch(AppContentProvider.AUTHORITY, batch);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                listener.onInsertBulkDataSuccess();
            }
        }.execute();

    }

    public BaseContract(Context mContext) {
        this.mContext    = mContext;
        mContentResolver = mContext.getContentResolver();
        this.mDbHelper   = new AppContentProvider.DbHelper(mContext);
    }

    public Context getContext() {
        return mContext;
    }
}
