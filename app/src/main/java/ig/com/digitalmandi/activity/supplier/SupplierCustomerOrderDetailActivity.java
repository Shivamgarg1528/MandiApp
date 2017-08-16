package ig.com.digitalmandi.activity.supplier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.adapter.supplier.SupplierOrderDetailAdapter;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierCustomerListRes;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderDetailListReq;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderDetailListRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderListRes;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.utils.ConstantValues;

public class SupplierCustomerOrderDetailActivity extends ParentActivity {

    public static String CUSTOMER_OBJECT_KEY = "customerObject";
    public static String ORDER_OBJECT_KEY = "orderObject";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyTextView)
    AppCompatTextView emptyTextView;
    private SupplierCustomerListRes.ResultBean customerInfo;
    private SupplierOrderListRes.ResultBean orderInfo;
    private SupplierOrderDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_supplier_customer_order_detail);

        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        if (intent == null || (SupplierCustomerListRes.ResultBean) intent.getParcelableExtra(CUSTOMER_OBJECT_KEY) == null || (SupplierOrderListRes.ResultBean) intent.getParcelableExtra(ORDER_OBJECT_KEY) == null) {
            Toast.makeText(mRunningActivity, "No Customer Information Found. Contact App Admin For Better Assistance", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        customerInfo = (SupplierCustomerListRes.ResultBean) intent.getParcelableExtra(CUSTOMER_OBJECT_KEY);
        orderInfo = (SupplierOrderListRes.ResultBean) intent.getParcelableExtra(ORDER_OBJECT_KEY);
        setTitle("Order " + orderInfo.getOrderId() + " Details");
        ButterKnife.bind(this);

        mAdapter = new SupplierOrderDetailAdapter(dataList, mRunningActivity);
        emptyTextView.setText("No Order Details Found\n Please Try Again Later");
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        fetchDataFromServer();
    }


    private void fetchDataFromServer() {
        onShowOrHideBar(true);
        SupplierOrderDetailListReq supplierOrderDetailListReq = new SupplierOrderDetailListReq();
        supplierOrderDetailListReq.setFlag(ConstantValues.COLUMN_ORDER_ID);
        supplierOrderDetailListReq.setId(orderInfo.getOrderId());

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
