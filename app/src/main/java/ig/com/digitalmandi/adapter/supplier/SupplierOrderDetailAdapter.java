package ig.com.digitalmandi.adapter.supplier;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderDetailListResponse;
import ig.com.digitalmandi.utils.Utils;

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
        holder.rowOrderDetailProductName.setText(data.getProductName());
        holder.rowOrderDetailProductUnit.setText(Utils.formatStringUpTo2Precision(data.getUnitValue()));
        holder.rowOrderDetailProductQty.setText(data.getQty());
        holder.rowOrderDetailProductPrice.setText(Utils.formatStringUpTo2Precision(data.getPrice()));
        holder.rowOrderDetailTotalAmount.setText(Utils.formatStringUpTo2Precision(data.getTotalPrice()));
        holder.rowOrderDetailQtyInKg.setText(Utils.formatStringUpTo2Precision(data.getQtyInKg()) + "(KG)");
        holder.rowOrderDetailQtyInQuintal.setText(Utils.formatStringUpTo2Precision(String.valueOf(Float.parseFloat(data.getQtyInKg()) * .01f)) + "(Q)");
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rowOrderDetailProductName)
        AppCompatTextView rowOrderDetailProductName;
        @BindView(R.id.rowOrderDetailProductUnit)
        AppCompatTextView rowOrderDetailProductUnit;
        @BindView(R.id.rowOrderDetailProductQty)
        AppCompatTextView rowOrderDetailProductQty;
        @BindView(R.id.rowOrderDetailProductPrice)
        AppCompatTextView rowOrderDetailProductPrice;
        @BindView(R.id.rowOrderDetailTotalAmount)
        AppCompatTextView rowOrderDetailTotalAmount;
        @BindView(R.id.rowOrderDetailQtyInKg)
        AppCompatTextView rowOrderDetailQtyInKg;
        @BindView(R.id.rowOrderDetailQtyInQuintal)
        AppCompatTextView rowOrderDetailQtyInQuintal;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
