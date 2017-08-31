package ig.com.digitalmandi.fragment.supplier;

import android.app.Activity;
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
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.activity.seller.SupplierPurchaseActivity;
import ig.com.digitalmandi.activity.seller.SupplierPurchasePaymentActivity;
import ig.com.digitalmandi.activity.seller.SupplierPurchaseSoldActivity;
import ig.com.digitalmandi.adapter.supplier.SupplierPurchaseAdapter;
import ig.com.digitalmandi.bean.request.seller.SupplierItemDeleteRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierPurchaseListReq;
import ig.com.digitalmandi.bean.response.seller.SupplierPurchaseListRes;
import ig.com.digitalmandi.database.PurchaseContract;
import ig.com.digitalmandi.dialog.DatePickerClass;
import ig.com.digitalmandi.fragment.BaseFragment;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.LoadMoreClass;
import ig.com.digitalmandi.util.MyPrefrences;
import ig.com.digitalmandi.util.Utils;

/**
 * Created by shiva on 10/22/2016.
 */

public class PurchaseFragment extends BaseFragment implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor>, SupplierPurchaseAdapter.PurchaseCallBack, DatePickerClass.OnDateSelected {

    public static final int PURCHASE_REQUEST_CODE = 1001;
    @BindView(R.id.layout_common_list_recycler_view)
    RecyclerView mRecyclerViewCustomer;
    @BindView(R.id.layout_common_list_tv_empty_text_view)
    TextView emptyView;
    @BindView(R.id.activity_supplier_customer_more_info_btn_start_date)
    AppCompatButton mButtonStartDate;
    @BindView(R.id.activity_supplier_customer_more_info_btn_end_date)
    AppCompatButton mButtonEndDate;
    @BindView(R.id.activity_supplier_customer_more_info_btn_reset_date)
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
        mBaseActivity.showOrHideProgressBar(true);
        SupplierPurchaseListReq purchaseListReqModel = new SupplierPurchaseListReq();

        if (endDate != null)
            purchaseListReqModel.setEndDate(Utils.getDateString(endDate.getTime(), AppConstant.API_DATE_FORMAT));
        else
            purchaseListReqModel.setEndDate("");

        if (startDate != null)
            purchaseListReqModel.setStartDate(Utils.getDateString(startDate.getTime(), AppConstant.API_DATE_FORMAT));
        else
            purchaseListReqModel.setStartDate("");

        purchaseListReqModel.setSellerId(MyPrefrences.getStringPrefrences(AppConstant.USER_SELLER_ID, mBaseActivity));
        purchaseListReqModel.setPage(String.valueOf(pageCount));
        purchaseListReqModel.setFlag(AppConstant.PURCHASE_LIST_PAGING);

