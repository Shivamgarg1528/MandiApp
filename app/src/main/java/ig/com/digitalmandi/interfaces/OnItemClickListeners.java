package ig.com.digitalmandi.interfaces;

import android.view.View;

public interface OnItemClickListeners<T> {

    void onItemDelete(View view, T object);

    void onItemEdit(View view, T object);

    void onItemStatusChange(View view, T object);

}
