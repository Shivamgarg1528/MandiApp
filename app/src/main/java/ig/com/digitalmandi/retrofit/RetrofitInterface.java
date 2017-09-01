package ig.com.digitalmandi.retrofit;

import ig.com.digitalmandi.bean.request.ForgotPasswordRequest;
import ig.com.digitalmandi.bean.request.LoginRequest;
import ig.com.digitalmandi.bean.request.LogoutRequest;
import ig.com.digitalmandi.bean.request.seller.ItemDeleteRequest;
import ig.com.digitalmandi.bean.request.seller.PaymentRequest;
import ig.com.digitalmandi.bean.request.seller.RegistrationRequest;
import ig.com.digitalmandi.bean.request.seller.SellerCustomerList;
import ig.com.digitalmandi.bean.request.seller.SellerCustomerListRequest;
import ig.com.digitalmandi.bean.request.seller.SellerOrdersRequest;
import ig.com.digitalmandi.bean.request.seller.SellerProductListRequest;
import ig.com.digitalmandi.bean.request.seller.SellerUnitListRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierListRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderAddReq;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderBillPrintRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderDetailListRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderListRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierProductModifyRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierPurchaseAddReq;
import ig.com.digitalmandi.bean.request.seller.SupplierPurchasePaymentListRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierUnitModifyRequest;
import ig.com.digitalmandi.bean.response.EmptyResponse;
import ig.com.digitalmandi.bean.response.LoginResponse;
import ig.com.digitalmandi.bean.response.seller.SellerOrderResponse;
import ig.com.digitalmandi.bean.response.seller.SellerProductList;
import ig.com.digitalmandi.bean.response.seller.SellerUnitList;
import ig.com.digitalmandi.bean.response.seller.SupplierBillPrintRes;
import ig.com.digitalmandi.bean.response.seller.SupplierListResponse;
import ig.com.digitalmandi.bean.response.seller.SupplierOrderDetailListResponse;
import ig.com.digitalmandi.bean.response.seller.SupplierOrderListResponse;
import ig.com.digitalmandi.bean.response.seller.SupplierPaymentListResponse;
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
    String PURCHASE_LIST = "getSellerOrders";
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
    Call<SellerOrderResponse> getSellerOrders(@Body SellerOrdersRequest purchaseListReqModel);

    @POST(PURCHASE_MODIFICATION_API)
    Call<SellerOrderResponse> purchaseModification(@Body SupplierPurchaseAddReq purchaseListReqModel);

    @POST(PURCHASE_PAYMENT)
    Call<EmptyResponse> doPurchase(@Body PaymentRequest purchasePaymentReqModel);

    @POST(PURCHASE_PAYMENT_LIST)
    Call<SupplierPaymentListResponse> supplierPurchasePaymentList(@Body SupplierPurchasePaymentListRequest paymentListReqModel);

    @POST(CUSTOMER_ORDER_LIST)
    Call<SupplierOrderListResponse> getOrdersOfGivenCustomer(@Body SupplierOrderListRequest supplierOrderListRequest);

    @POST(CUSTOMER_ORDER_DETAIL_LIST)
    Call<SupplierOrderDetailListResponse> orderDetailsOfGivenCustomer(@Body SupplierOrderDetailListRequest supplierOrderDetailListRequest);

    @POST(INSERT_NEW_ORDER)
    Call<EmptyResponse> insertNewOrder(@Body SupplierOrderAddReq supplierOrderAddReq);

    @POST(DELETE_ORDER)
    Call<EmptyResponse> deleteOrder(@Body ItemDeleteRequest itemDeleteRequest);

    @POST(DELETE_PURCHASE)
    Call<EmptyResponse> deletePurchase(@Body ItemDeleteRequest itemDeleteRequest);

    @POST(DELETE_PRODUCT_UNIT)
    Call<EmptyResponse> deleteProductUnit(@Body ItemDeleteRequest itemDeleteRequest);

    @POST(ORDER_BILL_PRINT)
    Call<SupplierBillPrintRes> orderBillPrint(@Body SupplierOrderBillPrintRequest supplierOrderBillPrintReq);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}

