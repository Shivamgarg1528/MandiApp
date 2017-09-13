package ig.com.digitalmandi.fragment.supplier;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Comparator;
import java.util.Date;

import ig.com.digitalmandi.R;
import ig.com.digitalmandi.activity.BaseActivity;
import ig.com.digitalmandi.activity.seller.OrderDetailsActivity;
import ig.com.digitalmandi.activity.seller.PaymentsActivity;
import ig.com.digitalmandi.activity.seller.SupplierPurchaseActivity;
import ig.com.digitalmandi.adapter.supplier.PurchaseAdapter;
import ig.com.digitalmandi.bean.AbstractResponse;
import ig.com.digitalmandi.bean.request.seller.ItemDeleteRequest;
import ig.com.digitalmandi.bean.request.seller.OrderDetailsRequest;
import ig.com.digitalmandi.bean.request.seller.PaymentsRequest;
import ig.com.digitalmandi.bean.request.seller.SellerOrdersRequest;
import ig.com.digitalmandi.bean.response.EmptyResponse;
import ig.com.digitalmandi.bean.response.seller.PurchaseResponse;
import ig.com.digitalmandi.callback.EventCallback;
import ig.com.digitalmandi.dialog.ConfirmDialog;
import ig.com.digitalmandi.dialog.DatePickerClass;
import ig.com.digitalmandi.fragment.ListBaseFragment;
import ig.com.digitalmandi.retrofit.ResponseVerification;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitClient;
import ig.com.digitalmandi.util.AppConstant;
import ig.com.digitalmandi.util.Helper;
import ig.com.digitalmandi.util.LoadMoreClass;

public class PurchaseFragment extends ListBaseFragment<PurchaseResponse.Purchase> implements SearchView.OnQueryTextListener, DatePickerClass.OnDateSelected, View.OnClickListener, EventCallback<PurchaseResponse.Purchase> {

    private int mPageCount = 1;
    private boolean mLoadMore = false;

