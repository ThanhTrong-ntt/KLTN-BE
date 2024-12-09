package hcmute.nhom.kltn.mapper.order;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import hcmute.nhom.kltn.dto.order.OrderDTO;
import hcmute.nhom.kltn.mapper.AbstractMapper;
import hcmute.nhom.kltn.model.order.Order;

/**
 * Class OrderMapper.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Mapper()
public interface OrderMapper extends AbstractMapper<OrderDTO, Order> {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
}
