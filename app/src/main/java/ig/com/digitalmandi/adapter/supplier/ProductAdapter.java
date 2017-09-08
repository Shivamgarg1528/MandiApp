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
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.adapter.BaseAdapter;
import ig.com.digitalmandi.bean.response.seller.SellerProductList;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;

public class ProductAdapter extends BaseAdapter<SellerProductList.Product> {

    public ProductAdapter(BaseActivity pBaseActivity, List<SellerProductList.Product> pDataList, EventCallback pEventCallback) {
        super(pBaseActivity, pDataList, pEventCallback);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        SellerProductList.Product data = mDataList.get(position);

        Helper.setImage(mBaseActivity, data.getProductImage(), viewHolder.mCircleIVProductImage);

        viewHolder.mTextViewProductName.setText(data.getProductName());
        viewHolder.mImageViewStatus.setImageResource(AppConstant.ENABLE.equalsIgnoreCase(data.getProductStatus()) ? R.drawable.ic_checkbox_checked : R.drawable.ic_checkbox_unchecked);
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {

                final SellerProductList.Product data = mDataList.get(viewHolder.getAdapterPosition());
                CharSequence dataStatus = data.getProductStatus().equalsIgnoreCase(AppConstant.ENABLE) ? mBaseActivity.getString(R.string.string_disable) : mBaseActivity.getString(R.string.string_enable);

                CharSequence[] charSequenceArray = {
                        mBaseActivity.getString(R.string.string_delete),
                        mBaseActivity.getString(R.string.string_edit),
                        dataStatus
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(mBaseActivity);
                builder.setTitle(R.string.string_select_operation);
                builder.setItems(charSequenceArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                mEventCallback.onEvent(AppConstant.OPERATION_DELETE, data);
                                break;

                            case 1:
                                mEventCallback.onEvent(AppConstant.OPERATION_EDIT, data);
                                break;

                            case 2:
                                mEventCallback.onEvent(AppConstant.OPERATION_STATUS_MODIFY, data);
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            }
        });

    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatTextView mTextViewProductName;
        private final CircleImageView mCircleIVProductImage;
        private final AppCompatImageView mImageViewStatus;

        ViewHolder(View view) {
            super(view);
            mTextViewProductName = view.findViewById(R.id.row_product_tv_product_info);
            mCircleIVProductImage = view.findViewById(R.id.row_product_iv_product_image);
            mImageViewStatus = view.findViewById(R.id.row_product_iv_status);
        }
    }
}
