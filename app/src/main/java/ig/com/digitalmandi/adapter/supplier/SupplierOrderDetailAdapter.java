package ig.com.digitalmandi.adapter.supplier;

import android.support.v7.app.AppCompatActivity;
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
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderDetailListRes;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by Shivam.Garg on 27-10-2016.
 */

public class SupplierOrderDetailAdapter extends RecyclerView.Adapter<SupplierOrderDetailAdapter.ViewHolder> {

    private List<SupplierOrderDetailListRes.ResultBean> purchaseList;
    private AppCompatActivity mHostActivity;

    public SupplierOrderDetailAdapter(List<SupplierOrderDetailListRes.ResultBean> purchaseList, AppCompatActivity mHostActivity) {
        this.purchaseList = purchaseList;
        this.mHostActivity = mHostActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_order_details_cardview, parent, false);
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
        SupplierOrderDetailListRes.ResultBean purchaseObject = purchaseList.get(position);
        holder.rowOrderDetailProductName  .setText(purchaseObject.getProductName());
        holder.rowOrderDetailProductUnit  .setText(Utils.onStringFormat(purchaseObject.getUnitValue()));
        holder.rowOrderDetailProductQty   .setText(purchaseObject.getQty());
        holder.rowOrderDetailProductPrice .setText(Utils.onStringFormat(purchaseObject.getPrice()));
        holder.rowOrderDetailTotalAmount  .setText(Utils.onStringFormat(purchaseObject.getTotalPrice()));
        holder.rowOrderDetailQtyInKg      .setText(Utils.onStringFormat(purchaseObject.getQtyInKg())+"(KG)");
        holder.rowOrderDetailQtyInQuintal .setText(Utils.onStringFormat(String.valueOf(Float.parseFloat(purchaseObject.getQtyInKg()) * .01f))+"(Q)");
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
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
