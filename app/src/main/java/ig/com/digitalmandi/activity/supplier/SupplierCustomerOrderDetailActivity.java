package ig.com.digitalmandi.activity.supplier;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.adapter.supplier.SupplierOrderDetailAdapter;
import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderDetailListRequest;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderDetailListResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderListResponse;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.utils.AppConstant;

public class SupplierCustomerOrderDetailActivity extends BaseActivity<SupplierOrderDetailListResponse.OrderDetail> {

    private SupplierOrderDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_customer_order_detail);
        ButterKnife.bind(this);
        setToolbar(true);

        SupplierOrderListResponse.Order mOrderObj = (SupplierOrderListResponse.Order) getIntent().getSerializableExtra(AppConstant.KEY_OBJECT);
        setTitle(String.format(getString(R.string.string_order_id_details), mOrderObj.getOrderId()));

        mAdapter = new SupplierOrderDetailAdapter(mDataList);

        final AppCompatTextView textViewEmpty = (AppCompatTextView) findViewById(R.id.emptyTextView);
        textViewEmpty.setText(R.string.string_no_order_details_found_please_try_again_later);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        showOrHideProgressBar(true);

        SupplierOrderDetailListRequest supplierOrderDetailListRequest = new SupplierOrderDetailListRequest();
        supplierOrderDetailListRequest.setFlag(AppConstant.COLUMN_ORDER_ID);
        supplierOrderDetailListRequest.setId(mOrderObj.getOrderId());

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().orderDetailsOfGivenCustomer(supplierOrderDetailListRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<SupplierOrderDetailListResponse>(mBaseActivity, false) {

            @Override
            public void onSuccess(SupplierOrderDetailListResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    mDataList.addAll(pResponse.getResult());
                }
                mAdapter.notifyData(textViewEmpty);
            }

            @Override
            public void onFailure(String pErrorMsg) {
                mAdapter.notifyData(textViewEmpty);
            }
        });
    }


}
