package ig.com.digitalmandi.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.bean.request.seller.SupplierPurchasePaymentReq;
import ig.com.digitalmandi.bean.response.seller.SupplierOrderListResponse;
import ig.com.digitalmandi.bean.response.seller.SupplierPurchaseListRes;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Utils;

/**
 * Created by shivam.garg on 25-10-2016.
 */

public class PurchasePaymentDialog extends BaseDialog implements DatePickerClass.OnDateSelected, View.OnFocusChangeListener {

    //private SupplierPurchaseListRes.LoginUser purchaseObject;
    private final int OFFERS_DAYS = 10;
    @BindView(R.id.mEditTextPaymentAmt)
    AppCompatEditText mEditTextPaymentAmt;
    @BindView(R.id.mEditTextInterestRate)
    AppCompatEditText mEditTextInterestRate;
    @BindView(R.id.mEditTextInterestAmt)
    AppCompatEditText mEditTextInterestAmt;
    @BindView(R.id.mCheckBoxIsInterstPaid)
    CheckBox mCheckBoxIsInterstPaid;
    @BindView(R.id.mButtonDatePicker)
    AppCompatButton mButtonDatePicker;
    @BindView(R.id.mButtonPayment)
    AppCompatButton mButtonPayment;
    @BindView(R.id.mSpinnerPaymentType)
    AppCompatSpinner mSpinnerPaymentType;
    @BindView(R.id.container)
    LinearLayout containerOfAllChild;
    @BindView(R.id.mTextViewNoOfDays)
    AppCompatTextView mTextViewNoOfDays;
    private Date paymentDate, purchaseDate;
    private OnPaymentDone onPaymentDone;
    private int interestDays = 0;
    private int numberOfDaysInMonth = 30;
    private String dateString = "",objectId="",flag="";

    public PurchasePaymentDialog(Context context, boolean isOutSideTouch, boolean isCancelable, int layoutId) {
        super(context, isOutSideTouch, isCancelable, layoutId);
    }

