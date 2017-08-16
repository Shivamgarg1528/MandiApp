package ig.com.digitalmandi.adapter.supplier;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.response.supplier.SupplierProductListRes;
import ig.com.digitalmandi.interfaces.OnItemClickListeners;
import ig.com.digitalmandi.utils.ConstantValues;

/**
 * Created by Shivam.Garg on 12-10-2016.
 */

public class SupplierProductAdapter extends RecyclerView.Adapter<SupplierProductAdapter.ViewHolder> {

    List<SupplierProductListRes.ResultBean> productList;
    ParentActivity mHostActivity;
    OnItemClickListeners listener;

    public interface OnProductLongPressed {
        public void onLongPressed(SupplierProductListRes.ResultBean product, View view);
    }

    public void notifyData(TextView emptyView) {
        if(productList.isEmpty())
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
        notifyDataSetChanged();
    }
    public interface OnProductPressed {
        public void onPressed(SupplierProductListRes.ResultBean product, View view);
    }

    public SupplierProductAdapter(List<SupplierProductListRes.ResultBean> productList, AppCompatActivity mHostActivity, OnItemClickListeners iListener) {
        this.productList   = productList;
        this.mHostActivity = (ParentActivity) mHostActivity;
        this.listener      = iListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_product_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        SupplierProductListRes.ResultBean product = productList.get(position);
        holder.mTextViewProductName.setText(product.getProductName());
        holder.mTextViewProductInfo.setText(product.getProductQty());
        holder.mImageViewStatus.setImageResource(product.getProductStatus().equalsIgnoreCase(ConstantValues.ENABLE) ? R.drawable.ic_checkbox_checked : R.drawable.ic_checkbox_unchecked);
        Picasso.with(mHostActivity).load("http://www.aiob.in/shivam/product/201610111330154088.png").into(holder.mImageViewProductImage);

        holder.mParentView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {

                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);

                final SupplierProductListRes.ResultBean product = productList.get(position);

                CharSequence status = product.getProductStatus().equalsIgnoreCase(ConstantValues.ENABLE) ? "Disable" : "Enable";

                CharSequence array[] = {"Delete", "Edit", status};

                AlertDialog.Builder builder = new AlertDialog.Builder(mHostActivity);
                builder.setTitle("Select Operation");
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {

                            case 0:
                                listener.onItemDelete(view, product);
                                break;

                            case 1:
                                listener.onItemEdit(view, product);
                                break;

                            case 2:
                                listener.onItemChangeStatus(view, product);
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mImageViewProductImage)
        AppCompatImageView mImageViewProductImage;
        @BindView(R.id.mTextViewProductName)
        AppCompatTextView mTextViewProductName;
        @BindView(R.id.mTextViewProductInfo)
        AppCompatTextView mTextViewProductInfo;
        View mParentView;
        @BindView(R.id.mImageViewStatus)
        AppCompatImageView mImageViewStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mParentView = view;
        }
    }
}
