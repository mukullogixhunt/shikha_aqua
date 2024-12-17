package com.logixhunt.shikhaaqua.utils;

public class Constant {

    public static final String SUCCESS_RESPONSE_CODE = "200";
    public static final String SUCCESS_RESPONSE = "success";

    public static final String yyyyMMdd = "yyyy-MM-dd";
    public static final String MMdd = "MM-dd";
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String ddMMMyyyy = "dd-MMM-yyyy";

    public static final String ddMMM = "dd-MMM";
    public static final String HHmmss = "HH:mm:ss";
    public static final String hhmma = "hh:mm a";
    public static final String MM = "MM";

    public static final String EEddMMM = "EE, dd MMM";
    public static String WEBVIEW_TITLE = "";
    public static String WEBVIEW_URL = "";

    public static final String HOME_SCREEN = "home";
    public static final String OTP_SCREEN = "otp";
    public static final String DB_NAME = "AppDatabase";
    public static String EEEEddMMMM = "EEEE, dd MMMM";


    public interface PreferenceConstant {
        String USER_DATA = "user_data";
        String IS_LOGGED_IN = "is_logged_in";
        String NOTIFICATION_COUNT = "notification_count";
        String FIREBASE_TOKEN = "firebase_token";
        String FIRST_LAUNCH_COMPLETE = "first_launch_complete";

        String IS_LOCATION_SELECTED = "is_location_selected";
        String STATE_ID = "state_id";
        String STATE_NAME = "state_name";
        String CITY_NAME = "city_name";
        String CITY_ID = "city_id";

        String LAT_LNG = "lat_lng";
        String LAT = "lat";
        String LNG = "lng";
        String ADDRESS = "address";
        String PINCODE = "pincode";
        String AREA_ID = "area_id";
        String AREA_NAME = "area_name";
        String OTHER_ADDRESS = "other_address";
    }

    public interface ApiKey {


        String M_USER_MOBILE = "m_user_mobile";
        String M_USER_ID = "m_user_id";
        String M_USER_OTP = "m_user_otp";
        String M_STATE_ID = "m_state_id";
        String M_USER_NAME = "m_user_name";
        String M_USER_EMAIL = "m_user_email";
        String M_USER_DOB = "m_user_dob";
        String M_USER_STATE = "m_user_state";
        String M_USER_CITY = "m_user_city";
        String M_USER_ADDRESS = "m_user_address";
        String M_ORDER_LAT = "m_order_lat";
        String M_ORDER_LONG = "m_order_long";
        String M_ORDER_ADDRESS = "m_order_address";
        String M_ORDER_DELV_DATE = "m_order_delv_date";
        String M_ORDER_DELV_TIME = "m_order_delv_time";
        String M_ORDER_DELV_CHARGES = "m_order_delv_charges";
        String M_ORDER_PAYMODE = "m_order_paymode";
        String M_ORDER_G_TOTAL = "m_order_g_total";

        String M_ORDER_STATUS = "m_order_status";
        String M_ORDER_ID = "m_order_id";
        String M_CITY_ID = "m_city_id";
        String M_ORDER_AREA = "m_order_area";
        String M_USER_FCM = "m_user_fcm";
        String M_EXECUTIVE_ID = "m_executive_id" ;
        String M_ORDER_REMARK = "m_order_remark";
        String M_ORDER_OTHER_ADDRESS = "m_order_other_address";

        String M_COUPON_CODE = "m_coupon_code";
        String M_ORDER_COUPON_ID = "m_order_coupon_id";
        String M_ORDER_COUPON_DISCOUNT = "m_order_coupon_discount";
        String M_PAID_AMOUNT = "m_paid_amount";
        String M_TRANSACTION_ID = "m_transaction_id";
        String M_TRANS_ID = "m_trans_id";
        String M_PAYMENT_ID = "m_payment_id";
        String M_TRANS_STATUS = "m_trans_status";
        String M_ORDER_BILL_NO = "m_order_bill_no";
        String M_PAYMENT_STATUS = "m_payment_status";
        String M_TRANSACTION_NUMBER = "m_transaction_number";
    }

    public interface EndPoint {


        String USER_LOGIN = "user_login";
        String SEND_USER_OTP = "send_user_otp";
        String VERIFY_USER_OTP = "verify_user_otp";
        String GET_STATE = "get_state";
        String GET_CITY = "get_city";
        String GET_SLIDERS = "get_sliders";
        String GET_BOTTLES = "get_bottles";
        String UPDATE_USER_PROFILE = "update_user_profile";
        String INSERT_ORDER = "insert_order";
        String ORDER_LIST = "order_list";
        String RECENT_ORDER_LIST = "recent_order_list";
        String ORDER_DETAILS = "order_details";
        String GET_AREA = "get_area";
        String UPDATE_FCM = "update_fcm";
        String GET_NOTIFICATION = "get_notification";
        String GET_DELIVERY_CHARGES = "get_delivery_charges";
        String CHECK_USER_ACTIVITY = "check_user_activity";
        String NO_ORDER_DATE = "no_order_date";
        String GET_ALL_COUPON_LIST = "get_all_coupon_list";
        String GET_COUPON = "get_coupon";
        String PAYMENT_BALANCE = "payment_balance";
        String INSERT_PAYMENT = "insert_payment";
        String UPDATE_PAYMENT_STATUS = "update_payment_status";
        String UPDATE_ORDER_PAYMENT_STATUS = "update_order_payment_status";
    }

    public interface BundleExtras {

        String USER_ID = "user_id";
        String IS_FROM = "is_from";

        String STATE_ID = "state_id";
        String CITY_ID = "city_id";
        String REDIRECT = "redirect";

        String LAT = "lat_lng";
        String LNG = "lat_lng";
        String ADDRESS = "address";
        String PINCODE = "pincode";
        String ORDER_ID = "order_id";
        String AREA_ID = "area_id";
        String CITY_NAME = "city_name";
        String COUPON_DETAILS = "coupon_details";
    }

}
