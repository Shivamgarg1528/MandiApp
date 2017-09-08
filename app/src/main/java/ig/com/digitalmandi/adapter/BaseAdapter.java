package ig.com.digitalmandi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.callback.EventCallback;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter {

    protected BaseActivity mBaseActivity;
    protected List<T> mDataList;
    protected EventCallback mEventCallback;

    public BaseAdapter(BaseActivity pBaseActivity, List<T> pDataList, EventCallback pEventCallback) {
        this.mBaseActivity = pBaseActivity;
        this.mDataList = pDataList;
        this.mEventCallback = pEventCallback;
    }

    public void notifyData(TextView pEmptyView) {
        pEmptyView.setVisibility(mDataList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

}