        mBaseActivity.mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().purchaseList(purchaseListReqModel);
        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<SupplierPurchaseListRes>(mBaseActivity, false) {

            @Override
            public void onSuccess(SupplierPurchaseListRes pResponse, BaseActivity pBaseActivity) {

                stopLoading();
                if (ResponseVerification.isResponseOk(pResponse, false)) {

                    if (pResponse.getResult().size() == 0)
                        Toast.makeText(pBaseActivity, "No Purchased Item Found", Toast.LENGTH_SHORT).show();

                    if (refresh) {
                        mRecyclerViewCustomer.scrollToPosition(0);
                        mDataList.clear();
                        mBackUpList.clear();
                    }

                    mDataList.addAll(pResponse.getResult());
                    mBackUpList.addAll(pResponse.getResult());
                }
                mAdapter.notifyData(emptyView);

            }

            @Override
            public void onFailure(String pErrorMsg) {
                stopLoading();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyView.setText("No Purchase Added Yet\n Please Add Purchased Items");
        mRecyclerViewCustomer.setLayoutManager(new GridLayoutManager(mBaseActivity, 1));
        mRecyclerViewCustomer.setHasFixedSize(true);
        mAdapter = new SupplierPurchaseAdapter(mDataList, mBaseActivity, this);
        mRecyclerViewCustomer.setAdapter(mAdapter);

        loadMoreClass = new LoadMoreClass() {

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
        mDataList.clear();

        if (TextUtils.isEmpty(newText)) {
            mDataList.addAll(mBackUpList);
            mAdapter.notifyData(emptyView);
            return true;
        }

        for (int index = 0; index < mBackUpList.size(); index++) {
            SupplierPurchaseListRes.ResultBean model = (SupplierPurchaseListRes.ResultBean) mBackUpList.get(index);
            if (model.getPurchaseDate().contains(newText) || model.getNameOfPerson().toLowerCase().contains(newText.toLowerCase()) || model.getProductName().toLowerCase().contains(newText.toLowerCase())) {
                mDataList.add(model);
            }
        }
        mAdapter.notifyData(emptyView);
        return true;

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mBaseActivity, PurchaseContract.Purchase.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("PurchaseFragment", "data.getCount():" + data.getCount());
        mDataList.addAll(new PurchaseContract(mBaseActivity).getListOfObject(data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDataList.clear();
    }

    @Override
    public void onPayment(SupplierPurchaseListRes.ResultBean object, View view) {
        Utils.onActivityStart(mBaseActivity, false, new int[]{}, convertIntoIntent(object), null);
    }

    @Override
    public void onDelete(SupplierPurchaseListRes.ResultBean object, View view) {

        final SupplierItemDeleteRequest supplierItemDeleteRequest = new SupplierItemDeleteRequest();
        supplierItemDeleteRequest.setFlag(AppConstant.DELETE_OR_PAYMENT_PURCHASE);
        supplierItemDeleteRequest.setId(object.getPurchaseId());

       /* MyAlertDialog.onShowAlertDialog(mBaseActivity, "Continue,To Delete This Purchase!", "Continue", "Leave", true,  new OnAlertDialogCallBack() {

            @Override
            public void onNegative(DialogInterface dialogInterface, int i) {

            }

            @Override
            public void onPositive(DialogInterface dialogInterface, int i) {
                mBaseActivity.mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().deletePurchase(supplierItemDeleteRequest);
                mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mBaseActivity) {

                    @Override
                    public void onSuccess(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                        if (ResponseVerification.isResponseOk(pResponse)) {
                            Toast.makeText(pBaseActivity, "Purchased Deleted Successfully", Toast.LENGTH_SHORT).show();
                            fetchDataFromServer(false, true);
                        } else
                            Toast.makeText(pBaseActivity, "Sorry You Can't Delete This Purchase", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String pErrorMsg) {

                    }
                });
            }
        });*/


    }

    @Override
    public void onSales(SupplierPurchaseListRes.ResultBean object, View view) {
        Utils.onActivityStart(mBaseActivity, false, new int[]{}, convertIntoIntentForSale(object), null);
    }

    private Intent convertIntoIntent(SupplierPurchaseListRes.ResultBean object) {
        Intent intent = new Intent(mBaseActivity, SupplierPurchasePaymentActivity.class);
        intent.putExtra(SupplierPurchasePaymentActivity.PURCHASE_OBJECT_KEY, object);
        return intent;
    }

    private Intent convertIntoIntentForSale(SupplierPurchaseListRes.ResultBean object) {
        Intent intent = new Intent(mBaseActivity, SupplierPurchaseSoldActivity.class);
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
            Toast.makeText(mBaseActivity, "Please Select Start Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDate == null) {
            Toast.makeText(mBaseActivity, "Please Select End Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startDate.after(endDate)) {
            Toast.makeText(mBaseActivity, "Start Date Must Be Greater Or Equals To End Date", Toast.LENGTH_SHORT).show();
            return;
        }

        pageCount = 1;
        fetchDataFromServer(false, true);
    }

    @OnClick({R.id.activity_supplier_customer_more_info_btn_start_date, R.id.activity_supplier_customer_more_info_btn_end_date, R.id.activity_supplier_customer_more_info_btn_reset_date})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.activity_supplier_customer_more_info_btn_start_date:
                DatePickerClass.showDatePicker(mBaseActivity, DatePickerClass.START_DATE, this, AppConstant.API_DATE_FORMAT);
                break;

            case R.id.activity_supplier_customer_more_info_btn_end_date:
                DatePickerClass.showDatePicker(mBaseActivity, DatePickerClass.END_DATE, this, AppConstant.API_DATE_FORMAT);
                break;

            case R.id.activity_supplier_customer_more_info_btn_reset_date:

                /*MyAlertDialog.onShowAlertDialog(mBaseActivity, "Reset Applied Filters", "Reset", "Leave", new OnAlertDialogCallBack() {
                    @Override
                    public void onNegative(DialogInterface dialogInterface, int i) {

                    }

                    @Override
                    public void onPositive(DialogInterface dialogInterface, int i) {
                        callAfterReset();
                    }
                });*/

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
