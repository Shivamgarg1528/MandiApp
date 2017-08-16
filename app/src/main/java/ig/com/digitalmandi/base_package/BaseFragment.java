package ig.com.digitalmandi.base_package;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiva on 10/11/2016.
 */

public abstract class BaseFragment<T> extends Fragment {

    protected ParentActivity mHostActivity;
    protected View mRootView;

    protected List<T> dataList     = new ArrayList<>();
    protected List<T> backUpList   = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof AppCompatActivity)
            mHostActivity = (ParentActivity) context;

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    protected void disableTouchEvent(){
        mHostActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void enableTouchEvent(){
        mHostActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}
