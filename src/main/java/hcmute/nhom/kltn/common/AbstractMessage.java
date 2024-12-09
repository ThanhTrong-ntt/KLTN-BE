package hcmute.nhom.kltn.common;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import hcmute.nhom.kltn.util.Utilities;

/**
 * Class AbstractMessage.
 *
 * @author: ThanhTrong
 **/
public class AbstractMessage implements MessageSourceAware {

    private static final Logger logger =
            LoggerFactory.getLogger(AbstractMessage.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        return getMessage(code, null);
    }

    /**
     * Get message from message source.
     *
     * @param code message code
     * @param args message arguments
     * @return message
     */
    public String getMessage(String code, Object[] args) {
        try {
            return this.messageSource.getMessage(code, args, Locale.US);
        } catch (Exception e) {
            logger.debug(code);
            logger.error("Could not get message source: \n{}", e.getMessage(), e);
            return "";
        }
    }

    public String getMessageStart(String blNo, String method) {
        return getMessage("common.start.method", new String[]{blNo, method});
    }

    public String getMessageEnd(String blNo, String method) {
        return getMessage("common.end.method", new String[]{blNo, method});
    }

    public String getMessageInputParam(String blNo, String param, Object value) {
        return getMessage("common.input.parameter", new String[]{blNo, param, Utilities.parseString(value)});
    }

    /**
     * Get message output parameter.
     *
     * @param blNo  business logic number
     * @param param parameter name
     * @param value parameter value
     * @return String
     */
    public String getMessageOutputParam(String blNo, String param, Object value) {
        if (Objects.nonNull(value)) {
            if (value instanceof List) {
                value = Utilities.formatOutputListSize(value);
            } else if (value instanceof Map) {
                value = Utilities.formatOutputMap(value);
            }
        }
        return getMessage("common.output.parameter", new String[]{blNo, param, Utilities.parseString(value)});
    }
}
