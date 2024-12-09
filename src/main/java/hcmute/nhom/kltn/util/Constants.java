package hcmute.nhom.kltn.util;

import java.math.BigDecimal;

/**
 * Class Constants.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public class Constants {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "5";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    public static final int STAGE_LOGIN_SUCCESS = 1;
    public static final boolean REMOVAL_FLAG_FALSE = false;
    public static final boolean REMOVAL_FLAG_TRUE = true;
    public static final BigDecimal HUNDRED = new BigDecimal(100);
    public static final BigDecimal THOUSAND = new BigDecimal(1000);

    public static final String DEFAULT_AVATAR =
            "https://firebasestorage.googleapis.com/v0/b/kltn-91a43.appspot.com/o/image%2Fdefault-avatar.png?alt=media";

    public static class SEND_MAIL {
        public final static String CLIENT_FORGET_PASSWORD = "CONFIRM NEW PASSWORD USER";

        public final static String CLIENT_ACTIVE_USER = "CONFIRM ACTIVE USER";
    }

    public final static class TEMPLATE_FILE_NAME {
        public final static String CLIENT_FORGET_PASSWORD = "ForgotPassword";
        public final static String CLIENT_ACTIVE_USER = "ActiveUser";
    }

}
