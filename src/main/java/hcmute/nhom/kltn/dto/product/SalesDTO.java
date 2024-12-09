package hcmute.nhom.kltn.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import hcmute.nhom.kltn.dto.AbstractDTO;

/**
 * Class SalesDTO.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SalesDTO extends AbstractDTO {
    private String id;
    private String name;
    private String description;
    private String code;
    private BigDecimal discount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private Date endDate;
}
