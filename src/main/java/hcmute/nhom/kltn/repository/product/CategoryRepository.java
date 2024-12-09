package hcmute.nhom.kltn.repository.product;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import hcmute.nhom.kltn.model.product.Category;
import hcmute.nhom.kltn.repository.AbstractRepository;

/**
 * Class CategoryRepository.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface CategoryRepository extends AbstractRepository<Category, String> {

    @Query("SELECT c FROM Category c WHERE c.level = :level")
    Page<Category> getAllFromLevel(@Param("level") Integer level, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.level = :level")
    List<Category> getAllFromLevelList(@Param("level") Integer level);

    @Query("SELECT c FROM Category c WHERE c.parentCategory.id = :parentId")
    List<Category> findByParentId(@Param("parentId") String parentId);

    @Query("SELECT c FROM Category c WHERE c.categoryName = :name")
    Optional<Category> findByName(@Param("name") String name);

    Page<Category> findAllByRemovalFlagIsFalse(Pageable pageable);
}
