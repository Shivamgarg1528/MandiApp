package ig.com.digitalmandi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.bean.AbstractResponse;
import ig.com.digitalmandi.callback.ApiCallback;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public abstract class ListBaseFragment<T> extends BaseFragment implements EventCallback<T>, SearchView.OnQueryTextListener, ApiCallback, SwipeRefreshLayout.OnRefreshListener {

    protected final List<T> mDataList = new ArrayList<>(0);
    protected final List<T> mBackUpList = new ArrayList<>(0);
    protected RecyclerView mRecyclerView;
    private TextView mTextViewEmpty;
    private RecyclerView.Adapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    protected abstract AbstractResponse getResponse();

    protected abstract RecyclerView.Adapter getAdapter();

    protected abstract int getEmptyTextStringId();

    protected abstract void fetchData(boolean pRefresh);

    protected abstract Intent getRequestedIntent();

    protected abstract Comparator getComparator(int pComparatorType);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_fragment_supplier_product_unit_purchase, container, false);
        return mRootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_common_list_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        mTextViewEmpty = (TextView) view.findViewById(R.id.layout_common_list_tv_empty_text_view);
        mTextViewEmpty.setText(getEmptyTextStringId());

        // create adapter and set on recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.layout_common_list_recycler_view);
        mRecyclerView.setAdapter(mAdapter = getAdapter());
        mRecyclerView.setHasFixedSize(true);

        onApiResponse();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.supplier_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.supplier_menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.supplier_menu_add:
                Intent intent = getRequestedIntent();
                Helper.onActivityStartForResultInFragment(this, false, null, intent, null, AppConstant.REQUEST_CODE_EDIT);
                return true;

            case R.id.supplier_menu_sort: {
                sortDataList(AppConstant.COMPARATOR_ALPHA);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AppConstant.REQUEST_CODE_EDIT:
                    fetchData(true);
                    break;
            }
        }
    }

    @Override
    public void onApiResponse() {
        AbstractResponse preferenceResponse = getResponse();
        if (preferenceResponse != null) {
            mDataList.clear();
            mDataList.addAll(preferenceResponse.getResult());
            mBackUpList.clear();
            mBackUpList.addAll(preferenceResponse.getResult());
            notifyAdapterAndView();
        }
    }

    private void sortDataList(int pComparatorType) {
        Comparator comparator = getComparator(pComparatorType);
        if (comparator != null) {
            Collections.sort(mDataList, comparator);
            notifyAdapterAndView();
        }
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

    @Override
    public void onEvent(int pOperationType, T pObject) {

    }
}
