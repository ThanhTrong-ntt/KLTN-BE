package hcmute.nhom.kltn.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import hcmute.nhom.kltn.dto.AbstractDTO;
import hcmute.nhom.kltn.dto.product.ProductItemDTO;
import net.minidev.json.annotate.JsonIgnore;

/**
 * Class OrderItemDTO.
 *
 * @author: ThanhTrong
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO extends AbstractDTO {
    private String id;
    private int quantity;
    private ProductItemDTO product;
    @JsonIgnore
    private OrderDTO order;
    private Double subTotal;

}
