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
import ig.com.digitalmandi.adapter.BaseAdapter;
import ig.com.digitalmandi.bean.response.seller.PurchaseResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class PurchaseAdapter extends BaseAdapter<PurchaseResponse.Purchase> {

    public PurchaseAdapter(BaseActivity pBaseActivity, List<PurchaseResponse.Purchase> pDataList, EventCallback pEventCallback) {
        super(pBaseActivity, pDataList, pEventCallback);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(mBaseActivity).inflate(R.layout.row_purchase, parent, false);
        return new ViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ViewHolder viewHolder = (ViewHolder) holder;
        PurchaseResponse.Purchase data = mDataList.get(position);

        viewHolder.mTextViewPersonName.setText(data.getNameOfPerson());
        viewHolder.mTextViewDate.setText(Helper.getAppDateFormatFromApiDateFormat(data.getPurchaseDate()));
        viewHolder.mTextViewProductName.setText(data.getProductName());
        viewHolder.mTextViewProductUnit.setText(Helper.formatStringUpTo2Precision(data.getUnitValue()));
        viewHolder.mTextViewProductQty.setText(data.getProductQty());
        viewHolder.mTextViewProductPrice.setText(Helper.formatStringUpTo2Precision(data.getPurchaseAmtAcc100Kg()));
        viewHolder.mTextViewSubTotal.setText(Helper.formatStringUpTo2Precision(data.getSubTotalAmt()));
        viewHolder.mTextViewDaamiValue.setText(Helper.formatStringUpTo2Precision(data.getDaamiCost()));
        viewHolder.mTextViewLabourValue.setText(Helper.formatStringUpTo2Precision(data.getLabourCost()));
        viewHolder.mTextViewTotalValue.setText(Helper.formatStringUpTo2Precision(data.getTotalAmount()));
        viewHolder.mTextViewQtyInHand.setText(mBaseActivity.getString(R.string.string_kg_in_hand) + "\n" + Helper.formatStringUpTo2Precision(data.getProductInKg()));
        viewHolder.mTextViewQtySold.setText(mBaseActivity.getString(R.string.string_kg_sold) + "\n" + Helper.formatStringUpTo2Precision(data.getSumOfProductInKg()));

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                final PurchaseResponse.Purchase data = mDataList.get(viewHolder.getAdapterPosition());
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

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView mTextViewPersonName;
        private AppCompatTextView mTextViewDate;
        private AppCompatTextView mTextViewProductName;
        private AppCompatTextView mTextViewProductQty;
        private AppCompatTextView mTextViewProductPrice;
        private AppCompatTextView mTextViewDaamiValue;
        private AppCompatTextView mTextViewLabourValue;
        private AppCompatTextView mTextViewTotalValue;
        private AppCompatTextView mTextViewQtyInHand;
        private AppCompatTextView mTextViewQtySold;
        private AppCompatTextView mTextViewProductUnit;
        private AppCompatTextView mTextViewSubTotal;

        private ViewHolder(View itemView) {
            super(itemView);
            mTextViewPersonName = itemView.findViewById(R.id.row_purchase_tv_person_name);
            mTextViewDate = itemView.findViewById(R.id.row_purchase_tv_date);
            mTextViewProductName = itemView.findViewById(R.id.row_purchase_tv_product_name);
            mTextViewProductUnit = itemView.findViewById(R.id.row_purchase_tv_unit);
            mTextViewProductQty = itemView.findViewById(R.id.row_purchase_tv_qty);
            mTextViewProductPrice = itemView.findViewById(R.id.row_purchase_tv_price);
            mTextViewSubTotal = itemView.findViewById(R.id.row_purchase_tv_subtotal);
            mTextViewDaamiValue = itemView.findViewById(R.id.row_purchase_tv_daami);
            mTextViewLabourValue = itemView.findViewById(R.id.row_purchase_tv_labour_value);
            mTextViewTotalValue = itemView.findViewById(R.id.row_purchase_tv_total_value);
            mTextViewQtyInHand = itemView.findViewById(R.id.row_purchase_tv_in_hand_kg);
            mTextViewQtySold = itemView.findViewById(R.id.row_purchase_tv_sold_kg);
        }
    }
}
