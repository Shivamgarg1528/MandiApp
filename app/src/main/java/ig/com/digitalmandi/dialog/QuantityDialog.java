package ig.com.digitalmandi.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class QuantityDialog extends BaseDialog {

    private final int mDefaultValue;
    private NumberPicker mNumberPickerQty;

    public QuantityDialog(BaseActivity pBaseActivity, EventCallback pEventCallback, int mDefaultValue) {
        super(pBaseActivity, pEventCallback);
        this.mDefaultValue = mDefaultValue;
    }

    @Override
    public void onClick(View view) {
        Helper.hideSoftKeyBoard(mBaseActivity, mNumberPickerQty);
        mEventCallback.onEvent(AppConstant.OPERATION_NONE, mNumberPickerQty.getValue());
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_qty_change);
        findViewById(R.id.layout_dialog_qty_change_btn_set).setOnClickListener(this);

        mNumberPickerQty = findViewById(R.id.layout_dialog_qty_change_number_picker);
        mNumberPickerQty.setMinValue(1);
        mNumberPickerQty.setMaxValue(1000);
        mNumberPickerQty.setValue(mDefaultValue);
        mNumberPickerQty.setWrapSelectorWheel(true);
        mNumberPickerQty.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                numberPicker.setValue(i1);
            }
        });
    }
}
