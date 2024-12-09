package hcmute.nhom.kltn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class WebMvcConfig.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    //    @Value("${app.cors.allowedOrigins}")
    //    private String[] allowedOrigins;
    //    @Value("${app.cors.allowedOriginsAdmin}")
    //    private String[] allowedOriginsAdmin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4000","http://localhost:3000", "http://localhost:3080","http://localhost:3001")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }
}
