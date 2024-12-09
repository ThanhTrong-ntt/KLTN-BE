package hcmute.nhom.kltn.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import hcmute.nhom.kltn.dto.AbstractDTO;

/**
 * Class RatingDTO.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO extends AbstractDTO {
    private String id;
    private int rating;
    private String comment;
}
