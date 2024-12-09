package hcmute.nhom.kltn.service.product;

import java.util.List;
import hcmute.nhom.kltn.dto.product.ProductItemDTO;
import hcmute.nhom.kltn.model.product.ProductItem;
import hcmute.nhom.kltn.service.AbstractService;

/**
 * Class ProductItemService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface ProductItemService extends AbstractService<ProductItemDTO, ProductItem> {
    List<ProductItemDTO> findByProductId(String productId);

    ProductItemDTO updateProductItem(String id, ProductItemDTO productItemDTO);
}
