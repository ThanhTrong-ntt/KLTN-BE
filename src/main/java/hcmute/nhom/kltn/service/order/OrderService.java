package hcmute.nhom.kltn.service.order;

import java.util.List;
import org.springframework.data.domain.Page;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.order.OrderDTO;
import hcmute.nhom.kltn.dto.order.OrderItemDTO;
import hcmute.nhom.kltn.model.order.Order;
import hcmute.nhom.kltn.service.AbstractService;

/**
 * Class OrderService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface OrderService extends AbstractService<OrderDTO, Order> {
    PaginationDTO<OrderDTO> getOrderByUser(String email, int pageNo, int pageSize, String sortBy, String sortDir);

    OrderDTO createOrder (OrderDTO orderDTO, List<OrderItemDTO> orderItemDTOList);

    PaginationDTO<OrderDTO> getAllOrderPagination(int pageNo, int pageSize, String sortBy, String sortDir);
}
