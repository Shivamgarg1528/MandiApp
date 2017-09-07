package ig.com.digitalmandi.adapter.supplier;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.bean.response.seller.PaymentsResponse;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class SupplierPurchasePaymentAdapter extends RecyclerView.Adapter<SupplierPurchasePaymentAdapter.ViewHolder> {

    private final List<PaymentsResponse.Payment> mDataList;

    public SupplierPurchasePaymentAdapter(List<PaymentsResponse.Payment> pDataList) {
        this.mDataList = pDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payment_details, parent, false);
        return new ViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        PaymentsResponse.Payment purchaseObject = mDataList.get(position);
        holder.rowPurchasePaymentListAmountPaid.setText(Helper.formatStringUpTo2Precision(purchaseObject.getAmount()));
        holder.rowPurchaseInterestAmt.setText(Helper.formatStringUpTo2Precision(purchaseObject.getInterestAmt()));
        holder.rowPurchasePaymentdate.setText(purchaseObject.getDate());
        holder.rowPurchasePaymentType.setText(purchaseObject.getPaymentType());
        holder.mTextViewInterestRate.setText(String.format(holder.mTextViewInterestRate.getContext().getString(R.string.string_interest_text), Helper.formatStringUpTo2Precision(purchaseObject.getInterestRate())));
        if (AppConstant.INTEREST_PAID.equalsIgnoreCase(purchaseObject.getInterestPaid())) {
            holder.rowPurchaseTotalAmtPaid.setText(Helper.formatStringUpTo2Precision(String.valueOf(Float.parseFloat(purchaseObject.getAmount()) + Float.parseFloat(purchaseObject.getInterestAmt()))));
            holder.rowPurchaseCheckBox.setChecked(true);
        } else {
            holder.rowPurchaseTotalAmtPaid.setText(Helper.formatStringUpTo2Precision(purchaseObject.getAmount()));
            holder.rowPurchaseCheckBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {


        AppCompatTextView rowPurchasePaymentdate;
        AppCompatTextView rowPurchasePaymentType;
        AppCompatTextView rowPurchasePaymentListAmountPaid;
        AppCompatTextView rowPurchaseInterestAmt;
        AppCompatTextView rowPurchaseTotalAmtPaid;
        AppCompatTextView mTextViewInterestRate;
        AppCompatCheckBox rowPurchaseCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            findView(itemView);
        }

        private void findView(View itemView) {
            rowPurchasePaymentdate = (AppCompatTextView) itemView.findViewById(R.id.rowPurchasePaymentdate);
            rowPurchasePaymentType = (AppCompatTextView) itemView.findViewById(R.id.rowPurchasePaymentType);
            rowPurchasePaymentListAmountPaid = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseAmtPaidValue);
            rowPurchaseInterestAmt = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseInterestAmt);
            rowPurchaseTotalAmtPaid = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseTotalAmtPaid);
            mTextViewInterestRate = (AppCompatTextView) itemView.findViewById(R.id.mTextViewInterestRate);
            rowPurchaseCheckBox = (AppCompatCheckBox) itemView.findViewById(R.id.rowPurchaseCheckBox);
        }
    }
}
