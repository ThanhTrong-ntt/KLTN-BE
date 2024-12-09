package hcmute.nhom.kltn.repository.order;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import hcmute.nhom.kltn.model.order.OrderItem;
import hcmute.nhom.kltn.repository.AbstractRepository;

/**
 * Class OrderItemRepository.
 *
 * @author: ThanhTrong
 **/
public interface OrderItemRepository extends AbstractRepository<OrderItem, String> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") String orderId);
}
