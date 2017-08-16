package ig.com.digitalmandi.adapter.supplier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.supplier.SupplierCustomerOrderActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierCustomerListRes;
import ig.com.digitalmandi.dialogs.CustomerImageTapDialog;
import ig.com.digitalmandi.utils.Utils;

/**
 * Created by shiva on 10/11/2016.
 */

public class SupplierCustomerAdapter extends RecyclerView.Adapter<SupplierCustomerAdapter.ViewHolder> {

    List<SupplierCustomerListRes.ResultBean> customerList;
    AppCompatActivity mHostActivity;
    public CustomerImageTapDialog dialog;

    public SupplierCustomerAdapter(List<SupplierCustomerListRes.ResultBean> customerList, AppCompatActivity mHostActivity) {
        this.customerList = customerList;
        this.mHostActivity = mHostActivity;
    }

    public void notifyData(TextView emptyView) {
        if(customerList.isEmpty())
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
        notifyDataSetChanged();
    }


    public CharSequence highlight(String search, String originalText) {
        // ignore case and accents
        // the same thing should have been done for the search text
        String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();

        int start = normalizedText.indexOf(search);
        if (start < 0) {
            // not found, nothing to to
            return originalText;
        } else {
            // highlight each appearance in the original text
            // while searching in normalized text
            Spannable highlighted = new SpannableString(originalText);
            while (start >= 0) {
                int spanStart = Math.min(start, originalText.length());
                int spanEnd = Math.min(start + search.length(), originalText.length());

                highlighted.setSpan(new BackgroundColorSpan(R.color.colorAccent), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                start = normalizedText.indexOf(search, spanEnd);
            }

            return highlighted;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_supplier_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        SupplierCustomerListRes.ResultBean customer = customerList.get(position);
        holder.mTextViewCustomerFirmName.setText(customer.getUserFirmName());
        holder.mTextViewCustomerName.setText(customer.getUserName());
        Utils.uploadImageIfUrlValid(mHostActivity,customer.getUserImageUrl(),holder.mTextViewCustomerImage);

        holder.mTextViewCustomerImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SupplierCustomerListRes.ResultBean customer = customerList.get(position);
                dialog = new CustomerImageTapDialog(mHostActivity,true,true,R.layout.layout_customer_image_info);
                dialog.show(customer);
            }
        });

        holder.mParentView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SupplierCustomerListRes.ResultBean customer = customerList.get(position);
                Intent customerInfoIntent = new Intent(mHostActivity, SupplierCustomerOrderActivity.class);
                customerInfoIntent.putExtra(SupplierCustomerOrderActivity.CUSTOMER_OBJECT_KEY,customer);
                Utils.onActivityStart(mHostActivity,false,new int[]{},customerInfoIntent,null);
            }
        });

    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.mTextViewCustomerImage)
        CircleImageView mTextViewCustomerImage;
        @BindView(R.id.mTextViewCustomerName)
        AppCompatTextView mTextViewCustomerName;
        @BindView(R.id.mTextViewCustomerFirmName)
        AppCompatTextView mTextViewCustomerFirmName;
        View mParentView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mParentView = view;
        }
    }
}
