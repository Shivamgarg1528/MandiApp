package ig.com.digitalmandi.util;

public class AppConstant {

    /* Device Type*/
    public static final String ANDROID_DEVICE = "1";

    /*End Point*/
    public static final String END_POINT = "http://parentingnall.com/shivam/to/";

    /* Customer Type */
    public static final int SELLER = 1;
    public static final int CUSTOMER = 2;

    /* Item Status*/
    public static final String DISABLE = "0";
    public static final String ENABLE = "1";

    /* Seller Purchase Flag*/
    public static final String PURCHASE_LIST_ALL = "0";
    public static final String PURCHASE_LIST_PAGING = "1";
    public static final String COLUMN_ORDER_ID = "0";
    public static final String COLUMN_PURCHASE_ID = "1";
    public static final String DELETE_PRODUCT = "0";
    public static final String DELETE_UNIT = "1";
    public static final String DELETE_OR_PAYMENT_ORDER = "0";
    public static final String DELETE_OR_PAYMENT_PURCHASE = "1";
    public static final String IN_STOCK = "1";
    public static final String OUT_STOCK = "0";
    public static final String API_DATE_FORMAT = "yyyy-MM-dd";
    public static final String APP_DATE_FORMAT = "dd-MMM-yyyy";

    /* Key*/
    public static final String KEY_OBJECT = "keyObject";

    /*Request Codes*/
    public static final int REQUEST_CODE_EDIT = 1;
    public static final int REQUEST_CODE_CALL_PERMISSION = 2;
    public static final int REQUEST_CODE_WRITE_PERMISSION = 3;
    public static final int REQUEST_CODE_PLACE_NEW_ORDER = 4;
    public static final int REQUEST_CODE_IMAGE = 5;

    /* Comparator*/
    public static final int COMPARATOR_ALPHA = 1;
    public static final int COMPARATOR_PHONE = 2;

    /* Operations On Events */
    public static final int OPERATION_NONE = 0;
    public static final int OPERATION_EDIT = 1;
    public static final int OPERATION_DELETE = 2;
    public static final int OPERATION_STATUS_MODIFY = 3;
    public static final int OPERATION_CUSTOMER_OPEN = 4;
    public static final int OPERATION_CUSTOMER_CALL = 5;
    public static final int OPERATION_CUSTOMER_ORDERS = 6;
    public static final int OPERATION_ORDER_PAYMENT_DETAILS = 7;
    public static final int OPERATION_ORDER_DETAILS = 8;
    public static final int OPERATION_ORDER_BILL_PRINT = 9;

    /* Operation On Items*/
    public static final String ADD = "add";
    public static final String DELETE = "delete";
    public static final String UPDATE = "update";
    public static final String INTEREST_UNPAID = "0";
    public static final String INTEREST_PAID = "1";

    /* Qty*/
    public static final int QTY_MIN = 1;
    public static final int QTY_MAX = 1000;

    /* Files Name*/
    static final String DIRECTORY_NAME = "DIGITAL_MANDI";
    static final String ORDER_BILL_PREFIX = "Order_#";
    static final String PAYMENT_BILL_PREFIX = "Payment_#";
}


