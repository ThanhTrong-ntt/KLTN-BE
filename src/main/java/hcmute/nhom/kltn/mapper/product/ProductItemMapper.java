package hcmute.nhom.kltn.mapper.product;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import hcmute.nhom.kltn.dto.product.ProductItemDTO;
import hcmute.nhom.kltn.mapper.AbstractMapper;
import hcmute.nhom.kltn.model.product.ProductItem;

/**
 * Class ProductItemMapper.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Mapper()
public interface ProductItemMapper extends AbstractMapper<ProductItemDTO, ProductItem> {
    ProductItemMapper INSTANCE = Mappers.getMapper(ProductItemMapper.class);
}
