package ig.com.digitalmandi.adapter.supplier;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderAddRequest;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class ItemsCartAdapter extends RecyclerView.Adapter<ItemsCartAdapter.ViewHolder> {

    private final List<SupplierOrderAddRequest.OrderDetailsBean> mDataList;
    private final BaseActivity mBaseActivity;
    private final EventCallback mEventCallback;

    public ItemsCartAdapter(List<SupplierOrderAddRequest.OrderDetailsBean> pDataList, BaseActivity pBaseActivity, EventCallback pEventCallback) {
        this.mDataList = pDataList;
        this.mBaseActivity = pBaseActivity;
        this.mEventCallback = pEventCallback;
    }

    @Override
    public ItemsCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(mBaseActivity).inflate(R.layout.row_order_details_cardview, parent, false);
        return new ItemsCartAdapter.ViewHolder(holderView);
    }

    public void notifyData(TextView emptyView) {
        if (mDataList.isEmpty())
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ItemsCartAdapter.ViewHolder holder, int position) {

        SupplierOrderAddRequest.OrderDetailsBean data = mDataList.get(position);
        holder.rowOrderDetailProductName.setText(data.getProductName());
        holder.rowOrderDetailProductUnit.setText(Helper.formatStringUpTo2Precision(data.getUnitValue()));
        holder.rowOrderDetailProductQty.setText(data.getQty());
        holder.rowOrderDetailProductPrice.setText(Helper.formatStringUpTo2Precision(data.getPrice()));
        holder.rowOrderDetailTotalAmount.setText(Helper.formatStringUpTo2Precision(data.getTotalPrice()));
        holder.rowOrderDetailQtyInKg.setText(Helper.formatStringUpTo2Precision(data.getQtyInKg()) + "(KG)");
        holder.rowOrderDetailQtyInQuintal.setText(Helper.formatStringUpTo2Precision(String.valueOf(Float.parseFloat(data.getQtyInKg()) * .01f)) + "(Q)");

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final SupplierOrderAddRequest.OrderDetailsBean data = mDataList.get(holder.getAdapterPosition());
                CharSequence array[] = {mBaseActivity.getString(R.string.string_delete)};

                AlertDialog.Builder builder = new AlertDialog.Builder(mBaseActivity);
                builder.setTitle(mBaseActivity.getString(R.string.string_select_operation));
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                mDataList.remove(holder.getAdapterPosition());
                                mEventCallback.onEvent(AppConstant.OPERATION_DELETE, data);
                                break;
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


        final View mView;
        AppCompatTextView rowOrderDetailProductName;
        AppCompatTextView rowOrderDetailProductUnit;
        AppCompatTextView rowOrderDetailProductQty;
        AppCompatTextView rowOrderDetailProductPrice;
        AppCompatTextView rowOrderDetailTotalAmount;
        AppCompatTextView rowOrderDetailQtyInKg;
        AppCompatTextView rowOrderDetailQtyInQuintal;
        ViewHolder(View view) {
            super(view);
            mView = view;
            findView(itemView);
        }

        private void findView(View itemView) {
            rowOrderDetailProductName = (AppCompatTextView) itemView.findViewById(R.id.row_layout_order_details_tv_product_name);
            rowOrderDetailProductUnit = (AppCompatTextView) itemView.findViewById(R.id.row_layout_order_details_tv_unit);
            rowOrderDetailProductQty = (AppCompatTextView) itemView.findViewById(R.id.row_layout_order_details_tv_qty);
            rowOrderDetailProductPrice = (AppCompatTextView) itemView.findViewById(R.id.row_layout_order_details_tv_price);
            rowOrderDetailTotalAmount = (AppCompatTextView) itemView.findViewById(R.id.row_layout_order_details_tv_total_amount);
            rowOrderDetailQtyInKg = (AppCompatTextView) itemView.findViewById(R.id.row_layout_order_details_tv_qty_in_kg);
            rowOrderDetailQtyInQuintal = (AppCompatTextView) itemView.findViewById(R.id.row_layout_order_details_tv_qty_in_quintal);
        }
    }
}
