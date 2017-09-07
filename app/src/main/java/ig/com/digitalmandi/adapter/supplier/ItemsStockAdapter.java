package ig.com.digitalmandi.adapter.supplier;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.response.seller.SellerOrderResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.Helper;

public class ItemsStockAdapter extends RecyclerView.Adapter<ItemsStockAdapter.ViewHolder> {

    private final BaseActivity mBaseActivity;
    private final List<SellerOrderResponse.Order> mDataList;
    private final EventCallback mEventCallback;

    public ItemsStockAdapter(BaseActivity pBaseActivity, List<SellerOrderResponse.Order> pDataList, EventCallback pEventCallback) {
        this.mBaseActivity = pBaseActivity;
        this.mDataList = pDataList;
        this.mEventCallback = pEventCallback;
    }

    public ItemsStockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_for_auto_complete_text_view, parent, false);
        return new ItemsStockAdapter.ViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(final ItemsStockAdapter.ViewHolder holder, int position) {

        SellerOrderResponse.Order data = mDataList.get(position);
        holder.rowAutoCompletePersonName.setText(data.getNameOfPerson());
        holder.rowAutoCompleteDate.setText(data.getPurchaseDate());
        holder.rowAutoCompleteProductName.setText(data.getProductName());
        holder.rowAutoCompleteProductQty.setText(Helper.formatStringUpTo2Precision(data.getLeftQty()));
        holder.rowAutoCompleteProductPrice.setText(Helper.formatStringUpTo2Precision(data.getPurchaseAmtAcc100Kg()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SellerOrderResponse.Order object = mDataList.get(holder.getAdapterPosition());
                mEventCallback.onEvent(0, object);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void notifyData(TextView emptyView) {
        if (mDataList.isEmpty())
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final AppCompatTextView rowAutoCompletePersonName;
        final AppCompatTextView rowAutoCompleteDate;
        final AppCompatTextView rowAutoCompleteProductName;
        final AppCompatTextView rowAutoCompleteProductQty;
        final AppCompatTextView rowAutoCompleteProductPrice;

        ViewHolder(View view) {
            super(view);
            rowAutoCompletePersonName = (AppCompatTextView) itemView.findViewById(R.id.rowAutoCompletePersonName);
            rowAutoCompleteDate = (AppCompatTextView) itemView.findViewById(R.id.rowAutoCompleteDate);
            rowAutoCompleteProductName = (AppCompatTextView) itemView.findViewById(R.id.rowAutoCompleteProductName);
            rowAutoCompleteProductQty = (AppCompatTextView) itemView.findViewById(R.id.rowAutoCompleteProductQty);
            rowAutoCompleteProductPrice = (AppCompatTextView) itemView.findViewById(R.id.rowAutoCompleteProductPrice);
        }
    }
}
