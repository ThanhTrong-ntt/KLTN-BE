package hcmute.nhom.kltn.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import hcmute.nhom.kltn.exception.CustomAccessDeniedHandler;
import hcmute.nhom.kltn.security.jwt.JwtEntryPoint;
import hcmute.nhom.kltn.security.jwt.JwtTokenFilter;
import hcmute.nhom.kltn.security.oauth2.CustomOAuth2UserService;
import hcmute.nhom.kltn.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import hcmute.nhom.kltn.security.oauth2.OAuth2AuthenticationFailureHandler;
import hcmute.nhom.kltn.security.oauth2.OAuth2AuthenticationSuccessHandler;
import hcmute.nhom.kltn.security.pricipal.CustomUserDetailService;

/**
 * Class WebSecurityConfig.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtEntryPoint jwtEntryPoint;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * JwtTokenFilter.
     *
     * @return JwtTokenFilter
     */
    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(customUserDetailsService) // Cung cấp userservice cho spring security
                .passwordEncoder(passwordEncoder()); // cung cấp password encoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors() //Ngăn chặn truy cập từ các domain khác
                .and()
                .csrf().disable()
                .authorizeRequests()
                // Cho phép các endpoint công khai
                .antMatchers(
                        "/oauth2/**",
                        "/login/**",
                        "/api/v1/auth/**",
                        "/api/v1/products/**",
                        "/api/v1/categories/**",
                        "/api/v1/sales/**",
                        "/api/v1/coupons/**"
                ).permitAll()
                // Yêu cầu quyền ADMIN cho các endpoint cần quản lý
                .antMatchers(
                        "/api/v1/users/**",
                        "/api/v1/orders",
                        "/api/v1/sale/**",
                        "/api/v1/color/**",
                        "/api/v1/coupon/**"
                ).hasAuthority("ADMIN")
                // Bảo vệ endpoint kích hoạt tài khoản người dùng
                .antMatchers(HttpMethod.POST, "/user/active/**").hasAuthority("ADMIN")
                //.antMatchers("/api/v1/user/active/**").permitAll()
                //.antMatchers("/api/v1/products/**").permitAll()
                //.antMatchers("/api/v1/media/**").permitAll()
                //.antMatchers("/api/v1/colors/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                //Tất cả các request khác đều được phải xác thực trước khi truy cập
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Thêm một lớp Filter kiểm tra jwt
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
