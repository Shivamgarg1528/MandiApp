package ig.com.digitalmandi.dialog;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderAddRequest;
import ig.com.digitalmandi.bean.response.seller.SellerOrderResponse;
import ig.com.digitalmandi.bean.response.seller.SellerUnitList;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.AppSharedPrefs;
import ig.com.digitalmandi.util.Helper;

public class AddItemDialog extends BaseDialog implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private final BaseActivity mBaseActivity;
    private final EventCallback mEventCallback;
    private final SellerOrderResponse.Order mItemObject;
    private final List<SellerUnitList.Unit> mUnitList = new ArrayList<>(0);
    private AppCompatTextView mTextViewQty;
    private AppCompatTextView mTextViewPurchasedQtyInKg;
    private AppCompatEditText mEditProductPrice;
    private String[] mUnitArray;
    private SellerUnitList.Unit mSelectedUnit;

    private boolean isProductPriceAccTo40Kg = false;
    private float mMaxQtyInKg = 0.0f;
    private int mProductQty = 1;

    public AddItemDialog(BaseActivity pBaseActivity, SellerOrderResponse.Order pItemObject, EventCallback pEventCallback) {
        super(pBaseActivity);
        mBaseActivity = pBaseActivity;
        mItemObject = pItemObject;
        mEventCallback = pEventCallback;
    }

    @Override
    public void show() {
        SellerUnitList sellerUnits = AppSharedPrefs.getInstance(mBaseActivity).getSellerUnits();
        mUnitList.clear();
        mUnitList.addAll(sellerUnits.getResult());
        if (mUnitList.isEmpty()) {
            return;
        }
        mUnitArray = new String[mUnitList.size()];
        for (int index = 0; index < mUnitList.size(); index++) {
            mUnitArray[index] = mUnitList.get(index).getUnitName();
        }
        super.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_item_into_cart);

        findViewById(R.id.layout_btn_increase_qty).setOnClickListener(this);
        findViewById(R.id.layout_btn_decrease_qty).setOnClickListener(this);
        findViewById(R.id.layout_btn_save).setOnClickListener(this);

        mTextViewQty = (AppCompatTextView) findViewById(R.id.layout_tv_qty);
        mTextViewQty.setOnClickListener(this);

        mEditProductPrice = (AppCompatEditText) findViewById(R.id.layout_edt_product_price);

        AppCompatSpinner spinnerUnit = (AppCompatSpinner) findViewById(R.id.layout_spinner_unit);
        spinnerUnit.setAdapter(Helper.getAdapter(mBaseActivity, mUnitArray));
        spinnerUnit.setOnItemSelectedListener(this);

        AppCompatSpinner spinnerKgPrice = (AppCompatSpinner) findViewById(R.id.layout_spinner_kg_price);
        spinnerKgPrice.setAdapter(Helper.getAdapter(mBaseActivity, R.array.string_array_kg_price));
        spinnerKgPrice.setOnItemSelectedListener(this);

        try {
            mMaxQtyInKg = Float.parseFloat(mItemObject.getLeftQty());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mTextViewPurchasedQtyInKg = (AppCompatTextView) findViewById(R.id.layout_tv_purchased_qty_in_kg);

        AppCompatTextView textViewMaxQtyInKg = (AppCompatTextView) findViewById(R.id.layout_tv_max_qty_in_kg);
        textViewMaxQtyInKg.setText(String.format(mBaseActivity.getString(R.string.string_max_qty), Helper.formatStringUpTo2Precision("" + mMaxQtyInKg)));

        AppCompatTextView textViewProductName = (AppCompatTextView) findViewById(R.id.layout_tv_product_name);
        textViewProductName.setText(mItemObject.getProductName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.layout_btn_decrease_qty:
                decreaseQtyByOne();
                break;

            case R.id.layout_tv_qty:
                Helper.onHideSoftKeyBoard(mBaseActivity, mEditProductPrice);
                QuantityDialog quantityDialog = new QuantityDialog(Integer.parseInt(mTextViewQty.getText().toString()), mBaseActivity, new EventCallback() {
                    @Override
                    public void onEvent(int pOperationType, Object pObject) {
                        mProductQty = (int) pObject;
                        updateQtyTextView();
                    }
                });
                quantityDialog.show();
                break;

            case R.id.layout_btn_increase_qty:
                increaseQtyByOne();
                break;

            case R.id.layout_btn_save:
                onSave();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {

            case R.id.layout_spinner_unit:
                mSelectedUnit = mUnitList.get(position);
                updateQtyTextView();
                break;

            case R.id.layout_spinner_kg_price:
                isProductPriceAccTo40Kg = Float.parseFloat((String) adapterView.getSelectedItem()) == 40;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void onSave() {

        String productPriceStr = mEditProductPrice.getText().toString();
        if (!Helper.isFloat(productPriceStr)) {
            mBaseActivity.showToast(mBaseActivity.getString(R.string.string_please_enter_product_price));
            return;
        }
        float totalProductKg = mProductQty * Float.parseFloat(mSelectedUnit.getKgValue()); // 25 * 42.5 = 1062.50
        float productPrice = Float.parseFloat(productPriceStr); // 2250 //100kg
        float productPriceAccTo40Kg;
        float productPriceAccTo100Kg;

        if (totalProductKg > mMaxQtyInKg) {
            mBaseActivity.showToast(mBaseActivity.getString(R.string.string_you_dont_have_enough_stock));
            return;
        }

        if (isProductPriceAccTo40Kg) {
            productPriceAccTo40Kg = productPrice;
            productPriceAccTo100Kg = productPriceAccTo40Kg * 2.5f;
        } else {
            productPriceAccTo100Kg = productPrice;
        }

        float actualPurchaseAmt = totalProductKg * productPriceAccTo100Kg * .01f;

        SupplierOrderAddRequest.OrderDetailsBean addedObject = new SupplierOrderAddRequest.OrderDetailsBean();
        addedObject.setUnitId(mSelectedUnit.getUnitId());
        addedObject.setUnitValue(Helper.formatStringUpTo2Precision(mSelectedUnit.getKgValue()));
        addedObject.setProductId(mItemObject.getProductId());
        addedObject.setProductName(mItemObject.getProductName());
        addedObject.setPrice(Helper.formatStringUpTo2Precision(String.valueOf(productPriceAccTo100Kg)));
        addedObject.setQty(String.valueOf(mProductQty));
        addedObject.setQtyInKg(Helper.formatStringUpTo2Precision(String.valueOf(totalProductKg)));
        addedObject.setTotalPrice(Helper.formatStringUpTo2Precision(String.valueOf(actualPurchaseAmt)));
        addedObject.setPurchaseId(mItemObject.getPurchaseId());
        mItemObject.setLocalSoldQtyInKg(String.valueOf(Float.parseFloat(mItemObject.getLocalSoldQtyInKg()) + totalProductKg));
        mEventCallback.onEvent(0, addedObject);
        dismiss();
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

    private void updateQtyTextView() {
        mTextViewQty.setText(String.valueOf(mProductQty));

        if (mSelectedUnit != null) {
            float totalProductKg = mProductQty * Float.parseFloat(mSelectedUnit.getKgValue());
            if (totalProductKg > mMaxQtyInKg) {
                mBaseActivity.showToast(mBaseActivity.getString(R.string.string_you_dont_have_enough_stock));
                return;
            }
            mTextViewPurchasedQtyInKg.setText(mBaseActivity.getString(R.string.string_max_purchase, Helper.formatStringUpTo2Precision("" + totalProductKg)));
        }
    }
}
