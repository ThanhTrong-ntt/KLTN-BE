package hcmute.nhom.kltn.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import hcmute.nhom.kltn.model.Image;

/**
 * Class MediaFileRepository.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface ImageRepository extends AbstractRepository<Image, String> {

    Image findByFileName(String fileName);

    @Query(value = "SELECT img FROM Image img WHERE img.product.id = :productId")
    List<Image> findByProductId(@Param("productId") String productId);
}
