package ig.com.digitalmandi.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class LoadMoreClass extends RecyclerView.OnScrollListener {

    private boolean mLoading = true;

    public void onLoadMore() {
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0) //check for scroll down
        {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemsCount = layoutManager.getChildCount();
            int totalItemsCount = layoutManager.getItemCount();
            int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

            if (mLoading && (visibleItemsCount + pastVisibleItems) >= totalItemsCount) {
                mLoading = false;
                onLoadMore();
            }
        }
    }
}

