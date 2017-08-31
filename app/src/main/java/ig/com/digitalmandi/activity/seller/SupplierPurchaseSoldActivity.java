package ig.com.digitalmandi.activity.seller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.adapter.supplier.SupplierOrderDetailAdapter;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderDetailListRequest;
import ig.com.digitalmandi.bean.response.seller.SupplierOrderDetailListResponse;
import ig.com.digitalmandi.bean.response.seller.SupplierPurchaseListRes;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.util.AppConstant;

public class SupplierPurchaseSoldActivity extends BaseActivity {

    public static String PURCHASE_OBJECT_KEY = "purchaseObject";
    @BindView(R.id.layout_common_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_common_list_tv_empty_text_view)
    AppCompatTextView emptyTextView;
    private SupplierPurchaseListRes.ResultBean purchaseInfo;
    private SupplierOrderDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_customer_order_details);

        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        purchaseInfo = intent.getParcelableExtra(PURCHASE_OBJECT_KEY);
        setTitle("Sold Details Of " + purchaseInfo.getNameOfPerson().trim() +"'s Item" );
        ButterKnife.bind(this);

        mAdapter = new SupplierOrderDetailAdapter(mDataList);
        emptyTextView.setText("No Details Found\n Please Sale Item First");
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        fetchDataFromServer();
    }


    private void fetchDataFromServer() {
        showOrHideProgressBar(true);
        SupplierOrderDetailListRequest supplierOrderDetailListRequest = new SupplierOrderDetailListRequest();
        supplierOrderDetailListRequest.setFlag(AppConstant.COLUMN_PURCHASE_ID);
        supplierOrderDetailListRequest.setId(purchaseInfo.getPurchaseId());

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().orderDetailsOfGivenCustomer(supplierOrderDetailListRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<SupplierOrderDetailListResponse>(mBaseActivity, false) {

            @Override
            public void onSuccess(SupplierOrderDetailListResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    mDataList.addAll(pResponse.getResult());
                }
                // mAdapter.notifyData(emptyTextView);
            }

            @Override
            public void onFailure(String pErrorMsg) {
                //mAdapter.notifyData(emptyTextView);
            }
        });
    }
}
