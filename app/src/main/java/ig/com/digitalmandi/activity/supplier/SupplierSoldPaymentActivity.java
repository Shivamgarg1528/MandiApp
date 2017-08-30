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
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchasePaymentListReq;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderListResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierPaymentListRes;
import ig.com.digitalmandi.dialogs.PurchasePaymentDialog;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebClient;
import ig.com.digitalmandi.utils.AppConstant;
import ig.com.digitalmandi.utils.Utils;

public class SupplierSoldPaymentActivity extends BaseActivity {

    public static final String SOLD_OBJECT_KEY = "soldObjKey";
    @BindView(R.id.mButtonPurchasePayment)
    AppCompatButton mButtonPurchasePayment;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyTextView)
    AppCompatTextView emptyTextView;
    @BindView(R.id.mTextViewPurchaseAmt)
    AppCompatTextView mTextViewPurchaseAmt;
    @BindView(R.id.mTextViewPaidAmt)
    AppCompatTextView mTextViewPaidAmt;
    @BindView(R.id.mTextViewDueAmt)
    AppCompatTextView mTextViewDueAmt;
    @BindView(R.id.mTextViewInterestAmt)
    AppCompatTextView mTextViewInterestAmt;
    @BindView(R.id.mTextViewInterestPaidAmt)
    AppCompatTextView mTextViewInterestPaidAmt;
    @BindView(R.id.mTextViewInterestDueAmt)
    AppCompatTextView mTextViewInterestDueAmt;
    @BindView(R.id.mTextViewTotalPaidAmt)
    AppCompatTextView mTextViewTotalPaidAmt;
    @BindView(R.id.activity_purchase_payment)
    LinearLayout activityPurchasePayment;
    private SupplierOrderListResponse.Order soldObject;
    private SupplierPurchasePaymentAdapter mAdapter;
    private List<SupplierPaymentListRes.ResultBean> dataList = new ArrayList<>();
    private float orderTotalAmt = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_purchase_payment);

        if (mToolBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (getIntent() == null) {
            Toast.makeText(mBaseActivity, R.string.please_provide_sold_item, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        soldObject = (SupplierOrderListResponse.Order) getIntent().getSerializableExtra(AppConstant.KEY_OBJECT);

        if (soldObject == null) {
            Toast.makeText(mBaseActivity, R.string.please_provide_sold_item, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ButterKnife.bind(this);
        emptyTextView.setText("No Payment Found\nPlease Pay Due Amount");
        setTitle(getString(R.string.payment_history_title, soldObject.getOrderId()));
        orderTotalAmt = Float.parseFloat(Utils.formatStringUpTo2Precision(soldObject.getOrderTotalAmt()));
        mAdapter = new SupplierPurchasePaymentAdapter(dataList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        onFetchDataFromServer(false);
    }

    @OnClick(R.id.mButtonPurchasePayment)
    public void onClick() {
        PurchasePaymentDialog dialog = new PurchasePaymentDialog(mBaseActivity, true, true, R.layout.dilaog_purchase_payment);
        dialog.show(soldObject, new PurchasePaymentDialog.OnPaymentDone() {

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

        for (SupplierPaymentListRes.ResultBean iterableObject : dataList) {
            paymentAmt += Float.parseFloat(iterableObject.getAmount());
            interestAmt += Float.parseFloat(iterableObject.getInterestAmt());
            paidInterestAmt += iterableObject.getInterestPaid().equalsIgnoreCase("1") ? Float.parseFloat(iterableObject.getInterestAmt()) : 0.0f;
        }
        dueAmt = orderTotalAmt - paymentAmt;
        dueInterestAmt = interestAmt - paidInterestAmt;
        total = paymentAmt + paidInterestAmt;

        mTextViewPurchaseAmt.setText(Utils.formatStringUpTo2Precision(String.valueOf(orderTotalAmt)));
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
        SupplierPurchasePaymentListReq supplierPurchasePaymentListReq = new SupplierPurchasePaymentListReq();
        supplierPurchasePaymentListReq.setId(soldObject.getOrderId());
        supplierPurchasePaymentListReq.setFlag(AppConstant.DELETE_ORDER);

        mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().supplierPurchasePaymentList(supplierPurchasePaymentListReq);
        mApiEnqueueObject.enqueue(new RetrofitCallBack<SupplierPaymentListRes>(mBaseActivity, showDialog) {

            @Override
            public void onSuccess(SupplierPaymentListRes pResponse, BaseActivity pBaseActivity) {
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
