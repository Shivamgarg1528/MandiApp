package ig.com.digitalmandi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.BaseActivity;

public abstract class ListBaseActivity<T> extends BaseActivity {

    protected List<T> mDataList = new ArrayList<>(0);
    protected List<T> mBackUpList = new ArrayList<>(0);
    private TextView mTextViewEmpty;
    private RecyclerView.Adapter mAdapter;

    protected abstract RecyclerView.Adapter getAdapter();

    protected abstract int getLayoutId();

    protected abstract int getEmptyTextStringId();

    protected abstract void fetchData();

    protected abstract void getIntentData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        getIntentData();

        mTextViewEmpty = (AppCompatTextView) findViewById(R.id.layout_common_list_tv_empty_text_view);
        mTextViewEmpty.setText(getEmptyTextStringId());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.layout_common_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter = getAdapter());
        fetchData();
    }


    protected void notifyAdapterAndView() {
        mTextViewEmpty.setVisibility(mDataList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        mAdapter.notifyDataSetChanged();
    }
}
