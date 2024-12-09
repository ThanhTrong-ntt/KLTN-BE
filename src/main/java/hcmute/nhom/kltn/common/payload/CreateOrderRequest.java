package hcmute.nhom.kltn.common.payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import hcmute.nhom.kltn.dto.order.OrderDTO;
import hcmute.nhom.kltn.dto.order.OrderItemDTO;

/**
 * Class CreateOrderRequest.
 *
 * @author: ThanhTrong
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    private OrderDTO order;
    private List<OrderItemDTO> orderItems;
}
