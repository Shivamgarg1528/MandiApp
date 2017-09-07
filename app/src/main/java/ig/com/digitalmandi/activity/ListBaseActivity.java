package ig.com.digitalmandi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ig.com.digitalmandi.R;

public abstract class ListBaseActivity<T> extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    protected final List<T> mDataList = new ArrayList<>(0);
    protected final List<T> mBackUpList = new ArrayList<>(0);
    protected RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private TextView mTextViewEmpty;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    protected abstract RecyclerView.Adapter getAdapter();

    protected abstract int getLayoutId();

    protected abstract int getEmptyTextStringId();

    protected abstract void fetchData(boolean pRefresh);

    protected abstract void getIntentData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        getIntentData();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_common_list_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mTextViewEmpty = (AppCompatTextView) findViewById(R.id.layout_common_list_tv_empty_text_view);
        mTextViewEmpty.setText(getEmptyTextStringId());

        mRecyclerView = (RecyclerView) findViewById(R.id.layout_common_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter = getAdapter());
        fetchData(true);
    }

    protected void notifyAdapterAndView() {
        mTextViewEmpty.setVisibility(mDataList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        fetchData(true);
    }
}
