package ig.com.digitalmandi.adapter.supplier;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.bean.response.seller.SupplierOrderDetailListResponse;
import ig.com.digitalmandi.util.Utils;

public class SupplierOrderDetailAdapter extends RecyclerView.Adapter<SupplierOrderDetailAdapter.ViewHolder> {

    private List<SupplierOrderDetailListResponse.OrderDetail> mDataList;

    public SupplierOrderDetailAdapter(List<SupplierOrderDetailListResponse.OrderDetail> pDataList) {
        this.mDataList = pDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_order_details_cardview, parent, false);
        return new ViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SupplierOrderDetailListResponse.OrderDetail data = mDataList.get(position);
        holder.mTextViewName.setText(data.getProductName());
        holder.mTextViewUnit.setText(Utils.formatStringUpTo2Precision(data.getUnitValue()));
        holder.mTextViewQty.setText(data.getQty());
        holder.mTextViewPrice.setText(Utils.formatStringUpTo2Precision(data.getPrice()));
        holder.mTextViewTotalAmount.setText(Utils.formatStringUpTo2Precision(data.getTotalPrice()));
        holder.mTextViewQtyInKg.setText(Utils.formatStringUpTo2Precision(data.getQtyInKg()) + "(KG)");
        holder.mTextViewQtyInQuintal.setText(Utils.formatStringUpTo2Precision(String.valueOf(Float.parseFloat(data.getQtyInKg()) * .01f)) + "(Q)");
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView mTextViewName;
        private AppCompatTextView mTextViewUnit;
        private AppCompatTextView mTextViewQty;
        private AppCompatTextView mTextViewPrice;
        private AppCompatTextView mTextViewTotalAmount;
        private AppCompatTextView mTextViewQtyInKg;
        private AppCompatTextView mTextViewQtyInQuintal;

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
