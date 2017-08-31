package ig.com.digitalmandi.callback;

import android.view.View;

public interface OrderCallback<T> {
    void onPayment(T object, View view);

    void onDelete(T object, View view);

    void onDetail(T object, View view);

    void onPrint(T object, View view);
}
