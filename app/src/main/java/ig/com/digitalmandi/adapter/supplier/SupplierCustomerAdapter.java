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
import ig.com.digitalmandi.bean.request.seller.SellerCustomerList;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class SupplierCustomerAdapter extends RecyclerView.Adapter<SupplierCustomerAdapter.ViewHolder> {

    private final BaseActivity mBaseActivity;
    private final EventCallback mEventCallback;
    private final List<SellerCustomerList.Customer> mDataList;

    public SupplierCustomerAdapter(List<SellerCustomerList.Customer> pDataList, BaseActivity pBaseActivity, EventCallback pEventCallback) {
        this.mDataList = pDataList;
        this.mBaseActivity = pBaseActivity;
        this.mEventCallback = pEventCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_supplier_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        SellerCustomerList.Customer data = mDataList.get(position);
        holder.mTextViewCustomerFirmName.setText(data.getUserFirmName());
        holder.mTextViewCustomerName.setText(data.getUserName());

        Helper.setImage(mBaseActivity, data.getUserImageUrl(), holder.mTextViewCustomerImage);

        holder.mTextViewCustomerImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SellerCustomerList.Customer data = mDataList.get(holder.getAdapterPosition());
                mEventCallback.onEvent(AppConstant.OPERATION_CUSTOMER_OPEN, data);
            }
        });

        holder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerCustomerList.Customer data = mDataList.get(holder.getAdapterPosition());
                mEventCallback.onEvent(AppConstant.OPERATION_CUSTOMER_ORDERS, data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final View mParentView;
        private final CircleImageView mTextViewCustomerImage;
        private final AppCompatTextView mTextViewCustomerName;
        private final AppCompatTextView mTextViewCustomerFirmName;

        public ViewHolder(View view) {
            super(view);
            mTextViewCustomerImage = (CircleImageView) view.findViewById(R.id.row_layout_seller_customer_iv_customer_image);
            mTextViewCustomerName = (AppCompatTextView) view.findViewById(R.id.row_layout_seller_customer_tv_customer_name);
            mTextViewCustomerFirmName = (AppCompatTextView) view.findViewById(R.id.row_layout_seller_customer_tv_customer_firm_name);
            mParentView = view;
        }
    }
}
