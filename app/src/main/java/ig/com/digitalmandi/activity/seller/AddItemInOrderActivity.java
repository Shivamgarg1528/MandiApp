package ig.com.digitalmandi.activity.seller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.adapter.supplier.OrderDetailAdapter;
import ig.com.digitalmandi.adapter.supplier.StockAdapter;
import ig.com.digitalmandi.bean.request.seller.CustomerResponse;
import ig.com.digitalmandi.bean.request.seller.SellerOrdersRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderAddRequest;
import ig.com.digitalmandi.bean.response.EmptyResponse;
import ig.com.digitalmandi.bean.response.seller.OrderDetailResponse;
import ig.com.digitalmandi.bean.response.seller.PurchaseResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.dialog.AddItemDialog;
import ig.com.digitalmandi.dialog.ConfirmDialog;
import ig.com.digitalmandi.dialog.DatePickerClass;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class AddItemInOrderActivity extends BaseActivity<PurchaseResponse.Purchase> implements AdapterView.OnItemSelectedListener, EventCallback<PurchaseResponse.Purchase>, TextWatcher, View.OnClickListener {

    private final SupplierOrderAddRequest mOrderAddRequest = new SupplierOrderAddRequest();
    private final List<OrderDetailResponse.OrderDetail> mListCartItem = new ArrayList<>();
    private AppCompatEditText mEditTextDriverName;
    private AppCompatEditText mEditTextDriverPhoneNumber;
    private AppCompatEditText mEditTextVechileRent100Kg;
    private AppCompatEditText mEditTextDriverVehicleNo;
    private AppCompatTextView mTextViewEmptyStock;
    private AppCompatTextView mTextViewEmptyCart;
    private AppCompatTextView mTextViewSubTotalAmt;
    private AppCompatTextView mTextViewExpensesAmt;
    private AppCompatTextView mTextViewLabourAmt;
    private AppCompatTextView mTextViewBardanaAmt;
    private AppCompatTextView mTextViewTotalAmt;
    private AppCompatTextView mTextViewVechileAmt;
    private AppCompatTextView mTextViewTotalLoadInQuintal;
    private AppCompatTextView mTextViewTotalNag;
    private AppCompatButton mButtonOrderDate;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private float mBardanaPer100KgInRs;
    private float mExpensesPercentage;
    private float mLabourPer100KgInRs;
    private float mTotalQty;
    private float mSubTotalAmt;
    private float mExpensesAmt;
    private float mTotalAmt;
    private float mLabourAmt;
    private float mBardanaAmt;
    private float mTotalQtyInKg;
    private float mTotalVechileAmt;
    private CustomerResponse.Customer mCustomerObj;
    private OrderDetailAdapter mCartAdapter;
    private StockAdapter mStockAdapter;

    private Date mDateOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_in_order);

        setToolbar(true);
        setTitle(getString(R.string.string_add_items));

        mCustomerObj = (CustomerResponse.Customer) getIntent().getSerializableExtra(AppConstant.KEY_OBJECT);

        findViewById(R.id.dialog_purchase_payment_tv_payment).setOnClickListener(this);

        mButtonOrderDate = findViewById(R.id.dialog_purchase_payment_tv_payment_date);
        mButtonOrderDate.setOnClickListener(this);

        mSwipeRefreshLayout = findViewById(R.id.layout_common_list_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mEditTextDriverName = findViewById(R.id.mEditTextDriverName);
        mEditTextDriverPhoneNumber = findViewById(R.id.mEditTextDriverPhoneNumber);
        mTextViewSubTotalAmt = findViewById(R.id.mTextViewSubTotalAmt);
        mTextViewExpensesAmt = findViewById(R.id.mTextViewExpensesAmt);
        mTextViewLabourAmt = findViewById(R.id.mTextViewLaboutAmt);
        mTextViewBardanaAmt = findViewById(R.id.mTextViewBardanaAmt);
        mTextViewTotalAmt = findViewById(R.id.mTextViewTotalAmt);
        mTextViewVechileAmt = findViewById(R.id.mTextViewVechileAmt);
        mTextViewTotalLoadInQuintal = findViewById(R.id.mTextViewTotalLoadInQuintal);

        mEditTextDriverVehicleNo = findViewById(R.id.mEditTextDriverVehicleNo);
        mTextViewTotalNag = findViewById(R.id.mTextViewTotalNag);


        mTextViewEmptyCart = findViewById(R.id.layout_common_list_tv_empty_text_view);
        mTextViewEmptyCart.setText(R.string.string_please_add_item_into_cart);

        mTextViewEmptyStock = findViewById(R.id.layout_activity_add_item_tv_empty_view);
        mTextViewEmptyStock.setText(R.string.string_no_purchased_item_found_please_add_item_first);

        mOrderAddRequest.setOrderDetails(mListCartItem);

        RecyclerView recyclerViewCart = findViewById(R.id.layout_common_list_recycler_view);
        recyclerViewCart.setHasFixedSize(true);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(mBaseActivity));
        recyclerViewCart.setAdapter(mCartAdapter = new OrderDetailAdapter(mBaseActivity, mListCartItem, new EventCallback() {

            @Override
            public void onEvent(int pOperationType, Object pObject) {
                if (pOperationType == AppConstant.OPERATION_DELETE) {
                    OrderDetailResponse.OrderDetail removedObject = (OrderDetailResponse.OrderDetail) pObject;
                    for (PurchaseResponse.Purchase traversedObject : mDataList) {
                        if (traversedObject.getPurchaseId().equalsIgnoreCase(removedObject.getPurchaseId())) {
                            traversedObject.setLocalSoldQtyInKg(String.valueOf(Float.parseFloat(traversedObject.getLocalSoldQtyInKg()) - (Float.parseFloat(removedObject.getQtyInKg()))));
                            setTextAfterCalculation();
                            break;
                        }
                    }
                }
            }
        }, true));

        RecyclerView recyclerViewStock = findViewById(R.id.layout_activity_add_item_rv_items);
        recyclerViewStock.setHasFixedSize(true);
        recyclerViewStock.setLayoutManager(new GridLayoutManager(mBaseActivity, 2));
        recyclerViewStock.setAdapter(mStockAdapter = new StockAdapter(mBaseActivity, mDataList, this));
        mStockAdapter.notifyData(mTextViewEmptyStock);

        AppCompatSpinner spinnerBardana = findViewById(R.id.mSpinnerBardana);
        spinnerBardana.setAdapter(Helper.getAdapter(mBaseActivity, R.array.string_array_labour_cost));
        spinnerBardana.setOnItemSelectedListener(this);
        spinnerBardana.setSelection(15, true);

        AppCompatSpinner spinnerExpenses = findViewById(R.id.mSpinnerExpenses);
        spinnerExpenses.setAdapter(Helper.getAdapter(mBaseActivity, R.array.string_array_labour_cost));
        spinnerExpenses.setOnItemSelectedListener(this);
        spinnerExpenses.setSelection(5, true);

        AppCompatSpinner spinnerLabour = findViewById(R.id.mSpinnerLabour);
        spinnerLabour.setAdapter(Helper.getAdapter(mBaseActivity, R.array.string_array_labour_cost));
        spinnerLabour.setOnItemSelectedListener(this);
        spinnerLabour.setSelection(5, true);

        mEditTextVechileRent100Kg = findViewById(R.id.mEditTextVechileRent100Kg);
        mEditTextVechileRent100Kg.addTextChangedListener(this);

        initializedStock();
    }

    @Override
    public void onEvent(int pOperationType, PurchaseResponse.Purchase pObject) {
        AddItemDialog dialog = new AddItemDialog(mBaseActivity, pObject, new EventCallback<OrderDetailResponse.OrderDetail>() {
            @Override
            public void onEvent(int pOperationType, OrderDetailResponse.OrderDetail pObject) {
                mListCartItem.add(0, pObject);
                setTextAfterCalculation();
            }
        });
        dialog.show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        if (!Helper.isEmpty(text)) {
            try {
                mTotalVechileAmt = Float.parseFloat(text) * mTotalQtyInKg * .01f;
                mTextViewVechileAmt.setText(Helper.formatStringUpTo2Precision(String.valueOf(mTotalVechileAmt)));
            } catch (Exception ex) {
                mTotalVechileAmt = 0.0f;
                mTextViewVechileAmt.setText(Helper.formatStringUpTo2Precision(String.valueOf(mTotalVechileAmt)));
            }
        } else {
            mTotalVechileAmt = 0.0f;
            mTextViewVechileAmt.setText(Helper.formatStringUpTo2Precision(String.valueOf(mTotalVechileAmt)));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.mSpinnerBardana:
                mBardanaPer100KgInRs = Float.parseFloat(String.valueOf(adapterView.getItemAtPosition(i)));
                setTextAfterCalculation();
                break;

            case R.id.mSpinnerLabour:
                mLabourPer100KgInRs = Float.parseFloat(String.valueOf(adapterView.getItemAtPosition(i)));
                setTextAfterCalculation();
                break;

            case R.id.mSpinnerExpenses:
                mExpensesPercentage = Float.parseFloat(String.valueOf(adapterView.getItemAtPosition(i)));
                setTextAfterCalculation();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.dialog_purchase_payment_tv_payment_date:
                DatePickerClass.showDatePicker(mBaseActivity, DatePickerClass.ORDER_DATE, new DatePickerClass.OnDateSelected() {
                    @Override
                    public void onDateSelectedCallBack(int id, Date pDate, String pDateAppShownFormat, int pMaxDaysInSelectedMonth) {
                        mButtonOrderDate.setText(pDateAppShownFormat);
                        mDateOrder = pDate;
                    }
                });
                break;

            case R.id.dialog_purchase_payment_tv_payment:
                placeOrder();
                break;
        }
    }

    private void initializedStock() {

        SellerOrdersRequest sellerOrdersRequest = new SellerOrdersRequest();
        sellerOrdersRequest.setEndDate("");
        sellerOrdersRequest.setStartDate("");
        sellerOrdersRequest.setSellerId(mLoginUser.getSellerId());
        sellerOrdersRequest.setPage("1");
        sellerOrdersRequest.setFlag(AppConstant.PURCHASE_LIST_ALL);

        mBaseActivity.mApiEnqueueObject = RetrofitClient.getInstance().getInterface().purchaseList(sellerOrdersRequest);
        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<PurchaseResponse>(mBaseActivity, false) {

            @Override
            public void onResponse(PurchaseResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    mDataList.clear();
                    mDataList.addAll(pResponse.getResult());
                    Helper.changeSellerOrderResponse(mDataList);
                    mStockAdapter.notifyData(mTextViewEmptyStock);
                }
            }
        });
    }

    private void setTextAfterCalculation() {

        mTotalQty = 0;
        mSubTotalAmt = 0;
        mTotalQtyInKg = 0;
        for (OrderDetailResponse.OrderDetail object : mListCartItem) {
            mTotalQty += Float.parseFloat(object.getQty());
            mSubTotalAmt += Float.parseFloat(object.getTotalPrice());
            mTotalQtyInKg += Float.parseFloat(object.getQtyInKg());
        }

        mExpensesAmt = mSubTotalAmt * mExpensesPercentage * .01f;
        mLabourAmt = mTotalQty * mLabourPer100KgInRs;
        mBardanaAmt = mTotalQty * mBardanaPer100KgInRs;
        mTotalAmt = mSubTotalAmt + mBardanaAmt + mExpensesAmt + mLabourAmt;

        mTextViewSubTotalAmt.setText(Helper.formatStringUpTo2Precision(String.valueOf(mSubTotalAmt)));
        mTextViewExpensesAmt.setText(Helper.formatStringUpTo2Precision(String.valueOf(mExpensesAmt)));
        mTextViewLabourAmt.setText(Helper.formatStringUpTo2Precision(String.valueOf(mLabourAmt)));
        mTextViewBardanaAmt.setText(Helper.formatStringUpTo2Precision(String.valueOf(mBardanaAmt)));
        mTextViewTotalAmt.setText(Helper.formatStringUpTo2Precision(String.valueOf(mTotalAmt)));
        mTextViewTotalLoadInQuintal.setText(Helper.formatStringUpTo2Precision(String.valueOf(mTotalQtyInKg * .01f)));
        mTextViewVechileAmt.setText(Helper.formatStringUpTo2Precision(String.valueOf(mTotalVechileAmt)));
        mTextViewTotalNag.setText(Helper.formatStringUpTo2Precision(String.valueOf(mTotalQty)));

        mCartAdapter.notifyData(mTextViewEmptyCart);
        mStockAdapter.notifyData(mTextViewEmptyStock);
    }

    private void placeOrder() {
        Helper.hideSoftKeyBoard(mBaseActivity, mEditTextDriverVehicleNo);

        if (mDataList.isEmpty()) {
            mBaseActivity.showToast(getString(R.string.string_no_purchased_item_found_please_add_item_first));
        } else if (mListCartItem.isEmpty()) {
            mBaseActivity.showToast(getString(R.string.string_please_add_item_into_cart));
        } else if (mDateOrder == null) {
            mBaseActivity.showToast(getString(R.string.string_please_select_order_date_first));
        } else if (Helper.isEmpty(mEditTextVechileRent100Kg.getText().toString()) || !Helper.isFloat(mEditTextVechileRent100Kg.getText().toString())) {
            mBaseActivity.showToast(getString(R.string.string_please_enter_valid_rent));
        } else {
            String driverName = mEditTextDriverName.getText().toString();
            String driverNumber = mEditTextDriverPhoneNumber.getText().toString();
            String driveVechileNumber = mEditTextDriverVehicleNo.getText().toString();
            if (!Helper.isPersonNameOk(driverName, mBaseActivity)) {
            } else if (!Helper.isPhoneNoOk(driverNumber, mBaseActivity)) {
            } else if (Helper.isEmpty(driveVechileNumber)) {
                mBaseActivity.showToast(getString(R.string.please_provide_vechile_number));
            } else {
                mOrderAddRequest.setCustomerId(mCustomerObj.getUserId());
                mOrderAddRequest.setDriverName(driverName);
                mOrderAddRequest.setDriverNumber(driverNumber);
                mOrderAddRequest.setDriverVechileNo(driveVechileNumber);
                mOrderAddRequest.setOrderBardanaAmt(String.valueOf(mBardanaAmt));
                mOrderAddRequest.setOrderBardanaValue(String.valueOf(mBardanaPer100KgInRs));
                mOrderAddRequest.setOrderDaamiAmt(String.valueOf(mExpensesAmt));
                mOrderAddRequest.setOrderDaamiValue(String.valueOf(mExpensesPercentage));
                mOrderAddRequest.setOrderLabourAmt(String.valueOf(mLabourAmt));
                mOrderAddRequest.setOrderLabourValue(String.valueOf(mLabourPer100KgInRs));
                mOrderAddRequest.setOrderDate(Helper.getDateString(mDateOrder.getTime(), AppConstant.API_DATE_FORMAT));
                mOrderAddRequest.setSellerId(mLoginUser.getSellerId());
                mOrderAddRequest.setOrderTotalAmt(String.valueOf(mTotalAmt));
                mOrderAddRequest.setOrderSubtotalAmt(String.valueOf(mSubTotalAmt));
                mOrderAddRequest.setVechileRentValue(Helper.formatStringUpTo2Precision(mEditTextVechileRent100Kg.getText().toString()));
                mOrderAddRequest.setVechileRent(Helper.formatStringUpTo2Precision(String.valueOf(mTotalVechileAmt)));
                mOrderAddRequest.setOrderTotalNag(String.valueOf(mTotalQty));
                mOrderAddRequest.setOrderTotalQuintal(String.valueOf(mTotalQtyInKg * .01f));

                ConfirmDialog.show(this, getString(R.string.string_continue_to_place_order), true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            mApiEnqueueObject = RetrofitClient.getInstance().getInterface().insertNewOrder(mOrderAddRequest);
                            mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mBaseActivity, true) {

                                @Override
                                public void onResponse(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                                    if (ResponseVerification.isResponseOk(pResponse)) {
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }
}

