package ig.com.digitalmandi.adapter.supplier;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.adapter.BaseAdapter;
import ig.com.digitalmandi.bean.response.seller.Payments;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class PaymentAdapter extends BaseAdapter<Payments.Payment> {

    public PaymentAdapter(BaseActivity pBaseActivity, List<Payments.Payment> pDataList, EventCallback pEventCallback) {
        super(pBaseActivity, pDataList, pEventCallback);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payment, parent, false);
        return new ViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Payments.Payment data = mDataList.get(position);

        viewHolder.mTextViewPaymentAmountPaid.setText(Helper.formatStringUpTo2Precision(data.getAmount()));
        viewHolder.mTextViewPaymentInterestAmount.setText(Helper.formatStringUpTo2Precision(data.getInterestAmt()));
        viewHolder.mTextViewPaymentDate.setText(Helper.getAppDateFormatFromApiDateFormat(data.getDate()));
        viewHolder.mTextViewPaymentType.setText(data.getPaymentType());
        viewHolder.mTextViewInterestRate.setText(String.format(viewHolder.mTextViewInterestRate.getContext().getString(R.string.string_interest_text), Helper.formatStringUpTo2Precision(data.getInterestRate())));

        if (AppConstant.INTEREST_PAID.equalsIgnoreCase(data.getInterestPaid())) {
            viewHolder.mTextViewPaymentTotalAmountPaid.setText(Helper.formatStringUpTo2Precision(String.valueOf(Float.parseFloat(data.getAmount()) + Float.parseFloat(data.getInterestAmt()))));
            viewHolder.mCheckBoxInterestPaid.setChecked(true);
        } else {
            viewHolder.mTextViewPaymentTotalAmountPaid.setText(Helper.formatStringUpTo2Precision(data.getAmount()));
            viewHolder.mCheckBoxInterestPaid.setChecked(false);
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView mTextViewPaymentDate;
        private AppCompatTextView mTextViewPaymentType;
        private AppCompatTextView mTextViewPaymentAmountPaid;
        private AppCompatTextView mTextViewPaymentInterestAmount;
        private AppCompatTextView mTextViewPaymentTotalAmountPaid;
        private AppCompatTextView mTextViewInterestRate;
        private AppCompatCheckBox mCheckBoxInterestPaid;

        private ViewHolder(View itemView) {
            super(itemView);
            mTextViewPaymentDate = itemView.findViewById(R.id.row_payment_tv_date);
            mTextViewPaymentType = itemView.findViewById(R.id.row_payment_tv_payment_type);
            mTextViewPaymentAmountPaid = itemView.findViewById(R.id.row_payment_tv_paid_amount);
            mTextViewPaymentInterestAmount = itemView.findViewById(R.id.row_payment_tv_interest_amount);
            mTextViewPaymentTotalAmountPaid = itemView.findViewById(R.id.row_payment_tv_total_paid_amount);
            mTextViewInterestRate = itemView.findViewById(R.id.row_payment_tv_interest_rate);
            mCheckBoxInterestPaid = itemView.findViewById(R.id.row_payment_cb_interest_paid);
        }
    }
}
