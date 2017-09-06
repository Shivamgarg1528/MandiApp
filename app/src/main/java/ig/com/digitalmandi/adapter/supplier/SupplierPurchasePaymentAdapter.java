package ig.com.digitalmandi.adapter.supplier;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.bean.response.seller.PaymentsResponse;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Utils;

public class SupplierPurchasePaymentAdapter extends RecyclerView.Adapter<SupplierPurchasePaymentAdapter.ViewHolder> {

    private List<PaymentsResponse.Payment> mDataList;

    public SupplierPurchasePaymentAdapter(List<PaymentsResponse.Payment> pDataList) {
        this.mDataList = pDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_payment_details, parent, false);
        return new ViewHolder(holderView);
    }

    public void notifyData(TextView pEmptyView) {
        pEmptyView.setVisibility(mDataList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        PaymentsResponse.Payment purchaseObject = mDataList.get(position);
        holder.rowPurchasePaymentListAmountPaid.setText(Utils.formatStringUpTo2Precision(purchaseObject.getAmount()));
        holder.rowPurchaseInterestAmt.setText(Utils.formatStringUpTo2Precision(purchaseObject.getInterestAmt()));
        holder.rowPurchasePaymentdate.setText(purchaseObject.getDate());
        holder.rowPurchasePaymentType.setText(purchaseObject.getPaymentType());
        holder.mTextViewInterestRate.setText(String.format(holder.mTextViewInterestRate.getContext().getString(R.string.string_interest_text), Utils.formatStringUpTo2Precision(purchaseObject.getInterestRate())));
        if (AppConstant.INTEREST_PAID.equalsIgnoreCase(purchaseObject.getInterestPaid())) {
            holder.rowPurchaseTotalAmtPaid.setText(Utils.formatStringUpTo2Precision(String.valueOf(Float.parseFloat(purchaseObject.getAmount()) + Float.parseFloat(purchaseObject.getInterestAmt()))));
            holder.rowPurchaseCheckBox.setChecked(true);
        } else {
            holder.rowPurchaseTotalAmtPaid.setText(Utils.formatStringUpTo2Precision(purchaseObject.getAmount()));
            holder.rowPurchaseCheckBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rowPurchasePaymentdate)
        AppCompatTextView rowPurchasePaymentdate;
        @BindView(R.id.rowPurchasePaymentType)
        AppCompatTextView rowPurchasePaymentType;
        @BindView(R.id.rowPurchaseAmtPaidValue)
        AppCompatTextView rowPurchasePaymentListAmountPaid;
        @BindView(R.id.rowPurchaseInterestAmt)
        AppCompatTextView rowPurchaseInterestAmt;
        @BindView(R.id.rowPurchaseTotalAmtPaid)
        AppCompatTextView rowPurchaseTotalAmtPaid;
        @BindView(R.id.mTextViewInterestRate)
        AppCompatTextView mTextViewInterestRate;
        @BindView(R.id.rowPurchaseCheckBox)
        AppCompatCheckBox rowPurchaseCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
