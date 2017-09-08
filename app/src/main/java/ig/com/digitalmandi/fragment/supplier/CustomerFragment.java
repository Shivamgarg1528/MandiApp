package ig.com.digitalmandi.fragment.supplier;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.Comparator;
import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.seller.CustomerOrdersActivity;
import ig.com.digitalmandi.activity.seller.SupplierCustomerAddActivity;
import ig.com.digitalmandi.adapter.supplier.CustomerAdapter;
import ig.com.digitalmandi.bean.request.seller.CustomerResponse;
import ig.com.digitalmandi.dialog.CustomerDialog;
import ig.com.digitalmandi.fragment.ListBaseFragment;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.AppSharedPrefs;
import ig.com.digitalmandi.util.Helper;
import ig.com.digitalmandi.util.ModifyPreference;
import pub.devrel.easypermissions.EasyPermissions;

public class CustomerFragment extends ListBaseFragment<CustomerResponse.Customer> implements EasyPermissions.PermissionCallbacks {

    private CustomerDialog mCustomerDialog;

    @Override
    protected CustomerResponse getResponse() {
        return AppSharedPrefs.getInstance(mBaseActivity).getSellerCustomers();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CustomerAdapter(mBaseActivity, mDataList, this);
    }

    @Override
    protected int getEmptyTextStringId() {
        return R.string.string_no_customer_found_please_add_new_customer;
    }

    @Override
    protected void fetchData(boolean pRefresh) {
        ModifyPreference modifyPreference = new ModifyPreference(mBaseActivity, this);
        modifyPreference.addOrUpdateSellerCustomers();
    }

    @Override
    protected Intent getRequestedIntent() {
        return new Intent(mBaseActivity, SupplierCustomerAddActivity.class);
    }

    @Override
    protected Comparator getComparator(int pComparatorType) {
        switch (pComparatorType) {
            case AppConstant.COMPARATOR_ALPHA: {
                return new Comparator<CustomerResponse.Customer>() {
                    public int compare(CustomerResponse.Customer left, CustomerResponse.Customer right) {
                        return String.CASE_INSENSITIVE_ORDER.compare(left.getUserName(), right.getUserName());
                    }
                };
            }
            case AppConstant.COMPARATOR_PHONE: {
                return new Comparator<CustomerResponse.Customer>() {
                    public int compare(CustomerResponse.Customer left, CustomerResponse.Customer right) {
                        return String.CASE_INSENSITIVE_ORDER.compare(left.getUserMobileNo(), right.getUserMobileNo());
                    }
                };
            }
        }
        return null;
    }

    @Override
    public boolean onQueryTextChange(String pNewText) {
        mDataList.clear();
        for (CustomerResponse.Customer data : mBackUpList) {
            if (data.getUserFirmName().toLowerCase().contains(pNewText.toLowerCase()) || data.getUserName().toLowerCase().contains(pNewText.toLowerCase())) {
                mDataList.add(data);
            }
        }
        notifyAdapterAndView();
        return true;
    }

    @Override
    public void onEvent(int pOperationType, CustomerResponse.Customer pCustomer) {
        switch (pOperationType) {

            case AppConstant.OPERATION_CUSTOMER_OPEN: {
                mCustomerDialog = new CustomerDialog(mBaseActivity, this);
                mCustomerDialog.show(pCustomer);
                break;
            }

            case AppConstant.OPERATION_CUSTOMER_ORDERS: {
                Intent intent = new Intent(mBaseActivity, CustomerOrdersActivity.class);
                intent.putExtra(AppConstant.KEY_OBJECT, pCustomer);
                Helper.onActivityStart(mBaseActivity, false, null, intent, null);
                break;
            }

            case AppConstant.OPERATION_CUSTOMER_CALL: {
                if (EasyPermissions.hasPermissions(mBaseActivity, Manifest.permission.CALL_PHONE)) {
                    mCustomerDialog.makeCall();
                } else {
                    EasyPermissions.requestPermissions(this, getString(R.string.string_allow_customer_to_make_a_call), AppConstant.REQUEST_CODE_CALL_PERMISSION, Manifest.permission.CALL_PHONE);
                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == AppConstant.REQUEST_CODE_CALL_PERMISSION && perms.size() > 0) {
            mCustomerDialog.makeCall();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
