package ig.com.digitalmandi.activity.supplier;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.adapter.supplier.SupplierPurchasePaymentAdapter;
import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchasePaymentListRequest;
import ig.com.digitalmandi.beans.response.supplier.SupplierPaymentListResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierPurchaseListRes;
import ig.com.digitalmandi.dialogs.PurchasePaymentDialog;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.utils.AppConstant;
import ig.com.digitalmandi.utils.Utils;

public class SupplierPurchasePaymentActivity extends BaseActivity {

    public static final String PURCHASE_OBJECT_KEY = "purchaseObjKey";
    @BindView(R.id.activity_purchase_payment_tv_payment)
    AppCompatButton mButtonPurchasePayment;
    @BindView(R.id.layout_common_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_common_list_tv_empty_text_view)
    AppCompatTextView emptyTextView;
    @BindView(R.id.activity_purchase_payment_tv_order_amount)
    AppCompatTextView mTextViewPurchaseAmt;
    @BindView(R.id.activity_purchase_payment_tv_paid_amount)
    AppCompatTextView mTextViewPaidAmt;
    @BindView(R.id.activity_purchase_payment_tv_due_amount)
    AppCompatTextView mTextViewDueAmt;
    @BindView(R.id.activity_purchase_payment_tv_interest_amount)
    AppCompatTextView mTextViewInterestAmt;
    @BindView(R.id.activity_purchase_payment_tv_interest_paid_amount)
    AppCompatTextView mTextViewInterestPaidAmt;
    @BindView(R.id.activity_purchase_payment_tv_interest_due_amount)
    AppCompatTextView mTextViewInterestDueAmt;
    @BindView(R.id.activity_purchase_payment_tv_total_paid_amount)
    AppCompatTextView mTextViewTotalPaidAmt;
    @BindView(R.id.activity_purchase_payment)
    LinearLayout activityPurchasePayment;
    private SupplierPurchaseListRes.ResultBean purchaseObject;
    private SupplierPurchasePaymentAdapter mAdapter;
    private List<SupplierPaymentListResponse.Payment> dataList = new ArrayList<>();
    private float purchaseAmt = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_purchase_payment);

        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (getIntent() == null) {
            Toast.makeText(mBaseActivity, R.string.please_provide_purchased_item, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        purchaseObject = getIntent().getParcelableExtra(PURCHASE_OBJECT_KEY);

        if (purchaseObject == null) {
            Toast.makeText(mBaseActivity, R.string.please_provide_purchased_item, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ButterKnife.bind(this);
        emptyTextView.setText("No Payment Found\nPlease Pay Due Amount");
        setTitle(getString(R.string.payment_history_title, purchaseObject.getNameOfPerson()));
        purchaseAmt = Float.parseFloat(Utils.formatStringUpTo2Precision(purchaseObject.getTotalAmount()));
        mAdapter = new SupplierPurchasePaymentAdapter(dataList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        onFetchDataFromServer(false);
    }

    @OnClick(R.id.activity_purchase_payment_tv_payment)
    public void onClick() {
        PurchasePaymentDialog dialog = new PurchasePaymentDialog(mBaseActivity, true, true, R.layout.dilaog_purchase_payment);
        dialog.show(purchaseObject, new PurchasePaymentDialog.OnPaymentDone() {

            @Override
            public void onPaymentDoneSuccess() {
                onFetchDataFromServer(false);
            }
        });
    }

    private void onCalculateAllPaidAmt() {
        mAdapter.notifyData(emptyTextView);
        float paymentAmt = 0.0f;
        float interestAmt = 0.0f;
        float paidInterestAmt = 0.0f;
        float dueAmt = 0.0f;
        float dueInterestAmt = 0.0f;
        float total = 0.0f;

        for (SupplierPaymentListResponse.Payment iterableObject : dataList) {
            paymentAmt += Float.parseFloat(iterableObject.getAmount());
            interestAmt += Float.parseFloat(iterableObject.getInterestAmt());
            paidInterestAmt += iterableObject.getInterestPaid().equalsIgnoreCase("1") ? Float.parseFloat(iterableObject.getInterestAmt()) : 0.0f;
        }
        dueAmt = purchaseAmt - paymentAmt;
        dueInterestAmt = interestAmt - paidInterestAmt;
        total = paymentAmt + paidInterestAmt;

        mTextViewPurchaseAmt.setText(Utils.formatStringUpTo2Precision(String.valueOf(purchaseAmt)));
        mTextViewPaidAmt.setText(Utils.formatStringUpTo2Precision(String.valueOf(paymentAmt)));
        mTextViewDueAmt.setText(Utils.formatStringUpTo2Precision(String.valueOf(dueAmt)));
        mTextViewInterestAmt.setText(Utils.formatStringUpTo2Precision(String.valueOf(interestAmt)));
        mTextViewInterestPaidAmt.setText(Utils.formatStringUpTo2Precision(String.valueOf(paidInterestAmt)));
        mTextViewInterestDueAmt.setText(Utils.formatStringUpTo2Precision(String.valueOf(dueInterestAmt)));
        mTextViewTotalPaidAmt.setText(Utils.formatStringUpTo2Precision(String.valueOf(total)));

        if (dueAmt > 0 || dueInterestAmt > 0)
            mButtonPurchasePayment.setVisibility(View.VISIBLE);
        else
            mButtonPurchasePayment.setVisibility(View.INVISIBLE);
    }

    private void onFetchDataFromServer(boolean showDialog) {

        showOrHideProgressBar(true);
        SupplierPurchasePaymentListRequest supplierPurchasePaymentListRequest = new SupplierPurchasePaymentListRequest();
        supplierPurchasePaymentListRequest.setId(purchaseObject.getPurchaseId());
        supplierPurchasePaymentListRequest.setFlag(AppConstant.DELETE_OR_PAYMENT_PURCHASE);

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().supplierPurchasePaymentList(supplierPurchasePaymentListRequest);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<SupplierPaymentListResponse>(mBaseActivity, showDialog) {

            @Override
            public void onSuccess(SupplierPaymentListResponse pResponse, BaseActivity pBaseActivity) {
                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    dataList.clear();
                    dataList.addAll(pResponse.getResult());
                }
                onCalculateAllPaidAmt();
            }

            @Override
            public void onFailure(String pErrorMsg) {
                onCalculateAllPaidAmt();
            }
        });
    }
}
