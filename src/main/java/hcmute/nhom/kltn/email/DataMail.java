package hcmute.nhom.kltn.email;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class DataMail.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataMail {
    private String to;
    private String subject;
    private String content;
    private Map<String, Object> props;
}
