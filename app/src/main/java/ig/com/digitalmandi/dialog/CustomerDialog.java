package ig.com.digitalmandi.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.request.seller.SellerCustomerList;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;


public class CustomerDialog extends BaseDialog implements View.OnClickListener {

    private final EventCallback mEventCallback;
    private SellerCustomerList.Customer mCustomer;

    public CustomerDialog(BaseActivity pBaseActivity, EventCallback pEventCallback) {
        super(pBaseActivity);
        this.mEventCallback = pEventCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_customer_info);

        findViewById(R.id.dialog_layout_customer_info_iv_info).setOnClickListener(this);
        findViewById(R.id.dialog_layout_customer_info_iv_call).setOnClickListener(this);

        View customerImage = findViewById(R.id.dialog_layout_customer_info_iv_customer_image);
        Helper.setImage(mBaseActivity, "http://via.placeholder.com/350x150", customerImage);

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
                mEventCallback.onEvent(AppConstant.OPERATION_CUSTOMER_ORDERS, mCustomer);
                break;

            case R.id.dialog_layout_customer_info_iv_call:
                mEventCallback.onEvent(AppConstant.OPERATION_CUSTOMER_CALL, mCustomer);
                break;
        }
    }
}
