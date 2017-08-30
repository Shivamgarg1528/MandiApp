package ig.com.digitalmandi.adapter.supplier;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.beans.response.supplier.SupplierPurchaseListRes;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by shivam.garg on 24-10-2016.
 */

public class SupplierPurchaseAdapter extends RecyclerView.Adapter<SupplierPurchaseAdapter.ViewHolder> {

    private List<SupplierPurchaseListRes.ResultBean> purchaseList;
    private AppCompatActivity mHostActivity;
    private PurchaseCallBack callBack;

    public SupplierPurchaseAdapter(List<SupplierPurchaseListRes.ResultBean> purchaseList, AppCompatActivity mHostActivity, PurchaseCallBack callBack) {
        this.purchaseList = purchaseList;
        this.mHostActivity = mHostActivity;
        this.callBack = callBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_supplier_purchase_cardview, parent, false);
        return new ViewHolder(holderView);
    }

    public void notifyData(TextView emptyView) {
        if (purchaseList.isEmpty())
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SupplierPurchaseListRes.ResultBean purchaseObject = purchaseList.get(position);

        holder.rowPurchasePersonName   .setText(purchaseObject.getNameOfPerson());
        holder.rowPurchaseDate         .setText(Utils.onConvertDateStringToOtherStringFormat(purchaseObject.getPurchaseDate()));

        holder.rowPurchaseProductName  .setText(purchaseObject.getProductName());
        holder.rowPurchaseProductUnit.setText(Utils.formatStringUpTo2Precision(purchaseObject.getUnitValue()));
        holder.rowPurchaseProductQty   .setText(purchaseObject.getProductQty());
        holder.rowPurchaseProductPrice.setText(Utils.formatStringUpTo2Precision(purchaseObject.getPurchaseAmtAcc100Kg()));

        holder.rowPurchaseSubTotal.setText(Utils.formatStringUpTo2Precision(purchaseObject.getSubTotalAmt()));
        holder.rowPurchaseDaamiValue.setText(Utils.formatStringUpTo2Precision(purchaseObject.getDaamiCost()));
        holder.rowPurchaseLabourValue.setText(Utils.formatStringUpTo2Precision(purchaseObject.getLabourCost()));
        holder.rowPurchaseTotalValue.setText(Utils.formatStringUpTo2Precision(purchaseObject.getTotalAmount()));

        holder.rowPurchaseQtyInHand.setText("Kg (" + Utils.formatStringUpTo2Precision(purchaseObject.getProductInKg()) + ")");
        holder.rowPurchaseQtySold.setText("Kg (" + Utils.formatStringUpTo2Precision(purchaseObject.getSumOfProductInKg()) + ")");

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                final SupplierPurchaseListRes.ResultBean purchaseObject = purchaseList.get(position);

                CharSequence array[] = {"Delete","Payment History","Sold History"};

                AlertDialog.Builder builder = new AlertDialog.Builder(mHostActivity);
                builder.setTitle("Select Operation");
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {

                            case 0:
                                callBack.onDelete(purchaseObject, view);
                                break;

                            case 1:
                                callBack.onPayment(purchaseObject, view);
                                break;

                            case 2:
                                callBack.onSales(purchaseObject, view);
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    public interface PurchaseCallBack {
        void onPayment(SupplierPurchaseListRes.ResultBean object, View view);

        void onDelete(SupplierPurchaseListRes.ResultBean object, View view);

        void onSales(SupplierPurchaseListRes.ResultBean object, View view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rowPurchasePersonName)
        AppCompatTextView rowPurchasePersonName;
        @BindView(R.id.rowPurchaseDate)
        AppCompatTextView rowPurchaseDate;
        @BindView(R.id.rowPurchaseProductName)
        AppCompatTextView rowPurchaseProductName;
        @BindView(R.id.rowPurchaseProductQty)
        AppCompatTextView rowPurchaseProductQty;
        @BindView(R.id.rowPurchaseProductPrice)
        AppCompatTextView rowPurchaseProductPrice;
        @BindView(R.id.rowPurchaseDaamiValue)
        AppCompatTextView rowPurchaseDaamiValue;
        @BindView(R.id.rowPurchaseLabourValue)
        AppCompatTextView rowPurchaseLabourValue;
        @BindView(R.id.rowPurchaseTotalValue)
        AppCompatTextView rowPurchaseTotalValue;
        @BindView(R.id.rowPurchaseQtyInHand)
        AppCompatTextView rowPurchaseQtyInHand;
        @BindView(R.id.rowPurchaseQtySold)
        AppCompatTextView rowPurchaseQtySold;
        @BindView(R.id.rowPurchaseProductUnit)
        AppCompatTextView rowPurchaseProductUnit;
        @BindView(R.id.rowPurchaseSubTotal)
        AppCompatTextView rowPurchaseSubTotal;
        View parentView;

        public ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
