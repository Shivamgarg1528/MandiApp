package ig.com.digitalmandi.adapter.supplier;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.bean.response.seller.OrderDetailResponse;
import ig.com.digitalmandi.util.Helper;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    private final List<OrderDetailResponse.OrderDetail> mDataList;

    public OrderDetailAdapter(List<OrderDetailResponse.OrderDetail> pDataList) {
        this.mDataList = pDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_order_details_cardview, parent, false);
        return new ViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        OrderDetailResponse.OrderDetail data = mDataList.get(position);
        holder.mTextViewName.setText(data.getProductName());
        holder.mTextViewUnit.setText(Helper.formatStringUpTo2Precision(data.getUnitValue()));
        holder.mTextViewQty.setText(data.getQty());
        holder.mTextViewPrice.setText(Helper.formatStringUpTo2Precision(data.getPrice()));
        holder.mTextViewTotalAmount.setText(Helper.formatStringUpTo2Precision(data.getTotalPrice()));
        holder.mTextViewQtyInKg.setText(Helper.formatStringUpTo2Precision(data.getQtyInKg()) + "(KG)");
        holder.mTextViewQtyInQuintal.setText(Helper.formatStringUpTo2Precision(String.valueOf(Float.parseFloat(data.getQtyInKg()) * .01f)) + "(Q)");
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatTextView mTextViewName;
        private final AppCompatTextView mTextViewUnit;
        private final AppCompatTextView mTextViewQty;
        private final AppCompatTextView mTextViewPrice;
        private final AppCompatTextView mTextViewTotalAmount;
        private final AppCompatTextView mTextViewQtyInKg;
        private final AppCompatTextView mTextViewQtyInQuintal;

        ViewHolder(View view) {
            super(view);
            mTextViewName = (AppCompatTextView) view.findViewById(R.id.row_layout_order_details_tv_product_name);
            mTextViewUnit = (AppCompatTextView) view.findViewById(R.id.row_layout_order_details_tv_unit);
            mTextViewQty = (AppCompatTextView) view.findViewById(R.id.row_layout_order_details_tv_qty);
            mTextViewPrice = (AppCompatTextView) view.findViewById(R.id.row_layout_order_details_tv_price);
            mTextViewTotalAmount = (AppCompatTextView) view.findViewById(R.id.row_layout_order_details_tv_total_amount);
            mTextViewQtyInKg = (AppCompatTextView) view.findViewById(R.id.row_layout_order_details_tv_qty_in_kg);
            mTextViewQtyInQuintal = (AppCompatTextView) view.findViewById(R.id.row_layout_order_details_tv_qty_in_quintal);
        }
    }
}
