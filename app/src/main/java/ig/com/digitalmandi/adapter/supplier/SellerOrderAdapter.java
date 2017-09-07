package ig.com.digitalmandi.adapter.supplier;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.response.seller.SellerOrderResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class SellerOrderAdapter extends RecyclerView.Adapter<SellerOrderAdapter.ViewHolder> {

    private final List<SellerOrderResponse.Order> mDataList;
    private final BaseActivity mBaseActivity;
    private final EventCallback mEventCallback;

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
        holder.rowPurchaseDate.setText(Helper.onConvertDateStringToOtherStringFormat(data.getPurchaseDate()));

        holder.rowPurchaseProductName.setText(data.getProductName());
        holder.rowPurchaseProductUnit.setText(Helper.formatStringUpTo2Precision(data.getUnitValue()));
        holder.rowPurchaseProductQty.setText(data.getProductQty());
        holder.rowPurchaseProductPrice.setText(Helper.formatStringUpTo2Precision(data.getPurchaseAmtAcc100Kg()));

        holder.rowPurchaseSubTotal.setText(Helper.formatStringUpTo2Precision(data.getSubTotalAmt()));
        holder.rowPurchaseDaamiValue.setText(Helper.formatStringUpTo2Precision(data.getDaamiCost()));
        holder.rowPurchaseLabourValue.setText(Helper.formatStringUpTo2Precision(data.getLabourCost()));
        holder.rowPurchaseTotalValue.setText(Helper.formatStringUpTo2Precision(data.getTotalAmount()));

        holder.rowPurchaseQtyInHand.setText("Kg (" + Helper.formatStringUpTo2Precision(data.getProductInKg()) + ")");
        holder.rowPurchaseQtySold.setText("Kg (" + Helper.formatStringUpTo2Precision(data.getSumOfProductInKg()) + ")");

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


        final View parentView;
        AppCompatTextView rowPurchasePersonName;
        AppCompatTextView rowPurchaseDate;
        AppCompatTextView rowPurchaseProductName;
        AppCompatTextView rowPurchaseProductQty;
        AppCompatTextView rowPurchaseProductPrice;
        AppCompatTextView rowPurchaseDaamiValue;
        AppCompatTextView rowPurchaseLabourValue;
        AppCompatTextView rowPurchaseTotalValue;
        AppCompatTextView rowPurchaseQtyInHand;
        AppCompatTextView rowPurchaseQtySold;
        AppCompatTextView rowPurchaseProductUnit;
        AppCompatTextView rowPurchaseSubTotal;
        public ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            findView(itemView);
        }

        private void findView(View itemView) {
            rowPurchasePersonName = (AppCompatTextView) itemView.findViewById(R.id.rowPurchasePersonName);
            rowPurchaseDate = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseDate);
            rowPurchaseProductName = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseProductName);
            rowPurchaseProductQty = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseProductQty);
            rowPurchaseProductPrice = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseProductPrice);
            rowPurchaseDaamiValue = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseDaamiValue);
            rowPurchaseLabourValue = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseLabourValue);
            rowPurchaseTotalValue = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseTotalValue);
            rowPurchaseQtyInHand = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseQtyInHand);
            rowPurchaseQtySold = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseQtySold);
            rowPurchaseProductUnit = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseProductUnit);
            rowPurchaseSubTotal = (AppCompatTextView) itemView.findViewById(R.id.rowPurchaseSubTotal);
        }
    }
}
