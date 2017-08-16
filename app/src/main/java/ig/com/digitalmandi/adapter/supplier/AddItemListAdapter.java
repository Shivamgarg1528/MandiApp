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
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderAddReq;
import ig.com.digitalmandi.interfaces.OrderCallBack;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by shiva on 10/31/2016.
 */

public class AddItemListAdapter extends RecyclerView.Adapter<AddItemListAdapter.ViewHolder> {

    private List<SupplierOrderAddReq.OrderDetailsBean> purchaseList;
    private AppCompatActivity mHostActivity;
    private OrderCallBack callBack;

    public AddItemListAdapter(List<SupplierOrderAddReq.OrderDetailsBean> purchaseList, AppCompatActivity mHostActivity, OrderCallBack callBack) {
        this.purchaseList = purchaseList;
        this.mHostActivity = mHostActivity;
        this.callBack = callBack;
    }

    @Override
    public AddItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_order_details_cardview, parent, false);
        return new AddItemListAdapter.ViewHolder(holderView);
    }

    public void notifyData(TextView emptyView) {
        if (purchaseList.isEmpty())
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
        notifyDataSetChanged();
        callBack.onDetail(null,null);
    }

    @Override
    public void onBindViewHolder(AddItemListAdapter.ViewHolder holder, final int position) {
        SupplierOrderAddReq.OrderDetailsBean purchaseObject = purchaseList.get(position);
        holder.rowOrderDetailProductName.setText(purchaseObject.getProductName());
        holder.rowOrderDetailProductUnit.setText(Utils.onStringFormat(purchaseObject.getUnitValue()));
        holder.rowOrderDetailProductQty.setText(purchaseObject.getQty());
        holder.rowOrderDetailProductPrice.setText(Utils.onStringFormat(purchaseObject.getPrice()));
        holder.rowOrderDetailTotalAmount.setText(Utils.onStringFormat(purchaseObject.getTotalPrice()));
        holder.rowOrderDetailQtyInKg.setText(Utils.onStringFormat(purchaseObject.getQtyInKg()) + "(KG)");
        holder.rowOrderDetailQtyInQuintal.setText(Utils.onStringFormat(String.valueOf(Float.parseFloat(purchaseObject.getQtyInKg()) * .01f)) + "(Q)");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                final SupplierOrderAddReq.OrderDetailsBean purchaseObject = purchaseList.get(position);
                CharSequence array[] = {"Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(mHostActivity);
                builder.setTitle("Select Operation");
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {

                            case 0:
                                purchaseList.remove(position);
                                callBack.onDelete(purchaseObject, view);
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
        View mView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
