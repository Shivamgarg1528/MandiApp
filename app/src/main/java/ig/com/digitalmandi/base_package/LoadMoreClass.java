package ig.com.digitalmandi.base_package;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by shivam.garg on 24-10-2016.
 */

public class LoadMoreClass extends RecyclerView.OnScrollListener {

    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private GridLayoutManager mLayoutManager;

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public LoadMoreClass(GridLayoutManager linearLayout) {
        this.mLayoutManager = linearLayout;
    }

    public void onLoadMore() {} ;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0) //check for scroll down
        {
            visibleItemCount = mLayoutManager.getChildCount();
            totalItemCount = mLayoutManager.getItemCount();
            pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    loading = false;
                    onLoadMore();
                }
            }
        }
    }
}

