package ig.com.digitalmandi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
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
import ig.com.digitalmandi.base_package.AbstractResponse;
import ig.com.digitalmandi.base_package.BaseFragment;
import ig.com.digitalmandi.interfaces.ApiCallback;
import ig.com.digitalmandi.interfaces.EventListener;
import ig.com.digitalmandi.utils.AppConstant;
import ig.com.digitalmandi.utils.Utils;

public abstract class ListBaseFragment<T> extends BaseFragment implements EventListener<T>, SearchView.OnQueryTextListener, ApiCallback {

    protected List<T> mDataList = new ArrayList<>(0);
    protected List<T> mBackUpList = new ArrayList<>(0);
    private TextView mTextViewEmpty;
    private RecyclerView.Adapter mAdapter;

    protected abstract AbstractResponse getResponse();

    protected abstract RecyclerView.Adapter getAdapter();

    protected abstract int getEmptyTextStringId();

    protected abstract void fetchData();

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

        mTextViewEmpty = (TextView) view.findViewById(R.id.emptyTextView);
        mTextViewEmpty.setText(getEmptyTextStringId());

        // create adapter and set on recycler view
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mAdapter = getAdapter());
        recyclerView.setHasFixedSize(true);

        onApiResponse();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.supplier_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.supplier_menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.supplier_menu_add:
                Intent intent = getRequestedIntent();
                Utils.onActivityStartForResultInFragment(this, false, null, intent, null, AppConstant.REQUEST_CODE_EDIT);
                return true;

            case R.id.supplier_menu_refresh:
                fetchData();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AppConstant.REQUEST_CODE_EDIT:
                    fetchData();
                    break;
            }
        }
    }

    @Override
    public void onApiResponse() {

        AbstractResponse preferenceResponse = getResponse();

        mDataList.clear();
        mDataList.addAll(preferenceResponse.getResult());

        mBackUpList.clear();
        mBackUpList.addAll(getResponse().getResult());

        notifyAdapterAndView();
    }

    private void sortDataList(int pComparatorType) {
        Collections.sort(mDataList, getComparator(pComparatorType));
        notifyAdapterAndView();
    }

    protected void notifyAdapterAndView() {
        mTextViewEmpty.setVisibility(mDataList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        mAdapter.notifyDataSetChanged();
    }
}
