package hcmute.nhom.kltn.service.product;

import java.util.List;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.product.CategoryDTO;
import hcmute.nhom.kltn.model.product.Category;
import hcmute.nhom.kltn.service.AbstractService;

/**
 * Class CategoryService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface CategoryService extends AbstractService<CategoryDTO, Category> {

    CategoryDTO updateCategory(String id, CategoryDTO categoryDTO);

    List<CategoryDTO> getCategoriesByLevelList(Integer level);

    List<CategoryDTO> getAllCategory();

    PaginationDTO<CategoryDTO> getAllFromLevel(Integer level,int pageNo,int pageSize,String sortBy,String sortDir);

    PaginationDTO<CategoryDTO> getAllCategoryPagination(Integer page, Integer size, String sortBy, String sortDir);

    List<CategoryDTO> getChildCategories(String parentId);
}
