package ig.com.digitalmandi.retrofit;

import ig.com.digitalmandi.bean.request.ForgotPasswordRequest;
import ig.com.digitalmandi.bean.request.LoginRequest;
import ig.com.digitalmandi.bean.request.LogoutRequest;
import ig.com.digitalmandi.bean.request.seller.CustomerResponse;
import ig.com.digitalmandi.bean.request.seller.ItemDeleteRequest;
import ig.com.digitalmandi.bean.request.seller.OrderDetailsRequest;
import ig.com.digitalmandi.bean.request.seller.PaymentRequest;
import ig.com.digitalmandi.bean.request.seller.PaymentsRequest;
import ig.com.digitalmandi.bean.request.seller.RegistrationRequest;
import ig.com.digitalmandi.bean.request.seller.SellerCustomerListRequest;
import ig.com.digitalmandi.bean.request.seller.SellerOrdersRequest;
import ig.com.digitalmandi.bean.request.seller.SellerProductListRequest;
import ig.com.digitalmandi.bean.request.seller.SellerUnitListRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierListRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderAddRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderBillPrintRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierOrderListRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierProductModifyRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierPurchaseAddRequest;
import ig.com.digitalmandi.bean.request.seller.SupplierUnitModifyRequest;
import ig.com.digitalmandi.bean.response.EmptyResponse;
import ig.com.digitalmandi.bean.response.LoginResponse;
import ig.com.digitalmandi.bean.response.seller.BillPrintResponse;
import ig.com.digitalmandi.bean.response.seller.OrderDetailResponse;
import ig.com.digitalmandi.bean.response.seller.OrderResponse;
import ig.com.digitalmandi.bean.response.seller.PaymentResponse;
import ig.com.digitalmandi.bean.response.seller.ProductResponse;
import ig.com.digitalmandi.bean.response.seller.PurchaseResponse;
import ig.com.digitalmandi.bean.response.seller.SellerResponse;
import ig.com.digitalmandi.bean.response.seller.UnitResponse;
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
    Call<SellerResponse> sellerInfo(@Body SupplierListRequest sellerReqModel);

    @POST(FORGOT_API)
    Call<EmptyResponse> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @POST(CUSTOMER_API)
    Call<CustomerResponse> supplierCustomerList(@Body SellerCustomerListRequest supplierCustomerReqModel);

    @POST(PRODUCT_API)
    Call<ProductResponse> supplierProductList(@Body SellerProductListRequest supplierProductReqModel);

    @POST(UNIT_API)
    Call<UnitResponse> supplierUnitList(@Body SellerUnitListRequest supplierUnitReqModel);

    @POST(PRODUCT_MODIFIED_API)
    Call<EmptyResponse> modifiedProduct(@Body SupplierProductModifyRequest supplierProductModifyRequestModel);

    @POST(UNIT_MODIFIED_API)
    Call<EmptyResponse> modifiedUnit(@Body SupplierUnitModifyRequest supplierUnitModifyReqModel);

    @POST(PURCHASE_LIST)
    Call<PurchaseResponse> purchaseList(@Body SellerOrdersRequest purchaseListReqModel);

    @POST(PURCHASE_MODIFICATION_API)
    Call<PurchaseResponse> purchaseModification(@Body SupplierPurchaseAddRequest purchaseListReqModel);

    @POST(PURCHASE_PAYMENT)
    Call<EmptyResponse> doPurchase(@Body PaymentRequest purchasePaymentReqModel);

    @POST(PURCHASE_PAYMENT_LIST)
    Call<PaymentResponse> supplierPurchasePaymentList(@Body PaymentsRequest paymentListReqModel);

    @POST(CUSTOMER_ORDER_LIST)
    Call<OrderResponse> getOrdersOfGivenCustomer(@Body SupplierOrderListRequest supplierOrderListRequest);

    @POST(CUSTOMER_ORDER_DETAIL_LIST)
    Call<OrderDetailResponse> orderDetailsOfGivenCustomer(@Body OrderDetailsRequest orderDetailsRequest);

    @POST(INSERT_NEW_ORDER)
    Call<EmptyResponse> insertNewOrder(@Body SupplierOrderAddRequest supplierOrderAddReq);

    @POST(DELETE_ORDER)
    Call<EmptyResponse> deleteOrder(@Body ItemDeleteRequest itemDeleteRequest);

    @POST(DELETE_PURCHASE)
    Call<EmptyResponse> deletePurchase(@Body ItemDeleteRequest itemDeleteRequest);

    @POST(DELETE_PRODUCT_UNIT)
    Call<EmptyResponse> deleteProductUnit(@Body ItemDeleteRequest itemDeleteRequest);

    @POST(ORDER_BILL_PRINT)
    Call<BillPrintResponse> orderBillPrint(@Body SupplierOrderBillPrintRequest supplierOrderBillPrintReq);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}

