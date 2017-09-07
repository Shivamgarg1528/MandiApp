package ig.com.digitalmandi.activity.seller;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.request.seller.SupplierPurchaseAddRequest;
import ig.com.digitalmandi.bean.response.seller.SellerOrderResponse;
import ig.com.digitalmandi.bean.response.seller.SellerProductList;
import ig.com.digitalmandi.bean.response.seller.SellerUnitList;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.dialog.DatePickerClass;
import ig.com.digitalmandi.dialog.QuantityDialog;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.AppSharedPrefs;
import ig.com.digitalmandi.util.Helper;

public class SupplierPurchaseActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, DatePickerClass.OnDateSelected, View.OnClickListener {

    private final List<SellerUnitList.Unit> mUnitList = new ArrayList<>(0);
    private final List<SellerProductList.Product> mProductList = new ArrayList<>(0);
    private final List<String> mUnitNameList = new ArrayList<>(0);
    private final List<String> mProductNameList = new ArrayList<>(0);
    // Default values
    private int mProductQty = 1;
    private int mLabourCostPer100Kg = 3;
    private float mDaamiPriceInPercentage = 2.5f;
    private boolean mProductPriceAccTo40Kg = false;
    private AppCompatTextView mTextViewQty;
    private AppCompatEditText mEditTextPersonName;
    private AppCompatEditText mEditTextProductPrice;
    private AppCompatButton mButtonPurchaseDate;
    private Date mDatePurchase;
    private SellerProductList.Product mProduct;
    private SellerUnitList.Unit mUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setToolbar(true);
        setTitle(getString(R.string.string_add_purchased_product));
        initializedProductAndUnitList();

        mEditTextPersonName = (AppCompatEditText) findViewById(R.id.mEditPersonNamePurchase);
        mEditTextProductPrice = (AppCompatEditText) findViewById(R.id.layout_edt_product_price);

        mTextViewQty = (AppCompatTextView) findViewById(R.id.layout_tv_qty);
        mTextViewQty.setOnClickListener(this);

        mButtonPurchaseDate = (AppCompatButton) findViewById(R.id.dialog_purchase_payment_tv_payment_date);
        mButtonPurchaseDate.setOnClickListener(this);

        findViewById(R.id.layout_btn_save).setOnClickListener(this);
        findViewById(R.id.layout_btn_decrease_qty).setOnClickListener(this);
        findViewById(R.id.layout_btn_increase_qty).setOnClickListener(this);

        AppCompatSpinner spinnerProduct = (AppCompatSpinner) findViewById(R.id.mSpinnerProductListPurchase);
        spinnerProduct.setAdapter(Helper.getAdapter(this, mProductNameList.toArray(new String[mProductNameList.size()])));
        spinnerProduct.setOnItemSelectedListener(this);

        AppCompatSpinner spinnerUnit = (AppCompatSpinner) findViewById(R.id.layout_spinner_unit);
        spinnerUnit.setAdapter(Helper.getAdapter(this, mUnitNameList.toArray(new String[mUnitNameList.size()])));
        spinnerUnit.setOnItemSelectedListener(this);

        AppCompatSpinner spinnerKgPrice = (AppCompatSpinner) findViewById(R.id.layout_spinner_kg_price);
        spinnerKgPrice.setAdapter(Helper.getAdapter(this, R.array.string_array_kg_price));
        spinnerKgPrice.setOnItemSelectedListener(this);

        AppCompatSpinner spinnerLabour = (AppCompatSpinner) findViewById(R.id.mSpinnerLabour);
        spinnerLabour.setAdapter(Helper.getAdapter(this, R.array.string_array_labour_cost));
        spinnerLabour.setOnItemSelectedListener(this);
        spinnerLabour.setSelection(3, true);

