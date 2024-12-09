package hcmute.nhom.kltn.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import hcmute.nhom.kltn.dto.AbstractDTO;

/**
 * Class ColorDTO.
 *
 * @author: ThanhTrong
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ColorDTO extends AbstractDTO {
    private String id;
    private String name;
    private String code;
    private String displayCode;

}
