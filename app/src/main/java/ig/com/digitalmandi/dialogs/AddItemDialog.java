package ig.com.digitalmandi.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.BaseDialog;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderAddReq;
import ig.com.digitalmandi.beans.response.supplier.SupplierPurchaseListRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierUnitListRes;
import ig.com.digitalmandi.interfaces.OnItemAddedCallBack;
import ig.com.digitalmandi.utils.ChangeSpinnerItemBg;
import ig.com.digitalmandi.utils.CheckForFloat;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by shiva on 10/31/2016.
 */

public class AddItemDialog extends BaseDialog implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.mSpinnerUnitListPurchase)
    AppCompatSpinner mSpinnerUnitListPurchase;
    @BindView(R.id.decreaseQtyBtn)
    AppCompatImageView decreaseQtyBtn;
    @BindView(R.id.qtyTextView)
    AppCompatTextView qtyTextView;
    @BindView(R.id.increaseQtyBtn)
    AppCompatImageView increaseQtyBtn;
    @BindView(R.id.mEditProductPrice)
    AppCompatEditText mEditProductPrice;
    @BindView(R.id.mSpinnerKgPrice)
    AppCompatSpinner mSpinnerKgPrice;
    @BindView(R.id.saveBtnPurchase)
    AppCompatButton saveBtnPurchase;
    @BindView(R.id.mTextViewProductName)
    AppCompatTextView mTextViewProductName;
    @BindView(R.id.mTextViewMaxQtyInKg)
    AppCompatTextView mTextViewMaxQtyInKg;
    private SupplierPurchaseListRes.ResultBean selectedObj;
    private SupplierUnitListRes.ResultBean unit;
    List<SupplierUnitListRes.ResultBean> unitList;
    private String[] unitArray;

    private final int QTY_MAX = 1000;
    private final int QTY_MIN = 1;
    private int productQty = 1;
    private float unitValueFloat = 0.0f, actualPurchaseAmt = 0.0f;
    private boolean isProductPriceAccTo40Kg = false;
    private OnItemAddedCallBack<SupplierOrderAddReq.OrderDetailsBean> onItemAddedCallBack;
    private float maxQtyInKg = 0.00f;

    public AddItemDialog(Context context, boolean isOutSideTouch, boolean isCancelable, int layoutId) {
        super(context, isOutSideTouch, isCancelable, layoutId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_item_into_cart);
        ButterKnife.bind(this);

        ChangeSpinnerItemBg.onChangeSpinnerBgWhite(mRunningActivity, unitArray, mSpinnerUnitListPurchase);
        ChangeSpinnerItemBg.onChangeSpinnerBgWhite(mRunningActivity, mRunningActivity.mResources.getStringArray(R.array.kgPrice), mSpinnerKgPrice);

        mSpinnerUnitListPurchase.setOnItemSelectedListener(this);
        mSpinnerKgPrice         .setOnItemSelectedListener(this);

        mTextViewMaxQtyInKg.setText("Max Qty KG(" + Utils.onStringFormat(selectedObj.onGetLeftQty()) + ")");
        mTextViewProductName.setText(selectedObj.getProductName());

        try {
            maxQtyInKg = Float.parseFloat(selectedObj.onGetLeftQty());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @OnClick({R.id.decreaseQtyBtn, R.id.qtyTextView, R.id.increaseQtyBtn, R.id.saveBtnPurchase})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.decreaseQtyBtn:
                decreaseQtyByOne();
                break;
            case R.id.qtyTextView:

                Utils.onHideSoftKeyBoard(mContext, mEditProductPrice);
                QtyPickerDialog qtyPickerDialog = new QtyPickerDialog(mContext, true, true, R.layout.dialog_qty_selected_layout, new QtyPickerDialog.OnQtySelected() {

                    @Override
                    public void onQtySelectedCallBack(int qty) {
                        productQty = qty;
                        updateQtyTextView();
                    }
                });
                qtyPickerDialog.show();

                break;

            case R.id.increaseQtyBtn:
                increaseQtyByOne();
                break;

            case R.id.saveBtnPurchase:
                onSavePurchasedItem();
                break;
        }
    }

    public void show(SupplierPurchaseListRes.ResultBean selectedObject, String[] unitArray, List<SupplierUnitListRes.ResultBean> unitList, OnItemAddedCallBack<SupplierOrderAddReq.OrderDetailsBean> onItemAddedCallBack) {
        this.selectedObj = selectedObject;
        this.unitArray = unitArray;
        this.unitList = unitList;
        this.onItemAddedCallBack = onItemAddedCallBack;
        show();
    }


    private void updateQtyTextView() {
        qtyTextView.setText(String.valueOf(productQty));
    }

    private void onSavePurchasedItem() {


        if (!CheckForFloat.onCheckFloat(mEditProductPrice.getText().toString())) {
            Toast.makeText(mContext, "Please Enter Product Price", Toast.LENGTH_SHORT).show();
            return;
        }

        float totalProductKg = productQty * unitValueFloat; // 25 * 42.5 = 1062.50
        float productPrice = Float.parseFloat(mEditProductPrice.getText().toString()); // 2250 //100kg
        float productPriceAccTo40Kg = 0.0f;
        float productPriceAccTo100Kg = 0.0f;

        if (totalProductKg > maxQtyInKg) {
            Toast.makeText(mContext, "You Don't Have Enough Stock Please Reduce Qty Or Unit Value", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isProductPriceAccTo40Kg) {
            productPriceAccTo40Kg = productPrice;
            productPriceAccTo100Kg = productPriceAccTo40Kg * 2.5f;
        } else {
            productPriceAccTo100Kg = productPrice;
            productPriceAccTo40Kg = productPriceAccTo100Kg / 2.5f;
        }

        actualPurchaseAmt = totalProductKg * productPriceAccTo100Kg * .01f;

        SupplierOrderAddReq.OrderDetailsBean addedObject = new SupplierOrderAddReq.OrderDetailsBean();
        addedObject.setUnitId(unit.getUnitId());
        addedObject.setUnitValue(Utils.onStringFormat(String.valueOf(unit.getKgValue())));
        addedObject.setProductId(selectedObj.getProductId());
        addedObject.setProductName(selectedObj.getProductName());
        addedObject.setPrice(Utils.onStringFormat(String.valueOf(productPriceAccTo100Kg)));
        addedObject.setQty(String.valueOf(productQty));
        addedObject.setQtyInKg(Utils.onStringFormat(String.valueOf(totalProductKg)));
        addedObject.setTotalPrice(Utils.onStringFormat(String.valueOf(actualPurchaseAmt)));
        addedObject.setPurchaseId(selectedObj.getPurchaseId());
        selectedObj.setLocalSoldQty(String.valueOf(Float.parseFloat(selectedObj.getLocalSoldQty()) + totalProductKg));
        onItemAddedCallBack.onItemAddedCallBacks(addedObject);
        dismiss();
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

            case R.id.mSpinnerKgPrice:
                try {

                    if (Float.parseFloat((String) adapterView.getSelectedItem()) == 40)
                        isProductPriceAccTo40Kg = true;
                    else
                        isProductPriceAccTo40Kg = false;

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
