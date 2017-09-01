package ig.com.digitalmandi.activity.seller;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.activity.ListBaseActivity;
import ig.com.digitalmandi.adapter.supplier.SupplierPurchasePaymentAdapter;
import ig.com.digitalmandi.bean.request.seller.SupplierPurchasePaymentListRequest;
import ig.com.digitalmandi.bean.response.seller.SupplierOrderListResponse;
import ig.com.digitalmandi.bean.response.seller.SupplierPaymentListResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.dialog.PaymentDialog;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Utils;

public class CustomerOrderPaymentsActivity extends ListBaseActivity<SupplierPaymentListResponse.Payment> implements View.OnClickListener, EventCallback {

    private SupplierOrderListResponse.Order mOrderObj;
    private float mOrderAmt = 0.0f;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new SupplierPurchasePaymentAdapter(mDataList);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_purchase_payment;
    }

    @Override
    protected int getEmptyTextStringId() {
        return R.string.string_no_customer_payment_found_please_pay_due_amount;
    }

    @Override
    protected void fetchData(boolean pRefresh) {
        SupplierPurchasePaymentListRequest supplierPurchasePaymentListRequest = new SupplierPurchasePaymentListRequest();
        supplierPurchasePaymentListRequest.setId(mOrderObj.getOrderId());
        supplierPurchasePaymentListRequest.setFlag(AppConstant.DELETE_OR_PAYMENT_ORDER);

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().supplierPurchasePaymentList(supplierPurchasePaymentListRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<SupplierPaymentListResponse>(mBaseActivity, false) {

            @Override
            public void onResponse(SupplierPaymentListResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    mDataList.clear();
                    mDataList.addAll(pResponse.getResult());
                }
                setTextAfterEvaluation();
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
        setTitle(getString(R.string.payment_history_title, mOrderObj.getOrderId()));
        mOrderAmt = Float.parseFloat(Utils.formatStringUpTo2Precision(mOrderObj.getOrderTotalAmt()));
        findViewById(R.id.activity_purchase_payment_tv_payment).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        PaymentDialog paymentDialog = new PaymentDialog(mBaseActivity, this, mOrderObj.getOrderId(), mOrderObj.getOrderDate(), AppConstant.DELETE_OR_PAYMENT_ORDER);
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

    @Override
    public void onEvent(int pOperationType, Object pObject) {
        fetchData(true);
    }
}
