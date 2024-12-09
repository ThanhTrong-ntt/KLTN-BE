package hcmute.nhom.kltn.service.product;

import java.util.List;
import org.springframework.data.domain.Page;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.product.ProductDTO;
import hcmute.nhom.kltn.dto.product.ProductItemDTO;
import hcmute.nhom.kltn.model.product.Product;
import hcmute.nhom.kltn.service.AbstractService;

/**
 * Class ProductService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface ProductService extends AbstractService<ProductDTO, Product> {
    PaginationDTO<ProductDTO> searchProduct(String keyword, int pageNo, int pageSize, String sortBy, String sortDir);

    PaginationDTO<ProductDTO> searchProducts(String keyword, int pageNo, int pageSize, String sortBy, String sortDir);

    ProductDTO updateProduct(String id, ProductDTO productDTO);

    PaginationDTO<ProductDTO> searchProductByCategory(String genderName, String categoryName, int pageNo, int pageSize, String sortBy, String sortDir);

    PaginationDTO<ProductDTO> searchProductByPrice(Long minPrice, Long maxPrice, int pageNo, int pageSize, String sortBy, String sortDir);

    PaginationDTO<ProductDTO> getList(int pageNo, int pageSize, String sortBy, String sortDir);

    ProductItemDTO saveProductItem(String productId, ProductItemDTO productItemDTO);

    ProductDTO getProductBySlug(String slug);

    ProductDTO findProductByName(String productName);

    PaginationDTO<ProductItemDTO> getAllProductItem(String productId, int pageNo, int pageSize, String sortBy, String sortDir);

    List<ProductItemDTO> getAllProductItemList(String productId);

    void deleteProductByCategoryId(String categoryId);

    List<ProductDTO> getProductBySaleId(String saleId);

}
