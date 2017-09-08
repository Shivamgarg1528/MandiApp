package ig.com.digitalmandi.adapter.supplier;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.adapter.BaseAdapter;
import ig.com.digitalmandi.bean.request.seller.CustomerResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class CustomerAdapter extends BaseAdapter<CustomerResponse.Customer> {

    public CustomerAdapter(BaseActivity pBaseActivity, List<CustomerResponse.Customer> pDataList, EventCallback pEventCallback) {
        super(pBaseActivity, pDataList, pEventCallback);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ViewHolder viewHolder = (ViewHolder) holder;
        CustomerResponse.Customer data = mDataList.get(position);

        viewHolder.mTextViewCustomerFirmName.setText(data.getUserFirmName());
        viewHolder.mTextViewCustomerName.setText(data.getUserName());

        Helper.setImage(mBaseActivity, data.getUserImageUrl(), viewHolder.mTextViewCustomerImage);

        viewHolder.mTextViewCustomerImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CustomerResponse.Customer data = mDataList.get(viewHolder.getAdapterPosition());
                mEventCallback.onEvent(AppConstant.OPERATION_CUSTOMER_OPEN, data);
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerResponse.Customer data = mDataList.get(viewHolder.getAdapterPosition());
                mEventCallback.onEvent(AppConstant.OPERATION_CUSTOMER_ORDERS, data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView mTextViewCustomerImage;
        private final AppCompatTextView mTextViewCustomerName;
        private final AppCompatTextView mTextViewCustomerFirmName;

        ViewHolder(View view) {
            super(view);
            mTextViewCustomerImage = view.findViewById(R.id.row_customer_iv_customer_image);
            mTextViewCustomerName = view.findViewById(R.id.row_customer_tv_customer_name);
            mTextViewCustomerFirmName = view.findViewById(R.id.row_customer_tv_customer_firm_name);
        }
    }
}
