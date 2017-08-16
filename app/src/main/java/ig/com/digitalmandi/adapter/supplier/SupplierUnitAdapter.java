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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ig.com.digitalmandi.R;
import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.response.supplier.SupplierUnitListRes;
import ig.com.digitalmandi.interfaces.OnItemClickListeners;
import ig.com.digitalmandi.utils.ConstantValues;

/**
 * Created by Shivam.Garg on 12-10-2016.
 */

public class SupplierUnitAdapter extends RecyclerView.Adapter<SupplierUnitAdapter.ViewHolder> {

    private List<SupplierUnitListRes.ResultBean> unitList;
    private ParentActivity mHostActivity;
    private OnItemClickListeners listener;

    public SupplierUnitAdapter(List<SupplierUnitListRes.ResultBean> unitList, AppCompatActivity mHostActivity, OnItemClickListeners listener) {
        this.unitList      = unitList;
        this.mHostActivity = (ParentActivity) mHostActivity;
        this.listener      = listener;
    }

    public void notifyData(TextView emptyView) {
        if(unitList.isEmpty())
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_unit_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        SupplierUnitListRes.ResultBean unit = unitList.get(position);
        holder.mTextViewUnitName  .setText(unit.getUnitName());
        holder.mTextViewUnitInfo  .setText(unit.getKgValue());
        holder.mImageViewStatus   .setImageResource(unit.getUnitStatus().equalsIgnoreCase(ConstantValues.ENABLE) ? R.drawable.ic_checkbox_checked : R.drawable.ic_checkbox_unchecked);

        holder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                final SupplierUnitListRes.ResultBean unit = unitList.get(position);
                CharSequence status = unit.getUnitStatus().equalsIgnoreCase(ConstantValues.ENABLE)?"Disable":"Enable";

                CharSequence array[] = {"Delete","Edit",status};

                AlertDialog.Builder builder = new AlertDialog.Builder(mHostActivity);
                builder.setTitle("Select Operation");
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {

                            case 0:
                                listener.onItemDelete(view,unit);
                                break;

                            case 1:
                                listener.onItemEdit(view,unit);
                                break;

                            case 2:
                                listener.onItemChangeStatus(view,unit);
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
        return unitList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mTextViewUnitName)
        AppCompatTextView mTextViewUnitName;
        @BindView(R.id.mTextViewUnitInfo)
        AppCompatTextView mTextViewUnitInfo;
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
