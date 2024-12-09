package hcmute.nhom.kltn.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.Date;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import hcmute.nhom.kltn.util.DateHandler;
import hcmute.nhom.kltn.util.DateHandlerSerialize;

/**
 * Class JacksonConfig.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new DateHandler());
        module.addSerializer(Date.class, new DateHandlerSerialize());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
