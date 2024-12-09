package hcmute.nhom.kltn.repository.product;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import hcmute.nhom.kltn.model.product.ProductItem;
import hcmute.nhom.kltn.repository.AbstractRepository;

/**
 * Class ProductItemRepository.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface ProductItemRepository extends AbstractRepository<ProductItem, String> {
    @Query(value = "SELECT pi FROM ProductItem pi WHERE pi.product.id = :productId")
    List<ProductItem> findByProductId(@Param("productId") String productId);

    @Query(value = "SELECT pi FROM ProductItem pi WHERE pi.color.name = :colorName AND pi.size = :size")
    ProductItem findByColorAndSize(@Param("colorName") String colorName, @Param("size") String size);
}
