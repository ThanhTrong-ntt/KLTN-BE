package hcmute.nhom.kltn.service.order;

import java.util.List;
import hcmute.nhom.kltn.dto.order.OrderItemDTO;
import hcmute.nhom.kltn.model.order.OrderItem;
import hcmute.nhom.kltn.service.AbstractService;

/**
 * Class OrderItemService.
 *
 * @author: ThanhTrong
 **/
public interface OrderItemService extends AbstractService<OrderItemDTO, OrderItem> {
    List<OrderItemDTO> getOrderItemByOrderId(String orderId);
}
