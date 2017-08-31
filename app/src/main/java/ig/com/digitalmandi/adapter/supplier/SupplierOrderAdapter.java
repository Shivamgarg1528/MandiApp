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
import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderListResponse;
import ig.com.digitalmandi.interfaces.EventListener;
import ig.com.digitalmandi.utils.AppConstant;
import ig.com.digitalmandi.utils.Utils;

public class SupplierOrderAdapter extends RecyclerView.Adapter<SupplierOrderAdapter.ViewHolder> {

    private List<SupplierOrderListResponse.Order> mDataList;
    private BaseActivity mBaseActivity;
    private EventListener mEventListener;

    public SupplierOrderAdapter(List<SupplierOrderListResponse.Order> pDataList, BaseActivity pBaseActivity, EventListener pEventListener) {
        this.mDataList = pDataList;
        this.mBaseActivity = pBaseActivity;
        this.mEventListener = pEventListener;
    }

    public void notifyData(TextView emptyView) {
        if (mDataList.isEmpty())
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_supplier_order_cardview, parent, false);
        return new ViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SupplierOrderListResponse.Order data = mDataList.get(position);

        holder.mTextViewOrderDate.setText(data.getOrderDate());
        holder.mTextViewSubTotal.setText(Utils.formatStringUpTo2Precision(data.getOrderSubTotalAmt()));
        holder.mTextViewOrderExpenses.setText(Utils.formatStringUpTo2Precision(data.getOrderDaamiAmt()));
        holder.mTextViewLabourValue.setText(Utils.formatStringUpTo2Precision(data.getOrderLabourAmt()));
        holder.mTextViewBardanaValue.setText(Utils.formatStringUpTo2Precision(data.getOrderBardanaAmt()));
        holder.mTextViewTotalValue.setText(Utils.formatStringUpTo2Precision(data.getOrderTotalAmt()));
        holder.mTextViewVehicleRent.setText(Utils.formatStringUpTo2Precision(data.getVechileRent()));
        holder.mTextViewDriverNo.setText(String.format("%s%s", data.getDriverNumber(), mBaseActivity.getString(R.string.string_driver_mobile)));
        holder.mTextViewTotalNagAndQuintal.setText(String.format("%s (N) - %s (Q)", Utils.formatStringUpTo2Precision(data.getOrderTotalNag()), Utils.formatStringUpTo2Precision(data.getOrderTotalQuintal())));
        holder.mTextViewOrderId.setText(data.getOrderId());

        holder.mParentView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                final SupplierOrderListResponse.Order orderObject = mDataList.get(holder.getAdapterPosition());
                CharSequence array[] = {
                        mBaseActivity.getString(R.string.string_delete),
                        mBaseActivity.getString(R.string.string_payment_history),
                        mBaseActivity.getString(R.string.string_order_details),
                        mBaseActivity.getString(R.string.string_bill_print)
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(mBaseActivity);
                builder.setTitle(mBaseActivity.getString(R.string.string_select_operation));
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {

                            case 0: {
                                mEventListener.onEvent(AppConstant.OPERATION_DELETE, orderObject);
                                break;
                            }

                            case 1: {
                                mEventListener.onEvent(AppConstant.OPERATION_ORDER_PAYMENT_DETAILS, orderObject);
                                break;
                            }

                            case 2: {
                                mEventListener.onEvent(AppConstant.OPERATION_ORDER_DETAILS, orderObject);
                                break;
                            }

                            case 3: {
                                mEventListener.onEvent(AppConstant.OPERATION_ORDER_BILL_PRINT, orderObject);
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
        private AppCompatTextView mTextViewOrderId;
        private AppCompatTextView mTextViewOrderDate;
        private AppCompatTextView mTextViewSubTotal;
        private AppCompatTextView mTextViewOrderExpenses;
        private AppCompatTextView mTextViewLabourValue;
        private AppCompatTextView mTextViewTotalValue;
        private AppCompatTextView mTextViewDriverNo;
        private AppCompatTextView mTextViewVehicleRent;
        private AppCompatTextView mTextViewTotalNagAndQuintal;
        private AppCompatTextView mTextViewBardanaValue;
        private View mParentView;

        ViewHolder(View view) {
            super(view);
            mParentView = view;
            mTextViewOrderId = (AppCompatTextView) view.findViewById(R.id.row_layout_supplier_order_tv_order_id);
            mTextViewOrderDate = (AppCompatTextView) view.findViewById(R.id.row_layout_supplier_order_tv_order_date);
            mTextViewSubTotal = (AppCompatTextView) view.findViewById(R.id.row_layout_supplier_order_tv_order_subtotal);
            mTextViewOrderExpenses = (AppCompatTextView) view.findViewById(R.id.row_layout_supplier_order_tv_order_expenses);
            mTextViewLabourValue = (AppCompatTextView) view.findViewById(R.id.row_layout_supplier_order_tv_order_labour_value);
            mTextViewBardanaValue = (AppCompatTextView) view.findViewById(R.id.row_layout_supplier_order_tv_order_bardana_value);
            mTextViewTotalValue = (AppCompatTextView) view.findViewById(R.id.row_layout_supplier_order_tv_order_total_value);
            mTextViewVehicleRent = (AppCompatTextView) view.findViewById(R.id.row_layout_supplier_order_tv_order_vehicle_rent);
            mTextViewDriverNo = (AppCompatTextView) view.findViewById(R.id.row_layout_supplier_order_tv_driver_number);
            mTextViewTotalNagAndQuintal = (AppCompatTextView) view.findViewById(R.id.row_layout_supplier_order_tv_order_nag_and_quaintal);
        }
    }
}
