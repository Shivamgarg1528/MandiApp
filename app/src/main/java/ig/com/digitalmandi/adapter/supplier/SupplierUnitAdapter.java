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

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.bean.response.seller.SellerUnitList;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.util.AppConstant;

public class SupplierUnitAdapter extends RecyclerView.Adapter<SupplierUnitAdapter.ViewHolder> {

    private final BaseActivity mBaseActivity;
    private final List<SellerUnitList.Unit> mDataList;
    private final EventCallback mListener;

    public SupplierUnitAdapter(List<SellerUnitList.Unit> pDataList, BaseActivity pBaseActivity, EventCallback pListener) {
        this.mDataList = pDataList;
        this.mBaseActivity = pBaseActivity;
        this.mListener = pListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_unit_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SellerUnitList.Unit data = mDataList.get(position);
        holder.mTextViewUnitName.setText(data.getUnitName());
        holder.mTextViewUnitInfo.setText(data.getKgValue());
        holder.mImageViewStatus.setImageResource(AppConstant.ENABLE.equalsIgnoreCase(data.getUnitStatus()) ? R.drawable.ic_checkbox_checked : R.drawable.ic_checkbox_unchecked);

        holder.mParentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {

                final SellerUnitList.Unit data = mDataList.get(holder.getAdapterPosition());
                CharSequence dataStatus = AppConstant.ENABLE.equalsIgnoreCase(data.getUnitStatus()) ? mBaseActivity.getString(R.string.string_disable) : mBaseActivity.getString(R.string.string_enable);

                CharSequence[] charSequenceArray = {
                        mBaseActivity.getString(R.string.string_delete),
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

        final View mParentView;
        final AppCompatTextView mTextViewUnitName;
        final AppCompatTextView mTextViewUnitInfo;
        final AppCompatImageView mImageViewStatus;

        ViewHolder(View view) {
            super(view);
            mParentView = view;
            mTextViewUnitName = (AppCompatTextView) mParentView.findViewById(R.id.row_layout_unit_cardview_txtview_unit_name);
            mTextViewUnitInfo = (AppCompatTextView) mParentView.findViewById(R.id.row_layout_unit_cardview_txtview_unit_value);
            mImageViewStatus = (AppCompatImageView) mParentView.findViewById(R.id.row_layout_unit_card_view_img_status);
        }
    }
}
