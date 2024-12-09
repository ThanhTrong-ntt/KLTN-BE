package hcmute.nhom.kltn.mapper.product;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import hcmute.nhom.kltn.dto.product.ProductDTO;
import hcmute.nhom.kltn.mapper.AbstractMapper;
import hcmute.nhom.kltn.model.product.Product;

/**
 * Class ProductMapper.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Mapper()
public interface ProductMapper extends AbstractMapper<ProductDTO, Product> {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
}
