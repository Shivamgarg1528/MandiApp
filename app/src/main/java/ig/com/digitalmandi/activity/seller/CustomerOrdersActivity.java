package ig.com.digitalmandi.activity.seller;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Date;
import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.activity.ListBaseActivity;
import ig.com.digitalmandi.adapter.supplier.OrderAdapter;
import ig.com.digitalmandi.bean.request.seller.CustomerResponse;
import ig.com.digitalmandi.bean.request.seller.ItemDeleteRequest;
import ig.com.digitalmandi.bean.request.seller.OrderDetailsRequest;
import ig.com.digitalmandi.bean.request.seller.PaymentsRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderBillPrintRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderListRequest;
import ig.com.digitalmandi.bean.response.EmptyResponse;
import ig.com.digitalmandi.bean.response.seller.OrdersResponse;
import ig.com.digitalmandi.bean.response.seller.SupplierBillPrintRes;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.dialog.DatePickerClass;
import ig.com.digitalmandi.dialog.PreConfirmDialog;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;
import ig.com.digitalmandi.util.LoadMoreClass;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;

public class CustomerOrdersActivity extends ListBaseActivity<OrdersResponse.Order> implements SearchView.OnQueryTextListener, DatePickerClass.OnDateSelected, EasyPermissions.PermissionCallbacks, View.OnClickListener, EventCallback {

    private int mPageCount = 1;
    private boolean mLoadMore = false;

    private Date mDateStart;
    private Date mDateEnd;

    private CustomerResponse.Customer mCustomerObj;
    private final LoadMoreClass mLoadMoreClass = new LoadMoreClass() {

        @Override
        public void onLoadMore() {
            if (mLoadMore) {
                return;
            }
            ++mPageCount;
            fetchData(false);
        }
    };
    private OrdersResponse.Order mOrderObj;
    private SearchView mSearchView;
    private AppCompatButton mBtnStartDate;
    private AppCompatButton mBtnEndDate;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new OrderAdapter(this, mDataList, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_orders;
    }

    @Override
    protected int getEmptyTextStringId() {
        return R.string.string_no_customer_orders_found_please_add_new_order;
    }

