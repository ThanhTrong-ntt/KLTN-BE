package hcmute.nhom.kltn.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

/**
 * Class Utilities.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public final class Utilities {
    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);

    private Utilities() {
    }

    /**
     * Parse String.
     *
     * @param value Object
     * @return String
     */
    public static String parseString(Object value) {
        if (Objects.isNull(value)) {
            return null;
        }

        try {
            return String.valueOf(value);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Parse Integer.
     *
     * @param value Object
     * @return Integer
     */
    public static Object formatOutputListSize(Object value) {
        if (value instanceof List) {
            int size = ((List<Object>) value).size();
            value = size + (size > 1 ? " records" : " record");
        }

        return value;
    }

    /**
     * Parse Integer.
     *
     * @param value Object
     * @return Integer
     */
    public static Object formatOutputMap(Object value) {
        Map<Object, Object> map = new HashMap<>();
        Map<Object, Object> mapValue = (Map<Object, Object>) value;
        mapValue.entrySet().stream().forEach(e -> {
            Object valTemp = e.getValue();
            if (valTemp instanceof List) {
                valTemp = formatOutputListSize(valTemp);
            }
            map.put(e.getKey(), valTemp);
        });

        return map;
    }

    /**
     * Parse Integer.
     *
     * @param pageNo   Page number
     * @param pageSize Page size
     * @param sortBy   Sort by
     * @param sortDir  Sort direction
     * @return PageRequest
     */
    public static Pageable getPageRequest(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(pageNo, pageSize, sort);
    }

    public static <T> Page<T> createPageFromList(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());

        if (pageable.getSort().isSorted()) {
            Comparator<T> comparator = null;
            for (Sort.Order order : pageable.getSort()) {
                Comparator<T> orderComparator = Comparator.comparing(item -> {
                    try {
                        Method getter = item.getClass().getMethod("get" + capitalize(order.getProperty()));
                        return (Comparable) getter.invoke(item);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                if (order.getDirection() == Sort.Direction.DESC) {
                    orderComparator = orderComparator.reversed();
                }

                comparator = comparator == null ? orderComparator : comparator.thenComparing(orderComparator);
            }
            list.sort(comparator);
        }

        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Get IP Address.
     * @param request HttpServletRequest
     * @return
     * @throws UnknownHostException
     */
    public static String getIpAddress(HttpServletRequest request) throws UnknownHostException {
        String remoteAddr = request.getRemoteAddr();
        if (remoteAddr.equals("0:0:0:0:0:0:0:1")) {
            InetAddress localip = InetAddress.getLocalHost();
            remoteAddr = localip.getHostAddress();
        }

        logger.info("After process getRemoteAddr: {}", remoteAddr);
        return remoteAddr;
    }

    /**
     * Get IP Address By Header.
     * @param request HttpServletRequest
     * @return
     * @throws UnknownHostException
     */
    public static String getIpAdressByHeader(HttpServletRequest request) throws UnknownHostException {
        logger.info("getIpAdressByHeader - START");
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(ip)) {
            logger.info("IP Access from X-Forwarded-For: " + ip);
            logger.info("getIpAdressByHeader - END");
            return ip;
        } else {
            logger.info("Before process getRemoteAddr: {}", request.getRemoteAddr());
            ip = getIpAddress(request);
            logger.info("After process getRemoteAddr: {}", ip);
            logger.info("getIpAdressByHeader - END");
            return ip;
        }
    }

    /**
     * isBasicDataType.
     * @param obj
     * @return
     */
    public static boolean isBasicDataType(Object obj) {
        if (obj instanceof Integer) {
            return true;
        }
        if (obj instanceof Long) {
            return true;
        }
        if (obj instanceof Double) {
            return true;
        }
        if (obj instanceof BigDecimal) {
            return true;
        }
        if (obj instanceof String) {
            return true;
        }

        return false;
    }

    /**
     * getValue.
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getValue(Object obj, String fieldName) {
        if (Objects.isNull(obj) || Objects.isNull(fieldName) || Objects.equals(fieldName, "")) {
            return null;
        }
        if (isBasicDataType(obj)) {
            return obj;
        }

        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            if (Objects.isNull(field)) {
                return null;
            }

            String fieldType = field.getType().getTypeName();
            fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String method = "get" + fieldName;
            if (fieldType.equals("boolean")) {
                method = "is" + fieldName;
            }

            return obj.getClass().getMethod(method).invoke(obj);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * Generate Temp Password.
     * @param length int
     * @return String
     */
    public static String generateTempPwd(int length) {
        String numbers = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char otp[] = new char[length];
        Random getOtpNum = new Random();
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(getOtpNum.nextInt(numbers.length()));
        }
        String optCode = "";
        for (int i = 0; i < otp.length; i++) {
            optCode += otp[i];
        }
        return optCode;
    }

    /**
     * Get value by field name from Object.
     * @param obj Object is DTO/Entity
     * @param fieldName
     * @return value string
     */
    public static String getValueByFieldName(Object obj, String fieldName) {
        return Utilities.parseString(getValue(obj, fieldName));
    }

    /**
     * Create Pageable.
     * @param page int
     * @param size int
     * @return Pageable
     */
    public static Pageable createPageRequestUsing(int page, int size) {
        return PageRequest.of(page, size);
    }
}
