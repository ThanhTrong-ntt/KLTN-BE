package hcmute.nhom.kltn.service.impl.order;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import hcmute.nhom.kltn.dto.order.OrderItemDTO;
import hcmute.nhom.kltn.exception.SystemErrorException;
import hcmute.nhom.kltn.mapper.order.OrderItemMapper;
import hcmute.nhom.kltn.model.order.OrderItem;
import hcmute.nhom.kltn.repository.order.OrderItemRepository;
import hcmute.nhom.kltn.service.impl.AbstractServiceImpl;
import hcmute.nhom.kltn.service.order.OrderItemService;

/**
 * Class OrderItemServiceImpl.
 *
 * @author: ThanhTrong
 **/
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl
        extends AbstractServiceImpl<OrderItemRepository, OrderItemMapper, OrderItemDTO, OrderItem>
        implements OrderItemService {
    private static final Logger logger = LoggerFactory.getLogger(OrderItemServiceImpl.class);
    private final String BL_NO = "OrderItemService";
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderItemRepository getRepository() {
        return orderItemRepository;
    }

    @Override
    public OrderItemMapper getMapper() {
        return OrderItemMapper.INSTANCE;
    }


    @Override
    public List<OrderItemDTO> getOrderItemByOrderId(String orderId) {
        String methodName = "getOrderItemByOrderId";
        logger.info(getMessageStart(BL_NO, methodName));
        List<OrderItem> orderItemList = orderItemRepository.findByOrderId(orderId);
        List<OrderItemDTO> orderItemDTOS = orderItemList.stream().map(item ->
                getMapper().toDto(item, getCycleAvoidingMappingContext())).collect(Collectors.toList());
        logger.info(getMessageEnd(BL_NO, methodName));
        return orderItemDTOS;
    }
}
