package ig.com.digitalmandi.interfaces;

import android.view.View;

public interface OrderCallBack<T> {
    void onPayment(T object, View view);

    void onDelete(T object, View view);

    void onDetail(T object, View view);

    void onPrint(T object, View view);
}
