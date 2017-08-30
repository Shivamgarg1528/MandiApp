package ig.com.digitalmandi.base_package;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class LoadMoreClass extends RecyclerView.OnScrollListener {

    private boolean mLoading = true;
    private GridLayoutManager mLayoutManager;

    protected LoadMoreClass(GridLayoutManager linearLayout) {
        this.mLayoutManager = linearLayout;
    }

    public void onLoadMore() {
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0) //check for scroll down
        {
            int visibleItemsCount = mLayoutManager.getChildCount();
            int totalItemsCount = mLayoutManager.getItemCount();
            int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

            if (mLoading) {
                if ((visibleItemsCount + pastVisibleItems) >= totalItemsCount) {
                    mLoading = false;
                    onLoadMore();
                }
            }
        }
    }
}

