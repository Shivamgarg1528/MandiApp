package ig.com.digitalmandi.dialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.base_package.BaseDialog;
import ig.com.digitalmandi.beans.request.supplier.SellerCustomerList;
import ig.com.digitalmandi.interfaces.EventListener;
import ig.com.digitalmandi.utils.AppConstant;
import ig.com.digitalmandi.utils.Utils;


public class CustomerDialog extends BaseDialog implements View.OnClickListener {

    private final EventListener mEventListener;
    private SellerCustomerList.Customer mCustomer;

    public CustomerDialog(BaseActivity pBaseActivity, EventListener pEventListener) {
        super(pBaseActivity);
        this.mEventListener = pEventListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_customer_info);

        findViewById(R.id.dialog_layout_customer_info_iv_info).setOnClickListener(this);
        findViewById(R.id.dialog_layout_customer_info_iv_call).setOnClickListener(this);

        View customerImage = findViewById(R.id.dialog_layout_customer_info_iv_customer_image);
        Utils.setImage(mBaseActivity, "http://via.placeholder.com/350x150", customerImage);

        TextView textViewCustomerName = (TextView) findViewById(R.id.dialog_layout_customer_info_tv_customer_name);
        textViewCustomerName.setText(mCustomer.getUserName() + "\n" + mCustomer.getUserMobileNo());
    }

    public void show(SellerCustomerList.Customer pCustomer) {
        this.mCustomer = pCustomer;
        show();
    }

    public void makeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:".concat(mCustomer.getUserMobileNo())));
        mBaseActivity.startActivity(intent);
        dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_layout_customer_info_iv_info:
                dismiss();
                mEventListener.onEvent(AppConstant.OPERATION_EDIT, mCustomer);
                break;

            case R.id.dialog_layout_customer_info_iv_call:
                mEventListener.onEvent(AppConstant.OPERATION_CUSTOMER_CALL, mCustomer);
                break;
        }
    }
}
