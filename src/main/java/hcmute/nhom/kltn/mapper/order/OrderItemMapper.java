package hcmute.nhom.kltn.mapper.order;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import hcmute.nhom.kltn.dto.order.OrderItemDTO;
import hcmute.nhom.kltn.mapper.AbstractMapper;
import hcmute.nhom.kltn.model.order.OrderItem;

/**
 * Class OrderItemMapper.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Mapper()
public interface OrderItemMapper extends AbstractMapper<OrderItemDTO, OrderItem> {
    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);
}
