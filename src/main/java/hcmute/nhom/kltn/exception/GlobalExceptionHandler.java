package hcmute.nhom.kltn.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import hcmute.nhom.kltn.common.payload.ApiResponse;

/**
 * Class GlobalExceptionHandler.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Object> handleNotFoundException(NotFoundException e) {
        return ApiResponse.builder()
                .data(null)
                .code(HttpStatus.NOT_FOUND.toString())
                .result(false)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Object> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.builder()
                .data(null)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .result(false)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ApiResponse<Object> handleConstraintViolation(Exception ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException constraintEx = (ConstraintViolationException) ex;
            Set<ConstraintViolation<?>> violations = constraintEx.getConstraintViolations();

            for (ConstraintViolation<?> violation : violations) {
                String fieldName = violation.getPropertyPath().toString();
                String errorMessage = violation.getMessage();
                errors.put(fieldName, errorMessage);
            }
        } else if (ex instanceof DataIntegrityViolationException) {
            errors.put("error", "Data integrity violation:" + ex.getCause().getCause().getMessage());
        }

        return ApiResponse.builder()
                .data(errors)
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .result(false)
                .message("Validation error(s) occurred.")
                .build();
    }
}
