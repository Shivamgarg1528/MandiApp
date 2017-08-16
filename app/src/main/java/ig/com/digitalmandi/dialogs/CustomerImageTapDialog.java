package ig.com.digitalmandi.dialogs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.supplier.SupplierCustomerOrderActivity;
import ig.com.digitalmandi.activity.supplier.SupplierHomeActivity;
import ig.com.digitalmandi.base_package.BaseDialog;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierCustomerListRes;
import ig.com.digitalmandi.utils.Utils;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Shivam.Garg on 12-10-2016.
 */

public class CustomerImageTapDialog extends BaseDialog {

    @BindView(R.id.mTextViewCustomerName)
    AppCompatTextView mTextViewCustomerName;
    @BindView(R.id.mImageViewUserInfo)
    AppCompatImageView mImageViewUserInfo;
    @BindView(R.id.mImageViewCall)
    AppCompatImageView mImageViewCall;
    @BindView(R.id.mImageViewInfo)
    AppCompatImageView mImageViewInfo;
    private SupplierCustomerListRes.ResultBean tappedCustomer;
    public static int CALL_PERM_CODE = 102;

    public CustomerImageTapDialog(Context context, boolean isOutSideTouch, boolean isCancelable, int layoutId) {
        super(context, isOutSideTouch, isCancelable, layoutId);
    }

    public void show(SupplierCustomerListRes.ResultBean tappedCustomer) {
        this.tappedCustomer = tappedCustomer;
        show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_customer_image_info);
        ButterKnife.bind(this);
        mTextViewCustomerName.setText(tappedCustomer.getUserName());
        Picasso.with(mContext).load("http://www.aiob.in/shivam/product/201610111330154088.png").into(mImageViewUserInfo);
    }

    public void onDialNumber() {
        Intent dialIntent = new Intent(Intent.ACTION_CALL);
        dialIntent.setData(Uri.parse("tel:".concat(tappedCustomer.getUserMobileNo())));
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
           return;
        }
        dismiss();
        mContext.startActivity(dialIntent);
    }

    @OnClick({R.id.mImageViewInfo, R.id.mImageViewCall})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mImageViewInfo:
                dismiss();
                Intent customerInfoIntent = new Intent(mContext, SupplierCustomerOrderActivity.class);
                customerInfoIntent.putExtra(SupplierCustomerOrderActivity.CUSTOMER_OBJECT_KEY,tappedCustomer);
                Utils.onActivityStart((ParentActivity)mContext,false,new int[]{},customerInfoIntent,null);
                break;

            case R.id.mImageViewCall:
                if(EasyPermissions.hasPermissions(mContext, Manifest.permission.CALL_PHONE))
                    onDialNumber();
                else
                {
                  EasyPermissions.requestPermissions((SupplierHomeActivity)mContext,"Allow Customer To Make Call",CALL_PERM_CODE,Manifest.permission.CALL_PHONE);
                }
                break;
        }
    }
}
