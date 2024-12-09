package hcmute.nhom.kltn.dto.order;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import hcmute.nhom.kltn.dto.AbstractDTO;
import hcmute.nhom.kltn.dto.UserDTO;
import hcmute.nhom.kltn.model.Address;

/**
 * Class OrderDTO.
 *
 * @author: ThanhTrong
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO extends AbstractDTO {
    private String id;
    private int productsCount;
    private String note;
    private UserDTO user;
    private Double subTotal;
    private Double tax;
    private Double total;
    private String status;
    private String paymentMethod;
    private Address address;
    private Boolean isPaid;
    private List<OrderItemDTO> items;

}
