package ig.com.digitalmandi.adapter.supplier;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.bean.response.seller.SellerOrderResponse;
import ig.com.digitalmandi.callback.AdapterCallback;
import ig.com.digitalmandi.util.Utils;

/**
 * Created by shiva on 11/5/2016.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private AppCompatActivity mHostActivity;
    private List<SellerOrderResponse.Order> originalList;
    private AdapterCallback callBack;

    public ItemsAdapter(AppCompatActivity mHostActivity, List<SellerOrderResponse.Order> originalList, AdapterCallback callBack) {
        this.mHostActivity = mHostActivity;
        this.originalList = originalList;
        this.callBack = callBack;
    }


    public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_for_auto_complete_text_view, parent, false);
        return new ItemsAdapter.ViewHolder(holderView);
    }

    public void notifyData(TextView emptyView) {
        if (originalList.isEmpty())
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(ItemsAdapter.ViewHolder holder, final int position) {

        SellerOrderResponse.Order object = originalList.get(position);
        holder.rowAutoCompletePersonName.setText(object.getNameOfPerson());
        holder.rowAutoCompleteDate.setText(object.getPurchaseDate());
        holder.rowAutoCompleteProductName.setText(object.getProductName());
        holder.rowAutoCompleteProductQty.setText(Utils.formatStringUpTo2Precision(object.onGetLeftQty()));
        holder.rowAutoCompleteProductPrice.setText(Utils.formatStringUpTo2Precision(object.getPurchaseAmtAcc100Kg()));
        holder.parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SellerOrderResponse.Order object = originalList.get(position);
                callBack.onItemClick(object);
            }
        });
    }

    @Override
    public int getItemCount() {
        return originalList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View parent;
        @BindView(R.id.rowAutoCompletePersonName)
        AppCompatTextView rowAutoCompletePersonName;
        @BindView(R.id.rowAutoCompleteDate)
        AppCompatTextView rowAutoCompleteDate;
        @BindView(R.id.rowAutoCompleteProductName)
        AppCompatTextView rowAutoCompleteProductName;
        @BindView(R.id.rowAutoCompleteProductQty)
        AppCompatTextView rowAutoCompleteProductQty;
        @BindView(R.id.rowAutoCompleteProductPrice)
        AppCompatTextView rowAutoCompleteProductPrice;

        ViewHolder(View view) {
            super(view);
            parent = view;
            ButterKnife.bind(this, view);
        }
    }
}