    @Override
    protected void fetchData(final boolean pRefresh) {
        if (pRefresh) {
            mPageCount = 1;
            mLoadMore = false;
        }
        SupplierOrderListRequest supplierOrderListRequest = new SupplierOrderListRequest();

        if (mDateEnd != null)
            supplierOrderListRequest.setEndDate(Helper.getDateString(mDateEnd.getTime(), AppConstant.API_DATE_FORMAT));
        else
            supplierOrderListRequest.setEndDate("");

        if (mDateStart != null)
            supplierOrderListRequest.setStartDate(Helper.getDateString(mDateStart.getTime(), AppConstant.API_DATE_FORMAT));
        else
            supplierOrderListRequest.setStartDate("");

        supplierOrderListRequest.setCustomerId(mCustomerObj.getUserId());
        supplierOrderListRequest.setSellerId(mLoginUser.getSellerId());
        supplierOrderListRequest.setPage(String.valueOf(mPageCount));

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().getOrdersOfGivenCustomer(supplierOrderListRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<OrdersResponse>(mBaseActivity, false) {

            @Override
            public void onResponse(OrdersResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    if (pResponse.getResult().size() == 0) {
                        pBaseActivity.showToast(getString(R.string.string_no_orders_found));
                    }
                    if (pRefresh) {
                        mRecyclerView.scrollToPosition(0);
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
        mCustomerObj = (CustomerResponse.Customer) getIntent().getSerializableExtra(AppConstant.KEY_OBJECT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(true);
        setTitle(mCustomerObj.getUserName().toUpperCase() + getString(R.string.string_customer_orders));

        mBtnStartDate = findViewById(R.id.activity_orders_btn_start_date);
        mBtnStartDate.setOnClickListener(this);

        mBtnEndDate = findViewById(R.id.activity_orders_btn_end_date);
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
                intent.putExtra(AppConstant.KEY_OBJECT, mCustomerObj);
                Helper.onActivityStartForResult(this, false, null, intent, null, AppConstant.REQUEST_CODE_PLACE_NEW_ORDER);
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
        for (OrdersResponse.Order order : mBackUpList) {
            if (order.getOrderDate().contains(newText) || order.getDriverNumber().contains(newText) || order.getOrderId().contains(newText)) {
                mDataList.add(order);
            }
        }
        notifyAdapterAndView();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.activity_orders_btn_start_date: {
                DatePickerClass.showDatePicker(this, DatePickerClass.START_DATE, this);
                break;
            }

            case R.id.activity_orders_btn_end_date: {
                DatePickerClass.showDatePicker(this, DatePickerClass.END_DATE, this);
                break;
            }

            case R.id.activity_orders_btn_reset_date: {
                PreConfirmDialog.showAlertDialog(this, getString(R.string.string_reset_applied_filters), true, new DialogInterface.OnClickListener() {
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
    public void onDateSelectedCallBack(int id, Date pDate, String pDateAppShownFormat, int pMaxDaysInSelectedMonth) {
        switch (id) {
            case DatePickerClass.START_DATE:
                mDateStart = pDate;
                mBtnStartDate.setText(pDateAppShownFormat);
                break;

            case DatePickerClass.END_DATE:
                mDateEnd = pDate;
                mBtnEndDate.setText(pDateAppShownFormat);
                break;
        }
        fetchDataWhenFilterSet();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, mBaseActivity);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == AppConstant.REQUEST_CODE_WRITE_PERMISSION) {
            showPDFActivity();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onEvent(int pOperationType, Object pObject) {
        mOrderObj = (OrdersResponse.Order) pObject;

        switch (pOperationType) {
            case AppConstant.OPERATION_DELETE: {

                PreConfirmDialog.showAlertDialog(mBaseActivity, getString(R.string.string_continue_to_delete_order), true, new DialogInterface.OnClickListener() {

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
                OrderDetailsRequest orderDetailsRequest = new OrderDetailsRequest();
                orderDetailsRequest.setFlag(AppConstant.COLUMN_ORDER_ID);
                orderDetailsRequest.setId(mOrderObj.getOrderId());
                orderDetailsRequest.setMessage(getString(R.string.string_order_id_details, mOrderObj.getOrderId()));
                Intent intent = new Intent(mBaseActivity, OrderDetailsActivity.class);
                intent.putExtra(AppConstant.KEY_OBJECT, orderDetailsRequest);
                Helper.onActivityStart(mBaseActivity, false, null, intent, null);
                break;
            }
            case AppConstant.OPERATION_ORDER_PAYMENT_DETAILS: {

                PaymentsRequest paymentsRequest = new PaymentsRequest();
                paymentsRequest.setFlag(AppConstant.DELETE_OR_PAYMENT_ORDER);
                paymentsRequest.setId(mOrderObj.getOrderId());
                paymentsRequest.setOrderDate(mOrderObj.getOrderDate());
                paymentsRequest.setOrderAmount(mOrderObj.getOrderTotalAmt());

                Intent intent = new Intent(mBaseActivity, PaymentsActivity.class);
                intent.putExtra(AppConstant.KEY_OBJECT, paymentsRequest);
                Helper.onActivityStart(mBaseActivity, false, null, intent, null);
                break;
            }
            case AppConstant.OPERATION_ORDER_BILL_PRINT: {
                if (EasyPermissions.hasPermissions(mBaseActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showPDFActivity();
                } else {
                    EasyPermissions.requestPermissions(mBaseActivity, getString(R.string.string_allow_external_storage_access), AppConstant.REQUEST_CODE_WRITE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            }
        }
    }

    private void showPDFActivity() {

        SupplierOrderBillPrintRequest supplierOrderBillPrintRequest = new SupplierOrderBillPrintRequest();
        supplierOrderBillPrintRequest.setSellerId(mLoginUser.getSellerId());
        supplierOrderBillPrintRequest.setOrderId(mOrderObj.getOrderId());

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().orderBillPrint(supplierOrderBillPrintRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<SupplierBillPrintRes>(mBaseActivity) {

            @Override
            public void onResponse(SupplierBillPrintRes pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, true)) {

                    mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().downloadFileWithDynamicUrlSync(pResponse.getResult().get(0).getURL());
                    mApiEnqueueObject.enqueue(new RetrofitCallBack<ResponseBody>(mBaseActivity) {

                        @Override
                        public void onResponse(ResponseBody pResponse, BaseActivity pBaseActivity) {
                            boolean writeSuccessfully = Helper.writePdf(pResponse, mOrderObj.getOrderId(), true);
                            if (writeSuccessfully) {
                                Helper.readPdf(mBaseActivity, mOrderObj.getOrderId(), true);
                            } else {
                                mBaseActivity.showToast(getString(R.string.string_failed_to_get_pdf_from_server));
                            }
                        }

                    });
                } else {
                    mBaseActivity.showToast(getString(R.string.string_failed_to_get_pdf_from_server));
                }
            }

        });
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
}
