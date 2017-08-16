package ig.com.digitalmandi.retrofit;

import ig.com.digitalmandi.beans.request.common.ForgotPasswordReqModel;
import ig.com.digitalmandi.beans.request.common.LoginReqModel;
import ig.com.digitalmandi.beans.request.common.LogoutReqModel;
import ig.com.digitalmandi.beans.request.supplier.RegistrationReqModel;
import ig.com.digitalmandi.beans.request.supplier.SupplierCustomerListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierCustomerListRes;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderAddReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderBillPrintReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderDeleteReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderDetailListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierProductListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierProductModifyReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchaseAddReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchaseListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchasePaymentListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchasePaymentReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierSellerListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierUnitListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierUnitModifyReq;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.beans.response.common.LoginResModel;
import ig.com.digitalmandi.beans.response.supplier.SupplierBillPrintRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierListRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderDetailListRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderListRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierPaymentListRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierProductListRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierPurchaseListRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierUnitListRes;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by dinesh.sengar on 26-08-2016.
 */
public interface RetrofitInterface {

    @POST(RetrofitConstant.LOGIN_API)
    Call<LoginResModel> loginUser(@Body LoginReqModel loginReqModel);

    @POST(RetrofitConstant.LOGOUT_API)
    Call<EmptyResponse> logoutUser(@Body LogoutReqModel logoutReqModel);

    @POST(RetrofitConstant.REGISTRATION_API)
    Call<LoginResModel> registerUser(@Body RegistrationReqModel registrationReqModel);

    @POST(RetrofitConstant.SELLER_API)
    Call<SupplierListRes> sellerInfo(@Body SupplierSellerListReq sellerReqModel);

    @POST(RetrofitConstant.FORGOT_API)
    Call<EmptyResponse> forgotPassword(@Body ForgotPasswordReqModel forgotPasswordReqModel);

    @POST(RetrofitConstant.CUSTOMER_API)
    Call<SupplierCustomerListRes> supplierCustomerList(@Body SupplierCustomerListReq supplierCustomerReqModel);

    @POST(RetrofitConstant.PRODUCT_API)
    Call<SupplierProductListRes> supplierProductList(@Body SupplierProductListReq supplierProductReqModel);

    @POST(RetrofitConstant.UNIT_API)
    Call<SupplierUnitListRes> supplierUnitList(@Body SupplierUnitListReq supplierUnitReqModel);

    @POST(RetrofitConstant.PRODUCT_MODIFIED_API)
    Call<EmptyResponse> modifiedProduct(@Body SupplierProductModifyReq supplierProductModifyReqModel);


    @POST(RetrofitConstant.UNIT_MODIFIED_API)
    Call<EmptyResponse> modifiedUnit(@Body SupplierUnitModifyReq supplierUnitModifyReqModel);

    @POST(RetrofitConstant.PURCHASE_LIST)
    Call<SupplierPurchaseListRes> purchaseList(@Body SupplierPurchaseListReq purchaseListReqModel);

    @POST(RetrofitConstant.PURCHASE_MODIFICATION_API)
    Call<SupplierPurchaseListRes> purchaseModification(@Body SupplierPurchaseAddReq purchaseListReqModel);

    @POST(RetrofitConstant.PURCHASE_PAYMENT)
    Call<EmptyResponse> doPurchase(@Body SupplierPurchasePaymentReq purchasePaymentReqModel);

    @POST(RetrofitConstant.PURCHASE_PAYMENT_LIST)
    Call<SupplierPaymentListRes> supplierPurchasePaymentList(@Body SupplierPurchasePaymentListReq paymentListReqModel);

    @POST(RetrofitConstant.CUSTOMER_ORDER_LIST)
    Call<SupplierOrderListRes> orderListOfAnyCustomer(@Body SupplierOrderListReq supplierOrderListReq);

    @POST(RetrofitConstant.CUSTOMER_ORDER_DETAIL_LIST)
    Call<SupplierOrderDetailListRes> orderDetailListOfAnyCustomer(@Body SupplierOrderDetailListReq supplierOrderDetailListReq);

    @POST(RetrofitConstant.INSERT_NEW_ORDER)
    Call<EmptyResponse> insertNewOrder(@Body SupplierOrderAddReq supplierOrderAddReq);

    @POST(RetrofitConstant.DELETE_ORDER)
    Call<EmptyResponse> deleteOrder(@Body SupplierOrderDeleteReq supplierOrderDeleteReq);

    @POST(RetrofitConstant.DELETE_PURCHASE)
    Call<EmptyResponse> deletePurchase(@Body SupplierOrderDeleteReq supplierOrderDeleteReq);

    @POST(RetrofitConstant.DELETE_PRODUCT_UNIT)
    Call<EmptyResponse> deleteProductUnit(@Body SupplierOrderDeleteReq supplierOrderDeleteReq);

    @POST(RetrofitConstant.ORDER_BILL_PRINT)
    Call<SupplierBillPrintRes> orderBillPrint(@Body SupplierOrderBillPrintReq supplierOrderBillPrintReq);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}

