package ig.com.digitalmandi.fragment.supplier;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import java.util.Comparator;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.supplier.SellerUnitModifyActivity;
import ig.com.digitalmandi.adapter.supplier.SupplierUnitAdapter;
import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierItemDeleteRequest;
import ig.com.digitalmandi.beans.request.supplier.SupplierUnitModifyRequest;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.beans.response.supplier.SellerUnitList;
import ig.com.digitalmandi.database.ModifyPreference;
import ig.com.digitalmandi.dialogs.MyAlertDialog;
import ig.com.digitalmandi.fragment.ListBaseFragment;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.utils.AppConstant;
import ig.com.digitalmandi.utils.AppSharedPrefs;
import ig.com.digitalmandi.utils.Utils;

public class UnitFragment extends ListBaseFragment<SellerUnitList.Unit> {

    @Override
    protected SellerUnitList getResponse() {
        return AppSharedPrefs.getInstance(mBaseActivity).getSellerUnits();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new SupplierUnitAdapter(mDataList, mBaseActivity, this);
    }

    @Override
    protected int getEmptyTextStringId() {
        return R.string.string_no_unit_found_please_add_new_unit;
    }

    @Override
    protected void fetchData() {
        ModifyPreference modifyPreference = new ModifyPreference(mBaseActivity, this);
        modifyPreference.addOrUpdateSellerUnits();
    }

    @Override
    protected Intent getRequestedIntent() {
        return new Intent(mBaseActivity, SellerUnitModifyActivity.class);
    }

    @Override
    protected Comparator getComparator(int pComparatorType) {
        switch (pComparatorType) {
            case AppConstant.COMPARATOR_ALPHA: {
                return new Comparator<SellerUnitList.Unit>() {
                    public int compare(SellerUnitList.Unit left, SellerUnitList.Unit right) {
                        return String.CASE_INSENSITIVE_ORDER.compare(left.getUnitName(), right.getUnitName());
                    }
                };
            }
        }
        return null;
    }

    @Override
    public boolean onQueryTextChange(String pNewText) {
        mDataList.clear();
        for (SellerUnitList.Unit data : mBackUpList) {
            if (data.getUnitName().toLowerCase().contains(pNewText.toLowerCase())) {
                mDataList.add(data);
            }
        }
        notifyAdapterAndView();
        return true;
    }

    @Override
    public void onEvent(int pOperationType, final SellerUnitList.Unit pUnit) {
        switch (pOperationType) {

            case AppConstant.OPERATION_EDIT: {
                Intent intent = new Intent(mBaseActivity, SellerUnitModifyActivity.class);
                intent.putExtra(AppConstant.KEY_OBJECT, pUnit);
                Utils.onActivityStartForResultInFragment(this, false, null, intent, null, AppConstant.REQUEST_CODE_EDIT);
                break;
            }

            case AppConstant.OPERATION_DELETE: {
                MyAlertDialog.onShowAlertDialog(mBaseActivity, getString(R.string.string_continue_to_delete_unit), true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {

                            SupplierItemDeleteRequest supplierItemDeleteRequest = new SupplierItemDeleteRequest();
                            supplierItemDeleteRequest.setFlag(AppConstant.DELETE_UNIT);
                            supplierItemDeleteRequest.setId(pUnit.getUnitId());

                            mBaseActivity.mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().deleteProductUnit(supplierItemDeleteRequest);
                            mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mBaseActivity) {
                                @Override
                                public void onSuccess(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                                    if (ResponseVerification.isResponseOk(pResponse)) {
                                        mDataList.remove(pUnit);
                                        mBackUpList.remove(pUnit);
                                        fetchData();
                                    } else
                                        mBaseActivity.showToast(getString(R.string.string_sorry_you_cant_delete_this_unit));
                                }

                                @Override
                                public void onFailure(String pErrorMsg) {

                                }
                            });
                        }
                    }
                });
                break;
            }

            case AppConstant.OPERATION_STATUS_MODIFY: {
                final SupplierUnitModifyRequest supplierUnitModifyRequest = new SupplierUnitModifyRequest();
                supplierUnitModifyRequest.setUnitId(pUnit.getUnitId());
                supplierUnitModifyRequest.setSellerId(mLoginUser.getSellerId());
                supplierUnitModifyRequest.setUnitStatus(AppConstant.ENABLE.equalsIgnoreCase(pUnit.getUnitStatus()) ? AppConstant.DISABLE : AppConstant.ENABLE);
                supplierUnitModifyRequest.setOperation(AppConstant.DELETE);

                mBaseActivity.mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().modifiedUnit(supplierUnitModifyRequest);
                mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mBaseActivity, true) {

                    @Override
                    public void onSuccess(EmptyResponse pResponse, BaseActivity pBaseActivity) {

                        if (ResponseVerification.isResponseOk(pResponse)) {
                            pUnit.setUnitStatus(supplierUnitModifyRequest.getUnitStatus());
                            fetchData();
                        } else {
                            mBaseActivity.showToast(getString(R.string.please_try_again));
                        }
                    }

                    @Override
                    public void onFailure(String pErrorMsg) {
                        mBaseActivity.showToast(getString(R.string.please_try_again));
                    }
                });
                break;
            }
        }
    }
}
