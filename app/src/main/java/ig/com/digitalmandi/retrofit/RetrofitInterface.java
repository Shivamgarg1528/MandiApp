package ig.com.digitalmandi.retrofit;

import ig.com.digitalmandi.beans.request.common.ForgotPasswordRequest;
import ig.com.digitalmandi.beans.request.common.LoginRequest;
import ig.com.digitalmandi.beans.request.common.LogoutRequest;
import ig.com.digitalmandi.beans.request.supplier.RegistrationRequest;
import ig.com.digitalmandi.beans.request.supplier.SellerCustomerList;
import ig.com.digitalmandi.beans.request.supplier.SellerCustomerListRequest;
import ig.com.digitalmandi.beans.request.supplier.SellerProductListRequest;
import ig.com.digitalmandi.beans.request.supplier.SellerUnitListRequest;
import ig.com.digitalmandi.beans.request.supplier.SupplierItemDeleteRequest;
import ig.com.digitalmandi.beans.request.supplier.SupplierListRequest;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderAddReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderBillPrintRequest;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderDetailListRequest;
import ig.com.digitalmandi.beans.request.supplier.SupplierOrderListRequest;
import ig.com.digitalmandi.beans.request.supplier.SupplierProductModifyRequest;
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchaseAddReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchaseListReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchasePaymentListRequest;
import ig.com.digitalmandi.beans.request.supplier.SupplierPurchasePaymentReq;
import ig.com.digitalmandi.beans.request.supplier.SupplierUnitModifyRequest;
import ig.com.digitalmandi.beans.response.common.EmptyResponse;
import ig.com.digitalmandi.beans.response.common.LoginResponse;
import ig.com.digitalmandi.beans.response.supplier.SellerProductList;
import ig.com.digitalmandi.beans.response.supplier.SellerUnitList;
import ig.com.digitalmandi.beans.response.supplier.SupplierBillPrintRes;
import ig.com.digitalmandi.beans.response.supplier.SupplierListResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderDetailListResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierOrderListResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierPaymentListResponse;
import ig.com.digitalmandi.beans.response.supplier.SupplierPurchaseListRes;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface RetrofitInterface {

    String LOGIN_API = "loginUser";
    String LOGOUT_API = "logoutUser";
    String REGISTRATION_API = "registerUser";
    String SELLER_API = "sellerInfo";
    String FORGOT_API = "forgetPassword";
    String CUSTOMER_API = "customerInfo";
    String PRODUCT_API = "productList";
    String UNIT_API = "unitList";
    String PRODUCT_MODIFIED_API = "productModification";
    String UNIT_MODIFIED_API = "unitModification";
    String PURCHASE_LIST = "purchaseList";
    String PURCHASE_MODIFICATION_API = "purchaseModification";
    String PURCHASE_PAYMENT = "doPayment";
    String PURCHASE_PAYMENT_LIST = "paymentList";
    String CUSTOMER_ORDER_LIST = "orderList";
    String CUSTOMER_ORDER_DETAIL_LIST = "orderDetailList";
    String INSERT_NEW_ORDER = "insertOrder";
    String DELETE_ORDER = "deleteOrder";
    String DELETE_PURCHASE = "purchaseDelete";
    String DELETE_PRODUCT_UNIT = "deleteProductUnit";
    String ORDER_BILL_PRINT = "printBill";

    @POST(LOGIN_API)
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST(LOGOUT_API)
    Call<EmptyResponse> logoutUser(@Body LogoutRequest logoutRequest);

    @POST(REGISTRATION_API)
    Call<LoginResponse> registerUser(@Body RegistrationRequest registrationRequest);

    @POST(SELLER_API)
    Call<SupplierListResponse> sellerInfo(@Body SupplierListRequest sellerReqModel);

    @POST(FORGOT_API)
    Call<EmptyResponse> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @POST(CUSTOMER_API)
    Call<SellerCustomerList> supplierCustomerList(@Body SellerCustomerListRequest supplierCustomerReqModel);

    @POST(PRODUCT_API)
    Call<SellerProductList> supplierProductList(@Body SellerProductListRequest supplierProductReqModel);

    @POST(UNIT_API)
    Call<SellerUnitList> supplierUnitList(@Body SellerUnitListRequest supplierUnitReqModel);

    @POST(PRODUCT_MODIFIED_API)
    Call<EmptyResponse> modifiedProduct(@Body SupplierProductModifyRequest supplierProductModifyRequestModel);

    @POST(UNIT_MODIFIED_API)
    Call<EmptyResponse> modifiedUnit(@Body SupplierUnitModifyRequest supplierUnitModifyReqModel);

    @POST(PURCHASE_LIST)
    Call<SupplierPurchaseListRes> purchaseList(@Body SupplierPurchaseListReq purchaseListReqModel);

    @POST(PURCHASE_MODIFICATION_API)
    Call<SupplierPurchaseListRes> purchaseModification(@Body SupplierPurchaseAddReq purchaseListReqModel);

    @POST(PURCHASE_PAYMENT)
    Call<EmptyResponse> doPurchase(@Body SupplierPurchasePaymentReq purchasePaymentReqModel);

    @POST(PURCHASE_PAYMENT_LIST)
    Call<SupplierPaymentListResponse> supplierPurchasePaymentList(@Body SupplierPurchasePaymentListRequest paymentListReqModel);

    @POST(CUSTOMER_ORDER_LIST)
    Call<SupplierOrderListResponse> getOrdersOfGivenCustomer(@Body SupplierOrderListRequest supplierOrderListRequest);

    @POST(CUSTOMER_ORDER_DETAIL_LIST)
    Call<SupplierOrderDetailListResponse> orderDetailsOfGivenCustomer(@Body SupplierOrderDetailListRequest supplierOrderDetailListRequest);

    @POST(INSERT_NEW_ORDER)
    Call<EmptyResponse> insertNewOrder(@Body SupplierOrderAddReq supplierOrderAddReq);

    @POST(DELETE_ORDER)
    Call<EmptyResponse> deleteOrder(@Body SupplierItemDeleteRequest supplierItemDeleteRequest);

    @POST(DELETE_PURCHASE)
    Call<EmptyResponse> deletePurchase(@Body SupplierItemDeleteRequest supplierItemDeleteRequest);

    @POST(DELETE_PRODUCT_UNIT)
    Call<EmptyResponse> deleteProductUnit(@Body SupplierItemDeleteRequest supplierItemDeleteRequest);

    @POST(ORDER_BILL_PRINT)
    Call<SupplierBillPrintRes> orderBillPrint(@Body SupplierOrderBillPrintRequest supplierOrderBillPrintReq);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}

