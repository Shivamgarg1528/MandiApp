package ig.com.digitalmandi.interfaces;

import android.view.View;

/**
 * Created by Shivam.Garg on 27-10-2016.
 */

public interface OrderCallBack<T> {

    public void onPayment(T object, View view);
    public void onDelete (T object, View view);
    public void onDetail (T object, View view);
    public void onPrint(T object ,View view);
}
