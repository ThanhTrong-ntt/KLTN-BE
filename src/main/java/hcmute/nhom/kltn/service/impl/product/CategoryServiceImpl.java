package hcmute.nhom.kltn.service.impl.product;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.product.CategoryDTO;
import hcmute.nhom.kltn.exception.NotFoundException;
import hcmute.nhom.kltn.exception.SystemErrorException;
import hcmute.nhom.kltn.mapper.product.CategoryMapper;
import hcmute.nhom.kltn.model.product.Category;
import hcmute.nhom.kltn.repository.product.CategoryRepository;
import hcmute.nhom.kltn.service.impl.AbstractServiceImpl;
import hcmute.nhom.kltn.service.product.CategoryService;
import hcmute.nhom.kltn.service.product.ProductService;

/**
 * Class CategoryServiceImpl.
 *
 * @author: ThanhTrong
 **/
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends AbstractServiceImpl<CategoryRepository, CategoryMapper, CategoryDTO, Category>
        implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final String BL_NO = "CategoryService";
    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    @Override
    public CategoryRepository getRepository() {
        return categoryRepository;
    }

    @Override
    public CategoryMapper getMapper() {
        return CategoryMapper.INSTANCE;
    }

    @Override
    public CategoryDTO save(CategoryDTO dto) {
        String methodName = "saveCategory";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.info(getMessageInputParam(BL_NO, "dto", dto));
        if (dto == null) {
            logger.error("Save not success. DTO is null");
            throw new SystemErrorException("Save not success. DTO is null");
        }
        Category category = getRepository().findByName(dto.getCategoryName()).orElse(null);
        if (Objects.isNull(category)) {
            if(!Objects.isNull(dto.getParentCategory())) {
                Category parentCategory = categoryRepository.findByName(dto.getParentCategory().getCategoryName())
                        .orElseThrow(() -> new NotFoundException("Parent category not found"));
                dto.setParentCategory(getMapper().toDto(parentCategory, getCycleAvoidingMappingContext()));
            }
            // E item = getMapper().toEntity(dto, getCycleAvoidingMappingContext());
            Category item = getMapper().toEntity(dto, getCycleAvoidingMappingContext());
            entity = getRepository().save(item);
            CategoryDTO result = getMapper().toDto(entity, getCycleAvoidingMappingContext());
            logger.debug(getMessageOutputParam(BL_NO, "result", result));
            logger.info(getMessageEnd(BL_NO, methodName));
            return result;
        } else {
            logger.error("Category with name: " + dto.getCategoryName() + " is exist");
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException("Category with name: " + dto.getCategoryName() + " is exist");
        }

    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(String id, CategoryDTO categoryDTO) {
        String methodName = "updateCategory";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.info(getMessageInputParam(BL_NO, "id", id));
        logger.info(getMessageInputParam(BL_NO, "categoryDTO", categoryDTO));
        try {
            CategoryDTO category = findById(id);
            if (category == null) {
                throw new SystemErrorException("Không tìm thấy danh mục!");
            }
            category.setCategoryName(categoryDTO.getCategoryName());
            category.setLocale(categoryDTO.getLocale());
            Category categoryUpdate = getRepository().save(getMapper().toEntity(category, getCycleAvoidingMappingContext()));
            category = getMapper().toDto(categoryUpdate, getCycleAvoidingMappingContext());
            logger.debug(getMessageOutputParam(BL_NO, "category", category));
            logger.info(getMessageEnd(BL_NO, methodName));
            return category;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new SystemErrorException("Cập nhật danh mục lỗi!");
        }
    }

    @Override
    public List<CategoryDTO> getCategoriesByLevelList(Integer level) {
        String method = "getCategoriesByLevelList";
        logger.info(getMessageStart(BL_NO, method));
        logger.info(getMessageInputParam(BL_NO, "level", level));
        try {
            List<Category> categories = categoryRepository.getAllFromLevelList(level);
            List<CategoryDTO> categoryDTOS =
                    categories.stream().map(category -> getMapper().toDto(category, getCycleAvoidingMappingContext()))
                    .collect(Collectors.toList());
            logger.debug(getMessageOutputParam(BL_NO, "categoryDTOS", categoryDTOS));
            logger.info(getMessageEnd(BL_NO, method));
            return categoryDTOS;
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(BL_NO, method));
            throw new SystemErrorException("Get categories by level list failed");
        }
    }

    @Override
    public List<CategoryDTO> getAllCategory() {
        String method = "getAllCategory";
        logger.info(getMessageStart(BL_NO, method));
        List<CategoryDTO> categoryDTOs;
        try {
            List<Category> categories = categoryRepository.findAll();
            categoryDTOs =
                    categories.stream().map(category -> getMapper().toDto(category, getCycleAvoidingMappingContext()))
                    .collect(Collectors.toList());
            logger.debug(getMessageOutputParam(BL_NO, "categoryDTOs", categoryDTOs));
            logger.info(getMessageEnd(BL_NO, method));
            return categoryDTOs;
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(BL_NO, method));
            throw new SystemErrorException("Get all category failed");
        }
    }

    @Override
    public PaginationDTO<CategoryDTO> getAllFromLevel(Integer level,int pageNo,int pageSize,String sortBy,String sortDir) {
        String method = "getAllFromLevel";
        logger.info(getMessageStart(BL_NO, method));
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        List<CategoryDTO> categoryDTOs;
        try {
            Page<Category> categoriePage = categoryRepository.getAllFromLevel(level, pageable);
            categoryDTOs =
                    categoriePage.getContent().stream().map(category -> getMapper().toDto(category, getCycleAvoidingMappingContext()))
                    .collect(Collectors.toList());
            logger.debug(getMessageOutputParam(BL_NO, "categoryDTOs", categoryDTOs));
            logger.info(getMessageEnd(BL_NO, method));
            return PaginationDTO.<CategoryDTO>builder()
                    .items(categoryDTOs)
                    .currentPage(pageNo)
                    .pageSize(pageSize)
                    .totalItems(categoriePage.getTotalElements())
                    .itemCount(categoriePage.getNumberOfElements())
                    .totalPages(categoriePage.getTotalPages())
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(BL_NO, method));
            throw new SystemErrorException("Get all category from level failed");
        }
    }

    @Override
    public PaginationDTO<CategoryDTO>getAllCategoryPagination(
            Integer page, Integer size, String sortBy, String sortDir) {
        String method = "getAllCategoryPagination";
        logger.info(getMessageStart(BL_NO, method));
        logger.info(getMessageInputParam(BL_NO, "page", page));
        logger.info(getMessageInputParam(BL_NO, "size", size));
        logger.info(getMessageInputParam(BL_NO, "sortBy", sortBy));
        logger.info(getMessageInputParam(BL_NO, "sortDir", sortDir));
        Page<Category> categoryPage;
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        try {
            categoryPage = categoryRepository.findAll(pageable);
            List<CategoryDTO> categoryDTOS =
                    getMapper().toDtoList(categoryPage.getContent(), getCycleAvoidingMappingContext());
            logger.debug(getMessageOutputParam(BL_NO, "categoryDTOS", categoryDTOS));
            logger.info(getMessageEnd(BL_NO, method));
            return PaginationDTO.<CategoryDTO>builder()
                    .items(categoryDTOS)
                    .currentPage(page)
                    .pageSize(size)
                    .totalItems(categoryPage.getTotalElements())
                    .itemCount(categoryPage.getNumberOfElements())
                    .totalPages(categoryPage.getTotalPages())
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(BL_NO, method));
            throw new SystemErrorException("Get all category pagination failed");
        }
    }

    @Override
    public List<CategoryDTO> getChildCategories(String parentId) {
        String method = "getChildCategories";
        logger.info(getMessageStart(BL_NO, method));
        logger.info(getMessageInputParam(BL_NO, "parentId", parentId));
        List<CategoryDTO> categoryDTOs;
        try {
            List<Category> categories = categoryRepository.findByParentId(parentId);
            categoryDTOs =
                    categories.stream().map(category -> getMapper().toDto(category, getCycleAvoidingMappingContext()))
                    .collect(Collectors.toList());
            logger.debug(getMessageOutputParam(BL_NO, "categoryDTOs", categoryDTOs));
            logger.info(getMessageEnd(BL_NO, method));
            return categoryDTOs;
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(BL_NO, method));
            throw new SystemErrorException("Get child categories failed");
        }
    }

    @Override
    @Transactional
    public void delete(String id) {
        logger.info(getMessageStart("AbstractService", "Delete DTO"));
        logger.debug(getMessageInputParam("AbstractService", "dto - id", id));
        if (Objects.isNull(findById(id))) {
            throw new NotFoundException("DTO not found. Id: " + id);
        }
        try {
            productService.deleteProductByCategoryId(id);
            getRepository().deleteById(id);
            logger.info(getMessageEnd("AbstractService", "Delete DTO"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd("AbstractService", "Delete DTO"));
            throw new SystemErrorException("Delete not success. Error: " + e.getMessage());
        }
    }
}