    private Date mDateStart;
    private Date mDateEnd;
    private final LoadMoreClass mLoadMoreClass = new LoadMoreClass() {

        @Override
        public void onLoadMore() {
            if (mLoadMore) {
                return;
            }
            ++mPageCount;
            fetchData(false);
        }
    };
    private AppCompatButton mBtnStartDate;
    private AppCompatButton mBtnEndDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_seller_orders, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.layout_fragment_seller_orders_btn_reset_date).setOnClickListener(this);
        mBtnStartDate = view.findViewById(R.id.layout_fragment_seller_orders_btn_start_date);
        mBtnStartDate.setOnClickListener(this);
        mBtnEndDate = view.findViewById(R.id.layout_fragment_seller_orders_btn_end_date);
        mBtnEndDate.setOnClickListener(this);
        mRecyclerView.addOnScrollListener(mLoadMoreClass);
        fetchData(true);
    }

    @Override
    protected AbstractResponse getResponse() {
        return null;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new PurchaseAdapter(mBaseActivity, mDataList, this);
    }

    @Override
    protected int getEmptyTextStringId() {
        return R.string.string_no_purchase_added_yet_please_add_new_purchase;
    }

    @Override
    protected void fetchData(final boolean pRefresh) {
        if (pRefresh) {
            mPageCount = 1;
            mLoadMore = false;
        }
        SellerOrdersRequest sellerOrdersRequest = new SellerOrdersRequest();
        if (mDateEnd != null)
            sellerOrdersRequest.setEndDate(Helper.getDateString(mDateEnd.getTime(), AppConstant.API_DATE_FORMAT));
        else
            sellerOrdersRequest.setEndDate("");

        if (mDateStart != null)
            sellerOrdersRequest.setStartDate(Helper.getDateString(mDateStart.getTime(), AppConstant.API_DATE_FORMAT));
        else
            sellerOrdersRequest.setStartDate("");

        sellerOrdersRequest.setSellerId(mLoginUser.getSellerId());
        sellerOrdersRequest.setPage(String.valueOf(mPageCount));
        sellerOrdersRequest.setFlag(AppConstant.PURCHASE_LIST_PAGING);

        mBaseActivity.mApiEnqueueObject = RetrofitClient.getInstance().getInterface().purchaseList(sellerOrdersRequest);
        mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<PurchaseResponse>(mBaseActivity, false) {

            @Override
            public void onResponse(PurchaseResponse pResponse, BaseActivity pBaseActivity) {

                if (ResponseVerification.isResponseOk(pResponse, false)) {
                    if (pResponse.getResult().size() == 0) {
                        pBaseActivity.showToast(getString(R.string.string_no_purchased_item_found));
                    }
                    if (pRefresh) {
                        mRecyclerView.scrollToPosition(0);
                        mDataList.clear();
                        mBackUpList.clear();
                    }
                    mDataList.addAll(pResponse.getResult());
                    mBackUpList.addAll(pResponse.getResult());
                }
                notifyAdapterAndView();
            }
        });
    }

    @Override
    protected Intent getRequestedIntent() {
        return new Intent(mBaseActivity, SupplierPurchaseActivity.class);
    }

    @Override
    protected Comparator getComparator(int pComparatorType) {
        switch (pComparatorType) {
            case AppConstant.COMPARATOR_ALPHA: {
                return new Comparator<PurchaseResponse.Purchase>() {
                    public int compare(PurchaseResponse.Purchase left, PurchaseResponse.Purchase right) {
                        return String.CASE_INSENSITIVE_ORDER.compare(left.getNameOfPerson(), right.getNameOfPerson());
                    }
                };
            }
        }
        return null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mDataList.clear();
        for (PurchaseResponse.Purchase model : mBackUpList) {
            if (model.getPurchaseDate().contains(newText) || model.getNameOfPerson().toLowerCase().contains(newText.toLowerCase()) || model.getProductName().toLowerCase().contains(newText.toLowerCase())) {
                mDataList.add(model);
            }
        }
        notifyAdapterAndView();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.layout_fragment_seller_orders_btn_start_date: {
                DatePickerClass.showDatePicker(mBaseActivity, DatePickerClass.START_DATE, this);
                break;
            }

            case R.id.layout_fragment_seller_orders_btn_end_date: {
                DatePickerClass.showDatePicker(mBaseActivity, DatePickerClass.END_DATE, this);
                break;
            }

            case R.id.layout_fragment_seller_orders_btn_reset_date: {
                ConfirmDialog.show(mBaseActivity, getString(R.string.string_reset_applied_filters), true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            resetParamsAndCallApi();
                        }
                    }
                });
                break;
            }
        }
    }

    @Override
    public void onDateSelectedCallBack(int id, Date pDate, String pDateAppShownFormat, int pMaxDaysInSelectedMonth) {
        switch (id) {
            case DatePickerClass.START_DATE:
                mDateStart = pDate;
                mBtnStartDate.setText(pDateAppShownFormat);
                break;

            case DatePickerClass.END_DATE:
                mDateEnd = pDate;
                mBtnEndDate.setText(pDateAppShownFormat);
                break;
        }
        fetchDataWhenFilterSet();
    }

    private void fetchDataWhenFilterSet() {
        if (mDateStart == null) {
            mBaseActivity.showToast(getString(R.string.string_please_select_start_date));
            return;
        } else if (mDateEnd == null) {
            mBaseActivity.showToast(getString(R.string.string_please_select_end_date));
            return;
        } else if (mDateStart.after(mDateEnd)) {
            mBaseActivity.showToast(getString(R.string.string_start_date_must_less_than_end_date));
            return;
        }
        fetchData(true);
    }

    private void resetParamsAndCallApi() {
        mBtnEndDate.setText(getString(R.string.string_end_date));
        mBtnStartDate.setText(getString(R.string.string_start_date));
        mDateStart = null;
        mDateEnd = null;
        fetchData(true);
    }

    @Override
    public void onEvent(int pOperationType, final PurchaseResponse.Purchase pPurchaseObj) {

        switch (pOperationType) {
            case AppConstant.OPERATION_DELETE: {

                ConfirmDialog.show(mBaseActivity, getString(R.string.string_continue_to_delete_order), true, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {

                            ItemDeleteRequest itemDeleteRequest = new ItemDeleteRequest();
                            itemDeleteRequest.setFlag(AppConstant.DELETE_OR_PAYMENT_PURCHASE);
                            itemDeleteRequest.setId(pPurchaseObj.getPurchaseId());

                            mBaseActivity.mApiEnqueueObject = RetrofitClient.getInstance().getInterface().deletePurchase(itemDeleteRequest);
                            mBaseActivity.mApiEnqueueObject.enqueue(new RetrofitCallBack<EmptyResponse>(mBaseActivity) {

                                @Override
                                public void onResponse(EmptyResponse pResponse, BaseActivity pBaseActivity) {
                                    if (ResponseVerification.isResponseOk(pResponse)) {
                                        fetchData(true);
                                        pBaseActivity.showToast(getString(R.string.string_order_deleted_successfully));
                                    } else {
                                        pBaseActivity.showToast(getString(R.string.string_order_deleted_unsuccessfully));
                                    }
                                }
                            });
                        }
                    }
                });
                break;
            }
            case AppConstant.OPERATION_ORDER_DETAILS: {

                OrderDetailsRequest orderDetailsRequest = new OrderDetailsRequest();
                orderDetailsRequest.setFlag(AppConstant.COLUMN_PURCHASE_ID);
                orderDetailsRequest.setId(pPurchaseObj.getPurchaseId());
                orderDetailsRequest.setMessage(getString(R.string.string_sold_details_of_items, pPurchaseObj.getNameOfPerson()));

                Intent intent = new Intent(mBaseActivity, OrderDetailsActivity.class);
                intent.putExtra(AppConstant.KEY_OBJECT, orderDetailsRequest);
                Helper.onActivityStart(mBaseActivity, false, null, intent, null);
                break;
            }
            case AppConstant.OPERATION_ORDER_PAYMENT_DETAILS: {

                PaymentsRequest paymentsRequest = new PaymentsRequest();
                paymentsRequest.setFlag(AppConstant.DELETE_OR_PAYMENT_PURCHASE);
                paymentsRequest.setId(pPurchaseObj.getPurchaseId());
                paymentsRequest.setOrderDate(pPurchaseObj.getPurchaseDate());
                paymentsRequest.setOrderAmount(pPurchaseObj.getTotalAmount());

                Intent intent = new Intent(mBaseActivity, PaymentsActivity.class);
                intent.putExtra(AppConstant.KEY_OBJECT, paymentsRequest);
                Helper.onActivityStart(mBaseActivity, false, null, intent, null);
                break;
            }
        }
    }
}