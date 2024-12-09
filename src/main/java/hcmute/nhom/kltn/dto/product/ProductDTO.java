package hcmute.nhom.kltn.dto.product;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import hcmute.nhom.kltn.dto.AbstractDTO;
import hcmute.nhom.kltn.dto.ImageDTO;

/**
 * Class ProductDTO.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO extends AbstractDTO {
    private String id;
    private String freeInformation;
    private String longDescription;
    private String washingInformation;
    private String productName;
    private String slug;
    private BigDecimal basePrice;
    private BigDecimal promoPrice;
    private SalesDTO sales;
    private CategoryDTO gender;
    private CategoryDTO category;
    private List<ImageDTO> subImages;
    private List<RatingDTO> ratings;
    private Boolean removalFlag;

}
