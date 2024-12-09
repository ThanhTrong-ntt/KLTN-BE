package hcmute.nhom.kltn.security.oauth2;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import hcmute.nhom.kltn.common.payload.JwtAuthenticationResponse;
import hcmute.nhom.kltn.config.AppProperties;
import hcmute.nhom.kltn.exception.BaseException;
import hcmute.nhom.kltn.security.jwt.JwtProvider;
import hcmute.nhom.kltn.util.CookieUtils;
import static hcmute.nhom.kltn.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * Class OAuth2AuthenticationSuccessHandler.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private AppProperties appProperties;

    private final JwtProvider jwtProvider;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(AppProperties appProperties,
                                              JwtProvider jwtProvider, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.appProperties = appProperties;
        this.jwtProvider = jwtProvider;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        //OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        //Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        //String registrationId = authToken.getAuthorizedClientRegistrationId();
        //
        //// Create UserPrincipal from OAuth2User
        //UserPrincipal userPrincipal = UserPrincipal.createFromOAuth2User(authToken.getPrincipal());
        //
        //OAuth2UserDetail userDetail = OAuth2UserDetailFactory.getOAuth2UserDetail(registrationId, attributes);
        //
        //if (ObjectUtils.isEmpty(userDetail)) {
        //    throw new BaseException(HttpStatus.BAD_REQUEST.value(), "Cannot found oauth2 user from properties!");
        //}
        //
        //UserDTO userDTO = userService.findUserByEmailAndProviderId(userDetail.getEmail(), registrationId);
        ////UserDTO userDTO = userService.findByEmail(email);
        //if (Objects.isNull(userDTO)) {
        //    // Nếu user chưa tồn tại, thêm vào DB
        //    userDTO = new UserDTO();
        //    userDTO.setEmail(userDetail.getEmail());
        //    userDTO.setUserName(userDetail.getName());
        //    userDTO.setProviderId(registrationId);
        //    Set<RoleDTO> roleDTOS = new HashSet<>();
        //    roleDTOS.add(roleService.findByRoleName(RoleName.USER.name()));
        //    userDTO.setRoles(roleDTOS);
        //
        //    // Thêm các trường thông tin khác nếu cần
        //    userService.save(userDTO);
        //}
        //
        //// Tạo JWT token
        //Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, null, authToken.getAuthorities());
        //JwtAuthenticationResponse token = jwtProvider.createToken(auth);
        //
        //// Sử dụng ObjectMapper để chuyển đổi ApiResponse thành JSON
        //ObjectMapper objectMapper = new ObjectMapper();
        //String jsonResponse = objectMapper.writeValueAsString(new ApiResponse<>(token, "Login successfully!!"));
        //// Trả token về cho frontend qua response
        //response.setContentType("application/json;charset=UTF-8");
        //response.getWriter().write(jsonResponse);
        //response.getWriter().flush();
        String targetUrl = determineTargetUrl(request, response);
        JwtAuthenticationResponse token = jwtProvider.createToken(authentication);

        ResponseCookie isOAuth2 = ResponseCookie.from("oAuth2", String.valueOf(true))
                .httpOnly(false)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(3600) // Thời gian sống của accessToken
                .build();

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", token.getAccessToken())
                .httpOnly(false)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(3600) // Thời gian sống của accessToken
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .httpOnly(false)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(86400) // Thời gian sống của refreshToken
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, isOAuth2.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());



        return UriComponentsBuilder.fromUriString(targetUrl)
                .build().toUriString();
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }

}
