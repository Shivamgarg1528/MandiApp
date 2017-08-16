package ig.com.digitalmandi.activity.supplier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.adapter.supplier.SupplierOrderDetailAdapter;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderDetailListReq;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderDetailListRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierPurchaseListRes;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.utils.ConstantValues;

public class SupplierPurchaseSoldActivity extends ParentActivity {

    public static String PURCHASE_OBJECT_KEY = "purchaseObject";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyTextView)
    AppCompatTextView emptyTextView;
    private SupplierPurchaseListRes.ResultBean purchaseInfo;
    private SupplierOrderDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_supplier_customer_order_detail);

        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        purchaseInfo = (SupplierPurchaseListRes.ResultBean) intent.getParcelableExtra(PURCHASE_OBJECT_KEY);
        setTitle("Sold Details Of " + purchaseInfo.getNameOfPerson().trim() +"'s Item" );
        ButterKnife.bind(this);

        mAdapter = new SupplierOrderDetailAdapter(dataList, mRunningActivity);
        emptyTextView.setText("No Details Found\n Please Sale Item First");
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        fetchDataFromServer();
    }


    private void fetchDataFromServer() {
        onShowOrHideBar(true);
        SupplierOrderDetailListReq supplierOrderDetailListReq = new SupplierOrderDetailListReq();
        supplierOrderDetailListReq.setFlag(ConstantValues.COLUMN_PURCHASE_ID);
        supplierOrderDetailListReq.setId(purchaseInfo.getPurchaseId());

        apiEnqueueObject = RetrofitWebService.getInstance().getInterface().orderDetailListOfAnyCustomer(supplierOrderDetailListReq);
        apiEnqueueObject.enqueue(new RetrofitCallBack<SupplierOrderDetailListRes>(mRunningActivity, false) {

            @Override
            public void yesCall(SupplierOrderDetailListRes response, ParentActivity weakRef) {
                if (VerifyResponse.isResponseOk(response,false)) {
                    dataList.addAll(response.getResult());
                }
                mAdapter.notifyData(emptyTextView);
            }

            @Override
            public void noCall(Throwable error) {
                mAdapter.notifyData(emptyTextView);
            }
        });
    }
}
