package ig.com.digitalmandi.activity.seller;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.activity.ListBaseActivity;
import ig.com.digitalmandi.adapter.supplier.OrderDetailAdapter;
import ig.com.digitalmandi.bean.request.seller.OrderDetailsRequest;
import ig.com.digitalmandi.bean.response.seller.OrderDetailResponse;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.util.AppConstant;

public class OrderDetailsActivity extends ListBaseActivity<OrderDetailResponse.OrderDetail> {

    private OrderDetailsRequest mOrderDetailsRequest;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new OrderDetailAdapter(mDataList);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_details;
    }

    @Override
    protected int getEmptyTextStringId() {
        return R.string.string_no_customer_order_details_found_please_try_again_later;
    }

    @Override
    protected void fetchData(final boolean pRefresh) {
        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().orderDetailsOfGivenCustomer(mOrderDetailsRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<OrderDetailResponse>(mBaseActivity, false) {

            @Override
            public void onResponse(OrderDetailResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    if (pRefresh) {
                        mRecyclerView.scrollToPosition(0);
                        mDataList.clear();
                    }
                    mDataList.addAll(pResponse.getResult());
                }
                notifyAdapterAndView();
            }
        });
    }

    @Override
    protected void getIntentData() {
        mOrderDetailsRequest = (OrderDetailsRequest) getIntent().getSerializableExtra(AppConstant.KEY_OBJECT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(true);
        setTitle(mOrderDetailsRequest.getMessage());
    }
}
