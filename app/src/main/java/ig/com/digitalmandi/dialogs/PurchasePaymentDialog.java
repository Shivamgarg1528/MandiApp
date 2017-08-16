package ig.com.digitalmandi.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import ig.com.digitalmandi.base_package.BaseDialog;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchasePaymentReq;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderListRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierPurchaseListRes;
import ig.com.digitalmandi.interfaces.OnAlertDialogCallBack;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by shivam.garg on 25-10-2016.
 */

public class PurchasePaymentDialog extends BaseDialog implements DatePickerClass.OnDateSelected, View.OnFocusChangeListener {

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
    //private SupplierPurchaseListRes.ResultBean purchaseObject;
    private final int OFFERS_DAYS = 10;
    private OnPaymentDone onPaymentDone;
    private int interestDays = 0;
    private int numberOfDaysInMonth = 30;
    private String dateString = "",objectId="",flag="";

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
            mEditTextInterestAmt.setText(Utils.onStringFormat(String.valueOf(interestAmt)));
        }
    }

    public interface OnPaymentDone {
        public void onPaymentDoneSuccess();
    }

    public PurchasePaymentDialog(Context context, boolean isOutSideTouch, boolean isCancelable, int layoutId) {
        super(context, isOutSideTouch, isCancelable, layoutId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dilaog_purchase_payment);
        ButterKnife.bind(this);
        purchaseDate = Utils.onConvertStringToDate(dateString, ConstantValues.API_DATE_FORMAT);
        mEditTextPaymentAmt.setOnFocusChangeListener(this);
        mEditTextInterestRate.setOnFocusChangeListener(this);
    }

    @OnClick({R.id.mButtonDatePicker, R.id.mButtonPayment})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mButtonDatePicker:
                Utils.onHideSoftKeyBoard(mContext, mEditTextPaymentAmt);
                if (purchaseDate == null) {
                    Toast.makeText(mContext, "Purchase Date Can't Be Null", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatePickerClass.showDatePicker(DatePickerClass.PAYMENT_DATE, this, (AppCompatActivity) mContext, ConstantValues.API_DATE_FORMAT);
                break;

            case R.id.mButtonPayment:
                Utils.onHideSoftKeyBoard(mContext, mEditTextPaymentAmt);
                goForPayment();
                break;
        }
    }

    private void goForPayment() {

        if (mEditTextPaymentAmt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Please Enter Payment Amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mEditTextInterestRate.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Rate Of Interest Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mEditTextInterestAmt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Interest Amt Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        float paidAmount = Float.parseFloat(mEditTextPaymentAmt.getText().toString());
        float rateOfInter = Float.parseFloat(mEditTextInterestRate.getText().toString());
        float interestAmt = 0.0f;

        if (interestDays > 0) {
            interestAmt = ((paidAmount * rateOfInter * interestDays) / (numberOfDaysInMonth * 100.f));
            mEditTextInterestAmt.setText(Utils.onStringFormat(String.valueOf(interestAmt)));
        }

        SupplierPurchasePaymentReq purchasePaymentReqModel = new SupplierPurchasePaymentReq();
        purchasePaymentReqModel.setAmount(mEditTextPaymentAmt.getText().toString());
        purchasePaymentReqModel.setDate(Utils.onConvertDateToString(paymentDate.getTime(), ConstantValues.API_DATE_FORMAT));
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
        MyAlertDialog.onShowAlertDialog(mContext, "Continue For Payment", "Continue", "Leave", true, new OnAlertDialogCallBack() {
            @Override
            public void onNegative(DialogInterface dialogInterface, int i) {

            }

            @Override
            public void onPositive(DialogInterface dialogInterface, int i) {
                getmRunningActivity().apiEnqueueObject = RetrofitWebService.getInstance().getInterface().doPurchase(purchasePaymentReqModel);
                getmRunningActivity().apiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>((AppCompatActivity) mContext, true) {

                    @Override
                    public void yesCall(EmptyResponse response, ParentActivity weakRef) {
                        if (VerifyResponse.isResponseOk(response)) {
                            Toast.makeText(weakRef, "Payment Done SuccessFully", Toast.LENGTH_SHORT).show();
                            dismiss();
                            onPaymentDone.onPaymentDoneSuccess();
                        } else
                            Toast.makeText(mContext, response.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void noCall(Throwable error) {

                    }
                });
            }
        });
    }

    @Override
    public void onDateSelectedCallBack(int id, Date newCalendar1, String stringResOfDate, long milliSeconds, int numberOfDays) {
        paymentDate = newCalendar1;
        numberOfDaysInMonth = numberOfDays;
        Log.d("PurchasePaymentDialog", "numberOfDaysInMonth:" + numberOfDaysInMonth);

        if (paymentDate.before(purchaseDate)) {
            paymentDate = null;
            Toast.makeText(mContext, "Payment Date Must Be Equals or Greater Of Purchasing Date", Toast.LENGTH_SHORT).show();
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
        this.flag          = ConstantValues.PURCHASE_PAYMENT;
        show();
    }

    public void show(SupplierOrderListRes.ResultBean soldObject, OnPaymentDone onPaymentDone) {
        this.onPaymentDone = onPaymentDone;
        this.dateString    = soldObject.getOrderDate();
        this.objectId      = soldObject.getOrderId();
        this.flag          = ConstantValues.ORDER_PAYMENT;
        show();
    }
}
