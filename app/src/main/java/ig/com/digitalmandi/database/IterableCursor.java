package ig.com.digitalmandi.database;

import android.database.Cursor;

import java.util.Iterator;

/**
 * Created by shivam.garg on 18-10-2016.
 */

public class IterableCursor implements Iterable<Cursor>, Iterator<Cursor> {
    Cursor cursor;
    int toVisit;
    public IterableCursor(Cursor cursor) {
        this.cursor = cursor;
        toVisit = cursor.getCount();
    }
    public Iterator<Cursor> iterator() {
        cursor.moveToPosition(-1);
        return this;
    }
    public boolean hasNext() {
        return toVisit>0;
    }
    public Cursor next() {
        //  if (!hasNext()) {
        //      throw new NoSuchElementException();
        //  }
        cursor.moveToNext();
        toVisit--;
        return cursor;
    }
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