        AppCompatSpinner spinnerDaami = (AppCompatSpinner) findViewById(R.id.mSpinnerDaami);
        spinnerDaami.setAdapter(Helper.getAdapter(this, R.array.string_array_dami_amount));
        spinnerDaami.setOnItemSelectedListener(this);
        spinnerDaami.setSelection(1, true);
    }

    @Override
    public void onClick(View view) {
        Helper.onHideSoftKeyBoard(this, mEditTextProductPrice);
        switch (view.getId()) {

            case R.id.layout_btn_decrease_qty:
                decreaseQtyByOne();
                break;

            case R.id.layout_btn_increase_qty:
                increaseQtyByOne();
                break;

            case R.id.dialog_purchase_payment_tv_payment_date:
                DatePickerClass.showDatePicker(this, DatePickerClass.ORDER_DATE, this);
                break;

            case R.id.layout_btn_save:
                createOrder();
                break;

            case R.id.layout_tv_qty:
                QuantityDialog quantityDialog = new QuantityDialog(Integer.parseInt(mTextViewQty.getText().toString()), mBaseActivity, new EventCallback() {
                    @Override
                    public void onEvent(int pOperationType, Object pObject) {
                        mProductQty = (int) pObject;
                        updateQtyTextView();
                    }
                });
                quantityDialog.show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        switch (adapterView.getId()) {

            case R.id.layout_spinner_unit:
                mUnit = mUnitList.get(position);
                break;

            case R.id.mSpinnerProductListPurchase:
                mProduct = mProductList.get(position);
                break;

            case R.id.mSpinnerLabour:
                mLabourCostPer100Kg = Integer.parseInt((String) adapterView.getSelectedItem());
                break;

            case R.id.mSpinnerDaami:
                mDaamiPriceInPercentage = Float.parseFloat((String) adapterView.getSelectedItem());
                break;

            case R.id.layout_spinner_kg_price:
                mProductPriceAccTo40Kg = Float.parseFloat((String) adapterView.getSelectedItem()) == 40;
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onDateSelectedCallBack(int id, Date pDate, String pDateAppShownFormat, int pMaxDaysInSelectedMonth) {
        mDatePurchase = pDate;
        mButtonPurchaseDate.setText(pDateAppShownFormat);
    }

    private void updateQtyTextView() {
        mTextViewQty.setText(String.valueOf(mProductQty));
    }

    private void createOrder() {

        String personName = mEditTextPersonName.getText().toString();
        if (mDatePurchase == null
                || mProduct == null
                || mUnit == null
                || !Helper.isFloat(mEditTextProductPrice.getText().toString())
                || Helper.isEmpty(personName)) {
            mBaseActivity.showToast("Provide All Information");
            return;
        }

        float unitValue = Float.parseFloat(mUnit.getKgValue());
        float totalProductKg = mProductQty * unitValue; // 25 * 42.5 = 1062.50
        float productPrice = Float.parseFloat(mEditTextProductPrice.getText().toString()); // 2250
        float totalLabourCost = totalProductKg * mLabourCostPer100Kg * .01f; // 1062.50kg * 3 * .01f (100kg = .01 && 3 Rs Per 100 Kg)
        float productPriceAccTo40Kg;
        float productPriceAccTo100Kg;

        if (mProductPriceAccTo40Kg) {
            productPriceAccTo40Kg = productPrice;
            productPriceAccTo100Kg = productPriceAccTo40Kg * 2.5f;
        } else {
            productPriceAccTo100Kg = productPrice;
            productPriceAccTo40Kg = productPriceAccTo100Kg / 2.5f;
        }

        float purchaseAmt = totalProductKg * productPriceAccTo100Kg * .01f;
        float daamiCost = purchaseAmt * mDaamiPriceInPercentage / 100.f;

        SupplierPurchaseAddRequest supplierPurchaseAddRequest = new SupplierPurchaseAddRequest();
        supplierPurchaseAddRequest.setNameOfPerson(personName);
        supplierPurchaseAddRequest.setProductId(mProduct.getProductId());
        supplierPurchaseAddRequest.setProductName(mProduct.getProductName());
        supplierPurchaseAddRequest.setProductQty(mProductQty);
        supplierPurchaseAddRequest.setPurchaseAmtAcc40Kg(productPriceAccTo40Kg);
        supplierPurchaseAddRequest.setPurchaseAmtAcc100kg(productPriceAccTo100Kg);
        supplierPurchaseAddRequest.setPurchaseDate(Helper.getDateString(mDatePurchase.getTime(), "yyyy-MM-dd"));
        supplierPurchaseAddRequest.setSubTotalAmt(purchaseAmt);
        supplierPurchaseAddRequest.setTotalAmount(purchaseAmt + daamiCost + totalLabourCost);
        supplierPurchaseAddRequest.setUnitId(mUnit.getUnitId());
        supplierPurchaseAddRequest.setUnitValue(mUnit.getKgValue());
        supplierPurchaseAddRequest.setProductInKg(String.valueOf(totalProductKg));
        supplierPurchaseAddRequest.setDaamiCost(daamiCost);
        supplierPurchaseAddRequest.setDaamiValue(mDaamiPriceInPercentage);
        supplierPurchaseAddRequest.setLabourCost(totalLabourCost);
        supplierPurchaseAddRequest.setLabourValue(mLabourCostPer100Kg);
        supplierPurchaseAddRequest.setStockStatus(AppConstant.IN_STOCK);
        supplierPurchaseAddRequest.setPurchaseOperation(AppConstant.ADD);
        supplierPurchaseAddRequest.setSellerId(mLoginUser.getSellerId());

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().purchaseModification(supplierPurchaseAddRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<SellerOrderResponse>(this, true) {

            @Override
            public void onResponse(SellerOrderResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    setResult(RESULT_OK, null);
                    finish();
                } else {
                    pBaseActivity.showToast(pResponse.getMessage());
                }
            }
        });
    }

    private void increaseQtyByOne() {
        if (mProductQty >= AppConstant.QTY_MAX) {
            return;
        }
        ++mProductQty;
        updateQtyTextView();
    }

    private void decreaseQtyByOne() {
        if (mProductQty <= AppConstant.QTY_MIN) {
            return;
        }
        --mProductQty;
        updateQtyTextView();
    }

    private void initializedProductAndUnitList() {
        AppSharedPrefs prefs = AppSharedPrefs.getInstance(this);

        mUnitList.clear();
        mUnitNameList.clear();
        mUnitList.addAll(prefs.getSellerUnits().getResult());
        for (SellerUnitList.Unit unit : mUnitList) {
            mUnitNameList.add(unit.getUnitName());
        }

        mProductList.clear();
        mProductNameList.clear();
        mProductList.addAll(prefs.getSellerProducts().getResult());
        for (SellerProductList.Product product : mProductList) {
            mProductNameList.add(product.getProductName());
        }
    }
}
