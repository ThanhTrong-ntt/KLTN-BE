package hcmute.nhom.kltn.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import hcmute.nhom.kltn.dto.AbstractDTO;
import hcmute.nhom.kltn.dto.ImageDTO;

/**
 * Class ProductItemDTo.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemDTO extends AbstractDTO {
    private String id;
    private ColorDTO color;
    private String size;
    private Integer stock;
    private ImageDTO mainImage;
    private ProductDTO product;
}
