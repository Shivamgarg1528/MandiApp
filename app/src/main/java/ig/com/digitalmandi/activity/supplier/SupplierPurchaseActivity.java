package ig.com.digitalmandi.activity.supplier;

import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchaseAddReq;
import ig.com.digitalmandi.beans.response.supplier.SellerProductList;
import ig.com.digitalmandi.beans.response.supplier.SellerUnitList;
import ig.com.digitalmandi.beans.response.supplier.SupplierPurchaseListRes;
import ig.com.digitalmandi.database.ProductContract;
import ig.com.digitalmandi.database.UnitContract;
import ig.com.digitalmandi.dialogs.QtyPickerDialog;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.utils.AppConstant;
import ig.com.digitalmandi.utils.ChangeSpinnerItemBg;
import ig.com.digitalmandi.utils.CheckForFloat;
import ig.com.digitalmandi.utils.MyPrefrences;
import ig.com.digitalmandi.utils.Utils;

public class SupplierPurchaseActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    private final int QTY_MAX = 1000;
    private final int QTY_MIN = 1;
    private final int PRODUCT_LOADER = 0;
    private final int UNIT_LOADER = 1;
    @BindView(R.id.mSpinnerProductListPurchase)
    AppCompatSpinner mSpinnerProductListPurchase;
    @BindView(R.id.mSpinnerUnitListPurchase)
    AppCompatSpinner mSpinnerUnitListPurchase;
    @BindView(R.id.decreaseQtyBtn)
    AppCompatImageView decreaseQtyBtn;
    @BindView(R.id.qtyTextView)
    AppCompatTextView qtyTextView;
    @BindView(R.id.increaseQtyBtn)
    AppCompatImageView increaseQtyBtn;
    @BindView(R.id.mEditPersonNamePurchase)
    AppCompatEditText mEditPersonNamePurchase;
    @BindView(R.id.mButtonDatePicker)
    AppCompatButton mButtonDatePicker;
    @BindView(R.id.saveBtnPurchase)
    AppCompatButton saveBtnPurchase;
    @BindView(R.id.mEditProductPrice)
    AppCompatEditText mEditProductPrice;
    @BindView(R.id.mSpinnerDaami)
    AppCompatSpinner mSpinnerDaami;
    @BindView(R.id.mSpinnerLabour)
    AppCompatSpinner mSpinnerLabour;
    @BindView(R.id.mSpinnerKgPrice)
    AppCompatSpinner mSpinnerKgPrice;
    private Unbinder mUnbind;
    private int productQty = 1, labourCostPer100Kg = 3;
    private Calendar purchaseDate;
    private float unitValueFloat = 0.0f, actualPurchaseAmt = 0.0f, daamiPriceInPercentage = 2.5f;
    private List<SellerUnitList.Unit> unitList = new ArrayList<>();
    private List<SellerProductList.Product> productList = new ArrayList<>();
    private String[] unitArray, productArray;
    private SellerProductList.Product product;
    private SellerUnitList.Unit unit;
    private boolean isProductPriceAccTo40Kg = false;

    private void updateQtyTextView() {
        qtyTextView.setText(String.valueOf(productQty));
    }

    private void updateDateTextView(long miliSeconds) {
        mButtonDatePicker.setText(Utils.getDateString(miliSeconds, "dd-MM-yyyy"));
    }

    private void onStartBothLoaders() {
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
        getLoaderManager().initLoader(UNIT_LOADER, null, this);
    }

    private void onRestartBothLoaders() {
        getLoaderManager().restartLoader(PRODUCT_LOADER, null, this);
        getLoaderManager().restartLoader(UNIT_LOADER, null, this);
    }

    private void onSavePurchasedItem() {


        if (purchaseDate == null || product == null || unit == null || !CheckForFloat.onCheckFloat(mEditProductPrice.getText().toString()) || TextUtils.isEmpty(mEditPersonNamePurchase.getText().toString())) {
            Toast.makeText(mBaseActivity, "Provide All Information", Toast.LENGTH_SHORT).show();
            return;
        }

        String personName = mEditPersonNamePurchase.getText().toString();
        float totalProductKg = productQty * unitValueFloat; // 25 * 42.5 = 1062.50
        float productPrice = Float.parseFloat(mEditProductPrice.getText().toString()); // 2250 //100kg
        float totalLabourCost = totalProductKg * labourCostPer100Kg * .01f; // 1062.50kg * 3 * .01f (100kg = .01 && 3 Rs Per 100 Kg)
        float productPriceAccTo40Kg = 0.0f;
        float productPriceAccTo100Kg = 0.0f;

        if (isProductPriceAccTo40Kg) {
            productPriceAccTo40Kg = productPrice;
            productPriceAccTo100Kg = productPriceAccTo40Kg * 2.5f;
        } else {
            productPriceAccTo100Kg = productPrice;
            productPriceAccTo40Kg = productPriceAccTo100Kg / 2.5f;
        }

        actualPurchaseAmt = totalProductKg * productPriceAccTo100Kg * .01f;
        float daamiCost = actualPurchaseAmt * daamiPriceInPercentage / 100.f;

        SupplierPurchaseAddReq purchaseAddReqModel = new SupplierPurchaseAddReq();
        purchaseAddReqModel.setNameOfPerson(personName);
        purchaseAddReqModel.setProductId(product.getProductId());
        purchaseAddReqModel.setProductName(product.getProductName());
        purchaseAddReqModel.setProductQty(productQty);
        purchaseAddReqModel.setPurchaseAmtAcc40Kg(productPriceAccTo40Kg);
        purchaseAddReqModel.setPurchaseAmtAcc100kg(productPriceAccTo100Kg);
        purchaseAddReqModel.setPurchaseDate(Utils.getDateString(purchaseDate.getTimeInMillis(), "yyyy-MM-dd"));
        purchaseAddReqModel.setSubTotalAmt(actualPurchaseAmt);
        purchaseAddReqModel.setTotalAmount(actualPurchaseAmt + daamiCost + totalLabourCost);
        purchaseAddReqModel.setUnitId(unit.getUnitId());
        purchaseAddReqModel.setUnitValue(unit.getKgValue());
        purchaseAddReqModel.setProductInKg(String.valueOf(totalProductKg));
        purchaseAddReqModel.setDaamiCost(daamiCost);
        purchaseAddReqModel.setDaamiValue(daamiPriceInPercentage);
        purchaseAddReqModel.setLabourCost(totalLabourCost);
        purchaseAddReqModel.setLabourValue(labourCostPer100Kg);
        purchaseAddReqModel.setStockStatus(AppConstant.IN_STOCK);
        purchaseAddReqModel.setPurchaseOperation(AppConstant.ADD);
        purchaseAddReqModel.setSellerId(MyPrefrences.getStringPrefrences(AppConstant.USER_SELLER_ID, mBaseActivity));

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().purchaseModification(purchaseAddReqModel);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<SupplierPurchaseListRes>(mBaseActivity, true) {

            @Override
            public void onSuccess(SupplierPurchaseListRes pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    setResult(RESULT_OK, null);
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), pResponse.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(String pErrorMsg) {

            }
        });
    }


    private void showDatePicker() {


        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog startDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                purchaseDate = Calendar.getInstance();
                purchaseDate.set(year, monthOfYear, dayOfMonth);
                updateDateTextView(purchaseDate.getTimeInMillis());
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        startDatePicker.show();
    }

    private void increaseQtyByOne() {
        if (productQty == QTY_MAX)
            return;
        ++productQty;
        updateQtyTextView();
    }

    private void decreaseQtyByOne() {
        if (productQty == QTY_MIN)
            return;
        --productQty;
        updateQtyTextView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_purchase);

        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mUnbind = ButterKnife.bind(this);
        setTitle("Add Purchased Product");

        ChangeSpinnerItemBg.onChangeSpinnerBgWhite(mBaseActivity, mResources.getStringArray(R.array.daamiArray), mSpinnerDaami);
        ChangeSpinnerItemBg.onChangeSpinnerBgWhite(mBaseActivity, mResources.getStringArray(R.array.labourCost), mSpinnerLabour);
        ChangeSpinnerItemBg.onChangeSpinnerBgWhite(mBaseActivity, mResources.getStringArray(R.array.kgPrice), mSpinnerKgPrice);

        mSpinnerProductListPurchase.setOnItemSelectedListener(this);
        mSpinnerUnitListPurchase.setOnItemSelectedListener(this);
        mSpinnerDaami.setOnItemSelectedListener(this);
        mSpinnerLabour.setOnItemSelectedListener(this);
        mSpinnerKgPrice.setOnItemSelectedListener(this);
        onStartBothLoaders();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mSpinnerLabour.setSelection(3, true);
        mSpinnerDaami.setSelection(1, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbind.unbind();
    }

    @OnClick({R.id.decreaseQtyBtn, R.id.increaseQtyBtn, R.id.mButtonDatePicker, R.id.saveBtnPurchase, R.id.qtyTextView})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.decreaseQtyBtn:
                decreaseQtyByOne();
                break;

            case R.id.increaseQtyBtn:
                increaseQtyByOne();
                break;

            case R.id.mButtonDatePicker:
                showDatePicker();
                break;

            case R.id.saveBtnPurchase:
                onSavePurchasedItem();
                break;

            case R.id.qtyTextView:
                Utils.onHideSoftKeyBoard(mBaseActivity, mEditProductPrice);
                QtyPickerDialog qtyPickerDialog = new QtyPickerDialog(mBaseActivity, true, true, R.layout.dialog_qty_selected_layout, new QtyPickerDialog.OnQtySelected() {

                    @Override
                    public void onQtySelectedCallBack(int qty) {
                        productQty = qty;
                        updateQtyTextView();
                    }
                });

                qtyPickerDialog.show();
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cursorLoader = null;
        switch (i) {

            case UNIT_LOADER:
                cursorLoader = new CursorLoader(this, UnitContract.Unit.CONTENT_URI, null, UnitContract.Unit.UNIT_STATUS + " =? ", new String[]{"1"}, null);
                break;

            case PRODUCT_LOADER:
                cursorLoader = new CursorLoader(this, ProductContract.Product.CONTENT_URI, null, ProductContract.Product.PRODUCT_STATUS + "=? ", new String[]{"1"}, null);
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {

            case UNIT_LOADER:
                try {
                    UnitContract unitContract = new UnitContract(mBaseActivity);
                    unitList.clear();
                    unitList.addAll(unitContract.getListOfObject(cursor));
                    unitArray = new String[unitList.size()];
                    for (int index = 0; index < unitList.size(); index++) {
                        unitArray[index] = unitList.get(index).getUnitName();
                    }
                    ChangeSpinnerItemBg.onChangeSpinnerBgWhite(mBaseActivity, unitArray, mSpinnerUnitListPurchase);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case PRODUCT_LOADER:
                try {
                    ProductContract productContract = new ProductContract(mBaseActivity);
                    productList.clear();
                    productList.addAll(productContract.getListOfObject(cursor));
                    productArray = new String[productList.size()];
                    for (int index = 0; index < productList.size(); index++) {
                        productArray[index] = productList.get(index).getProductName();
                    }
                    ChangeSpinnerItemBg.onChangeSpinnerBgWhite(mBaseActivity, productArray, mSpinnerProductListPurchase);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        unitList.clear();
        productList.clear();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        switch (adapterView.getId()) {

            case R.id.mSpinnerUnitListPurchase:
                try {
                    unit = unitList.get(position);
                    try {
                        unitValueFloat = Float.parseFloat(unit.getKgValue());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;

            case R.id.mSpinnerProductListPurchase:
                try {
                    product = productList.get(position);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;

            case R.id.mSpinnerLabour:
                try {
                    labourCostPer100Kg = Integer.parseInt((String) adapterView.getSelectedItem());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;


            case R.id.mSpinnerDaami:
                try {
                    daamiPriceInPercentage = Float.parseFloat((String) adapterView.getSelectedItem());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;

            case R.id.mSpinnerKgPrice:
                try {

                    isProductPriceAccTo40Kg = Float.parseFloat((String) adapterView.getSelectedItem()) == 40;

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
