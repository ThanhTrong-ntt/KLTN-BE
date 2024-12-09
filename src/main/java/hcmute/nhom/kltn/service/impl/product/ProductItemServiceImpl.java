package hcmute.nhom.kltn.service.impl.product;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import hcmute.nhom.kltn.dto.product.ProductItemDTO;
import hcmute.nhom.kltn.exception.NotFoundException;
import hcmute.nhom.kltn.exception.SystemErrorException;
import hcmute.nhom.kltn.mapper.product.ProductItemMapper;
import hcmute.nhom.kltn.model.product.ProductItem;
import hcmute.nhom.kltn.repository.product.ProductItemRepository;
import hcmute.nhom.kltn.service.impl.AbstractServiceImpl;
import hcmute.nhom.kltn.service.product.ProductItemService;

/**
 * Class ProductItemServiceImpl.
 *
 * @author: ThanhTrong
 **/
@Service
@RequiredArgsConstructor
public class ProductItemServiceImpl
        extends AbstractServiceImpl<ProductItemRepository, ProductItemMapper, ProductItemDTO, ProductItem>
        implements ProductItemService {
    private static final Logger logger = LoggerFactory.getLogger(ProductItemServiceImpl.class);
    private final ProductItemRepository productItemRepository;

    private static final String BL_NO = "ProductItemService";

    @Override
    public ProductItemRepository getRepository() {
        return productItemRepository;
    }

    @Override
    public ProductItemMapper getMapper() {
        return ProductItemMapper.INSTANCE;
    }

    @Override
    public List<ProductItemDTO> findByProductId(String productId) {
        String methodName = "findByProductId";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "productId", productId));
        List<ProductItemDTO> productItemDTOS;
        try {
            List<ProductItem> productItems = productItemRepository.findByProductId(productId);
            productItemDTOS = productItems.stream().map(
                    productItem -> getMapper().toDto(productItem, getCycleAvoidingMappingContext())
            ).collect(Collectors.toList());
            logger.debug(getMessageOutputParam(BL_NO, methodName, productItemDTOS));
            logger.info(getMessageEnd(BL_NO, methodName));
            return productItemDTOS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    public ProductItemDTO updateProductItem(String id, ProductItemDTO productItemDTO) {
        String methodName = "updateProductItem";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "id", id));
        logger.debug(getMessageInputParam(BL_NO, "productItemDTO", productItemDTO));
        try {
            ProductItemDTO productItem = findById(id);
            if (Objects.isNull(productItem)) {
                throw new NotFoundException("Không tim thấy sản phẩm con");
            }
            ProductItem item = getMapper().toEntity(productItemDTO, getCycleAvoidingMappingContext());
            entity = getRepository().save(item);
            ProductItemDTO updatedProductItem = getMapper().toDto(entity, getCycleAvoidingMappingContext());
            logger.debug(getMessageOutputParam(BL_NO, "updatedProductItem", updatedProductItem));
            logger.info(getMessageEnd(BL_NO, methodName));
            return updatedProductItem;
        } catch (Exception e) {
            logger.error("Update product item failed!", e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    public ProductItemDTO save(ProductItemDTO dto) {
        String methodName = "save";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "dto", dto));
        if (Objects.isNull(dto)) {
            throw new SystemErrorException("Save not success. DTO is null");
        } else if (Objects.nonNull(getRepository().findByColorAndSize(dto.getColor().getName(), dto.getSize()))) {
            throw new SystemErrorException("Save not success. Color and size is exist");
        }
        ProductItem item = getMapper().toEntity(dto, getCycleAvoidingMappingContext());
        entity = getRepository().save(item);
        return getMapper().toDto(entity, getCycleAvoidingMappingContext());
    }
}
