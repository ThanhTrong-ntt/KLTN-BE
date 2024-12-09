package hcmute.nhom.kltn.service.impl.product;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hcmute.nhom.kltn.dto.ImageDTO;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.product.ProductDTO;
import hcmute.nhom.kltn.dto.product.ProductItemDTO;
import hcmute.nhom.kltn.exception.NotFoundException;
import hcmute.nhom.kltn.exception.SystemErrorException;
import hcmute.nhom.kltn.mapper.product.ProductMapper;
import hcmute.nhom.kltn.model.product.Product;
import hcmute.nhom.kltn.repository.product.ProductRepository;
import hcmute.nhom.kltn.service.ImageService;
import hcmute.nhom.kltn.service.impl.AbstractServiceImpl;
import hcmute.nhom.kltn.service.product.ProductItemService;
import hcmute.nhom.kltn.service.product.ProductService;
import hcmute.nhom.kltn.util.Constants;
import hcmute.nhom.kltn.util.Utilities;

/**
 * Class ProductServiceImpl.
 *
 * @author: ThanhTrong
 **/
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends AbstractServiceImpl<ProductRepository, ProductMapper, ProductDTO, Product>
        implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final String BL_NO = "ProductService";
    private final ProductRepository productRepository;
    private final ImageService mediaFileService;
    private final ProductItemService productItemService;
    private final ImageService imageService;

    @Override
    public ProductRepository getRepository() {
        return productRepository;
    }

    @Override
    public ProductMapper getMapper() {
        return ProductMapper.INSTANCE;
    }

    @Override
    @Transactional
    public ProductDTO save(ProductDTO dto) {
        if (dto == null) {
            throw new SystemErrorException("Save not success. DTO is null");
        }
        ProductDTO productDTO = findProductByName(dto.getProductName());
        ProductDTO productDTO1 = getProductBySlug(dto.getSlug());
        if (Objects.nonNull(productDTO) || Objects.nonNull(productDTO1)) {
            throw new SystemErrorException("Product name is existed");
        }
        if(Objects.nonNull(dto.getSales())) {
            dto.setPromoPrice(
                    dto.getBasePrice()
                            .subtract(
                                    dto.getBasePrice()
                                            .multiply(dto.getSales().getDiscount())
                                            .divide(Constants.HUNDRED, 2, RoundingMode.HALF_UP)
                            )
                            .divide(Constants.THOUSAND, 0, RoundingMode.HALF_UP)
                            .multiply(Constants.THOUSAND)
            );
        } else {
            dto.setPromoPrice(dto.getBasePrice());
        }
        dto.setRemovalFlag(false);
        Product item = getMapper().toEntity(dto, getCycleAvoidingMappingContext());
        entity = getRepository().save(item);
        ProductDTO savedDTO = getMapper().toDto(entity, getCycleAvoidingMappingContext());
        if (!dto.getSubImages().isEmpty()) {
            dto.getSubImages().forEach(subImage -> {
                subImage.setProduct(savedDTO);
                imageService.save(subImage);
            });
        }
        return savedDTO;
    }

    @Override
    public Page<ProductDTO> getPaging(int page, int size, String sortBy, String sortDir) {
        Pageable pageRequest = Utilities.getPageRequest(page, size, sortBy, sortDir);
        Page<Product> entities = getRepository().findAll(pageRequest);
        Page<ProductDTO> dtos = entities.map(item -> getMapper().toDto(item, getCycleAvoidingMappingContext()));
        entities.forEach(entity -> {
            List<ImageDTO> subImages = imageService.findByProductId(entity.getId());
            dtos.forEach(dto -> {
                if (dto.getId().equals(entity.getId())) {
                    dto.setSubImages(subImages);
                }
            });
        });
        return dtos;
    }

    @Override
    public PaginationDTO<ProductDTO> searchProduct(String keyword, int pageNo, int pageSize, String sortBy, String sortDir) {
        String methodName = "searchProduct";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.info(getMessageInputParam(BL_NO, "keyword", keyword));
        logger.info(getMessageInputParam(BL_NO, "pageNo", pageNo));
        logger.info(getMessageInputParam(BL_NO, "pageSize", pageSize));
        logger.info(getMessageInputParam(BL_NO, "sortBy", sortBy));
        logger.info(getMessageInputParam(BL_NO, "sortDir", sortDir));
        try {
            List<ProductDTO> productDTOS;
            if (keyword.isEmpty()) {
                productDTOS = findAll();
            } else {
                List<Product> products = productRepository.searchProduct(keyword);
                productDTOS = products.stream().map(product ->
                                getMapper().toDto(product, getCycleAvoidingMappingContext()))
                        .collect(Collectors.toList());
            }
            Pageable pageRequest = Utilities.getPageRequest(pageNo, pageSize, sortBy, sortDir);

            Page<ProductDTO> productDTOPage = Utilities.createPageFromList(productDTOS, pageRequest);
            logger.debug(getMessageOutputParam(BL_NO, "productDTOPage", productDTOPage));
            logger.info(getMessageEnd(BL_NO, methodName));
            return PaginationDTO.<ProductDTO>builder()
                    .items(productDTOS)
                    .currentPage(pageNo)
                    .pageSize(pageSize)
                    .totalItems(productDTOPage.getTotalElements())
                    .itemCount(productDTOPage.getNumberOfElements())
                    .totalPages(productDTOPage.getTotalPages())
                    .build();
        } catch (Exception e) {
            logger.error("Search product failed!", e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException("Error when forgot password");
        }
    }

    @Override
    public PaginationDTO<ProductDTO> searchProducts(String keyword, int pageNo, int pageSize, String sortBy, String sortDir) {
        String methodName = "searchProduct";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.info(getMessageInputParam(BL_NO, "keyword", keyword));
        logger.info(getMessageInputParam(BL_NO, "pageNo", pageNo));
        logger.info(getMessageInputParam(BL_NO, "pageSize", pageSize));
        logger.info(getMessageInputParam(BL_NO, "sortBy", sortBy));
        logger.info(getMessageInputParam(BL_NO, "sortDir", sortDir));
        try {
            List<ProductDTO> productDTOS;
            if (keyword.isEmpty()) {
                productDTOS = findAll();
            } else {
                String searchKeyword = "%" + keyword.toLowerCase() + "%";
                List<Product> products = productRepository.searchProduct(searchKeyword);
                productDTOS = products.stream().map(product ->
                                getMapper().toDto(product, getCycleAvoidingMappingContext()))
                        .collect(Collectors.toList());
            }
            Pageable pageRequest = Utilities.getPageRequest(pageNo, pageSize, sortBy, sortDir);

            Page<ProductDTO> productDTOPage = Utilities.createPageFromList(productDTOS, pageRequest);
            logger.debug(getMessageOutputParam(BL_NO, "productDTOPage", productDTOPage));
            logger.info(getMessageEnd(BL_NO, methodName));
            return PaginationDTO.<ProductDTO>builder()
                    .items(productDTOS)
                    .currentPage(pageNo)
                    .pageSize(pageSize)
                    .totalItems(productDTOPage.getTotalElements())
                    .itemCount(productDTOPage.getNumberOfElements())
                    .totalPages(productDTOPage.getTotalPages())
                    .build();
        } catch (Exception e) {
            logger.error("Search product failed!", e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        String methodName = "updateProduct";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.info(getMessageInputParam(BL_NO, "id", id));
        logger.info(getMessageInputParam(BL_NO, "productDTO", productDTO));
        try {
            ProductDTO product = findById(id);
            if (Objects.isNull(product)) {
                throw new NotFoundException("Product not found");
            }
            productDTO.setId(id);
            Product item = getMapper().toEntity(productDTO, getCycleAvoidingMappingContext());
            getRepository().save(item);
            ProductDTO updatedProduct = getMapper().toDto(entity, getCycleAvoidingMappingContext());
            logger.debug(getMessageOutputParam(BL_NO, "updatedProduct", updatedProduct));
            logger.info(getMessageEnd(BL_NO, methodName));
            return updatedProduct;
        } catch (Exception e) {
            logger.error("Update product failed!", e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException("Error when update product");
        }
    }

    @Override
    public void delete(String id) {
        String methodName = "deleteProduct";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.info(getMessageInputParam(BL_NO, "id", id));
        ProductDTO productDTO = findById(id);
        if (Objects.isNull(productDTO)) {
            throw new NotFoundException("Product not found");
        }
        delete(productDTO);
    }

    @Override
    public PaginationDTO<ProductDTO> searchProductByCategory(String genderName, String categoryName, int pageNo, int pageSize, String sortBy, String sortDir) {
        String methodName = "searchProductByCategory";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.info(getMessageInputParam(BL_NO, "categoryName", categoryName));
        logger.info(getMessageInputParam(BL_NO, "genderName", genderName));
        logger.info(getMessageInputParam(BL_NO, "pageNo", pageNo));
        logger.info(getMessageInputParam(BL_NO, "pageSize", pageSize));
        logger.info(getMessageInputParam(BL_NO, "sortBy", sortBy));
        logger.info(getMessageInputParam(BL_NO, "sortDir", sortDir));
        Page<Product> products = null;
        List<ProductDTO> productDTOS = new ArrayList<>();
        Pageable pageRequest = Utilities.getPageRequest(pageNo, pageSize, sortBy, sortDir);
        try {
           if (!categoryName.isEmpty()) {
                products = productRepository.searchProductByCategory(categoryName, pageRequest);
            } else if (!genderName.isEmpty()) {
                products = productRepository.searchProductByGender(genderName, pageRequest);
            } else if (categoryName.isEmpty() || genderName.isEmpty()) {
               products = productRepository.findAll( pageRequest);
            }
            productDTOS =
                    products.getContent().stream().map(category -> getMapper().toDto(category, getCycleAvoidingMappingContext()))
                            .collect(Collectors.toList());
            logger.info(getMessageEnd(BL_NO, methodName));
            return PaginationDTO.<ProductDTO>builder()
                    .items(productDTOS)
                    .currentPage(pageNo)
                    .pageSize(pageSize)
                    .totalItems(products.getTotalElements())
                    .itemCount(products.getNumberOfElements())
                    .totalPages(products.getTotalPages())
                    .build();
        } catch (Exception e) {
            logger.error("Search product by category failed!", e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException("Error when search product by category");
        }
    }

    @Override
    public PaginationDTO<ProductDTO> searchProductByPrice(Long minPrice, Long maxPrice, int pageNo, int pageSize, String sortBy, String sortDir) {
        String method = "searchProductByPrice";
        logger.info(getMessageStart(BL_NO, method));
        logger.debug(getMessageInputParam(BL_NO, "minPrice", minPrice));
        logger.debug(getMessageInputParam(BL_NO, "maxPrice", maxPrice));
        logger.debug(getMessageInputParam(BL_NO, "pageNo", pageNo));
        logger.debug(getMessageInputParam(BL_NO, "pageSize", pageSize));
        logger.debug(getMessageInputParam(BL_NO, "sortBy", sortBy));
        logger.debug(getMessageInputParam(BL_NO, "sortDir", sortDir));
        try {
            List<Product> products = productRepository.searchProductByPrice(minPrice, maxPrice);
            Pageable pageRequest = Utilities.getPageRequest(pageNo, pageSize, sortBy, sortDir);
            List<ProductDTO> productDTOS = products.stream().map(product ->
                            getMapper().toDto(product, getCycleAvoidingMappingContext()))
                    .collect(Collectors.toList());
            Page<ProductDTO> productDTOPage = Utilities.createPageFromList(productDTOS, pageRequest);
            logger.debug(getMessageOutputParam(BL_NO, "productDTOPage", productDTOPage));
            logger.info(getMessageEnd(BL_NO, method));
            return PaginationDTO.<ProductDTO>builder()
                    .items(productDTOS)
                    .currentPage(pageNo)
                    .pageSize(pageSize)
                    .totalItems(productDTOPage.getTotalElements())
                    .itemCount(productDTOPage.getNumberOfElements())
                    .totalPages(productDTOPage.getTotalPages())
                    .build();
        } catch (Exception e) {
            logger.error("Search Product by price failed!", e);
            logger.info(getMessageEnd(BL_NO, method));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    public PaginationDTO<ProductDTO> getList(int pageNo, int pageSize, String sortBy, String sortDir) {
        String methodName = "getList";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.info(getMessageInputParam(BL_NO, "pageNo", pageNo));
        logger.info(getMessageInputParam(BL_NO, "pageSize", pageSize));
        logger.info(getMessageInputParam(BL_NO, "sortBy", sortBy));
        logger.info(getMessageInputParam(BL_NO, "sortDir", sortDir));
        try {
            Pageable pageRequest = Utilities.getPageRequest(pageNo, pageSize, sortBy, sortDir);
            List<Product> products = productRepository.findAll();
            List<ProductDTO> productDTOS = products.stream().map(product ->
                            getMapper().toDto(product, getCycleAvoidingMappingContext()))
                    .collect(Collectors.toList());
            // Nhóm các sản phẩm theo tên và tạo danh sách ProductDTO
            Map<String, List<ProductDTO>> groupedProducts = productDTOS.stream()
                    .collect(Collectors.groupingBy(ProductDTO::getProductName));

            List<ProductDTO> finalProductDTOs = new ArrayList<>();

            Page<ProductDTO> productDTOPage = Utilities.createPageFromList(productDTOS, pageRequest);
            logger.debug(getMessageOutputParam(BL_NO, "productDTOPage", productDTOPage));
            logger.info(getMessageEnd(BL_NO, methodName));
            return PaginationDTO.<ProductDTO>builder()
                    .items(finalProductDTOs)
                    .currentPage(pageNo)
                    .pageSize(pageSize)
                    .totalItems(productDTOPage.getTotalElements())
                    .itemCount(productDTOPage.getNumberOfElements())
                    .totalPages(productDTOPage.getTotalPages())
                    .build();
        } catch (Exception e) {
            logger.error("Get list product failed!", e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException("Error when get list product");
        }
    }

    @Override
    public void delete(ProductDTO dto) {
        String methodName = "deleteProduct";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.info(getMessageInputParam(BL_NO, "dto", dto));
        try {
            List<ProductItemDTO> productItemDTOS = productItemService.findByProductId(dto.getId());
            if(!productItemDTOS.isEmpty()) {
                productItemDTOS.forEach(productItemService::delete);
            }
            List<ImageDTO> subImages = imageService.findByProductId(dto.getId());
            if(!subImages.isEmpty()) {
                subImages.forEach(imageService::delete);
            }
            getRepository().delete(getMapper().toEntity(dto, getCycleAvoidingMappingContext()));
            logger.info(getMessageEnd(BL_NO, methodName));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException("Delete not success. Error: " + e.getMessage());
        }
    }

    @Override
    public ProductItemDTO saveProductItem(String productId, ProductItemDTO productItemDTO) {
        logger.info(getMessageStart(BL_NO, "saveProductItem"));
        logger.info(getMessageInputParam(BL_NO, "productItemDTO", productItemDTO));
        ProductDTO productDTO = findById(productId);
        if (Objects.isNull(productDTO)) {
            throw new NotFoundException("Sản phẩm cha không tồn tại!");
        }
        try {
            productItemDTO.setProduct(productDTO);
            ProductItemDTO productItem = productItemService.save(productItemDTO);
            logger.debug(getMessageOutputParam(BL_NO, "productItem", productItem));
            logger.info(getMessageEnd(BL_NO, "saveProductItem"));
            return productItem;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd(BL_NO, "saveProductItem"));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    public ProductDTO getProductBySlug(String slug) {
        String methodName = "getProductBySlug";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "slug", slug));
        try {
            Product product = productRepository.findBySlug(slug);
            if (Objects.isNull(product)) {
                logger.debug(getMessageOutputParam(BL_NO, "product", null));
                logger.info(getMessageEnd(BL_NO, methodName));
               return null;
            }
            ProductDTO productDTO = getMapper().toDto(product, getCycleAvoidingMappingContext());
            List<ImageDTO> subImages = imageService.findByProductId(product.getId());
            productDTO.setSubImages(subImages);
            logger.debug(getMessageOutputParam(BL_NO, "product", productDTO));
            logger.info(getMessageEnd(BL_NO, methodName));
            return productDTO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    public ProductDTO findProductByName(String productName) {
        String methodName = "findProductByName";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "productName", productName));
        try {
            Product product = productRepository.findByProductName(productName);
            if (Objects.isNull(product)) {
                logger.debug(getMessageOutputParam(BL_NO, "product", null));
                logger.info(getMessageEnd(BL_NO, methodName));
                return null;
            }
            ProductDTO productDTO = getMapper().toDto(product, getCycleAvoidingMappingContext());
            logger.debug(getMessageOutputParam(BL_NO, "product", productDTO));
            logger.info(getMessageEnd(BL_NO, methodName));
            return productDTO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    public PaginationDTO<ProductItemDTO> getAllProductItem(String productId, int pageNo, int pageSize, String sortBy, String sortDir) {
        String methodName = "getAllProductItem";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "productId", productId));
        logger.debug(getMessageInputParam(BL_NO, "pageNo", pageNo));
        logger.debug(getMessageInputParam(BL_NO, "pageSize", pageSize));
        logger.debug(getMessageInputParam(BL_NO, "sortBy", sortBy));
        logger.debug(getMessageInputParam(BL_NO, "sortDir", sortDir));
        try {
            List<ProductItemDTO> productItemDTOS = productItemService.findByProductId(productId);
            Pageable pageRequest = Utilities.getPageRequest(pageNo, pageSize, sortBy, sortDir);
            Page<ProductItemDTO> productItemDTOPage = Utilities.createPageFromList(productItemDTOS, pageRequest);
            logger.debug(getMessageOutputParam(BL_NO, "productItemDTOPage", productItemDTOPage));
            logger.info(getMessageEnd(BL_NO, methodName));
            return PaginationDTO.<ProductItemDTO>builder()
                    .items(productItemDTOS)
                    .currentPage(pageNo)
                    .pageSize(pageSize)
                    .totalItems(productItemDTOPage.getTotalElements())
                    .itemCount(productItemDTOPage.getNumberOfElements())
                    .totalPages(productItemDTOPage.getTotalPages())
                    .build();
        } catch (Exception e) {
            logger.error("Get all product item failed!", e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    public List<ProductItemDTO> getAllProductItemList(String productId) {
        String methodName = "getAllProductItemList";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "productId", productId));
        try {
            List<ProductItemDTO> productItemDTOS = productItemService.findByProductId(productId);
            logger.debug(getMessageOutputParam(BL_NO, "productItemDTOS", productItemDTOS));
            logger.info(getMessageEnd(BL_NO, methodName));
            return productItemDTOS;
        } catch (Exception e) {
            logger.error("Get all product item list failed!", e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    public void deleteProductByCategoryId(String categoryId) {
        String methodName = "deleteProductByCategoryId";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "categoryId", categoryId));
        try {
            List<Product> products = productRepository.findByCategoryId(categoryId);
            if (!products.isEmpty()) {
                products.forEach(product -> {
                    List<ProductItemDTO> productItemDTOS = productItemService.findByProductId(product.getId());
                    if (!productItemDTOS.isEmpty()) {
                        productItemDTOS.forEach(productItemService::delete);
                    }
                    List<ImageDTO> subImages = imageService.findByProductId(product.getId());
                    if (!subImages.isEmpty()) {
                        subImages.forEach(imageService::delete);
                    }
                    getRepository().delete(product);
                });
            }
            logger.info(getMessageEnd(BL_NO, methodName));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException("Delete not success. Error: " + e.getMessage());
        }
    }

    @Override
    public List<ProductDTO> getProductBySaleId(String saleId) {
        String methodName = "getProductBySaleId";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "saleId", saleId));
        try {
            List<Product> products = productRepository.findBySaleId(saleId);
            List<ProductDTO> productDTOS = products.stream().map(product ->
                            getMapper().toDto(product, getCycleAvoidingMappingContext()))
                    .collect(Collectors.toList());
            logger.debug(getMessageOutputParam(BL_NO, "productDTOS", productDTOS));
            logger.info(getMessageEnd(BL_NO, methodName));
            return productDTOS;
        } catch (Exception e) {
            logger.error("Get product by sale id failed!", e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException(e.getMessage());
        }
    }
}
