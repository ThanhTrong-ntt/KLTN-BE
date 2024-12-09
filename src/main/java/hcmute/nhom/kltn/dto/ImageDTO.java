package hcmute.nhom.kltn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import hcmute.nhom.kltn.dto.product.ProductDTO;

/**
 * Class ImageDTO.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO extends AbstractDTO {
    private String id;
    private String fileName;
    private String fileType;
    private String url;
    private ProductDTO product;
    private Boolean removalFlag;
}
