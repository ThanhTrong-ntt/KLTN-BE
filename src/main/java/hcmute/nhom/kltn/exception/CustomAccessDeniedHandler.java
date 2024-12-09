package hcmute.nhom.kltn.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import hcmute.nhom.kltn.common.payload.ApiResponse;

/**
 * Class CustomAccessDeniedHandler.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ApiResponse<Object> responseMetaDto = ApiResponse.builder()
                .data(null)
                .code(HttpStatus.FOUND.toString())
                .result(false)
                .message(accessDeniedException.getMessage())
                .build();
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(responseMetaDto));
    }
}
