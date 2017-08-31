package ig.com.digitalmandi.dialog;

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
import ig.com.digitalmandi.base.BaseDialog;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderAddReq;
import ig.com.digitalmandi.bean.response.seller.SellerUnitList;
import ig.com.digitalmandi.bean.response.seller.SupplierPurchaseListRes;
import ig.com.digitalmandi.callback.OnItemAddedCallBack;
import ig.com.digitalmandi.util.ChangeSpinnerItemBg;
import ig.com.digitalmandi.util.CheckForFloat;
import ig.com.digitalmandi.util.Utils;

/**
 * Created by shiva on 10/31/2016.
 */

public class AddItemDialog extends BaseDialog implements AdapterView.OnItemSelectedListener {

    private final int QTY_MAX = 1000;
    private final int QTY_MIN = 1;
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
    List<SellerUnitList.Unit> unitList;
    private SupplierPurchaseListRes.ResultBean selectedObj;
    private SellerUnitList.Unit unit;
    private String[] unitArray;
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

        ChangeSpinnerItemBg.onChangeSpinnerBgWhite(mBaseActivity, unitArray, mSpinnerUnitListPurchase);
        ChangeSpinnerItemBg.onChangeSpinnerBgWhite(mBaseActivity, mBaseActivity.mResources.getStringArray(R.array.kgPrice), mSpinnerKgPrice);

        mSpinnerUnitListPurchase.setOnItemSelectedListener(this);
        mSpinnerKgPrice         .setOnItemSelectedListener(this);

        mTextViewMaxQtyInKg.setText("Max Qty KG(" + Utils.formatStringUpTo2Precision(selectedObj.onGetLeftQty()) + ")");
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

            case R.id.increaseQtyBtn:
                increaseQtyByOne();
                break;

            case R.id.saveBtnPurchase:
                onSavePurchasedItem();
                break;
        }
    }

    public void show(SupplierPurchaseListRes.ResultBean selectedObject, String[] unitArray, List<SellerUnitList.Unit> unitList, OnItemAddedCallBack<SupplierOrderAddReq.OrderDetailsBean> onItemAddedCallBack) {
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
            Toast.makeText(mBaseActivity, "Please Enter Product Price", Toast.LENGTH_SHORT).show();
            return;
        }

        float totalProductKg = productQty * unitValueFloat; // 25 * 42.5 = 1062.50
        float productPrice = Float.parseFloat(mEditProductPrice.getText().toString()); // 2250 //100kg
        float productPriceAccTo40Kg = 0.0f;
        float productPriceAccTo100Kg = 0.0f;

        if (totalProductKg > maxQtyInKg) {
            Toast.makeText(mBaseActivity, "You Don't Have Enough Stock Please Reduce Qty Or Unit Value", Toast.LENGTH_SHORT).show();
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
        addedObject.setUnitValue(Utils.formatStringUpTo2Precision(String.valueOf(unit.getKgValue())));
        addedObject.setProductId(selectedObj.getProductId());
        addedObject.setProductName(selectedObj.getProductName());
        addedObject.setPrice(Utils.formatStringUpTo2Precision(String.valueOf(productPriceAccTo100Kg)));
        addedObject.setQty(String.valueOf(productQty));
        addedObject.setQtyInKg(Utils.formatStringUpTo2Precision(String.valueOf(totalProductKg)));
        addedObject.setTotalPrice(Utils.formatStringUpTo2Precision(String.valueOf(actualPurchaseAmt)));
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
