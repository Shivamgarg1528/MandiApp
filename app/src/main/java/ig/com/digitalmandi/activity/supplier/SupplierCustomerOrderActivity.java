package ig.com.digitalmandi.activity.supplier;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.adapter.supplier.SupplierOrderAdapter;
import ig.com.digitalmandi.base_package.LoadMoreClass;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierCustomerListRes;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderBillPrintReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderDeleteReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderListReq;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierBillPrintRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderListRes;
import ig.com.digitalmandi.dialogs.DatePickerClass;
import ig.com.digitalmandi.dialogs.MyAlertDialog;
import ig.com.digitalmandi.interfaces.OnAlertDialogCallBack;
import ig.com.digitalmandi.interfaces.OrderCallBack;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.MyPrefrences;
import ig.com.digitalmandi.utils.Utils;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;

public class SupplierCustomerOrderActivity extends ParentActivity implements SearchView.OnQueryTextListener, DatePickerClass.OnDateSelected, OrderCallBack, EasyPermissions.PermissionCallbacks {

    private static final String TAG = "PDF";
    public static String CUSTOMER_OBJECT_KEY = "customerObject";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyTextView)
    AppCompatTextView emptyTextView;
    private SupplierCustomerListRes.ResultBean customerInfo;
    private SupplierOrderAdapter mAdapter;
    public static final int ORDER_REQUEST_CODE = 1001;
    @BindView(R.id.mButtonStartDate)
    AppCompatButton mButtonStartDate;
    @BindView(R.id.mButtonEndDate)
    AppCompatButton mButtonEndDate;
    @BindView(R.id.mButtonResetDate)
    AppCompatButton mButtonResetDate;
    private Date startDate, endDate;
    private int pageCount = 1;
    private LoadMoreClass loadMoreClass;
    private SearchView searchView;
    private boolean preventLoadMoreFire = false;
    private SupplierOrderListRes.ResultBean printObject;
    private String fileSavePermission[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {

                case ORDER_REQUEST_CODE:
                    fetchDataFromServer(false, true);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.suppiler_customer_order_menu, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.supplier_customer_order_menu_search));
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.supplier_customer_order_menu_add:
                MyPrefrences.setStringPrefrences(ConstantValues.CUSTOMER_ID_ORDER, customerInfo.getUserId(), mRunningActivity);
                Utils.onActivityStartForResult(this, false, new int[]{}, null, AddItemInOrderActivity.class, ORDER_REQUEST_CODE);
                return true;

            case R.id.supplier_customer_order_menu_refresh:
                callAfterReset();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_supplier_customer_more_info);

        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        if (intent == null || (SupplierCustomerListRes.ResultBean) intent.getParcelableExtra(CUSTOMER_OBJECT_KEY) == null) {
            Toast.makeText(mRunningActivity, "No Customer Information Found. Contact App Admin For Better Assistance", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        customerInfo = (SupplierCustomerListRes.ResultBean) intent.getParcelableExtra(CUSTOMER_OBJECT_KEY);
        setTitle(customerInfo.getUserName() + "'s Orders");
        ButterKnife.bind(this);
        emptyTextView.setText("No Order Found\nPlease Add New Order");
        mAdapter = new SupplierOrderAdapter(dataList, this, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        loadMoreClass = new LoadMoreClass((GridLayoutManager) recyclerView.getLayoutManager()) {

            @Override
            public void onLoadMore() {
                super.onLoadMore();

                if (preventLoadMoreFire)
                    return;

                ++pageCount;
                fetchDataFromServer(false, false);
            }
        };
        recyclerView.addOnScrollListener(loadMoreClass);
        fetchDataFromServer(false, true);
    }

    private void callAfterReset() {
        searchView.setIconified(true);
        mButtonEndDate.setText("End Date");
        mButtonStartDate.setText("Start Date");
        startDate = null;
        endDate = null;
        pageCount = 1;
        preventLoadMoreFire = false;
        fetchDataFromServer(false, true);
    }

    private void fetchDataFromServer(boolean showDialog, final boolean refresh) {
        onShowOrHideBar(true);
        SupplierOrderListReq supplierOrderListReq = new SupplierOrderListReq();

        if (endDate != null)
            supplierOrderListReq.setEndDate(Utils.onConvertDateToString(endDate.getTime(), ConstantValues.API_DATE_FORMAT));
        else
            supplierOrderListReq.setEndDate("");

        if (startDate != null)
            supplierOrderListReq.setStartDate(Utils.onConvertDateToString(startDate.getTime(), ConstantValues.API_DATE_FORMAT));
        else
            supplierOrderListReq.setStartDate("");

        supplierOrderListReq.setCustomerId(customerInfo.getUserId());
        supplierOrderListReq.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mRunningActivity));
        supplierOrderListReq.setPage(String.valueOf(pageCount));
        apiEnqueueObject = RetrofitWebService.getInstance().getInterface().orderListOfAnyCustomer(supplierOrderListReq);
        apiEnqueueObject.enqueue(new RetrofitCallBack<SupplierOrderListRes>(mRunningActivity, false) {

            @Override
            public void yesCall(SupplierOrderListRes response, ParentActivity weakRef) {
                if (VerifyResponse.isResponseOk(response, false)) {

                    if (response.getResult().size() == 0)
                        Toast.makeText(weakRef, "No Orders Found", Toast.LENGTH_SHORT).show();

                    if (refresh) {
                        recyclerView.scrollToPosition(0);
                        dataList.clear();
                        backUpList.clear();
                    }
                    dataList.addAll(response.getResult());
                    backUpList.addAll(response.getResult());
                }
                mAdapter.notifyData(emptyTextView);
            }

            @Override
            public void noCall(Throwable error) {

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        dataList.clear();
        preventLoadMoreFire = true;

        if (TextUtils.isEmpty(newText)) {
            dataList.addAll(backUpList);
            mAdapter.notifyData(emptyTextView);
            preventLoadMoreFire = false;
            return true;
        }

        for (int index = 0; index < backUpList.size(); index++) {
            SupplierOrderListRes.ResultBean model = (SupplierOrderListRes.ResultBean) backUpList.get(index);
            if (model.getOrderDate().contains(newText) || model.getDriverNumber().contains(newText) || model.getOrderId().contains(newText)) {
                dataList.add(model);
            }
        }
        mAdapter.notifyData(emptyTextView);
        return true;
    }

    private void callFilter() {
        if (startDate == null) {
            Toast.makeText(mRunningActivity, "Please Select Start Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDate == null) {
            Toast.makeText(mRunningActivity, "Please Select End Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startDate.after(endDate)) {
            Toast.makeText(mRunningActivity, "Start Date Must Be Greater Or Equals To End Date", Toast.LENGTH_SHORT).show();
            return;
        }

        pageCount = 1;
        fetchDataFromServer(false, true);
    }

    @OnClick({R.id.mButtonStartDate, R.id.mButtonEndDate, R.id.mButtonResetDate})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mButtonStartDate:
                DatePickerClass.showDatePicker(DatePickerClass.START_DATE, this, this, ConstantValues.API_DATE_FORMAT);
                break;

            case R.id.mButtonEndDate:
                DatePickerClass.showDatePicker(DatePickerClass.END_DATE, this, this, ConstantValues.API_DATE_FORMAT);
                break;

            case R.id.mButtonResetDate:

                MyAlertDialog.onShowAlertDialog(this, "Reset Applied Filters", "Reset", "Leave", true, new OnAlertDialogCallBack() {
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

    @Override
    public void onPayment(Object object, View view) {
        Utils.onActivityStart(mRunningActivity, false, new int[]{}, convertIntoIntent((SupplierOrderListRes.ResultBean) object), null);
    }

    private Intent convertIntoIntent(SupplierOrderListRes.ResultBean object) {
        Intent intent = new Intent(mRunningActivity, SupplierSoldPaymentActivity.class);
        intent.putExtra(SupplierSoldPaymentActivity.SOLD_OBJECT_KEY, object);
        return intent;
    }

    @Override
    public void onDelete(Object object, View view) {
        SupplierOrderListRes.ResultBean deletedObject = (SupplierOrderListRes.ResultBean) object;
        final SupplierOrderDeleteReq supplierOrderDeleteReq = new SupplierOrderDeleteReq();
        supplierOrderDeleteReq.setFlag(ConstantValues.ORDER_PAYMENT);
        supplierOrderDeleteReq.setId(deletedObject.getOrderId());

        MyAlertDialog.onShowAlertDialog(mRunningActivity, "Continue,To Delete This Order!", "Continue", "Leave", true, new OnAlertDialogCallBack() {

            @Override
            public void onNegative(DialogInterface dialogInterface, int i) {

            }

            @Override
            public void onPositive(DialogInterface dialogInterface, int i) {
                apiEnqueueObject = RetrofitWebService.getInstance().getInterface().deleteOrder(supplierOrderDeleteReq);
                apiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mRunningActivity) {

                    @Override
                    public void yesCall(EmptyResponse response, ParentActivity weakRef) {
                        if (VerifyResponse.isResponseOk(response)) {
                            Toast.makeText(weakRef, "Order Deleted Successfully", Toast.LENGTH_SHORT).show();
                            fetchDataFromServer(false, true);
                        } else
                            Toast.makeText(weakRef, "Sorry You Can't Delete This Order", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void noCall(Throwable error) {

                    }
                });
            }

        });
    }

    @Override
    public void onDetail(Object object, View view) {
        Utils.onActivityStart(mRunningActivity, false, new int[]{}, convertDetailIntent((SupplierOrderListRes.ResultBean) object), null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, mRunningActivity);
    }


    @Override
    public void onPrint(Object object, View view) {
        printObject = (SupplierOrderListRes.ResultBean) object;
        if (EasyPermissions.hasPermissions(mRunningActivity, fileSavePermission))
            showPDf();
        else
            EasyPermissions.requestPermissions(mRunningActivity, "Allow To Access External Storage For Reading And Writing Data", 100, fileSavePermission);
    }

    private Intent convertDetailIntent(SupplierOrderListRes.ResultBean object) {
        Intent intent = new Intent(mRunningActivity, SupplierCustomerOrderDetailActivity.class);
        intent.putExtra(SupplierCustomerOrderDetailActivity.ORDER_OBJECT_KEY, object);
        intent.putExtra(SupplierCustomerOrderDetailActivity.CUSTOMER_OBJECT_KEY, customerInfo);
        return intent;
    }

    private void showPDf() {
        final SupplierOrderBillPrintReq supplierOrderBillPrintReq = new SupplierOrderBillPrintReq();
        supplierOrderBillPrintReq.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mRunningActivity));
        supplierOrderBillPrintReq.setOrderId(printObject.getOrderId());

        apiEnqueueObject = RetrofitWebService.getInstance().getInterface().orderBillPrint(supplierOrderBillPrintReq);
        apiEnqueueObject.enqueue(new RetrofitCallBack<SupplierBillPrintRes>(mRunningActivity) {

            @Override
            public void yesCall(SupplierBillPrintRes response, ParentActivity weakRef) {
                if (VerifyResponse.isResponseOk(response,true)) {

                    apiEnqueueObject = RetrofitWebService.getInstance().getInterface().downloadFileWithDynamicUrlSync(response.getResult().get(0).getURL());
                    apiEnqueueObject.enqueue(new RetrofitCallBack<ResponseBody>(mRunningActivity) {
                        @Override
                        public void yesCall(ResponseBody response, ParentActivity weakRef) {
                            boolean writtenToDisk = Utils.writeResponseBodyToDisk(response, printObject.getOrderId(), true);
                            if (!writtenToDisk) {
                                Toast.makeText(mRunningActivity, "Failed To Get PDF From Server", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                Utils.showPDFActivity(mRunningActivity, Environment.getExternalStorageDirectory() + "/" + ConstantValues.ORDER_BILL_PATH + "/" + ConstantValues.ORDER_BILL_PREFIX + printObject.getOrderId() + ".pdf");
                            }
                        }

                        @Override
                        public void noCall(Throwable error) {
                        }
                    });
                } else
                    Toast.makeText(weakRef, "Sorry You Can't See PDF", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void noCall(Throwable error) {

            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == 100) {
            showPDf();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
