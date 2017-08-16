package ig.com.digitalmandi.fragment.supplier;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.supplier.SupplierCustomerAddActivity;
import ig.com.digitalmandi.adapter.supplier.SupplierCustomerAdapter;
import ig.com.digitalmandi.base_package.BaseFragment;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierCustomerListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierCustomerListRes;
import ig.com.digitalmandi.database.BaseContract;
import ig.com.digitalmandi.database.CustomerContract;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.MyPrefrences;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by shiva on 10/11/2016.
 */

public class CustomerFragment extends BaseFragment implements SearchView.OnQueryTextListener, BaseContract.OnInsertBulkDataSuccessFully, LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerViewCustomer;
    @BindView(R.id.emptyTextView)
    TextView emptyView;
    public SupplierCustomerAdapter mAdapter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:
                    onFetchDataFromServer(false);
                    break;
            }
        }
    }

    private void sortCustomerListAlphabetically() {
        Comparator<SupplierCustomerListRes.ResultBean> ALPHABETICAL_ORDER1 = new Comparator<SupplierCustomerListRes.ResultBean>() {
            public int compare(SupplierCustomerListRes.ResultBean object1, SupplierCustomerListRes.ResultBean object2) {
                int res = String.CASE_INSENSITIVE_ORDER.compare(object1.getUserName().toString(), object2.getUserName().toString());
                return res;
            }
        };
        Collections.sort(dataList, ALPHABETICAL_ORDER1);
        mAdapter.notifyData(emptyView);
    }

    private void sortCustomerListPhoneNumber() {
        Comparator<SupplierCustomerListRes.ResultBean> PHONE_ORDER1 = new Comparator<SupplierCustomerListRes.ResultBean>() {
            public int compare(SupplierCustomerListRes.ResultBean object1, SupplierCustomerListRes.ResultBean object2) {
                int res = (int) (Long.parseLong(object1.getUserMobileNo()) - Long.parseLong(object2.getUserMobileNo()));
                return res;
            }
        };
        Collections.sort(dataList, PHONE_ORDER1);
        mAdapter.notifyData(emptyView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_supplier_customer, null);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.supplier_customer_menu, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.supplier_customer_menu_search));
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.supplier_customer_menu_sort_alpha:
                sortCustomerListAlphabetically();
                return true;

            case R.id.supplier_customer_menu_sort_id:
                sortCustomerListPhoneNumber();
                return true;

            case R.id.supplier_customer_menu_add:
                Utils.onActivityStartForResultInFragment(this, false, new int[]{}, null, SupplierCustomerAddActivity.class, 100);
                return true;

            case R.id.supplier_customer_menu_refresh:
                onFetchDataFromServer(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new SupplierCustomerAdapter(dataList, mHostActivity);
        mRecyclerViewCustomer.setAdapter(mAdapter);
        mRecyclerViewCustomer.setHasFixedSize(true);
        emptyView.setText("No Customer Found\nPlease Add New Customer");
        getLoaderManager().initLoader(1, null, this);
    }

    private void onFetchDataFromServer(final boolean showDialog) {

        disableTouchEvent();
        SupplierCustomerListReq supplierCustomerReqModel = new SupplierCustomerListReq();
        supplierCustomerReqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mHostActivity));


        mHostActivity.apiEnqueueObject = RetrofitWebService.getInstance().getInterface().supplierCustomerList(supplierCustomerReqModel);
        mHostActivity.apiEnqueueObject.enqueue(new RetrofitCallBack<SupplierCustomerListRes>(mHostActivity, showDialog) {

            @Override
            public void yesCall(SupplierCustomerListRes response, ParentActivity weakRef) {
                enableTouchEvent();
                if (VerifyResponse.isResponseOk(response, true)) {
                    CustomerContract customerContract = new CustomerContract(weakRef);
                    customerContract.insertBulkData(response.getResult(), CustomerContract.Customer.CONTENT_URI, CustomerFragment.this);
                }
            }

            @Override
            public void noCall(Throwable error) {
                enableTouchEvent();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        dataList.clear();

        if (TextUtils.isEmpty(newText)) {
            dataList.addAll(backUpList);
            mAdapter.notifyData(emptyView);
            return true;
        }

        for (int index = 0; index < backUpList.size(); index++) {
            SupplierCustomerListRes.ResultBean mCustomerBean = (SupplierCustomerListRes.ResultBean) backUpList.get(index);

            if (mCustomerBean.getUserFirmName().toLowerCase().contains(newText.toLowerCase()) || mCustomerBean.getUserName().toLowerCase().contains(newText.toLowerCase())) {
                dataList.add(mCustomerBean);
            }
        }
        mAdapter.notifyData(emptyView);
        return true;
    }

    @Override
    public void onInsertBulkDataSuccess() {
        new Handler().post(restartLoaderASAP);
        enableTouchEvent();
    }

    Runnable restartLoaderASAP = new Runnable() {
        @Override
        public void run() {
            getLoaderManager().restartLoader(1, null, CustomerFragment.this);
        }
    };


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(mHostActivity, CustomerContract.Customer.CONTENT_URI, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        dataList.clear();
        backUpList.clear();

        CustomerContract customerContract = new CustomerContract(mHostActivity);
        dataList.addAll(customerContract.getListOfObject(data));

        backUpList.addAll(dataList);
        mAdapter.notifyData(emptyView);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataList.clear();
        backUpList.clear();
        mAdapter.notifyData(emptyView);
    }
}
