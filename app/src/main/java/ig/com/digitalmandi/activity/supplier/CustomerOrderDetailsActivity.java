package ig.com.digitalmandi.activity.supplier;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.ListBaseActivity;
import ig.com.digitalmandi.adapter.supplier.SupplierOrderDetailAdapter;
import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderDetailListRequest;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderDetailListResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderListResponse;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.utils.AppConstant;

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
    protected void fetchData() {

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
                notifyAdapterAndView();
            }

            @Override
            public void onFailure(String pErrorMsg) {
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
