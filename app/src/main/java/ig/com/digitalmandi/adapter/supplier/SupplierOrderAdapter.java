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
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderListRes;
import ig.com.digitalmandi.interfaces.OrderCallBack;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by Shivam.Garg on 27-10-2016.
 */

public class SupplierOrderAdapter extends RecyclerView.Adapter<SupplierOrderAdapter.OrderAdapter> {

    private List<SupplierOrderListRes.ResultBean> dataList;
    private AppCompatActivity mHostActivity;
    private OrderCallBack callBack;

    public SupplierOrderAdapter(List<SupplierOrderListRes.ResultBean> dataList, AppCompatActivity supplierCustomerOrderActivity, OrderCallBack callBack) {
        this.dataList = dataList;
        this.mHostActivity = supplierCustomerOrderActivity;
        this.callBack = callBack;
    }

    public void notifyData(TextView emptyView) {
        if (dataList.isEmpty())
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    @Override
    public OrderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_supplier_order_cardview, parent, false);
        return new OrderAdapter(holderView);
    }

    @Override
    public void onBindViewHolder(OrderAdapter holder, final int position) {
        SupplierOrderListRes.ResultBean orderObject = dataList.get(position);

        holder.rowOrderDate        .setText(orderObject.getOrderDate());
        holder.rowOrderSubTotal    .setText(Utils.onStringFormat(orderObject.getOrderSubTotalAmt()));
        holder.rowOrderExpenses    .setText(Utils.onStringFormat(orderObject.getOrderDaamiAmt()));
        holder.rowOrderLabourValue .setText(Utils.onStringFormat(orderObject.getOrderLabourAmt()));
        holder.rowOrderBardanaValue.setText(Utils.onStringFormat(orderObject.getOrderBardanaAmt()));
        holder.rowOrderTotalValue  .setText(Utils.onStringFormat(orderObject.getOrderTotalAmt()));
        holder.rowOrderVehicleRent .setText(Utils.onStringFormat(orderObject.getVechileRent()));
        holder.rowOrderDriverNo    .setText(orderObject.getDriverNumber()+" (M)");
        holder.rowOrderTotalNagAndQuintal.setText(Utils.onStringFormat(orderObject.getOrderTotalNag())+" (N) - "+Utils.onStringFormat(orderObject.getOrderTotalQuintal())+" (Q)");
        holder.rowOrderOrderId     .setText(orderObject.getOrderId());

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                final SupplierOrderListRes.ResultBean orderObject = dataList.get(position);
                CharSequence array[] = {"Delete","Payment History","Order Details","Bill Print"};

                AlertDialog.Builder builder = new AlertDialog.Builder(mHostActivity);
                builder.setTitle("Select Operation");
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {

                            case 0:
                                callBack.onDelete(orderObject, view);
                                break;

                            case 1:
                                callBack.onPayment(orderObject, view);
                                break;

                            case 2:
                                callBack.onDetail(orderObject, view);
                                break;

                            case 3:
                                callBack.onPrint(orderObject,view);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class OrderAdapter extends RecyclerView.ViewHolder  {
        @BindView(R.id.rowOrderOrderId)
        AppCompatTextView rowOrderOrderId;
        @BindView(R.id.rowOrderDate)
        AppCompatTextView rowOrderDate;
        @BindView(R.id.rowOrderSubTotal)
        AppCompatTextView rowOrderSubTotal;
        @BindView(R.id.rowOrderExpenses)
        AppCompatTextView rowOrderExpenses;
        @BindView(R.id.rowOrderLabourValue)
        AppCompatTextView rowOrderLabourValue;
        @BindView(R.id.rowOrderTotalValue)
        AppCompatTextView rowOrderTotalValue;
        @BindView(R.id.rowOrderDriverNo)
        AppCompatTextView rowOrderDriverNo;
        @BindView(R.id.rowOrderVehicleRent)
        AppCompatTextView rowOrderVehicleRent;
        @BindView(R.id.rowOrderTotalNagAndQuintal)
        AppCompatTextView rowOrderTotalNagAndQuintal;
        @BindView(R.id.rowOrderBardanaValue)
        AppCompatTextView rowOrderBardanaValue;
        View parentView;

        OrderAdapter(View view) {
            super(view);
            parentView = view;
            ButterKnife.bind(this, view);
        }
    }
}
