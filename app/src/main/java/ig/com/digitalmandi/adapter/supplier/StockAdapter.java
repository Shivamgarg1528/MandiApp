package ig.com.digitalmandi.adapter.supplier;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.adapter.BaseAdapter;
import ig.com.digitalmandi.bean.response.seller.PurchaseResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.Helper;

public class StockAdapter extends BaseAdapter<PurchaseResponse.Purchase> {

    public StockAdapter(BaseActivity pBaseActivity, List<PurchaseResponse.Purchase> pDataList, EventCallback pEventCallback) {
        super(pBaseActivity, pDataList, pEventCallback);
    }

    public StockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_stock, parent, false);
        return new StockAdapter.ViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ViewHolder viewHolder = (ViewHolder) holder;
        PurchaseResponse.Purchase data = mDataList.get(position);

        viewHolder.mTextViewPersonName.setText(data.getNameOfPerson());
        viewHolder.mTextViewDate.setText(Helper.getAppDateFormatFromApiDateFormat(data.getPurchaseDate()));
        viewHolder.mTextViewProductName.setText(data.getProductName());
        viewHolder.mTextViewProductQty.setText(Helper.formatStringUpTo2Precision(data.getLeftQty()));
        viewHolder.mTextViewProductPrice.setText(Helper.formatStringUpTo2Precision(data.getPurchaseAmtAcc100Kg()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PurchaseResponse.Purchase object = mDataList.get(viewHolder.getAdapterPosition());
                mEventCallback.onEvent(0, object);
            }
        });
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        final AppCompatTextView mTextViewPersonName;
        final AppCompatTextView mTextViewDate;
        final AppCompatTextView mTextViewProductName;
        final AppCompatTextView mTextViewProductQty;
        final AppCompatTextView mTextViewProductPrice;

        ViewHolder(View view) {
            super(view);
            mTextViewPersonName = itemView.findViewById(R.id.row_stock_tv_person_name);
            mTextViewDate = itemView.findViewById(R.id.row_stock_tv_date);
            mTextViewProductName = itemView.findViewById(R.id.row_stock_tv_product_name);
            mTextViewProductQty = itemView.findViewById(R.id.row_stock_tv_qty);
            mTextViewProductPrice = itemView.findViewById(R.id.row_stock_tv_price);
        }
    }
}
