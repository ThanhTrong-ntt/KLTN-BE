package hcmute.nhom.kltn.repository.product;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import hcmute.nhom.kltn.model.product.Product;
import hcmute.nhom.kltn.repository.AbstractRepository;

/**
 * Class ProductRepository.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface ProductRepository extends AbstractRepository<Product, String> {

    @Query(value = "SELECT p FROM Product p "
            + "WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))"
            + " AND LOWER(p.freeInformation) LIKE LOWER(CONCAT('%', :keyword, '%'))"
            + " AND LOWER(p.longDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))"
            + " AND LOWER(p.washingInformation) LIKE LOWER(CONCAT('%', :keyword, '%'))"
            + " AND p.removalFlag = false")
    List<Product> searchProduct(@Param("keyword") String keyword);

    @Query(value = "SELECT p FROM Product p JOIN Category c ON c.id = p.category.id "
            + "WHERE p.removalFlag = false AND c.categoryName = :categoryName AND LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProductByCategoryAndKeyword(@Param("categoryName") String categoryName, @Param("keyword") String keyword);

    @Query(value = "SELECT p FROM Product p "
            + "WHERE p.removalFlag = false AND p.category.categoryName like :categoryName")
    Page<Product> searchProductByCategory(@Param("categoryName") String categoryName, Pageable pageable);

    @Query(value = "SELECT p FROM Product p "
            + "WHERE LOWER(p.gender.categoryName) LIKE :genderName")
    Page<Product> searchProductByGender(@Param("genderName") String genderName, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.basePrice >= :minPrice AND p.basePrice <= :maxPrice AND p.removalFlag = false ")
    List<Product> searchProductByPrice(@Param("minPrice") Long minPrice, @Param("maxPrice") Long maxPrice);

    @Query(value = "SELECT p FROM Product p WHERE p.removalFlag = false "
            + " AND LOWER(p.gender.categoryName) LIKE LOWER(CONCAT('%', :gender, '%'))"
            + " AND LOWER(p.category.categoryName) LIKE LOWER(CONCAT('%', :category, '%'))")
    List<Product> searchByCategory(@Param("category") String category, @Param("gender") String gender);

    Product findBySlug(String slug);

    Product findByProductName(String productName);

    @Query(value = "SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") String categoryId);

    @Query(value = "SELECT p FROM Product p WHERE p.sales.id = :saleId")
    List<Product> findBySaleId(String saleId);
}
