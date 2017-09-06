package ig.com.digitalmandi.adapter.supplier;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.response.seller.SellerOrderResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Utils;

public class SellerOrderAdapter extends RecyclerView.Adapter<SellerOrderAdapter.ViewHolder> {

    private List<SellerOrderResponse.Order> mDataList;
    private BaseActivity mBaseActivity;
    private EventCallback mEventCallback;

    public SellerOrderAdapter(List<SellerOrderResponse.Order> pDataList, BaseActivity pBaseActivity, EventCallback pEventCallback) {
        this.mDataList = pDataList;
        this.mBaseActivity = pBaseActivity;
        this.mEventCallback = pEventCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(mBaseActivity).inflate(R.layout.row_layout_supplier_purchase_cardview, parent, false);
        return new ViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SellerOrderResponse.Order data = mDataList.get(position);

        holder.rowPurchasePersonName.setText(data.getNameOfPerson());
        holder.rowPurchaseDate.setText(Utils.onConvertDateStringToOtherStringFormat(data.getPurchaseDate()));

        holder.rowPurchaseProductName.setText(data.getProductName());
        holder.rowPurchaseProductUnit.setText(Utils.formatStringUpTo2Precision(data.getUnitValue()));
        holder.rowPurchaseProductQty.setText(data.getProductQty());
        holder.rowPurchaseProductPrice.setText(Utils.formatStringUpTo2Precision(data.getPurchaseAmtAcc100Kg()));

        holder.rowPurchaseSubTotal.setText(Utils.formatStringUpTo2Precision(data.getSubTotalAmt()));
        holder.rowPurchaseDaamiValue.setText(Utils.formatStringUpTo2Precision(data.getDaamiCost()));
        holder.rowPurchaseLabourValue.setText(Utils.formatStringUpTo2Precision(data.getLabourCost()));
        holder.rowPurchaseTotalValue.setText(Utils.formatStringUpTo2Precision(data.getTotalAmount()));

        holder.rowPurchaseQtyInHand.setText("Kg (" + Utils.formatStringUpTo2Precision(data.getProductInKg()) + ")");
        holder.rowPurchaseQtySold.setText("Kg (" + Utils.formatStringUpTo2Precision(data.getSumOfProductInKg()) + ")");

        holder.parentView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                final SellerOrderResponse.Order data = mDataList.get(holder.getAdapterPosition());
                CharSequence array[] = {
                        mBaseActivity.getString(R.string.string_delete),
                        mBaseActivity.getString(R.string.string_payment_details),
                        mBaseActivity.getString(R.string.string_sold_order_details),
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(mBaseActivity);
                builder.setTitle(mBaseActivity.getString(R.string.string_select_operation));
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {

                            case 0: {
                                mEventCallback.onEvent(AppConstant.OPERATION_DELETE, data);
                                break;
                            }

                            case 1: {
                                mEventCallback.onEvent(AppConstant.OPERATION_ORDER_PAYMENT_DETAILS, data);
                                break;
                            }

                            case 2: {
                                mEventCallback.onEvent(AppConstant.OPERATION_ORDER_DETAILS, data);
                                break;
                            }
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
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
