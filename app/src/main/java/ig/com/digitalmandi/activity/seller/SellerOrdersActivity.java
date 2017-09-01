package ig.com.digitalmandi.activity.seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.activity.ListBaseActivity;
import ig.com.digitalmandi.adapter.supplier.SellerOrderAdapter;
import ig.com.digitalmandi.bean.request.seller.ItemDeleteRequest;
import ig.com.digitalmandi.bean.request.seller.SellerOrdersRequest;
import ig.com.digitalmandi.bean.response.EmptyResponse;
import ig.com.digitalmandi.bean.response.seller.SellerOrderResponse;
import ig.com.digitalmandi.bean.response.seller.SupplierOrderListResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.dialog.DatePickerClass;
import ig.com.digitalmandi.dialog.MyAlertDialog;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.LoadMoreClass;
import ig.com.digitalmandi.util.Utils;

public class SellerOrdersActivity extends ListBaseActivity<SellerOrderResponse.Order> implements SearchView.OnQueryTextListener, DatePickerClass.OnDateSelected, View.OnClickListener, EventCallback, SellerOrderAdapter.PurchaseCallBack {

    private int mPageCount = 1;
    private boolean mLoadMore = false;

    private Date mDateStart;
    private Date mDateEnd;

    // TODO changes
    private SupplierOrderListResponse.Order mOrderObj;

    private SearchView mSearchView;
    private AppCompatButton mBtnStartDate;
    private AppCompatButton mBtnEndDate;

    private LoadMoreClass mLoadMoreClass = new LoadMoreClass() {

        @Override
        public void onLoadMore() {
            if (mLoadMore) {
                return;
            }
            ++mPageCount;
            fetchData(false);
        }
    };

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new SellerOrderAdapter(mDataList, this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_orders;
    }

    @Override
    protected int getEmptyTextStringId() {
        return R.string.string_no_purchase_added_yet_please_add_new_purchase;
    }

