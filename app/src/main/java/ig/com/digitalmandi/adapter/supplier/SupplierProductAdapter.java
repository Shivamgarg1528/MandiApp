package ig.com.digitalmandi.adapter.supplier;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.BaseActivity;
import ig.com.digitalmandi.beans.response.supplier.SellerProductList;
import ig.com.digitalmandi.interfaces.EventListener;
import ig.com.digitalmandi.utils.AppConstant;

public class SupplierProductAdapter extends RecyclerView.Adapter<SupplierProductAdapter.ViewHolder> {

    private List<SellerProductList.Product> mDataList;
    private BaseActivity mBaseActivity;
    private EventListener mListener;

    public SupplierProductAdapter(List<SellerProductList.Product> pProductList, BaseActivity pBaseActivity, EventListener pListener) {
        this.mDataList = pProductList;
        this.mBaseActivity = pBaseActivity;
        this.mListener = pListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_product_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SellerProductList.Product data = mDataList.get(position);
        holder.mTextViewProductName.setText(data.getProductName());
        holder.mImageViewStatus.setImageResource(AppConstant.ENABLE.equalsIgnoreCase(data.getProductStatus()) ? R.drawable.ic_checkbox_checked : R.drawable.ic_checkbox_unchecked);

        //Picasso.with(mBaseActivity).load(AppConstant.END_POINT.concat("/product/201610111330154088.png")).into(holder.mImageViewProductImage);

        holder.mParentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {

                final SellerProductList.Product data = mDataList.get(holder.getAdapterPosition());
                CharSequence dataStatus = data.getProductStatus().equalsIgnoreCase(AppConstant.ENABLE) ? "Disable" : "Enable";

                CharSequence[] charSequenceArray = {mBaseActivity.getString(R.string.string_delete),
                        mBaseActivity.getString(R.string.string_edit),
                        dataStatus};

                AlertDialog.Builder builder = new AlertDialog.Builder(mBaseActivity);
                builder.setTitle(R.string.string_select_operation);
                builder.setItems(charSequenceArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {

                            case 0:
                                mListener.onEvent(AppConstant.OPERATION_DELETE, data);
                                break;

                            case 1:
                                mListener.onEvent(AppConstant.OPERATION_EDIT, data);
                                break;

                            case 2:
                                mListener.onEvent(AppConstant.OPERATION_STATUS_MODIFY, data);
                                break;
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

        private final View mParentView;
        private CircleImageView mImageViewProductImage;
        private AppCompatTextView mTextViewProductName;
        private AppCompatImageView mImageViewStatus;

        ViewHolder(View view) {
            super(view);
            mParentView = view;
            mImageViewProductImage = (CircleImageView) view.findViewById(R.id.row_layout_product_card_view_iv_product_image);
            mTextViewProductName = (AppCompatTextView) view.findViewById(R.id.row_layout_product_card_view_tv_product_info);
            mImageViewStatus = (AppCompatImageView) view.findViewById(R.id.row_layout_product_card_view_img_status);
        }
    }
}
