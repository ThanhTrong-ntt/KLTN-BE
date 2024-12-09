package hcmute.nhom.kltn.controller.v1;

import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import hcmute.nhom.kltn.common.payload.ApiResponse;
import hcmute.nhom.kltn.common.payload.ForgotPasswordRequest;
import hcmute.nhom.kltn.common.payload.JwtAuthenticationResponse;
import hcmute.nhom.kltn.common.payload.LoginRequest;
import hcmute.nhom.kltn.dto.UserDTO;
import hcmute.nhom.kltn.security.jwt.JwtProvider;
import hcmute.nhom.kltn.service.UserService;
import hcmute.nhom.kltn.service.session.SessionManagementService;
import hcmute.nhom.kltn.util.Constants;
import hcmute.nhom.kltn.util.SessionConstants;
import hcmute.nhom.kltn.util.Utilities;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Class AuthenticationController.
 *
 * @author: ThanhTrong
 **/
@RestController
@RequiredArgsConstructor
public class AuthenticationController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final SessionManagementService sessionService;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    /**
     * Register User.
     *
     * @return ResponseEntity<?>
     */
    @PostMapping("auth/register")
    public ResponseEntity<ApiResponse<Boolean>> registerUser(
            HttpServletRequest httpServletRequest,
            @RequestBody UserDTO userDTO
    ) {
        String messageStart = getMessageStart(httpServletRequest.getRequestURL().toString(), "registerUser");
        String messageEnd = getMessageEnd(httpServletRequest.getRequestURL().toString(), "registerUser");
        logger.info("{}", messageStart);
        // Execute RegisterUser
        Boolean result = userService.registerUser(userDTO);
        logger.info("{}", messageEnd);
        return ResponseEntity.ok().body(
                ApiResponse.<Boolean>builder()
                        .result(result)
                        .message("User registered successfully! Please check your email to activate account!")
                        .build());
    }

    /**
     * Login User.
     *
     * @param httpServletRequest HttpServletRequest
     * @param loginRequest       LoginRequest
     * @return ResponseEntity<?>
     */
    @PostMapping("auth/login")
    public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> login(
            HttpServletRequest httpServletRequest,
            HttpSession session,
            @Validated @RequestBody LoginRequest loginRequest) {
        String messageStart = getMessageStart(httpServletRequest.getRequestURL().toString(), "loginUser");
        String messageEnd = getMessageEnd(httpServletRequest.getRequestURL().toString(), "loginUser");
        logger.info("{}", messageStart);
        try {
            if (session != null) {
                session.invalidate();
            }
            sessionService.setAttribute(SessionConstants.IP_ADDRESS, Utilities.getIpAdressByHeader(httpServletRequest));
            // Execute Login
            UserDTO userDTO = userService.findByEmail(loginRequest.getEmail());
            if (userDTO == null) {
                logger.info("{}", messageEnd);
                return new ResponseEntity<>(
                        ApiResponse.<JwtAuthenticationResponse>builder()
                                .result(false)
                                .code(HttpStatus.BAD_REQUEST.toString())
                                .message("Người dùng không xuất hiện trong hệ thống!")
                                .build(),
                        HttpStatus.BAD_REQUEST);
            }
            if (!passwordEncoder.matches(loginRequest.getPassword(), userDTO.getPassword())) {
                logger.info("{}", messageEnd);
                return new ResponseEntity<>(
                        ApiResponse.<JwtAuthenticationResponse>builder()
                                .result(false)
                                .code(HttpStatus.BAD_REQUEST.toString())
                                .message("The password was wrong!")
                                .build(), HttpStatus.BAD_REQUEST);
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            sessionService.setAttribute(SessionConstants.USER_ID, userDTO.getId());
            sessionService.setAttribute(SessionConstants.USER_NAME, userDTO.getUserName());
            sessionService.setAttribute(SessionConstants.USER_EMAIL, userDTO.getEmail());
            sessionService.setAttribute(SessionConstants.LOGGED_IN, Constants.STAGE_LOGIN_SUCCESS);
            logger.info("Creating Json Web Token!!");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("{}", messageEnd);
            return ResponseEntity.ok().body(
                    ApiResponse.<JwtAuthenticationResponse>builder()
                            .result(true)
                            .data(jwtProvider.createToken(authentication))
                            .message("Login successfully!")
                            .build());
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            return new ResponseEntity<>(
                    ApiResponse.<JwtAuthenticationResponse>builder()
                            .result(false)
                            .code(HttpStatus.BAD_REQUEST.toString())
                            .message(e.getMessage())
                            .build(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Logout.
     *
     * @param httpServletRequest HttpServletRequest
     * @return ResponseEntity<?>
     */
    @PostMapping("auth/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String messageStart = getMessageStart(httpServletRequest.getRequestURL().toString(), "logout");
        String messageEnd = getMessageEnd(httpServletRequest.getRequestURL().toString(), "logout");
        logger.info("{}", messageStart);
        // Execute Logout
        HttpSession session = sessionService.destroy();
        try {
            clearAllCookies(httpServletRequest, response);
            session.invalidate();
        } catch (Exception e) {
            logger.info("The HttpSession has already be invalidated. So no need invalidated");
        }
        ApiResponse<String> apiResponse =
                ApiResponse.<String>builder()
                        .code(HttpStatus.OK.toString())
                        .result(true)
                        .message("Logout successfully!")
                        .build();
        logger.info("{}", messageEnd);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Forgot Password.
     *
     * @param httpServletRequest    HttpServletRequest
     * @param forgotPasswordRequest ForgotPasswordRequest
     * @return ResponseEntity<?>
     */
    @PostMapping("auth/forgot-password")
    public ResponseEntity<ApiResponse<Boolean>> forgotPassword(
            HttpServletRequest httpServletRequest,
            @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        String messageStart = getMessageStart(httpServletRequest.getRequestURL().toString(), "forgotPassword");
        String messageEnd = getMessageEnd(httpServletRequest.getRequestURL().toString(), "forgotPassword");
        logger.info("{}", messageStart);
        try {
            // Execute ForgotPassword
            Boolean result = userService.forgotPassword(forgotPasswordRequest.getEmail());
            logger.debug("{}", forgotPasswordRequest);
            logger.info("{}", messageEnd);
            return ResponseEntity.ok().body(
                    ApiResponse.<Boolean>builder()
                            .result(result)
                            .message("Kiểm tra mail để lấy lại mật khẩu!")
                            .build());
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            logger.info("{}", messageEnd);
            return new ResponseEntity<>(
                    ApiResponse.<Boolean>builder()
                            .result(false)
                            .message(e.getMessage())
                            .build(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * refreshToken.
     * @param httpServletRequest HttpServletRequest
     * @param refreshToken     String
     * @return ResponseEntity<ApiResponse<JwtAuthenticationResponse>>
     */
    @PostMapping("auth/refresh-token/{refreshToken}")
    public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> refreshToken(
            HttpServletRequest httpServletRequest,
            @PathVariable("refreshToken") String refreshToken) {
        String messageStart = getMessageStart(httpServletRequest.getRequestURL().toString(), "refreshToken");
        String messageEnd = getMessageEnd(httpServletRequest.getRequestURL().toString(), "refreshToken");
        logger.info("{}", messageStart);
        try {
            if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {
                String email = jwtProvider.getUsernameFromJWT(refreshToken);
                UserDTO user = userService.findByEmail(email);
                Date now = new Date();
                Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
                String accessToken = Jwts.builder().setSubject(user.getEmail())
                        .setIssuedAt(new Date())
                        .setExpiration(expiryDate)
                        .signWith(SignatureAlgorithm.HS512, jwtSecret)
                        .compact();
                JwtAuthenticationResponse token = new JwtAuthenticationResponse(accessToken, refreshToken);
                logger.info("{}", messageEnd);
                return ResponseEntity.ok().body(
                        ApiResponse.<JwtAuthenticationResponse>builder()
                                .result(true)
                                .data(token)
                                .message("Refresh successfully!")
                                .build());
            }
            logger.info("{}", messageEnd);
            return new ResponseEntity<>(
                    ApiResponse.<JwtAuthenticationResponse>builder()
                            .result(false)
                            .code(HttpStatus.BAD_REQUEST.toString())
                            .message("Refresh failed!")
                            .build(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            logger.info("{}", messageEnd);
            return new ResponseEntity<>(
                    ApiResponse.<JwtAuthenticationResponse>builder()
                            .result(false)
                            .code(HttpStatus.BAD_REQUEST.toString())
                            .message(e.getMessage())
                            .build(), HttpStatus.BAD_REQUEST);
        }
    }
    public void clearAllCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue(null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
    }
}
