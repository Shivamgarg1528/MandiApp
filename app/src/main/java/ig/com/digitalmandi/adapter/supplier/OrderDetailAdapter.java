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
import ig.com.digitalmandi.bean.response.seller.OrderDetailResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class OrderDetailAdapter extends BaseAdapter<OrderDetailResponse.OrderDetail> {

    private final boolean mEnableListener;

    public OrderDetailAdapter(BaseActivity pBaseActivity, List<OrderDetailResponse.OrderDetail> pDataList, EventCallback pEventCallback, boolean pEnableListener) {
        super(pBaseActivity, pDataList, pEventCallback);
        this.mEnableListener = pEnableListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(mBaseActivity).inflate(R.layout.row_order_details, parent, false);
        return new ViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        OrderDetailResponse.OrderDetail data = mDataList.get(position);
        viewHolder.mTextViewName.setText(data.getProductName());
        viewHolder.mTextViewUnit.setText(Helper.formatStringUpTo2Precision(data.getUnitValue()));
        viewHolder.mTextViewQty.setText(data.getQty());
        viewHolder.mTextViewPrice.setText(Helper.formatStringUpTo2Precision(data.getPrice()));
        viewHolder.mTextViewTotalAmount.setText(Helper.formatStringUpTo2Precision(data.getTotalPrice()));
        viewHolder.mTextViewQtyInKg.setText(String.format(mBaseActivity.getString(R.string.string_kg), Helper.formatStringUpTo2Precision(data.getQtyInKg())));
        viewHolder.mTextViewQtyInQuintal.setText(String.format(mBaseActivity.getString(R.string.string_qunital), Helper.formatStringUpTo2Precision(String.valueOf(Float.parseFloat(data.getQtyInKg()) * .01f))));
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mEnableListener) {
                    final OrderDetailResponse.OrderDetail data = mDataList.get(holder.getAdapterPosition());
                    CharSequence array[] = {
                            mBaseActivity.getString(R.string.string_delete)
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(mBaseActivity);
                    builder.setTitle(mBaseActivity.getString(R.string.string_select_operation));
                    builder.setItems(array, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                                case 0:
                                    mDataList.remove(viewHolder.getAdapterPosition());
                                    mEventCallback.onEvent(AppConstant.OPERATION_DELETE, data);
                                    break;
                            }
                        }
                    });
                    builder.show();
                }
                return true;
            }
        });
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatTextView mTextViewName;
        private final AppCompatTextView mTextViewUnit;
        private final AppCompatTextView mTextViewQty;
        private final AppCompatTextView mTextViewPrice;
        private final AppCompatTextView mTextViewTotalAmount;
        private final AppCompatTextView mTextViewQtyInKg;
        private final AppCompatTextView mTextViewQtyInQuintal;

        ViewHolder(View view) {
            super(view);
            mTextViewName = view.findViewById(R.id.row_order_details_tv_product_name);
            mTextViewUnit = view.findViewById(R.id.row_order_details_tv_unit);
            mTextViewQty = view.findViewById(R.id.row_order_details_tv_qty);
            mTextViewPrice = view.findViewById(R.id.row_order_details_tv_price);
            mTextViewTotalAmount = view.findViewById(R.id.row_order_details_tv_total_amount);
            mTextViewQtyInKg = view.findViewById(R.id.row_order_details_tv_qty_in_kg);
            mTextViewQtyInQuintal = view.findViewById(R.id.row_order_details_tv_qty_in_quintal);
        }
    }
}