    @Override
    protected void fetchData(final boolean pRefresh) {
        if (pRefresh) {
            mPageCount = 1;
            mLoadMore = false;
        }
        SellerOrdersRequest sellerOrdersRequest = new SellerOrdersRequest();
        if (mDateEnd != null)
            sellerOrdersRequest.setEndDate(Utils.getDateString(mDateEnd.getTime(), AppConstant.API_DATE_FORMAT));
        else
            sellerOrdersRequest.setEndDate("");

        if (mDateStart != null)
            sellerOrdersRequest.setStartDate(Utils.getDateString(mDateStart.getTime(), AppConstant.API_DATE_FORMAT));
        else
            sellerOrdersRequest.setStartDate("");

        sellerOrdersRequest.setSellerId(mLoginUser.getSellerId());
        sellerOrdersRequest.setPage(String.valueOf(mPageCount));
        sellerOrdersRequest.setFlag(AppConstant.PURCHASE_LIST_PAGING);

        mBaseActivity.mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().getSellerOrders(sellerOrdersRequest);
        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<SellerOrderResponse>(mBaseActivity, false) {

            @Override
            public void onResponse(SellerOrderResponse pResponse, BaseActivity pBaseActivity) {

                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    if (pResponse.getResult().size() == 0) {
                        Toast.makeText(pBaseActivity, "No Purchased Item Found", Toast.LENGTH_SHORT).show();
                    }
                    if (pRefresh) {
                        mDataList.clear();
                        mBackUpList.clear();
                    }
                    mDataList.addAll(pResponse.getResult());
                    mBackUpList.addAll(pResponse.getResult());
                }
                notifyAdapterAndView();
            }
        });
    }

    @Override
    protected void getIntentData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(true);
        setTitle(getString(R.string.string_purchase));

        mBtnStartDate = (AppCompatButton) findViewById(R.id.activity_orders_btn_start_date);
        mBtnStartDate.setOnClickListener(this);

        mBtnEndDate = (AppCompatButton) findViewById(R.id.activity_orders_btn_end_date);
        mBtnEndDate.setOnClickListener(this);

        findViewById(R.id.activity_orders_btn_reset_date).setOnClickListener(this);

        mRecyclerView.addOnScrollListener(mLoadMoreClass);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.orders_menu, menu);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.orders_menu_search));
        mSearchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.orders_menu_add:
                Intent intent = new Intent(mBaseActivity, AddItemInOrderActivity.class);
                /*Utils.onActivityStartForResult(this, false, null, intent, null, AppConstant.REQUEST_CODE_PLACE_NEW_ORDER);
                */
                return true;

            case R.id.orders_menu_refresh:
                resetParamsAndCallApi();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConstant.REQUEST_CODE_PLACE_NEW_ORDER:
                    fetchData(true);
                    break;
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mDataList.clear();
        for (SellerOrderResponse.Order model : mBackUpList) {
            if (model.getPurchaseDate().contains(newText) || model.getNameOfPerson().toLowerCase().contains(newText.toLowerCase()) || model.getProductName().toLowerCase().contains(newText.toLowerCase())) {
                mDataList.add(model);
            }
        }
        notifyAdapterAndView();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.activity_orders_btn_start_date: {
                DatePickerClass.showDatePicker(this, DatePickerClass.START_DATE, this, AppConstant.API_DATE_FORMAT);
                break;
            }

            case R.id.activity_orders_btn_end_date: {
                DatePickerClass.showDatePicker(this, DatePickerClass.END_DATE, this, AppConstant.API_DATE_FORMAT);
                break;
            }

            case R.id.activity_orders_btn_reset_date: {
                MyAlertDialog.showAlertDialog(this, getString(R.string.string_reset_applied_filters), true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            resetParamsAndCallApi();
                        }
                    }
                });
                break;
            }
        }
    }

    @Override
    public void onDateSelectedCallBack(int id, Date date, String stringResOfDate, long milliSeconds, int numberOfDays) {
        switch (id) {
            case DatePickerClass.START_DATE:
                mDateStart = date;
                mBtnStartDate.setText(stringResOfDate);
                break;

            case DatePickerClass.END_DATE:
                mDateEnd = date;
                mBtnEndDate.setText(stringResOfDate);
                break;
        }
        fetchDataWhenFilterSet();
    }

    @Override
    public void onEvent(int pOperationType, Object pObject) {
        mOrderObj = (SupplierOrderListResponse.Order) pObject;

        switch (pOperationType) {
            case AppConstant.OPERATION_DELETE: {

                MyAlertDialog.showAlertDialog(mBaseActivity, getString(R.string.string_continue_to_delete_order), true, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {

                            ItemDeleteRequest itemDeleteRequest = new ItemDeleteRequest();
                            itemDeleteRequest.setFlag(AppConstant.DELETE_OR_PAYMENT_ORDER);
                            itemDeleteRequest.setId(mOrderObj.getOrderId());

                            mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().deleteOrder(itemDeleteRequest);
                            mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mBaseActivity) {

                                @Override
                                public void onResponse(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                                    if (ResponseVerification.isResponseOk(pResponse)) {
                                        fetchData(true);
                                        pBaseActivity.showToast(getString(R.string.string_order_deleted_successfully));
                                    } else {
                                        pBaseActivity.showToast(getString(R.string.string_order_deleted_unsuccessfully));
                                    }
                                }

                            });
                        }
                    }
                });
                break;
            }
            case AppConstant.OPERATION_ORDER_DETAILS: {
                Intent intent = new Intent(mBaseActivity, CustomerOrderDetailsActivity.class);
                intent.putExtra(AppConstant.KEY_OBJECT, mOrderObj);
                Utils.onActivityStart(mBaseActivity, false, null, intent, null);
                break;
            }
            case AppConstant.OPERATION_ORDER_PAYMENT_DETAILS: {
                Intent intent = new Intent(mBaseActivity, SupplierPurchasePaymentActivity.class);
                //intent.putExtra(SupplierPurchasePaymentActivity.PURCHASE_OBJECT_KEY, pObject);
                break;
            }
        }
    }

    private void fetchDataWhenFilterSet() {
        if (mDateStart == null) {
            mBaseActivity.showToast(getString(R.string.string_please_select_start_date));
            return;
        } else if (mDateEnd == null) {
            mBaseActivity.showToast(getString(R.string.string_please_select_end_date));
            return;
        } else if (mDateStart.after(mDateEnd)) {
            mBaseActivity.showToast(getString(R.string.string_start_date_must_less_than_end_date));
            return;
        }
        fetchData(true);
    }

    private void resetParamsAndCallApi() {
        mSearchView.setIconified(true);
        mBtnEndDate.setText(getString(R.string.string_end_date));
        mBtnStartDate.setText(getString(R.string.string_start_date));
        mDateStart = null;
        mDateEnd = null;
        fetchData(true);
    }

    @Override
    public void onPayment(SellerOrderResponse.Order object, View view) {

    }

    @Override
    public void onDelete(SellerOrderResponse.Order object, View view) {

    }

    @Override
    public void onSales(SellerOrderResponse.Order object, View view) {

    }


}
