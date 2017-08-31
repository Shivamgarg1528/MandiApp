package ig.com.digitalmandi.activity.seller;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.adapter.supplier.SupplierPurchasePaymentAdapter;
import ig.com.digitalmandi.bean.request.seller.SupplierPurchasePaymentListRequest;
import ig.com.digitalmandi.bean.response.seller.SupplierOrderListResponse;
import ig.com.digitalmandi.bean.response.seller.SupplierPaymentListResponse;
import ig.com.digitalmandi.dialog.PurchasePaymentDialog;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Utils;

public class CustomerOrderPaymentsActivity extends BaseActivity<SupplierPaymentListResponse.Payment> implements View.OnClickListener {

    private AppCompatTextView mTextViewEmpty;
    private SupplierOrderListResponse.Order mOrderObj;
    private SupplierPurchasePaymentAdapter mAdapter;

    private float mOrderAmt = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_payment);
        ButterKnife.bind(this);
        setToolbar(true);

        mOrderObj = (SupplierOrderListResponse.Order) getIntent().getSerializableExtra(AppConstant.KEY_OBJECT);
        setTitle(getString(R.string.payment_history_title, mOrderObj.getOrderId()));
        mOrderAmt = Float.parseFloat(Utils.formatStringUpTo2Precision(mOrderObj.getOrderTotalAmt()));

        mTextViewEmpty = (AppCompatTextView) findViewById(R.id.layout_common_list_tv_empty_text_view);
        mTextViewEmpty.setText(getString(R.string.string_no_customer_payment_found_please_pay_due_amount));

        mAdapter = new SupplierPurchasePaymentAdapter(mDataList);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.layout_common_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        fetchData();
        findViewById(R.id.activity_purchase_payment_tv_payment).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        PurchasePaymentDialog dialog = new PurchasePaymentDialog(mBaseActivity, true, true, R.layout.dilaog_purchase_payment);
        dialog.show(mOrderObj, new PurchasePaymentDialog.OnPaymentDone() {

            @Override
            public void onPaymentDoneSuccess() {
                fetchData();
            }
        });
    }

    private void fetchData() {

        showOrHideProgressBar(true);
        SupplierPurchasePaymentListRequest supplierPurchasePaymentListRequest = new SupplierPurchasePaymentListRequest();
        supplierPurchasePaymentListRequest.setId(mOrderObj.getOrderId());
        supplierPurchasePaymentListRequest.setFlag(AppConstant.DELETE_OR_PAYMENT_ORDER);

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().supplierPurchasePaymentList(supplierPurchasePaymentListRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<SupplierPaymentListResponse>(mBaseActivity, false) {

            @Override
            public void onSuccess(SupplierPaymentListResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    mDataList.clear();
                    mDataList.addAll(pResponse.getResult());
                }
                setTextAfterEvaluation();
            }

            @Override
            public void onFailure(String pErrorMsg) {
                setTextAfterEvaluation();
            }
        });
    }

    private void setTextAfterEvaluation() {
        mAdapter.notifyData(mTextViewEmpty);
        float paidAmt = 0.0f;
        float interestAmt = 0.0f;
        float interestPaidAmt = 0.0f;
        float dueAmt;
        float interestDueAmt;
        float totalPaid;

        for (SupplierPaymentListResponse.Payment payment : mDataList) {
            paidAmt += Float.parseFloat(payment.getAmount());
            interestAmt += Float.parseFloat(payment.getInterestAmt());
            interestPaidAmt += AppConstant.INTEREST_PAID.equalsIgnoreCase(payment.getInterestPaid()) ? Float.parseFloat(payment.getInterestAmt()) : 0.0f;
        }

        dueAmt = mOrderAmt - paidAmt;
        interestDueAmt = interestAmt - interestPaidAmt;
        totalPaid = paidAmt + interestPaidAmt;

        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_order_amount)).setText(Utils.formatStringUpTo2Precision(String.valueOf(mOrderAmt)));
        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_paid_amount)).setText(Utils.formatStringUpTo2Precision(String.valueOf(paidAmt)));
        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_due_amount)).setText(Utils.formatStringUpTo2Precision(String.valueOf(dueAmt)));
        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_interest_amount)).setText(Utils.formatStringUpTo2Precision(String.valueOf(interestAmt)));
        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_interest_paid_amount)).setText(Utils.formatStringUpTo2Precision(String.valueOf(interestPaidAmt)));
        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_interest_due_amount)).setText(Utils.formatStringUpTo2Precision(String.valueOf(interestDueAmt)));
        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_total_paid_amount)).setText(Utils.formatStringUpTo2Precision(String.valueOf(totalPaid)));

        if (dueAmt > 0 || interestDueAmt > 0) {
            findViewById(R.id.activity_purchase_payment_tv_payment).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.activity_purchase_payment_tv_payment).setVisibility(View.GONE);
        }
    }
}
