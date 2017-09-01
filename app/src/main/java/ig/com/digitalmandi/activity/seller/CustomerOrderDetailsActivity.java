package ig.com.digitalmandi.activity.seller;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.activity.ListBaseActivity;
import ig.com.digitalmandi.adapter.supplier.SupplierOrderDetailAdapter;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderDetailListRequest;
import ig.com.digitalmandi.bean.response.seller.SupplierOrderDetailListResponse;
import ig.com.digitalmandi.bean.response.seller.SupplierOrderListResponse;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.util.AppConstant;

public class CustomerOrderDetailsActivity extends ListBaseActivity<SupplierOrderDetailListResponse.OrderDetail> {

    private SupplierOrderListResponse.Order mOrderObj;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new SupplierOrderDetailAdapter(mDataList);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customer_order_details;
    }

    @Override
    protected int getEmptyTextStringId() {
        return R.string.string_no_customer_order_details_found_please_try_again_later;
    }

    @Override
    protected void fetchData(boolean pRefresh) {

        SupplierOrderDetailListRequest supplierOrderDetailListRequest = new SupplierOrderDetailListRequest();
        supplierOrderDetailListRequest.setFlag(AppConstant.COLUMN_ORDER_ID);
        supplierOrderDetailListRequest.setId(mOrderObj.getOrderId());

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().orderDetailsOfGivenCustomer(supplierOrderDetailListRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<SupplierOrderDetailListResponse>(mBaseActivity, false) {

            @Override
            public void onResponse(SupplierOrderDetailListResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    mDataList.addAll(pResponse.getResult());
                }
                notifyAdapterAndView();
            }

        });
    }

    @Override
    protected void getIntentData() {
        mOrderObj = (SupplierOrderListResponse.Order) getIntent().getSerializableExtra(AppConstant.KEY_OBJECT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(true);
        setTitle(String.format(getString(R.string.string_order_id_details), mOrderObj.getOrderId()));
    }
}
