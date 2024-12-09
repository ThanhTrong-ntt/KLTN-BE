package hcmute.nhom.kltn.config;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Class MessageConfig.
 *
 * @author: ThanhTrong
 **/
@Configuration
public class MessageConfig {

    /**
     * Message source.
     * @return MessageSource
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:message"); // Tên tệp tin properties
        source.setDefaultEncoding("UTF-8");
        source.setDefaultLocale(Locale.US); // Thiết lập Locale mặc định
        return source;
    }
}