    @Override
    public void onFocusChange(View view, boolean b) {

        if (mEditTextPaymentAmt.getText().toString().isEmpty()) {
            return;
        }

        if (mEditTextInterestRate.getText().toString().isEmpty()) {
            return;
        }

        float paidAmount = Float.parseFloat(mEditTextPaymentAmt.getText().toString());
        float rateOfInter = Float.parseFloat(mEditTextInterestRate.getText().toString());
        float interestAmt = 0.0f;

        if (interestDays > 0) {
            interestAmt = ((paidAmount * rateOfInter * interestDays) / (numberOfDaysInMonth * 100.f));
            mEditTextInterestAmt.setText(Utils.formatStringUpTo2Precision(String.valueOf(interestAmt)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dilaog_purchase_payment);
        ButterKnife.bind(this);
        purchaseDate = Utils.onConvertStringToDate(dateString, AppConstant.API_DATE_FORMAT);
        mEditTextPaymentAmt.setOnFocusChangeListener(this);
        mEditTextInterestRate.setOnFocusChangeListener(this);
    }

    @OnClick({R.id.mButtonDatePicker, R.id.mButtonPayment})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mButtonDatePicker:
                Utils.onHideSoftKeyBoard(mBaseActivity, mEditTextPaymentAmt);
                if (purchaseDate == null) {
                    Toast.makeText(mBaseActivity, "Purchase Date Can't Be Null", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatePickerClass.showDatePicker(mBaseActivity, DatePickerClass.PAYMENT_DATE, this, AppConstant.API_DATE_FORMAT);
                break;

            case R.id.mButtonPayment:
                Utils.onHideSoftKeyBoard(mBaseActivity, mEditTextPaymentAmt);
                goForPayment();
                break;
        }
    }

    private void goForPayment() {

        if (mEditTextPaymentAmt.getText().toString().isEmpty()) {
            Toast.makeText(mBaseActivity, "Please Enter Payment Amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mEditTextInterestRate.getText().toString().isEmpty()) {
            Toast.makeText(mBaseActivity, "Rate Of Interest Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mEditTextInterestAmt.getText().toString().isEmpty()) {
            Toast.makeText(mBaseActivity, "Interest Amt Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        float paidAmount = Float.parseFloat(mEditTextPaymentAmt.getText().toString());
        float rateOfInter = Float.parseFloat(mEditTextInterestRate.getText().toString());
        float interestAmt = 0.0f;

        if (interestDays > 0) {
            interestAmt = ((paidAmount * rateOfInter * interestDays) / (numberOfDaysInMonth * 100.f));
            mEditTextInterestAmt.setText(Utils.formatStringUpTo2Precision(String.valueOf(interestAmt)));
        }

        SupplierPurchasePaymentReq purchasePaymentReqModel = new SupplierPurchasePaymentReq();
        purchasePaymentReqModel.setAmount(mEditTextPaymentAmt.getText().toString());
        purchasePaymentReqModel.setDate(Utils.getDateString(paymentDate.getTime(), AppConstant.API_DATE_FORMAT));
        purchasePaymentReqModel.setFlag(flag);
        purchasePaymentReqModel.setId(objectId);
        purchasePaymentReqModel.setInterestAmt(String.valueOf(interestAmt));
        purchasePaymentReqModel.setInterestPaid(mCheckBoxIsInterstPaid.isChecked() == true ? "1" : "0");
        purchasePaymentReqModel.setInterestRate(String.valueOf(rateOfInter));
        purchasePaymentReqModel.setPaymentType((String) mSpinnerPaymentType.getSelectedItem());
        showAlertDialogWithOption(purchasePaymentReqModel);
    }

    private void showAlertDialogWithOption(final SupplierPurchasePaymentReq purchasePaymentReqModel) {
        Gson h = new Gson();
        Log.d("PurchasePaymentDialog", h.toJson(purchasePaymentReqModel));
        /*MyAlertDialog.onShowAlertDialog(mBaseActivity, "Continue For Payment", "Continue", "Leave", new OnAlertDialogCallBack() {
            @Override
            public void onNegative(DialogInterface dialogInterface, int i) {

            }

            @Override
            public void onPositive(DialogInterface dialogInterface, int i) {
                getBaseActivity().mApiEnqueueObject = RetrofitWebClient.getInstance().getInterface().doPurchase(purchasePaymentReqModel);
                getBaseActivity().mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>((AppCompatActivity) mBaseActivity, true) {

                    @Override
                    public void onSuccess(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                        if (ResponseVerification.isResponseOk(pResponse)) {
                            Toast.makeText(pBaseActivity, "Payment Done SuccessFully", Toast.LENGTH_SHORT).show();
                            dismiss();
                            onPaymentDone.onPaymentDoneSuccess();
                        } else
                            Toast.makeText(mBaseActivity, pResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String pErrorMsg) {

                    }
                });
            }
        });*/
    }

    @Override
    public void onDateSelectedCallBack(int id, Date newCalendar1, String stringResOfDate, long milliSeconds, int numberOfDays) {
        paymentDate = newCalendar1;
        numberOfDaysInMonth = numberOfDays;
        Log.d("PurchasePaymentDialog", "numberOfDaysInMonth:" + numberOfDaysInMonth);

        if (paymentDate.before(purchaseDate)) {
            paymentDate = null;
            Toast.makeText(mBaseActivity, "Payment Date Must Be Equals or Greater Of Purchasing Date", Toast.LENGTH_SHORT).show();
            return;
        }

        mButtonDatePicker.setText(stringResOfDate);
        containerOfAllChild.setVisibility(View.VISIBLE);
        onCalculateDays();
    }

    private void onCalculateDays() {
        int numberOfDays = Utils.getNumberOfDaysBetweenDate(paymentDate, purchaseDate);
        interestDays = numberOfDays - OFFERS_DAYS;

        if (interestDays <= 0)
            mTextViewNoOfDays.setText("No Interest Days");
        else if (interestDays == 1)
            mTextViewNoOfDays.setText("Interest Day is " + interestDays);
        else if (interestDays > 1)
            mTextViewNoOfDays.setText("Interest Days are " + interestDays);
    }

    public void show(SupplierPurchaseListRes.ResultBean purchaseObject, OnPaymentDone onPaymentDone) {
        this.onPaymentDone = onPaymentDone;
        this.dateString    = purchaseObject.getPurchaseDate();
        this.objectId      = purchaseObject.getPurchaseId();
        this.flag = AppConstant.DELETE_OR_PAYMENT_PURCHASE;
        show();
    }

    public void show(SupplierOrderListResponse.Order soldObject, OnPaymentDone onPaymentDone) {
        this.onPaymentDone = onPaymentDone;
        this.dateString    = soldObject.getOrderDate();
        this.objectId      = soldObject.getOrderId();
        this.flag = AppConstant.DELETE_OR_PAYMENT_ORDER;
        show();
    }

    public interface OnPaymentDone {
        void onPaymentDoneSuccess();
    }
}
