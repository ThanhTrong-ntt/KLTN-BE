package hcmute.nhom.kltn.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import hcmute.nhom.kltn.dto.AbstractDTO;

/**
 * Class CouponDTO.
 *
 * @author: ThanhTrong
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponDTO extends AbstractDTO {
    private String id;
    private String code;
    private double discount;
    private String description;
    private String startDate;
    private String endDate;
    private Boolean removalFlag;
}
