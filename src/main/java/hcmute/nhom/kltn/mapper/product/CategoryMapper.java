package hcmute.nhom.kltn.mapper.product;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import hcmute.nhom.kltn.dto.product.CategoryDTO;
import hcmute.nhom.kltn.mapper.AbstractMapper;
import hcmute.nhom.kltn.model.product.Category;

/**
 * Class CategoryMapper.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Mapper()
public interface CategoryMapper extends AbstractMapper<CategoryDTO, Category> {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
}
