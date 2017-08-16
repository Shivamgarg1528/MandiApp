package ig.com.digitalmandi.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.NumberPicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.BaseDialog;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by shiva on 10/22/2016.
 */

public class QtyPickerDialog extends BaseDialog {

    @BindView(R.id.mNumberPickerQty)
    NumberPicker mNumberPickerQty;
    @BindView(R.id.mSpinnerMultiplier)
    AppCompatSpinner mSpinnerMultiplier;
    @BindView(R.id.mButtonSave)
    AppCompatButton mButtonSave;
    private OnQtySelected listener;

    @OnClick(R.id.mButtonSave)
    public void onClick() {
        Utils.onHideSoftKeyBoard(mContext,mNumberPickerQty);
        int qty = Integer.parseInt((String) mSpinnerMultiplier.getSelectedItem()) * mNumberPickerQty.getValue();
        listener.onQtySelectedCallBack(qty);
        dismiss();
    }

    public interface OnQtySelected {
        public void onQtySelectedCallBack(int qty);
    }

    public QtyPickerDialog(Context context, boolean isOutSideTouch, boolean isCancelable, int layoutId, OnQtySelected nQtySelected) {
        super(context, isOutSideTouch, isCancelable, layoutId);
        this.listener = nQtySelected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_qty_selected_layout);
        ButterKnife.bind(this);
        mNumberPickerQty.setMinValue(1);
        mNumberPickerQty.setMaxValue(100);
        mNumberPickerQty.setValue(40);
        mNumberPickerQty.setWrapSelectorWheel(true);
        mNumberPickerQty.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                numberPicker.setValue(i1);
            }
        });
    }
}
