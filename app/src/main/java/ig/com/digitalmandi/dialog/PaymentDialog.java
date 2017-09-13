package ig.com.digitalmandi.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.Date;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.request.seller.PaymentRequest;
import ig.com.digitalmandi.bean.response.EmptyResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class PaymentDialog extends BaseDialog implements DatePickerClass.OnDateSelected, View.OnClickListener, TextWatcher {

    private static final int OFFERS_DAYS = 10;
    private final Date mDatePurchase;
    private final String mOrderIdStr;
    private final String mPaymentFlag;
    private AppCompatEditText mEditTextPaymentAmt;
    private AppCompatEditText mEditTextInterestRate;
    private AppCompatEditText mEditTextInterestAmt;
    private AppCompatTextView mTextViewInterestDays;
    private AppCompatButton mButtonPaymentDate;
    private AppCompatSpinner mSpinnerPaymentType;
    private LinearLayout mLinearLayoutParent;
    private Date mDatePayment;
    private int mInterestDays = 0;
    private int mDaysInMonth = 30;

    public PaymentDialog(BaseActivity pBaseActivity, EventCallback pEventCallBack, String pOrderIdStr, String pOrderDateStr, String pPaymentFlag) {
        super(pBaseActivity, pEventCallBack);
        this.mOrderIdStr = pOrderIdStr;
        this.mPaymentFlag = pPaymentFlag;
        this.mDatePurchase = Helper.getDateObject(pOrderDateStr, AppConstant.API_DATE_FORMAT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dilaog_purchase_payment);

        mLinearLayoutParent = findViewById(R.id.dialog_purchase_payment_ll_parent);
        mTextViewInterestDays = findViewById(R.id.dialog_purchase_payment_tv_interest_days);
        mSpinnerPaymentType = findViewById(R.id.dialog_purchase_payment_spinner_payment_type);

        mEditTextPaymentAmt = findViewById(R.id.dialog_purchase_payment_edt_payment_amount);
        mEditTextPaymentAmt.addTextChangedListener(this);

        mEditTextInterestRate = findViewById(R.id.dialog_purchase_payment_edt_rate_of_interest);
        mEditTextInterestRate.addTextChangedListener(this);

        mEditTextInterestAmt = findViewById(R.id.dialog_purchase_payment_edt_interest_amount);

        mButtonPaymentDate = findViewById(R.id.dialog_purchase_payment_tv_payment_date);
        mButtonPaymentDate.setOnClickListener(this);

        findViewById(R.id.dialog_purchase_payment_tv_payment).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Helper.hideSoftKeyBoard(mBaseActivity, mEditTextPaymentAmt);
        switch (view.getId()) {

            case R.id.dialog_purchase_payment_tv_payment_date:
                DatePickerClass.showDatePicker(mBaseActivity, DatePickerClass.PAYMENT_DATE, this);
                break;

            case R.id.dialog_purchase_payment_tv_payment:
                startPaymentProcess();
                break;
        }
    }

    @Override
    public void onDateSelectedCallBack(int id, Date pDate, String pDateAppShownFormat, int pMaxDaysInSelectedMonth) {
        mDatePayment = null;
        if (pDate.before(mDatePurchase)) {
            mBaseActivity.showToast(mBaseActivity.getString(R.string.string_invalid_payment_date));
            return;
        }
        mDatePayment = pDate;
        mDaysInMonth = pMaxDaysInSelectedMonth;
        mButtonPaymentDate.setText(pDateAppShownFormat);
        mLinearLayoutParent.setVisibility(View.VISIBLE);
        findInterestDays();
        findInterestAmount();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        findInterestAmount();
    }

    private void findInterestAmount() {
        String payAmountStr = mEditTextPaymentAmt.getText().toString();
        String interestRateStr = mEditTextInterestRate.getText().toString();

        if (Helper.isEmpty(payAmountStr) || Helper.isEmpty(interestRateStr)) {
            mEditTextInterestAmt.setText("0.00");
        } else {
            float payAmount;
            float rateOfInterest;
            try {
                payAmount = Float.parseFloat(payAmountStr);
                rateOfInterest = Float.parseFloat(interestRateStr);
            } catch (NumberFormatException e) {
                mEditTextInterestAmt.setText("0.00");
                return;
            }
            float interestAmount = 0.0f;
            if (mInterestDays > 0) {
                interestAmount = ((payAmount * rateOfInterest * mInterestDays) / (mDaysInMonth * 100.f));
            }
            mEditTextInterestAmt.setText(Helper.formatStringUpTo2Precision(String.valueOf(interestAmount)));
        }
    }

    private void findInterestDays() {
        int numberOfDays = Helper.getNumberOfDaysBetweenDate(mDatePayment, mDatePurchase);
        mInterestDays = numberOfDays - OFFERS_DAYS;
        if (mInterestDays <= 0) {
            mTextViewInterestDays.setText(R.string.string_no_interest_days);
        } else if (mInterestDays == 1) {
            mTextViewInterestDays.setText(mBaseActivity.getString(R.string.string_interest_day_is) + " " + mInterestDays);
        } else {
            mTextViewInterestDays.setText(mBaseActivity.getString(R.string.string_interest_days_are) + " " + mInterestDays);
        }
    }

    private void startPaymentProcess() {

        String payAmountStr = mEditTextPaymentAmt.getText().toString();
        String interestRateStr = mEditTextInterestRate.getText().toString();
        String interestAmtStr = mEditTextInterestAmt.getText().toString();

        if (Helper.isEmpty(payAmountStr)) {
            mBaseActivity.showToast(mBaseActivity.getString(R.string.string_please_enter_payment_amount));
        } else if (Helper.isEmpty(interestRateStr)) {
            mBaseActivity.showToast(mBaseActivity.getString(R.string.string_interest_rate_can_not_be_empty));
        } else if (Helper.isEmpty(interestAmtStr)) {
            mBaseActivity.showToast(mBaseActivity.getString(R.string.string_interest_amount_can_not_be_empty));
        } else {
            float payAmount = Float.parseFloat(payAmountStr);
            float rateOfInterest = Float.parseFloat(interestRateStr);
            float interestAmount = 0.0f;

            if (mInterestDays > 0) {
                interestAmount = ((payAmount * rateOfInterest * mInterestDays) / (mDaysInMonth * 100.f));
                mEditTextInterestAmt.setText(Helper.formatStringUpTo2Precision(String.valueOf(interestAmount)));
            }

            boolean isInterestPaid = ((CheckBox) findViewById(R.id.dialog_purchase_payment_cb_interest_paid)).isChecked();

            final PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setDate(Helper.getDateString(mDatePayment.getTime(), AppConstant.API_DATE_FORMAT));
            paymentRequest.setFlag(mPaymentFlag);
            paymentRequest.setId(mOrderIdStr);
            paymentRequest.setAmount(payAmountStr);
            paymentRequest.setInterestAmt(String.valueOf(interestAmount));
            paymentRequest.setInterestRate(String.valueOf(rateOfInterest));
            paymentRequest.setInterestPaid(isInterestPaid ? AppConstant.INTEREST_PAID : AppConstant.INTEREST_UNPAID);
            paymentRequest.setPaymentType((String) mSpinnerPaymentType.getSelectedItem());

            String paidAmt = Helper.formatStringUpTo2Precision(String.valueOf(isInterestPaid ? payAmount + interestAmount : payAmount));
            String messageWithAmount = mBaseActivity.getString(R.string.string_continue_for_payment, paidAmt);

            ConfirmDialog.show(mBaseActivity, messageWithAmount, true, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {

                        mBaseActivity.mApiEnqueueObject = RetrofitClient.getInstance().getInterface().doPurchase(paymentRequest);
                        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mBaseActivity) {
                            @Override
                            public void onResponse(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                                if (ResponseVerification.isResponseOk(pResponse)) {
                                    mBaseActivity.showToast(pBaseActivity.getString(R.string.string_payment_done_successfully));
                                    mEventCallback.onEvent(0, null);
                                    dismiss();
                                } else {
                                    mBaseActivity.showToast(pResponse.getMessage());
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
