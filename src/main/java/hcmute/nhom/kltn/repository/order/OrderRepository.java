package hcmute.nhom.kltn.repository.order;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import hcmute.nhom.kltn.model.order.Order;
import hcmute.nhom.kltn.repository.AbstractRepository;

/**
 * Class OrderRepository.
 *
 * @author: ThanhTrong
 **/
public interface OrderRepository extends AbstractRepository<Order, String> {

    @Query(value = "SELECT o FROM Order o WHERE o.user.email = :email")
    List<Order> getOrderByUser(@Param("email") String email);
}
