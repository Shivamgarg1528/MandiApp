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
import ig.com.digitalmandi.bean.response.seller.OrderResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class CustomerOrderAdapter extends RecyclerView.Adapter<CustomerOrderAdapter.ViewHolder> {

    private final List<OrderResponse.Order> mDataList;
    private final BaseActivity mBaseActivity;
    private final EventCallback mEventCallback;

    public CustomerOrderAdapter(List<OrderResponse.Order> pDataList, BaseActivity pBaseActivity, EventCallback pEventCallback) {
        this.mDataList = pDataList;
        this.mBaseActivity = pBaseActivity;
        this.mEventCallback = pEventCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_supplier_order_cardview, parent, false);
        return new ViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        OrderResponse.Order data = mDataList.get(position);

        holder.mTextViewOrderDate.setText(data.getOrderDate());
        holder.mTextViewSubTotal.setText(Helper.formatStringUpTo2Precision(data.getOrderSubTotalAmt()));
        holder.mTextViewOrderExpenses.setText(Helper.formatStringUpTo2Precision(data.getOrderDaamiAmt()));
        holder.mTextViewLabourValue.setText(Helper.formatStringUpTo2Precision(data.getOrderLabourAmt()));
        holder.mTextViewBardanaValue.setText(Helper.formatStringUpTo2Precision(data.getOrderBardanaAmt()));
        holder.mTextViewTotalValue.setText(Helper.formatStringUpTo2Precision(data.getOrderTotalAmt()));
        holder.mTextViewVehicleRent.setText(Helper.formatStringUpTo2Precision(data.getVechileRent()));
        holder.mTextViewDriverNo.setText(String.format("%s%s", data.getDriverNumber(), mBaseActivity.getString(R.string.string_driver_mobile)));
        holder.mTextViewTotalNagAndQuintal.setText(String.format("%s (N) - %s (Q)", Helper.formatStringUpTo2Precision(data.getOrderTotalNag()), Helper.formatStringUpTo2Precision(data.getOrderTotalQuintal())));
        holder.mTextViewOrderId.setText(data.getOrderId());

        holder.mParentView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                final OrderResponse.Order orderObject = mDataList.get(holder.getAdapterPosition());
                CharSequence array[] = {
                        mBaseActivity.getString(R.string.string_delete),
                        mBaseActivity.getString(R.string.string_payment_details),
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
                                mEventCallback.onEvent(AppConstant.OPERATION_DELETE, orderObject);
                                break;
                            }

                            case 1: {
                                mEventCallback.onEvent(AppConstant.OPERATION_ORDER_PAYMENT_DETAILS, orderObject);
                                break;
                            }

                            case 2: {
                                mEventCallback.onEvent(AppConstant.OPERATION_ORDER_DETAILS, orderObject);
                                break;
                            }

                            case 3: {
                                mEventCallback.onEvent(AppConstant.OPERATION_ORDER_BILL_PRINT, orderObject);
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
        private final AppCompatTextView mTextViewOrderId;
        private final AppCompatTextView mTextViewOrderDate;
        private final AppCompatTextView mTextViewSubTotal;
        private final AppCompatTextView mTextViewOrderExpenses;
        private final AppCompatTextView mTextViewLabourValue;
        private final AppCompatTextView mTextViewTotalValue;
        private final AppCompatTextView mTextViewDriverNo;
        private final AppCompatTextView mTextViewVehicleRent;
        private final AppCompatTextView mTextViewTotalNagAndQuintal;
        private final AppCompatTextView mTextViewBardanaValue;
        private final View mParentView;

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
