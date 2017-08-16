package ig.com.digitalmandi.activity.supplier;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.adapter.supplier.AddItemListAdapter;
import ig.com.digitalmandi.adapter.supplier.ItemsAdapter;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderAddReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchaseListReq;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierPurchaseListRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierUnitListRes;
import ig.com.digitalmandi.database.UnitContract;
import ig.com.digitalmandi.dialogs.AddItemDialog;
import ig.com.digitalmandi.dialogs.DatePickerClass;
import ig.com.digitalmandi.dialogs.MyAlertDialog;
import ig.com.digitalmandi.interfaces.AdapterCallBack;
import ig.com.digitalmandi.interfaces.OnAlertDialogCallBack;
import ig.com.digitalmandi.interfaces.OnItemAddedCallBack;
import ig.com.digitalmandi.interfaces.OrderCallBack;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.utils.ChangePurchaseModel;
import ig.com.digitalmandi.utils.ChangeSpinnerItemBg;
import ig.com.digitalmandi.utils.CheckForFloat;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.EditTextVerification;
import ig.com.digitalmandi.utils.MyPrefrences;
import ig.com.digitalmandi.utils.Utils;

public class AddItemInOrderActivity extends ParentActivity implements AdapterCallBack, LoaderManager.LoaderCallbacks<Cursor>, OrderCallBack, AdapterView.OnItemSelectedListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyTextView)
    AppCompatTextView emptyTextView;
    @BindView(R.id.id_fragment_progressbar)
    ProgressBar idFragmentProgressbar;
    @BindView(R.id.mButtonDatePicker)
    AppCompatButton mButtonDatePicker;
    @BindView(R.id.mSpinnerExpenses)
    AppCompatSpinner mSpinnerExpenses;
    @BindView(R.id.mSpinnerBardana)
    AppCompatSpinner mSpinnerBardana;
    @BindView(R.id.mSpinnerLabour)
    AppCompatSpinner mSpinnerLabour;
    @BindView(R.id.mEditTextDriverName)
    AppCompatEditText mEditTextDriverName;
    @BindView(R.id.mEditTextDriverPhoneNumber)
    AppCompatEditText mEditTextDriverPhoneNumber;
    @BindView(R.id.mEditTextVechileRent100Kg)
    AppCompatEditText mEditTextVechileRent100Kg;
    @BindView(R.id.mTextViewSubTotalAmt)
    AppCompatTextView mTextViewSubTotalAmt;
    @BindView(R.id.mTextViewExpensesAmt)
    AppCompatTextView mTextViewExpensesAmt;
    @BindView(R.id.mTextViewLaboutAmt)
    AppCompatTextView mTextViewLaboutAmt;
    @BindView(R.id.mTextViewBardanaAmt)
    AppCompatTextView mTextViewBardanaAmt;
    @BindView(R.id.mTextViewTotalAmt)
    AppCompatTextView mTextViewTotalAmt;
    @BindView(R.id.mTextViewVechileAmt)
    AppCompatTextView mTextViewVechileAmt;
    @BindView(R.id.mTextViewTotalLoadInQuintal)
    AppCompatTextView mTextViewTotalLoadInQuintal;
    @BindView(R.id.mButtonPayment)
    AppCompatButton mButtonOrder;
    @BindView(R.id.mEditTextDriverVehicleNo)
    AppCompatEditText mEditTextDriverVehicleNo;
    @BindView(R.id.mTextViewTotalNag)
    AppCompatTextView mTextViewTotalNag;
    @BindView(R.id.recyclerViewItems)
    RecyclerView recyclerViewItems;
    @BindView(R.id.emptyTextViewItems)
    AppCompatTextView emptyTextViewItems;
    private final int UNIT_LOADER = 1;
    private List<SupplierUnitListRes.ResultBean> unitList = new ArrayList<>();
    private String[] unitArray;
    private SupplierOrderAddReq orderAddReqModel;
    private List<SupplierOrderAddReq.OrderDetailsBean> orderDetailList = new ArrayList<>();
    private AddItemListAdapter mAdapter;
    private ItemsAdapter mItemsAdapter;
    private float totalQty, subTotalAmt, expensesAmt, totalAmt, labourAmt, bardanaAmt, totalQtyInKg, totalVechileAmt;

    float mFloatBardanaPer100KgInRs, mFloatExpensesPercentage, mFloatLabourPer100KgInRs;
    private Date orderDate;

    private void onStartBothLoaders() {
        getLoaderManager().initLoader(UNIT_LOADER, null, this);
    }

    private void onRestartBothLoaders() {
        getLoaderManager().restartLoader(UNIT_LOADER, null, this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_add_item_in_order);

        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ButterKnife.bind(this);
        setTitle("Add Items In Cart");
        emptyTextView.setText("Please Add Item Into Cart\nFor Purchase");
        emptyTextViewItems.setText("No Purchased Item Found\nPlease Purchase Item First");

        orderAddReqModel = new SupplierOrderAddReq();
        orderAddReqModel.setOrderDetails(orderDetailList);

        mAdapter = new AddItemListAdapter(orderDetailList, mRunningActivity, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mRunningActivity));


        mItemsAdapter = new ItemsAdapter(mRunningActivity, dataList, this);
        recyclerViewItems.setHasFixedSize(true);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(mRunningActivity, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewItems.setAdapter(mItemsAdapter);

        onStartBothLoaders();
        onGetDataStockFromServer();
        mAdapter.notifyData(emptyTextView);

        ChangeSpinnerItemBg.onChangeSpinnerBgWhite(mRunningActivity, mResources.getStringArray(R.array.labourCost),mSpinnerBardana);
        ChangeSpinnerItemBg.onChangeSpinnerBgWhite(mRunningActivity, mResources.getStringArray(R.array.labourCost),mSpinnerExpenses);
        ChangeSpinnerItemBg.onChangeSpinnerBgWhite(mRunningActivity, mResources.getStringArray(R.array.labourCost),mSpinnerLabour);

        mSpinnerBardana  .setOnItemSelectedListener(this);
        mSpinnerExpenses .setOnItemSelectedListener(this);
        mSpinnerLabour   .setOnItemSelectedListener(this);

        mSpinnerBardana.setSelection(15, true);
        mSpinnerExpenses.setSelection(5, true);
        mSpinnerLabour.setSelection(5, true);
        mEditTextVechileRent100Kg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();

                if(text.length() > 0){
                    try {
                        totalVechileAmt = Float.parseFloat(text) * totalQtyInKg * .01f;
                        mTextViewVechileAmt.setText(Utils.onStringFormat(String.valueOf(totalVechileAmt)));
                    } catch (Exception ex) {
                        totalVechileAmt = 0.0f;
                        mTextViewVechileAmt.setText(Utils.onStringFormat(String.valueOf(totalVechileAmt)));
                    }
                }
                else{
                    totalVechileAmt = 0.0f;
                    mTextViewVechileAmt.setText(Utils.onStringFormat(String.valueOf(totalVechileAmt)));
                }
            }
        });
    }

    private void onGetDataStockFromServer() {

        SupplierPurchaseListReq purchaseListReqModel = new SupplierPurchaseListReq();
        purchaseListReqModel.setEndDate("");
        purchaseListReqModel.setStartDate("");
        purchaseListReqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mRunningActivity));
        purchaseListReqModel.setPage(String.valueOf(1));
        purchaseListReqModel.setFlag(ConstantValues.PURCHASE_LIST_ALL);

        apiEnqueueObject = RetrofitWebService.getInstance().getInterface().purchaseList(purchaseListReqModel);
        apiEnqueueObject.enqueue(new RetrofitCallBack<SupplierPurchaseListRes>(mRunningActivity, false) {

            @Override
            public void yesCall(SupplierPurchaseListRes response, ParentActivity weakRef) {

                if (VerifyResponse.isResponseOk(response, false)) {
                    dataList.addAll(response.getResult());
                }

                if (!dataList.isEmpty())
                    ChangePurchaseModel.onChangePurchaseModel(dataList);
                mItemsAdapter.notifyData(emptyTextViewItems);
            }

            @Override
            public void noCall(Throwable error) {
            }
        });
    }

    @Override
    public void onItemClick(Object object) {
        SupplierPurchaseListRes.ResultBean selectedObject = (SupplierPurchaseListRes.ResultBean) object;
        if (unitList.isEmpty()) {
            Toast.makeText(mRunningActivity, "No Unit Added Yet Please Add Any Unit", Toast.LENGTH_SHORT).show();
            return;
        }

        AddItemDialog addItemDialog = new AddItemDialog(mRunningActivity, true, true, R.layout.dialog_add_item_into_cart);
        addItemDialog.show(selectedObject, unitArray, unitList, new OnItemAddedCallBack<SupplierOrderAddReq.OrderDetailsBean>() {
            @Override
            public void onItemAddedCallBacks(SupplierOrderAddReq.OrderDetailsBean object) {
                orderDetailList.add(0, object);
                mAdapter.notifyData(emptyTextView);
                mItemsAdapter.notifyData(emptyTextViewItems);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cursorLoader = null;
        switch (i) {

            case UNIT_LOADER:
                cursorLoader = new CursorLoader(this, UnitContract.Unit.CONTENT_URI, null, UnitContract.Unit.UNIT_STATUS + " =? ", new String[]{"1"}, null);
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {

            case UNIT_LOADER:
                try {
                    UnitContract unitContract = new UnitContract(mRunningActivity);
                    unitList.clear();
                    unitList.addAll(unitContract.getListOfObject(cursor));
                    unitArray = new String[unitList.size()];
                    for (int index = 0; index < unitList.size(); index++) {
                        unitArray[index] = unitList.get(index).getUnitName();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        unitList.clear();
    }

    @Override
    public void onPayment(Object object, View view) {
    }

    @Override
    public void onDelete(Object object, View view) {
        SupplierOrderAddReq.OrderDetailsBean removedObject = (SupplierOrderAddReq.OrderDetailsBean) object;
        for (int index = dataList.size() - 1; index >= 0; index--) {
            SupplierPurchaseListRes.ResultBean traversedObject = (SupplierPurchaseListRes.ResultBean) dataList.get(index);
            if (traversedObject.getPurchaseId().equalsIgnoreCase(removedObject.getPurchaseId())) {
                traversedObject.setLocalSoldQty(String.valueOf(Float.parseFloat(traversedObject.getLocalSoldQty()) - (Float.parseFloat(removedObject.getQtyInKg()))));
                break;
            }
        }
        mAdapter.notifyData(emptyTextView);
        mItemsAdapter.notifyData(emptyTextViewItems);
    }

    // Calculating the values
    @Override
    public void onDetail(Object object, View view) {
        totalQty     = 0;
        subTotalAmt  = 0;
        totalQtyInKg = 0;
        for (int index = orderDetailList.size() - 1; index >= 0; index--) {
            SupplierOrderAddReq.OrderDetailsBean addedObject = (SupplierOrderAddReq.OrderDetailsBean) orderDetailList.get(index);
            totalQty     += Float.parseFloat(addedObject.getQty());
            subTotalAmt  += Float.parseFloat(addedObject.getTotalPrice());
            totalQtyInKg += Float.parseFloat(addedObject.getQtyInKg());
        }
        calculateRestValueAndSetOnTextView();
    }

    @Override
    public void onPrint(Object object, View view) {

    }

    private void calculateRestValueAndSetOnTextView() {

        expensesAmt = subTotalAmt * mFloatExpensesPercentage * .01f;
        labourAmt = totalQty * mFloatLabourPer100KgInRs;
        bardanaAmt = totalQty * mFloatBardanaPer100KgInRs;
        totalAmt = subTotalAmt + bardanaAmt + expensesAmt + labourAmt;

        mTextViewBardanaAmt.setText(Utils.onStringFormat(String.valueOf(bardanaAmt)));
        mTextViewExpensesAmt.setText(Utils.onStringFormat(String.valueOf(expensesAmt)));
        mTextViewLaboutAmt.setText(Utils.onStringFormat(String.valueOf(labourAmt)));
        mTextViewSubTotalAmt.setText(Utils.onStringFormat(String.valueOf(subTotalAmt)));
        mTextViewTotalAmt.setText(Utils.onStringFormat(String.valueOf(totalAmt)));
        mTextViewTotalLoadInQuintal.setText(Utils.onStringFormat(String.valueOf(totalQtyInKg * .01f)));
        mTextViewVechileAmt.setText(Utils.onStringFormat(String.valueOf(0.00)));
        mTextViewTotalNag.setText(Utils.onStringFormat(String.valueOf(totalQty)));

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.mSpinnerBardana:
                mFloatBardanaPer100KgInRs = Float.parseFloat(String.valueOf(adapterView.getItemAtPosition(i)));
                calculateRestValueAndSetOnTextView();
                break;

            case R.id.mSpinnerLabour:
                mFloatLabourPer100KgInRs = Float.parseFloat(String.valueOf(adapterView.getItemAtPosition(i)));
                calculateRestValueAndSetOnTextView();
                break;

            case R.id.mSpinnerExpenses:
                mFloatExpensesPercentage = Float.parseFloat(String.valueOf(adapterView.getItemAtPosition(i)));
                calculateRestValueAndSetOnTextView();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @OnClick({R.id.mButtonDatePicker, R.id.mButtonPayment})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mButtonDatePicker:
                DatePickerClass.showDatePicker(DatePickerClass.END_DATE, new DatePickerClass.OnDateSelected() {
                    @Override
                    public void onDateSelectedCallBack(int id, Date newCalendar1, String stringResOfDate, long milliSeconds, int numberOfDays) {
                        mButtonDatePicker.setText(stringResOfDate);
                        orderDate = newCalendar1;
                    }
                }, mRunningActivity, ConstantValues.API_DATE_FORMAT);
                break;

            case R.id.mButtonPayment:
                doOrder();
                break;
        }
    }

    private void doOrder() {
        Utils.onHideSoftKeyBoard(mRunningActivity, mEditTextDriverVehicleNo);

        if (dataList.isEmpty()) {
            Toast.makeText(mRunningActivity, "Please Add Any Item IntoCart First", Toast.LENGTH_SHORT).show();
            return;
        }

        if (orderDate == null) {
            Toast.makeText(mRunningActivity, "Please Select Order Date First", Toast.LENGTH_SHORT).show();
            return;
        }


        if (mEditTextVechileRent100Kg.getText().toString().isEmpty()) {
            Toast.makeText(mRunningActivity, "Vehicle Rent Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!CheckForFloat.onCheckFloat(mEditTextVechileRent100Kg.getText().toString())){
            Toast.makeText(mRunningActivity, "Please Enter Valid Vehicle Rent", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!EditTextVerification.isPersonNameOk(mEditTextDriverName.getText().toString(), (ParentActivity) mRunningActivity))
            return;

        if(!EditTextVerification.isPhoneNoOk(mEditTextDriverPhoneNumber.getText().toString(), (ParentActivity) mRunningActivity))
            return;

        if (mEditTextDriverVehicleNo.getText().toString().isEmpty()) {
            Toast.makeText(mRunningActivity, "Please Provide Vehicle Number", Toast.LENGTH_SHORT).show();
            return;
        }

        orderAddReqModel.setCustomerId(MyPrefrences.getStringPrefrences(ConstantValues.CUSTOMER_ID_ORDER, mRunningActivity));
        orderAddReqModel.setDriverName(mEditTextDriverName.getText().toString());
        orderAddReqModel.setDriverNumber(mEditTextDriverPhoneNumber.getText().toString());
        orderAddReqModel.setDriverVechileNo(mEditTextDriverVehicleNo.getText().toString());
        orderAddReqModel.setOrderBardanaAmt(String.valueOf(bardanaAmt));
        orderAddReqModel.setOrderBardanaValue(String.valueOf(mFloatBardanaPer100KgInRs));
        orderAddReqModel.setOrderDaamiAmt(String.valueOf(expensesAmt));
        orderAddReqModel.setOrderDaamiValue(String.valueOf(mFloatExpensesPercentage));
        orderAddReqModel.setOrderLabourAmt(String.valueOf(labourAmt));
        orderAddReqModel.setOrderLabourValue(String.valueOf(mFloatLabourPer100KgInRs));
        orderAddReqModel.setOrderDate(Utils.onConvertDateToString(orderDate.getTime(), ConstantValues.API_DATE_FORMAT));
        orderAddReqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mRunningActivity));
        orderAddReqModel.setOrderTotalAmt(String.valueOf(totalAmt));
        orderAddReqModel.setOrderSubtotalAmt(String.valueOf(subTotalAmt));
        orderAddReqModel.setVechileRentValue(Utils.onStringFormat(mEditTextVechileRent100Kg.getText().toString()));
        orderAddReqModel.setVechileRent(Utils.onStringFormat(String.valueOf(totalVechileAmt)));
        orderAddReqModel.setOrderTotalNag(String.valueOf(totalQty));
        orderAddReqModel.setOrderTotalQuintal(String.valueOf(totalQtyInKg * .01f));

        MyAlertDialog.onShowAlertDialog(this, "Continue, To Place Order!", "Continue", "Leave", true, new OnAlertDialogCallBack() {
            @Override
            public void onNegative(DialogInterface dialogInterface, int i) {

            }

            @Override
            public void onPositive(DialogInterface dialogInterface, int i) {
                apiEnqueueObject = RetrofitWebService.getInstance().getInterface().insertNewOrder(orderAddReqModel);
                apiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mRunningActivity, true) {

                    @Override
                    public void yesCall(EmptyResponse response, ParentActivity weakRef) {
                        if (VerifyResponse.isResponseOk(response)) {
                            Toast.makeText(weakRef, "Order Placed SuccessFully", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void noCall(Throwable error) {

                    }
                });
            }
        });
    }
}
