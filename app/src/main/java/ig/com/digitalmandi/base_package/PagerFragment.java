package ig.com.digitalmandi.base_package;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shivam.garg on 17-10-2016.
 */

public abstract class PagerFragment extends Fragment {

    protected static final String ARG_PAGE = "ARG_PAGE";
    protected int mPage;
    protected AppCompatActivity mHostActivity;
    public abstract <T> T newInstance(int page);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AppCompatActivity)
            mHostActivity = (AppCompatActivity) context;
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }
}
