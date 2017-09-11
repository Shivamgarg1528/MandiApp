package ig.com.digitalmandi.fragment.supplier;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import java.util.Comparator;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.activity.seller.SupplierProductModifyActivity;
import ig.com.digitalmandi.adapter.supplier.ProductAdapter;
import ig.com.digitalmandi.bean.AbstractResponse;
import ig.com.digitalmandi.bean.request.seller.ItemDeleteRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierProductModifyRequest;
import ig.com.digitalmandi.bean.response.EmptyResponse;
import ig.com.digitalmandi.bean.response.seller.ProductResponse;
import ig.com.digitalmandi.dialog.ConfirmDialog;
import ig.com.digitalmandi.fragment.ListBaseFragment;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.AppSharedPrefs;
import ig.com.digitalmandi.util.Helper;
import ig.com.digitalmandi.util.ModifyPreference;

public class ProductFragment extends ListBaseFragment<ProductResponse.Product> {

    @Override
    protected AbstractResponse getResponse() {
        return AppSharedPrefs.getInstance(mBaseActivity).getSellerProducts();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ProductAdapter(mBaseActivity, mDataList, this);
    }

    @Override
    protected int getEmptyTextStringId() {
        return R.string.string_no_product_found_please_add_new_product;
    }

    @Override
    protected void fetchData(boolean pRefresh) {
        ModifyPreference modifyPreference = new ModifyPreference(mBaseActivity, this);
        modifyPreference.addOrUpdateSellerProducts();
    }

    @Override
    protected Intent getRequestedIntent() {
        return new Intent(mBaseActivity, SupplierProductModifyActivity.class);
    }

    @Override
    protected Comparator getComparator(int pComparatorType) {
        switch (pComparatorType) {
            case AppConstant.COMPARATOR_ALPHA: {
                return new Comparator<ProductResponse.Product>() {
                    public int compare(ProductResponse.Product left, ProductResponse.Product right) {
                        return String.CASE_INSENSITIVE_ORDER.compare(left.getProductName(), right.getProductName());
                    }
                };
            }
        }
        return null;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mDataList.clear();
        for (ProductResponse.Product data : mBackUpList) {
            if (data.getProductName().toLowerCase().contains(newText.toLowerCase())) {
                mDataList.add(data);
            }
        }
        notifyAdapterAndView();
        return true;
    }

    @Override
    public void onEvent(int pOperationType, final ProductResponse.Product pProduct) {

        switch (pOperationType) {

            case AppConstant.OPERATION_EDIT: {
                Intent intent = new Intent(mBaseActivity, SupplierProductModifyActivity.class);
                intent.putExtra(AppConstant.KEY_OBJECT, pProduct);
                Helper.onActivityStartForResultInFragment(this, false, null, intent, null, AppConstant.REQUEST_CODE_EDIT);
                break;
            }

            case AppConstant.OPERATION_DELETE: {
                ConfirmDialog.show(mBaseActivity, getString(R.string.string_continue_to_delete_product), true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {

                            ItemDeleteRequest itemDeleteRequest = new ItemDeleteRequest();
                            itemDeleteRequest.setFlag(AppConstant.DELETE_PRODUCT);
                            itemDeleteRequest.setId(pProduct.getProductId());

                            mBaseActivity.mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().deleteProductUnit(itemDeleteRequest);
                            mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mBaseActivity) {
                                @Override
                                public void onResponse(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                                    if (ResponseVerification.isResponseOk(pResponse)) {
                                        mDataList.remove(pProduct);
                                        mBackUpList.remove(pProduct);
                                        fetchData(true);
                                    } else
                                        mBaseActivity.showToast(getString(R.string.string_sorry_you_cant_delete_this_product));
                                }

                            });
                        }
                    }
                });
                break;
            }

            case AppConstant.OPERATION_STATUS_MODIFY: {
                final SupplierProductModifyRequest supplierProductModifyRequest = new SupplierProductModifyRequest();
                supplierProductModifyRequest.setProductId(pProduct.getProductId());
                supplierProductModifyRequest.setSellerId(mLoginUser.getSellerId());
                supplierProductModifyRequest.setProductStatus(AppConstant.ENABLE.equalsIgnoreCase(pProduct.getProductStatus()) ? AppConstant.DISABLE : AppConstant.ENABLE);
                supplierProductModifyRequest.setProductOperation(AppConstant.DELETE);

                mBaseActivity.mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().modifiedProduct(supplierProductModifyRequest);
                mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mBaseActivity) {
                    @Override
                    public void onResponse(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                        if (ResponseVerification.isResponseOk(pResponse)) {
                            pProduct.setProductStatus(supplierProductModifyRequest.getProductStatus());
                            fetchData(true);
                        } else {
                            mBaseActivity.showToast(getString(R.string.please_try_again));
                        }
                    }
                });
                break;
            }
        }
    }
}
