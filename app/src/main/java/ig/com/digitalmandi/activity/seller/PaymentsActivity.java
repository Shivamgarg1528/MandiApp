package ig.com.digitalmandi.activity.seller;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.activity.ListBaseActivity;
import ig.com.digitalmandi.adapter.supplier.PaymentAdapter;
import ig.com.digitalmandi.bean.request.seller.PaymentsRequest;
import ig.com.digitalmandi.bean.response.seller.PaymentResponse;
import ig.com.digitalmandi.dialog.PaymentDialog;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class PaymentsActivity extends ListBaseActivity<PaymentResponse.Payment> implements View.OnClickListener {

    private PaymentsRequest mPaymentsRequest;
    private float mOrderAmt = 0.0f;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new PaymentAdapter(this, mDataList, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_payment;
    }

    @Override
    protected int getEmptyTextStringId() {
        return R.string.string_no_customer_payment_found_please_pay_due_amount;
    }

    @Override
    protected void fetchData(final boolean pRefresh) {

        mApiEnqueueObject = RetrofitClient.getInstance().getInterface().supplierPurchasePaymentList(mPaymentsRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<PaymentResponse>(mBaseActivity, false) {

            @Override
            public void onResponse(PaymentResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    if (pRefresh) {
                        mRecyclerView.scrollToPosition(0);
                        mDataList.clear();
                    }
                    mDataList.addAll(pResponse.getResult());
                }
                setTextAfterEvaluation();
            }
        });
    }

    @Override
    protected void getIntentData() {
        mPaymentsRequest = (PaymentsRequest) getIntent().getSerializableExtra(AppConstant.KEY_OBJECT);
        mOrderAmt = Float.parseFloat(Helper.formatStringUpTo2Precision(mPaymentsRequest.getOrderAmount()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(true);
        setTitle(getString(R.string.string_payment_history_title, mPaymentsRequest.getId()));
        findViewById(R.id.activity_purchase_payment_tv_payment).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        PaymentDialog paymentDialog = new PaymentDialog(mBaseActivity, this, mPaymentsRequest.getId(), mPaymentsRequest.getOrderDate(), mPaymentsRequest.getFlag());
        paymentDialog.show();
    }

    private void setTextAfterEvaluation() {
        notifyAdapterAndView();
        float paidAmt = 0.0f;
        float interestAmt = 0.0f;
        float interestPaidAmt = 0.0f;
        float dueAmt;
        float interestDueAmt;
        float totalPaid;

        for (PaymentResponse.Payment payment : mDataList) {
            paidAmt += Float.parseFloat(payment.getAmount());
            interestAmt += Float.parseFloat(payment.getInterestAmt());
            interestPaidAmt += AppConstant.INTEREST_PAID.equalsIgnoreCase(payment.getInterestPaid()) ? Float.parseFloat(payment.getInterestAmt()) : 0.0f;
        }

        dueAmt = mOrderAmt - paidAmt;
        interestDueAmt = interestAmt - interestPaidAmt;
        totalPaid = paidAmt + interestPaidAmt;

        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_order_amount)).setText(Helper.formatStringUpTo2Precision(String.valueOf(mOrderAmt)));
        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_paid_amount)).setText(Helper.formatStringUpTo2Precision(String.valueOf(paidAmt)));
        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_due_amount)).setText(Helper.formatStringUpTo2Precision(String.valueOf(dueAmt)));
        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_interest_amount)).setText(Helper.formatStringUpTo2Precision(String.valueOf(interestAmt)));
        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_interest_paid_amount)).setText(Helper.formatStringUpTo2Precision(String.valueOf(interestPaidAmt)));
        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_interest_due_amount)).setText(Helper.formatStringUpTo2Precision(String.valueOf(interestDueAmt)));
        ((AppCompatTextView) findViewById(R.id.activity_purchase_payment_tv_total_paid_amount)).setText(Helper.formatStringUpTo2Precision(String.valueOf(totalPaid)));

        if (dueAmt > 0 || interestDueAmt > 0) {
            findViewById(R.id.activity_purchase_payment_tv_payment).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.activity_purchase_payment_tv_payment).setVisibility(View.GONE);
        }
    }

    @Override
    public void onEvent(int pOperationType, PaymentResponse.Payment pObject) {
        fetchData(true);
    }
}
