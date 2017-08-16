package ig.com.digitalmandi.fragment.supplier;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.supplier.SupplierProductModifyActivity;
import ig.com.digitalmandi.adapter.supplier.SupplierProductAdapter;
import ig.com.digitalmandi.base_package.BaseFragment;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderDeleteReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierProductListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierProductModifyReq;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierProductListRes;
import ig.com.digitalmandi.database.BaseContract;
import ig.com.digitalmandi.database.ProductContract;
import ig.com.digitalmandi.dialogs.MyAlertDialog;
import ig.com.digitalmandi.interfaces.OnAlertDialogCallBack;
import ig.com.digitalmandi.interfaces.OnItemClickListeners;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.MyPrefrences;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by Shivam.Garg on 12-10-2016.
 */

public class ProductFragment extends BaseFragment implements SearchView.OnQueryTextListener,BaseContract.OnInsertBulkDataSuccessFully, LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListeners {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerViewCustomer;
    @BindView(R.id.emptyTextView)
    TextView emptyView;
    public SupplierProductAdapter mAdapter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case SupplierProductModifyActivity.REQUEST_CODE_ADD_UPDATE_PRODUCT:
                    onFetchDataFromServer(false);
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_supplier_product_and_unit_and_purchase, null);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.supplier_product_menu, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.supplier_product_menu_search));
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.supplier_product_menu_add:
                Intent intent = new Intent(mHostActivity, SupplierProductModifyActivity.class);
                intent.putExtra(SupplierProductModifyActivity.PRODUCT_OBJECT_KEY_UPDATE, false);
                Utils.onActivityStartForResultInFragment(this, false, new int[]{}, intent, null, SupplierProductModifyActivity.REQUEST_CODE_ADD_UPDATE_PRODUCT);
                return true;

            case R.id.supplier_product_menu_refresh:
                onFetchDataFromServer(false);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new SupplierProductAdapter(dataList, mHostActivity, this);
        mRecyclerViewCustomer.setAdapter(mAdapter);
        mRecyclerViewCustomer.setHasFixedSize(true);
        emptyView.setText("No Product Found\nPlease Add New Product");
        getLoaderManager().initLoader(1, null, this);
    }


    private void onFetchDataFromServer(final boolean showDialog) {
        disableTouchEvent();
        SupplierProductListReq supplierProductReqModel = new SupplierProductListReq();
        supplierProductReqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mHostActivity));


        mHostActivity.apiEnqueueObject = RetrofitWebService.getInstance().getInterface().supplierProductList(supplierProductReqModel);
        mHostActivity.apiEnqueueObject.enqueue(new RetrofitCallBack<SupplierProductListRes>(mHostActivity, showDialog) {

            @Override
            public void yesCall(SupplierProductListRes response, ParentActivity weakRef) {
                stopLoading();
                if (VerifyResponse.isResponseOk(response,true)){
                    disableTouchEvent();
                    ProductContract productContract = new ProductContract(weakRef);
                    productContract.insertBulkData(response.getResult(), ProductContract.Product.CONTENT_URI, ProductFragment.this);
                } else
                    stopLoading();
            }

            @Override
            public void noCall(Throwable error) {
                stopLoading();
            }
        });
    }

    private void stopLoading() {
        enableTouchEvent();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        dataList.clear();

        if (TextUtils.isEmpty(newText)) {
            dataList.addAll(backUpList);
            mAdapter.notifyData(emptyView);
            return true;
        }

        for (int index = 0; index < backUpList.size(); index++) {
            SupplierProductListRes.ResultBean mCustomerBean = (SupplierProductListRes.ResultBean) backUpList.get(index);
            if (mCustomerBean.getProductName().toLowerCase().contains(newText.toLowerCase())) {
                dataList.add(mCustomerBean);
            }
        }
        mAdapter.notifyData(emptyView);
        return true;
    }



    @Override
    public void onInsertBulkDataSuccess() {
        new Handler().post(restartLoaderASAP);
        stopLoading();
        enableTouchEvent();
    }

    Runnable restartLoaderASAP = new Runnable() {
        @Override
        public void run() {
            getLoaderManager().restartLoader(1, null, ProductFragment.this);
        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(mHostActivity, ProductContract.Product.CONTENT_URI, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        dataList.clear();
        backUpList.clear();

        ProductContract prodContract = new ProductContract(mHostActivity);
        dataList.addAll(prodContract.getListOfObject(data));

        backUpList.addAll(dataList);
        mAdapter.notifyData(emptyView);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataList.clear();
        backUpList.clear();
        mAdapter.notifyData(emptyView);
    }

    @Override
    public void onItemDelete(View view, Object object) {


        final SupplierProductListRes.ResultBean product  = (SupplierProductListRes.ResultBean) object;

        MyAlertDialog.onShowAlertDialog(mHostActivity, "Continue, To Delete Product", "Continue", "Leave", true, new OnAlertDialogCallBack() {

            @Override
            public void onNegative(DialogInterface dialogInterface, int i) {

            }

            @Override
            public void onPositive(DialogInterface dialogInterface, int i) {

                SupplierOrderDeleteReq supplierOrderDeleteReq = new SupplierOrderDeleteReq();
                supplierOrderDeleteReq.setFlag(ConstantValues.PRODUCT_DELETE);
                supplierOrderDeleteReq.setId(product.getProductId());

                mHostActivity.apiEnqueueObject = RetrofitWebService.getInstance().getInterface().deleteProductUnit(supplierOrderDeleteReq);
                mHostActivity.apiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mHostActivity) {

                    @Override
                    public void yesCall(EmptyResponse response, ParentActivity weakRef) {
                        if (VerifyResponse.isResponseOk(response)) {
                            dataList.remove(product);
                            mAdapter.notifyData(emptyView);
                            onFetchDataFromServer(false);
                        } else
                            Toast.makeText(mHostActivity, "Sorry You can't delete this Product", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void noCall(Throwable error) {

                    }
                });

            }
        });
    }

    @Override
    public void onItemEdit(View view, Object object) {
        final SupplierProductListRes.ResultBean product   = (SupplierProductListRes.ResultBean) object;
        Intent intent = new Intent(mHostActivity, SupplierProductModifyActivity.class);
        intent.putExtra(SupplierProductModifyActivity.PRODUCT_OBJECT_KEY, product);
        intent.putExtra(SupplierProductModifyActivity.PRODUCT_OBJECT_KEY_UPDATE, true);
        Utils.onActivityStartForResultInFragment(this, false, new int[]{}, intent, null, SupplierProductModifyActivity.REQUEST_CODE_ADD_UPDATE_PRODUCT);
    }

    @Override
    public void onItemChangeStatus(View view, Object object) {
        final SupplierProductListRes.ResultBean product   = (SupplierProductListRes.ResultBean) object;
        final SupplierProductModifyReq reqModel           = new SupplierProductModifyReq();
        reqModel.setProductId(product.getProductId());
        reqModel.setProductStatus(product.getProductStatus().equalsIgnoreCase(ConstantValues.ENABLE) ? ConstantValues.DISABLE : ConstantValues.ENABLE);
        reqModel.setProductOperation(ConstantValues.DELETE);
        reqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mHostActivity));

        mHostActivity.apiEnqueueObject =  RetrofitWebService.getInstance().getInterface().modifiedProduct(reqModel);
        mHostActivity.apiEnqueueObject .enqueue(new RetrofitCallBack<EmptyResponse>(mHostActivity,true) {

            @Override
            public void yesCall(EmptyResponse response, ParentActivity weakRef) {
                if (VerifyResponse.isResponseOk(response)) {
                    product.setProductStatus(reqModel.getProductStatus());
                    mAdapter.notifyData(emptyView);
                    onFetchDataFromServer(false);
                }
                else
                    Toast.makeText(mHostActivity, R.string.please_try_again, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void noCall(Throwable error) {

            }
        });
    }
}
