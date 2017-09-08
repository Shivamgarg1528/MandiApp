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
import ig.com.digitalmandi.bean.response.seller.OrdersResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class OrderAdapter extends BaseAdapter<OrdersResponse.Order> {

    public OrderAdapter(BaseActivity pBaseActivity, List<OrdersResponse.Order> pDataList, EventCallback pEventCallback) {
        super(pBaseActivity, pDataList, pEventCallback);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mBaseActivity).inflate(R.layout.row_order, parent, false);
        return new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        OrdersResponse.Order data = mDataList.get(position);

        viewHolder.mTextViewOrderDate.setText(Helper.getAppDateFormatFromApiDateFormat(data.getOrderDate()));
        viewHolder.mTextViewSubTotal.setText(Helper.formatStringUpTo2Precision(data.getOrderSubTotalAmt()));
        viewHolder.mTextViewOrderExpenses.setText(Helper.formatStringUpTo2Precision(data.getOrderDaamiAmt()));
        viewHolder.mTextViewLabourValue.setText(Helper.formatStringUpTo2Precision(data.getOrderLabourAmt()));
        viewHolder.mTextViewBardanaValue.setText(Helper.formatStringUpTo2Precision(data.getOrderBardanaAmt()));
        viewHolder.mTextViewTotalValue.setText(Helper.formatStringUpTo2Precision(data.getOrderTotalAmt()));
        viewHolder.mTextViewVehicleRent.setText(Helper.formatStringUpTo2Precision(data.getVechileRent()));
        viewHolder.mTextViewDriverNo.setText(String.format("%s\n%s%s", data.getDriverName().toUpperCase(), data.getDriverNumber(), mBaseActivity.getString(R.string.string_driver_mobile)));
        viewHolder.mTextViewTotalNagAndQuintal.setText(String.format("%s (N)\n%s (Q)", Helper.formatStringUpTo2Precision(data.getOrderTotalNag()), Helper.formatStringUpTo2Precision(data.getOrderTotalQuintal())));
        viewHolder.mTextViewOrderId.setText(data.getOrderId());

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                final OrdersResponse.Order orderObject = mDataList.get(viewHolder.getAdapterPosition());
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

    private static class ViewHolder extends RecyclerView.ViewHolder {

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

        ViewHolder(View view) {
            super(view);
            mTextViewOrderId = view.findViewById(R.id.row_order_tv_order_id);
            mTextViewOrderDate = view.findViewById(R.id.row_order_tv_order_date);
            mTextViewSubTotal = view.findViewById(R.id.row_order_tv_order_subtotal);
            mTextViewOrderExpenses = view.findViewById(R.id.row_order_tv_order_expenses);
            mTextViewLabourValue = view.findViewById(R.id.row_order_tv_order_labour_value);
            mTextViewBardanaValue = view.findViewById(R.id.row_order_tv_order_bardana_value);
            mTextViewTotalValue = view.findViewById(R.id.row_order_tv_order_total_value);
            mTextViewVehicleRent = view.findViewById(R.id.row_order_tv_order_vehicle_rent);
            mTextViewDriverNo = view.findViewById(R.id.row_order_tv_driver_number);
            mTextViewTotalNagAndQuintal = view.findViewById(R.id.row_order_tv_order_nag_and_quaintal);
        }
    }
}
