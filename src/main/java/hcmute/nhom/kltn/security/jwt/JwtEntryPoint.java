package hcmute.nhom.kltn.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import hcmute.nhom.kltn.common.AbstractMessage;
import hcmute.nhom.kltn.common.payload.ApiResponse;

/**
 * Class JwtEntryPoint.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Component
public class JwtEntryPoint extends AbstractMessage implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Commence.
     *
     * @param request       HttpServletRequest
     * @param response      HttpServletResponse
     * @param authException AuthenticationException
     * @throws IOException      IOException
     * @throws ServletException ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        logger.error(getMessage("common.error.unAuthorized", new String[]{authException.getMessage()}));
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(mapper.writeValueAsString(ApiResponse.<Long>builder()
                .result(false)
                .code(HttpStatus.UNAUTHORIZED.toString())
                .message("Invalid token")
                .build()));
        response.getWriter().flush();
    }
}
