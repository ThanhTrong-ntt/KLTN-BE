package hcmute.nhom.kltn.exception;

import java.util.Objects;

/**
 * Class SystemErrorException.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public class SystemErrorException extends BaseException {

    private static final long serialVersionUID = 1L;

    private static final int ERROR_STATUS_DEFAULT = 500;

    private static final String COMMON_MESSAGE =
            "Processing failed. Please try again. If the problem persists, please contact HappyTime support.";

    public SystemErrorException(String message, Throwable e) {
        super(ERROR_STATUS_DEFAULT, message, e);
    }

    public SystemErrorException(String message) {
        super(ERROR_STATUS_DEFAULT, message);
    }

    /**
     * Check start system error message.
     *
     * @param message String
     * @return boolean
     */
    public static boolean isStartSystemErrorMessage(String message) {
        return Objects.nonNull(message) && message.startsWith(COMMON_MESSAGE);
    }
}
