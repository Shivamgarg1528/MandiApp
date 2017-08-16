package ig.com.digitalmandi.database;

import ig.com.digitalmandi.base_package.ParentActivity;
import ig.com.digitalmandi.beans.request.supplier.SupplierCustomerListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierCustomerListRes;
import ig.com.digitalmandi.beans.request.supplier.SupplierProductListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierUnitListReq;
import ig.com.digitalmandi.beans.response.supplier.SupplierProductListRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierUnitListRes;
import ig.com.digitalmandi.retrofit.RetrofitCallBack;
import ig.com.digitalmandi.retrofit.RetrofitWebService;
import ig.com.digitalmandi.retrofit.VerifyResponse;
import ig.com.digitalmandi.utils.ConstantValues;
import ig.com.digitalmandi.utils.MyPrefrences;

/**
 * Created by shivam.garg on 18-10-2016.
 */

public class DataBaseOperation {

    public static int operationCount = 0;

    public static void insertIntoCustomerTable(final ParentActivity mRunningActivity, final BaseContract.OnInsertBulkDataSuccessFully listener){

        SupplierCustomerListReq supplierCustomerReqModel = new SupplierCustomerListReq();
        supplierCustomerReqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mRunningActivity));

        mRunningActivity.apiEnqueueObject = RetrofitWebService.getInstance().getInterface().supplierCustomerList(supplierCustomerReqModel);
        mRunningActivity.apiEnqueueObject.enqueue(new RetrofitCallBack<SupplierCustomerListRes>(mRunningActivity, false) {

            @Override
            public void yesCall(SupplierCustomerListRes response, ParentActivity weakRef) {
                if (VerifyResponse.isResponseOk(response,true)) {
                    CustomerContract customerContract = new CustomerContract(weakRef);
                    customerContract.insertBulkData(response.getResult(), CustomerContract.Customer.CONTENT_URI,listener);
                }
                else
                   listener.onInsertBulkDataSuccess();
            }
            @Override
            public void noCall(Throwable error) {
                listener.onInsertBulkDataSuccess();
            }
        });
    }

    public static void insertIntoUnitTable(final ParentActivity mRunningActivity, final BaseContract.OnInsertBulkDataSuccessFully listener){

        SupplierUnitListReq supplierUnitReqModel = new SupplierUnitListReq();
        supplierUnitReqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mRunningActivity));


        mRunningActivity.apiEnqueueObject = RetrofitWebService.getInstance().getInterface().supplierUnitList(supplierUnitReqModel);
        mRunningActivity.apiEnqueueObject.enqueue(new RetrofitCallBack<SupplierUnitListRes>(mRunningActivity, false) {

            @Override
            public void yesCall(SupplierUnitListRes response, ParentActivity weakRef) {
                if (VerifyResponse.isResponseOk(response,true)) {
                    UnitContract unitContract = new UnitContract(weakRef);
                    unitContract.insertBulkData(response.getResult(), UnitContract.Unit.CONTENT_URI,listener);
                }
                else
                    listener.onInsertBulkDataSuccess();
            }
            @Override
            public void noCall(Throwable error) {
                listener.onInsertBulkDataSuccess();
            }
        });
    }

    public static void insertIntoProductTable(final ParentActivity mRunningActivity, final BaseContract.OnInsertBulkDataSuccessFully listener) {

        SupplierProductListReq supplierProductReqModel = new SupplierProductListReq();
        supplierProductReqModel.setSellerId(MyPrefrences.getStringPrefrences(ConstantValues.USER_SELLER_ID, mRunningActivity));

        mRunningActivity.apiEnqueueObject = RetrofitWebService.getInstance().getInterface().supplierProductList(supplierProductReqModel);
        mRunningActivity.apiEnqueueObject.enqueue(new RetrofitCallBack<SupplierProductListRes>(mRunningActivity, false) {

            @Override
            public void yesCall(SupplierProductListRes response, ParentActivity weakRef) {
                if (VerifyResponse.isResponseOk(response,true)) {
                    ProductContract productContract = new ProductContract(weakRef);
                    productContract.insertBulkData(response.getResult(), ProductContract.Product.CONTENT_URI, listener);
                } else
                    listener.onInsertBulkDataSuccess();
            }

            @Override
            public void noCall(Throwable error) {
                listener.onInsertBulkDataSuccess();
            }
        });
    }

}
