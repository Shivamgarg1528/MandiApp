package ig.com.digitalmandi.adapter.supplier;

import android.support.v7.app.AppCompatActivity;
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
import ig.com.digitalmandi.beans.response.supplier.SupplierPaymentListRes;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by shivam.garg on 25-10-2016.
 */

public class SupplierPurchasePaymentAdapter extends RecyclerView.Adapter<SupplierPurchasePaymentAdapter.ViewHolder> {

    private List<SupplierPaymentListRes.ResultBean> paymentList;
    private AppCompatActivity mHostActivity;

    public SupplierPurchasePaymentAdapter(List<SupplierPaymentListRes.ResultBean> purchaseList, AppCompatActivity mHostActivity) {
        this.paymentList  = purchaseList;
        this.mHostActivity = mHostActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_payment_details, parent, false);
        return new ViewHolder(holderView);
    }

    public void notifyData(TextView emptyView) {
        if (paymentList.isEmpty())
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SupplierPaymentListRes.ResultBean purchaseObject = paymentList.get(position);
        holder.rowPurchasePaymentListAmountPaid.setText(Utils.formatStringUpTo2Precision(purchaseObject.getAmount()));
        holder.rowPurchaseInterestAmt.setText(Utils.formatStringUpTo2Precision(purchaseObject.getInterestAmt()));
        holder.rowPurchasePaymentdate          .setText(purchaseObject.getDate());
        holder.rowPurchasePaymentType          .setText(purchaseObject.getPaymentType());
        holder.mTextViewInterestRate.setText("@" + Utils.formatStringUpTo2Precision(purchaseObject.getInterestRate()) + "% Interest");
        holder.rowPurchaseCheckBox             .setChecked(purchaseObject.getInterestPaid().equalsIgnoreCase("1"));
        if(purchaseObject.getInterestPaid().equalsIgnoreCase("1")) {
            holder.rowPurchaseTotalAmtPaid.setText(Utils.formatStringUpTo2Precision(String.valueOf(Float.parseFloat(purchaseObject.getAmount()) + Float.parseFloat(purchaseObject.getInterestAmt()))));
        }
        else {
            holder.rowPurchaseTotalAmtPaid.setText(Utils.formatStringUpTo2Precision(purchaseObject.getAmount()));
        }

    }

    @Override
    public int getItemCount() {
        return paymentList.size();
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
