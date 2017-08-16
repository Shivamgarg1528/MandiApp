package ig.com.digitalmandi.fragment.supplier;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.supplier.SupplierPurchaseActivity;
import ig.com.digitalmandi.activity.supplier.SupplierPurchasePaymentActivity;
import ig.com.digitalmandi.activity.supplier.SupplierPurchaseSoldActivity;
import ig.com.digitalmandi.adapter.supplier.SupplierPurchaseAdapter;
import ig.com.digitalmandi.base_package.BaseFragment;
import ig.com.digitalmandi.base_package.LoadMoreClass;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderDeleteReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchaseListReq;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierPurchaseListRes;
import ig.com.digitalmandi.database.PurchaseContract;
import ig.com.digitalmandi.dialogs.DatePickerClass;
import ig.com.digitalmandi.dialogs.MyAlertDialog;
import ig.com.digitalmandi.interfaces.OnAlertDialogCallBack;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.MyPrefrences;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by shiva on 10/22/2016.
 */

public class PurchaseFragment extends BaseFragment implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor>, SupplierPurchaseAdapter.PurchaseCallBack, DatePickerClass.OnDateSelected {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerViewCustomer;
    @BindView(R.id.emptyTextView)
    TextView emptyView;
    public static final int PURCHASE_REQUEST_CODE = 1001;
    @BindView(R.id.mButtonStartDate)
    AppCompatButton mButtonStartDate;
    @BindView(R.id.mButtonEndDate)
    AppCompatButton mButtonEndDate;
    @BindView(R.id.mButtonResetDate)
    AppCompatButton mButtonResetDate;
    private SupplierPurchaseAdapter mAdapter;
    private Date startDate, endDate;
    private int pageCount = 1;
    private LoadMoreClass loadMoreClass;
    private SearchView searchView;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case PURCHASE_REQUEST_CODE:
                    callAfterReset();
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_supplier_product_purchase, null);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    private void stopLoading() {
        enableTouchEvent();
    }

    private void fetchDataFromServer(boolean show, final boolean refresh) {
        disableTouchEvent();
        mHostActivity.onShowOrHideBar(true);
        SupplierPurchaseListReq purchaseListReqModel = new SupplierPurchaseListReq();

        if (endDate != null)
            purchaseListReqModel.setEndDate(Utils.onConvertDateToString(endDate.getTime(), ConstantValues.API_DATE_FORMAT));
        else
            purchaseListReqModel.setEndDate("");

        if (startDate != null)
            purchaseListReqModel.setStartDate(Utils.onConvertDateToString(startDate.getTime(), ConstantValues.API_DATE_FORMAT));
        else
            purchaseListReqModel.setStartDate("");

        purchaseListReqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mHostActivity));
        purchaseListReqModel.setPage(String.valueOf(pageCount));
        purchaseListReqModel.setFlag(ConstantValues.PURCHASE_LIST_PAGING);

        mHostActivity.apiEnqueueObject = RetrofitWebService.getInstance().getInterface().purchaseList(purchaseListReqModel);
        mHostActivity.apiEnqueueObject.enqueue(new RetrofitCallBack<SupplierPurchaseListRes>(mHostActivity, false) {

            @Override
            public void yesCall(SupplierPurchaseListRes response, ParentActivity weakRef) {

                stopLoading();
                if (VerifyResponse.isResponseOk(response, false)) {

                    if (response.getResult().size() == 0)
                        Toast.makeText(weakRef, "No Purchased Item Found", Toast.LENGTH_SHORT).show();

                    if (refresh) {
                        mRecyclerViewCustomer.scrollToPosition(0);
                        dataList.clear();
                        backUpList.clear();
                    }

                    dataList.addAll(response.getResult());
                    backUpList.addAll(response.getResult());
                }
                mAdapter.notifyData(emptyView);

            }

            @Override
            public void noCall(Throwable error) {
                stopLoading();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyView.setText("No Purchase Added Yet\n Please Add Purchased Items");
        mRecyclerViewCustomer.setLayoutManager(new GridLayoutManager(mHostActivity, 1));
        mRecyclerViewCustomer.setHasFixedSize(true);
        mAdapter = new SupplierPurchaseAdapter(dataList, mHostActivity, this);
        mRecyclerViewCustomer.setAdapter(mAdapter);

        loadMoreClass = new LoadMoreClass((GridLayoutManager) mRecyclerViewCustomer.getLayoutManager()) {

            @Override
            public void onLoadMore() {
                super.onLoadMore();
                ++pageCount;
                fetchDataFromServer(false, false);
            }
        };
        mRecyclerViewCustomer.addOnScrollListener(loadMoreClass);
        fetchDataFromServer(false, true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.supplier_purchase_menu, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.supplier_purchase_menu_search));
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.supplier_purchase_menu_add:
                Utils.onActivityStartForResultInFragment(this, false, new int[]{}, null, SupplierPurchaseActivity.class, PURCHASE_REQUEST_CODE);
                return true;

            case R.id.supplier_purchase_menu_refresh:
                callAfterReset();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
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
            SupplierPurchaseListRes.ResultBean model = (SupplierPurchaseListRes.ResultBean) backUpList.get(index);
            if (model.getPurchaseDate().contains(newText) || model.getNameOfPerson().toLowerCase().contains(newText.toLowerCase()) || model.getProductName().toLowerCase().contains(newText.toLowerCase())) {
                dataList.add(model);
            }
        }
        mAdapter.notifyData(emptyView);
        return true;

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mHostActivity, PurchaseContract.Purchase.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("PurchaseFragment", "data.getCount():" + data.getCount());
        dataList.addAll(new PurchaseContract(mHostActivity).getListOfObject(data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataList.clear();
    }

    @Override
    public void onPayment(SupplierPurchaseListRes.ResultBean object, View view) {
        Utils.onActivityStart(mHostActivity, false, new int[]{}, convertIntoIntent(object), null);
    }

    @Override
    public void onDelete(SupplierPurchaseListRes.ResultBean object, View view) {

        final SupplierOrderDeleteReq supplierOrderDeleteReq = new SupplierOrderDeleteReq();
        supplierOrderDeleteReq.setFlag(ConstantValues.PURCHASE_PAYMENT);
        supplierOrderDeleteReq.setId(object.getPurchaseId());

        MyAlertDialog.onShowAlertDialog(mHostActivity, "Continue,To Delete This Purchase!", "Continue", "Leave", true, new OnAlertDialogCallBack() {

            @Override
            public void onNegative(DialogInterface dialogInterface, int i) {

            }

            @Override
            public void onPositive(DialogInterface dialogInterface, int i) {
                mHostActivity.apiEnqueueObject = RetrofitWebService.getInstance().getInterface().deletePurchase(supplierOrderDeleteReq);
                mHostActivity.apiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mHostActivity) {

                    @Override
                    public void yesCall(EmptyResponse response, ParentActivity weakRef) {
                        if (VerifyResponse.isResponseOk(response)) {
                            Toast.makeText(weakRef, "Purchased Deleted Successfully", Toast.LENGTH_SHORT).show();
                            fetchDataFromServer(false, true);
                        } else
                            Toast.makeText(weakRef, "Sorry You Can't Delete This Purchase", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void noCall(Throwable error) {

                    }
                });
            }
        });


    }

    @Override
    public void onSales(SupplierPurchaseListRes.ResultBean object, View view) {
        Utils.onActivityStart(mHostActivity, false, new int[]{}, convertIntoIntentForSale(object), null);
    }

    private Intent convertIntoIntent(SupplierPurchaseListRes.ResultBean object) {
        Intent intent = new Intent(mHostActivity, SupplierPurchasePaymentActivity.class);
        intent.putExtra(SupplierPurchasePaymentActivity.PURCHASE_OBJECT_KEY, object);
        return intent;
    }

    private Intent convertIntoIntentForSale(SupplierPurchaseListRes.ResultBean object) {
        Intent intent = new Intent(mHostActivity, SupplierPurchaseSoldActivity.class);
        intent.putExtra(SupplierPurchaseSoldActivity.PURCHASE_OBJECT_KEY, object);
        return intent;
    }

    private void callAfterReset() {
        searchView.setIconified(true);
        mButtonEndDate.setText("End Date");
        mButtonStartDate.setText("Start Date");
        startDate = null;
        endDate = null;
        pageCount = 1;
        fetchDataFromServer(false, true);
    }

    private void callFilter() {
        if (startDate == null) {
            Toast.makeText(mHostActivity, "Please Select Start Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDate == null) {
            Toast.makeText(mHostActivity, "Please Select End Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startDate.after(endDate)) {
            Toast.makeText(mHostActivity, "Start Date Must Be Greater Or Equals To End Date", Toast.LENGTH_SHORT).show();
            return;
        }

        pageCount = 1;
        fetchDataFromServer(false, true);
    }

    @OnClick({R.id.mButtonStartDate, R.id.mButtonEndDate, R.id.mButtonResetDate})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mButtonStartDate:
                DatePickerClass.showDatePicker(DatePickerClass.START_DATE, this, mHostActivity, ConstantValues.API_DATE_FORMAT);
                break;

            case R.id.mButtonEndDate:
                DatePickerClass.showDatePicker(DatePickerClass.END_DATE, this, mHostActivity, ConstantValues.API_DATE_FORMAT);
                break;

            case R.id.mButtonResetDate:

                MyAlertDialog.onShowAlertDialog(mHostActivity, "Reset Applied Filters", "Reset", "Leave", true, new OnAlertDialogCallBack() {
                    @Override
                    public void onNegative(DialogInterface dialogInterface, int i) {

                    }

                    @Override
                    public void onPositive(DialogInterface dialogInterface, int i) {
                        callAfterReset();
                    }
                });

                break;
        }
    }

    @Override
    public void onDateSelectedCallBack(int id, Date date, String stringResOfDate, long milliSeconds, int numberOfDays) {
        switch (id) {

            case DatePickerClass.START_DATE:
                startDate = date;
                mButtonStartDate.setText(stringResOfDate);
                break;

            case DatePickerClass.END_DATE:
                endDate = date;
                mButtonEndDate.setText(stringResOfDate);
                break;
        }
        callFilter();
    }
}
