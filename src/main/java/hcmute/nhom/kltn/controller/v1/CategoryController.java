package hcmute.nhom.kltn.controller.v1;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import hcmute.nhom.kltn.common.payload.ApiResponse;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.product.CategoryDTO;
import hcmute.nhom.kltn.service.product.CategoryService;
import hcmute.nhom.kltn.util.Constants;

/**
 * Class CategoryController.
 *
 * @author: ThanhTrong
 * @function_id: 
 * @version:
**/
@RestController
public class CategoryController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<PaginationDTO<CategoryDTO>>> getAllCategories(
            HttpServletRequest request,
            @RequestParam(value = "pageNo", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false)
            int pageNo,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false)
            int pageSize,
            @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false)
            String sortBy,
            @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false)
            String sortDir
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getAllCategories"));
        PaginationDTO<CategoryDTO> categoryDTOS = categoryService.getAllCategoryPagination(pageNo, pageSize, sortBy, sortDir);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getAllCategories"));
        return ResponseEntity.ok(
                ApiResponse.<PaginationDTO<CategoryDTO>>builder()
                        .result(true)
                        .data(categoryDTOS)
                        .message("Get all categories successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @GetMapping("/categories/list")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories(
            HttpServletRequest request
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getAllCategories"));
        List<CategoryDTO> categoryDTOS = categoryService.getAllCategory();
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getAllCategories"));
        return ResponseEntity.ok(
                ApiResponse.<List<CategoryDTO>>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(categoryDTOS)
                        .message("Get all categories successfully!")
                        .build());
    }

    @GetMapping("/categories/list/{level}")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories(
            HttpServletRequest request,
            @PathVariable("level") Integer level
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getAllCategories"));
        List<CategoryDTO> categoryDTOS = categoryService.getCategoriesByLevelList(level);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getAllCategories"));
        return ResponseEntity.ok(
                ApiResponse.<List<CategoryDTO>>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(categoryDTOS)
                        .message("Get all categories successfully!")
                        .build());
    }

    @GetMapping("/categories/level/{level}")
    public ResponseEntity<ApiResponse<PaginationDTO<CategoryDTO>>> getAllFromLevel(
            HttpServletRequest request,
            @PathVariable("level") Integer level,
            @RequestParam(value = "pageNo", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false)
            int pageNo,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false)
            int pageSize,
            @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false)
            String sortBy,
            @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false)
            String sortDir
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getAllFromLevel"));
        PaginationDTO<CategoryDTO> categoryDTOS = categoryService.getAllFromLevel(level, pageNo, pageSize, sortBy, sortDir);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getAllFromLevel"));
        return ResponseEntity.ok(
                ApiResponse.<PaginationDTO<CategoryDTO>>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(categoryDTOS)
                        .message("Get all from level successfully!")
                        .build());
    }

    @GetMapping("/categories/child-categories/{id}")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getChildCategories(
            HttpServletRequest request,
            @PathVariable("id") String id
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getChildCategories"));
        List<CategoryDTO> categoryDTOS = categoryService.getChildCategories(id);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getChildCategories"));
        return ResponseEntity.ok(
                ApiResponse.<List<CategoryDTO>>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(categoryDTOS)
                        .message("Get child categories successfully!")
                        .build());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> searchCategory(
            HttpServletRequest request,
            @PathVariable("id") String id
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "searchCategory"));
        CategoryDTO categoryDTO = categoryService.findById(id);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "searchCategory"));
        return ResponseEntity.ok(
                ApiResponse.<CategoryDTO>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(categoryDTO)
                        .message("Search category successfully!")
                        .build());
    }

    @PostMapping("/category")
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(
            HttpServletRequest request,
            @RequestBody CategoryDTO categoryDTO
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "createCategory"));
        CategoryDTO category = categoryService.save(categoryDTO);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "createCategory"));
        return ResponseEntity.ok(
                ApiResponse.<CategoryDTO>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(category)
                        .message("Create category successfully!")
                        .build());
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(
            HttpServletRequest request,
            @PathVariable("id") String id,
            @RequestBody CategoryDTO categoryDTO
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "updateCategory"));
        CategoryDTO category = categoryService.updateCategory(id, categoryDTO);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "updateCategory"));
        return ResponseEntity.ok(
                ApiResponse.<CategoryDTO>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(category)
                        .message("Update category successfully!")
                        .build());
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteCategory(
            HttpServletRequest request,
            @PathVariable("id") String id
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "deleteCategory"));
        categoryService.delete(id);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "deleteCategory"));
        return ResponseEntity.ok(
                ApiResponse.<Boolean>builder()
                        .result(true)
                        .code(String.valueOf(HttpStatus.OK.value()))
                        .message("Delete category successfully!")
                        .build());
    }

}
