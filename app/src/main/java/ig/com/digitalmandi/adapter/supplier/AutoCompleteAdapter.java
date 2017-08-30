package ig.com.digitalmandi.adapter.supplier;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.beans.response.supplier.SupplierPurchaseListRes;
import ig.com.digitalmandi.interfaces.AdapterCallBack;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by shiva on 10/28/2016.
 */

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<SupplierPurchaseListRes.ResultBean> originalList;
    private List<SupplierPurchaseListRes.ResultBean> suggestions = new ArrayList<>();
    private Filter filter = new CustomFilter();
    private AdapterCallBack callBack;

    /**
     * @param context      Context
     * @param originalList Original list used to compare in constraints.
     */
    public AutoCompleteAdapter(Context context, List<SupplierPurchaseListRes.ResultBean> originalList, AdapterCallBack callBack) {
        this.context      = context;
        this.originalList = originalList;
        this.callBack     = callBack;
    }

    @Override
    public int getCount() {
        return suggestions.size(); // Return the size of the suggestions list.
    }

    @Override
    public SupplierPurchaseListRes.ResultBean getItem(int position) {
        return suggestions.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * This is where you inflate the layout and also where you set what you want to display.
     * Here we also implement a View Holder in order to recycle the views.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_layout_for_auto_complete_text_view, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SupplierPurchaseListRes.ResultBean object = suggestions.get(position);
        holder.rowAutoCompletePersonName.setText(object.getNameOfPerson());
        holder.rowAutoCompleteDate.setText(object.getPurchaseDate());
        holder.rowAutoCompleteProductName.setText(object.getProductName());
        holder.rowAutoCompleteProductQty.setText(Utils.formatStringUpTo2Precision(object.onGetLeftQty()));
        holder.rowAutoCompleteProductPrice.setText(Utils.formatStringUpTo2Precision(object.getPurchaseAmtAcc100Kg()));
        holder.parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SupplierPurchaseListRes.ResultBean object = suggestions.get(position);
                callBack.onItemClick(object);
            }
        });
        return convertView;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    static class ViewHolder {
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
            parent = view;
            ButterKnife.bind(this, view);
        }
    }

    /**
     * Our Custom Filter Class.
     */
    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestions.clear();

            if (originalList != null && constraint != null) { // Check if the Original List and Constraint aren't null.
                for (int i = 0; i < originalList.size(); i++) {
                    SupplierPurchaseListRes.ResultBean preModel = originalList.get(i);
                    if (preModel.getProductInKg().contains(constraint) || preModel.getPurchaseDate().contains(constraint) || preModel.getProductName().toLowerCase().contains(constraint.toString().toLowerCase()) || preModel.getNameOfPerson().toLowerCase().contains(constraint.toString().toLowerCase())) { // Compare item in original list if it contains constraints.

                        if(Float.parseFloat(preModel.onGetLeftQty()) > 0)
                        suggestions.add(originalList.get(i)); // If TRUE add item in Suggestions.
                    }
                }
            }
            FilterResults results = new FilterResults(); // Create new Filter Results and return this to publishResults;
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
