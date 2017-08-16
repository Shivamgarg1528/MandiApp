package ig.com.digitalmandi.interfaces;

import android.view.View;

/**
 * Created by Shivam.Garg on 09-11-2016.
 */

public interface OnItemClickListeners {

    public void onItemDelete(View view,Object object);
    public void onItemEdit(View view,Object object);
    public void onItemChangeStatus(View view,Object object);

}
