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
import ig.com.digitalmandi.activity.supplier.SupplierUnitModifyActivity;
import ig.com.digitalmandi.adapter.supplier.SupplierUnitAdapter;
import ig.com.digitalmandi.base_package.BaseFragment;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderDeleteReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierUnitListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierUnitModifyReq;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierUnitListRes;
import ig.com.digitalmandi.database.BaseContract;
import ig.com.digitalmandi.database.UnitContract;
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

public class UnitFragment extends BaseFragment implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor>, BaseContract.OnInsertBulkDataSuccessFully, OnItemClickListeners {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerViewCustomer;
    @BindView(R.id.emptyTextView)
    TextView emptyView;

    public SupplierUnitAdapter mAdapter;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SupplierUnitModifyActivity.REQUEST_CODE_ADD_UPDATE_UNIT:
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
        inflater.inflate(R.menu.supplier_unit_menu, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.supplier_unit_menu_search));
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.supplier_unit_menu_add:
                Intent intent = new Intent(mHostActivity, SupplierUnitModifyActivity.class);
                intent.putExtra(SupplierUnitModifyActivity.UNIT_OBJECT_KEY_UPDATE, false);
                Utils.onActivityStartForResultInFragment(this, false, new int[]{}, intent, null, SupplierUnitModifyActivity.REQUEST_CODE_ADD_UPDATE_UNIT);
                return true;

            case R.id.supplier_unit_menu_refresh:
                onFetchDataFromServer(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new SupplierUnitAdapter(dataList, mHostActivity, this);
        mRecyclerViewCustomer.setAdapter(mAdapter);
        mRecyclerViewCustomer.setHasFixedSize(true);
        emptyView.setText("No Unit Found\nPlease Add New Unit");
        getLoaderManager().initLoader(1, null, this);
    }

    Runnable restartLoaderASAP = new Runnable() {
        @Override
        public void run() {
            getLoaderManager().restartLoader(1, null, UnitFragment.this);
        }
    };


    private void onFetchDataFromServer(final boolean showDialog) {

        disableTouchEvent();
        SupplierUnitListReq supplierUnitReqModel = new SupplierUnitListReq();
        supplierUnitReqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mHostActivity));

        mHostActivity.apiEnqueueObject = RetrofitWebService.getInstance().getInterface().supplierUnitList(supplierUnitReqModel);
        mHostActivity.apiEnqueueObject.enqueue(new RetrofitCallBack<SupplierUnitListRes>(mHostActivity, showDialog) {

            @Override
            public void yesCall(SupplierUnitListRes response, ParentActivity weakRef) {
                enableTouchEvent();
                if (VerifyResponse.isResponseOk(response, true)) {
                    UnitContract unitContract = new UnitContract(weakRef);
                    unitContract.insertBulkData(response.getResult(), UnitContract.Unit.CONTENT_URI, UnitFragment.this);
                }
            }

            @Override
            public void noCall(Throwable error) {
                enableTouchEvent();
            }

        });
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
            SupplierUnitListRes.ResultBean mCustomerBean = (SupplierUnitListRes.ResultBean) backUpList.get(index);
            if (mCustomerBean.getUnitName().toLowerCase().contains(newText.toLowerCase())) {
                dataList.add(mCustomerBean);
            }
        }
        mAdapter.notifyData(emptyView);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(mHostActivity, UnitContract.Unit.CONTENT_URI, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        dataList.clear();
        backUpList.clear();

        UnitContract unitContract = new UnitContract(mHostActivity);
        dataList.addAll(unitContract.getListOfObject(data));

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
    public void onInsertBulkDataSuccess() {
        new Handler().post(restartLoaderASAP);
        enableTouchEvent();
    }

    @Override
    public void onItemDelete(View view, Object object) {

        final SupplierUnitListRes.ResultBean unit  = (SupplierUnitListRes.ResultBean) object;

        MyAlertDialog.onShowAlertDialog(mHostActivity, "Continue, To Delete Unit", "Continue", "Leave", true, new OnAlertDialogCallBack() {

            @Override
            public void onNegative(DialogInterface dialogInterface, int i) {

            }

            @Override
            public void onPositive(DialogInterface dialogInterface, int i) {

                SupplierOrderDeleteReq supplierOrderDeleteReq = new SupplierOrderDeleteReq();
                supplierOrderDeleteReq.setFlag(ConstantValues.UNIT_DELETE);
                supplierOrderDeleteReq.setId(unit.getUnitId());

                mHostActivity.apiEnqueueObject = RetrofitWebService.getInstance().getInterface().deleteProductUnit(supplierOrderDeleteReq);
                mHostActivity.apiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mHostActivity) {

                    @Override
                    public void yesCall(EmptyResponse response, ParentActivity weakRef) {
                        if (VerifyResponse.isResponseOk(response)) {
                            dataList.remove(unit);
                            mAdapter.notifyData(emptyView);
                            onFetchDataFromServer(false);
                        } else
                            Toast.makeText(mHostActivity, "Sorry You can't delete this unit", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(mHostActivity, SupplierUnitModifyActivity.class);
        intent.putExtra(SupplierUnitModifyActivity.UNIT_OBJECT_KEY, (SupplierUnitListRes.ResultBean) object);
        intent.putExtra(SupplierUnitModifyActivity.UNIT_OBJECT_KEY_UPDATE, true);
        Utils.onActivityStartForResultInFragment(this, false, new int[]{}, intent, null, SupplierUnitModifyActivity.REQUEST_CODE_ADD_UPDATE_UNIT);
    }

    @Override
    public void onItemChangeStatus(View view, Object object) {
        final SupplierUnitListRes.ResultBean unit = (SupplierUnitListRes.ResultBean) object;
        final SupplierUnitModifyReq reqModel = new SupplierUnitModifyReq();
        reqModel.setUnitId(unit.getUnitId());
        reqModel.setUnitStatus(unit.getUnitStatus().equalsIgnoreCase(ConstantValues.ENABLE) ? ConstantValues.DISABLE : ConstantValues.ENABLE);
        reqModel.setOperation(ConstantValues.DELETE);
        reqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mHostActivity));
        reqModel.setKgValue(unit.getKgValue());

        mHostActivity.apiEnqueueObject = RetrofitWebService.getInstance().getInterface().modifiedUnit(reqModel);
        mHostActivity.apiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mHostActivity, true) {

            @Override
            public void yesCall(EmptyResponse response, ParentActivity weakRef) {

                if (VerifyResponse.isResponseOk(response)) {
                    unit.setUnitStatus(reqModel.getUnitStatus());
                    mAdapter.notifyData(emptyView);
                    onFetchDataFromServer(false);
                } else
                    Toast.makeText(mHostActivity, R.string.please_try_again, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void noCall(Throwable error) {

            }
        });
    }
}
