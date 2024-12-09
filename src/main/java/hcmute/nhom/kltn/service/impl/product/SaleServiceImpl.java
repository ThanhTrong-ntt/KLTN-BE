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
import hcmute.nhom.kltn.dto.ImageDTO;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.product.ProductDTO;
import hcmute.nhom.kltn.dto.product.ProductItemDTO;
import hcmute.nhom.kltn.dto.product.SalesDTO;
import hcmute.nhom.kltn.exception.NotFoundException;
import hcmute.nhom.kltn.exception.SystemErrorException;
import hcmute.nhom.kltn.mapper.product.SalesMapper;
import hcmute.nhom.kltn.model.product.Sales;
import hcmute.nhom.kltn.repository.product.SaleRepository;
import hcmute.nhom.kltn.service.impl.AbstractServiceImpl;
import hcmute.nhom.kltn.service.product.ProductService;
import hcmute.nhom.kltn.service.product.SaleService;

/**
 * Class SaleServiceImpl.
 *
 * @author: ThanhTrong
 **/
@Service
@RequiredArgsConstructor
public class SaleServiceImpl
        extends AbstractServiceImpl<SaleRepository, SalesMapper, SalesDTO, Sales>
        implements SaleService {
    private static final Logger logger = LoggerFactory.getLogger(SaleServiceImpl.class);
    private final String BL_NO = "SaleService";
    private final SaleRepository saleRepository;
    private final ProductService productService;

    @Override
    public SaleRepository getRepository() {
        this.repository = saleRepository;
        return this.repository;
    }

    @Override
    public SalesMapper getMapper() {
        return SalesMapper.INSTANCE;
    }

    @Override
    public void delete(String id) {
        logger.info(getMessageStart("AbstractService", "Delete DTO"));
        logger.debug(getMessageInputParam("AbstractService", "dto - id", id));
        if (Objects.isNull(findById(id))) {
            throw new NotFoundException("DTO not found. Id: " + id);
        }
        try {
            List<ProductDTO> productDTOList = productService.getProductBySaleId(id);
            if (!productDTOList.isEmpty()) {
                productDTOList.forEach(product -> {
                    product.setSales(null);
                    productService.updateProduct(product.getId(), product);
                });
            }
            getRepository().deleteById(id);
            logger.info(getMessageEnd("AbstractService", "Delete DTO"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd("AbstractService", "Delete DTO"));
            throw new SystemErrorException("Delete not success. Error: " + e.getMessage());
        }
    }

    @Override
    public PaginationDTO<SalesDTO> getAllSalePagination(
            int pageNo, int pageSize, String sortBy, String sortDir) {
    String methodName = "getAllSalePagination";
    logger.info(getMessageStart(BL_NO, methodName));
    logger.debug(getMessageInputParam(BL_NO, "pageNo", pageNo));
    logger.debug(getMessageInputParam(BL_NO, "pageSize", pageSize));
    logger.debug(getMessageInputParam(BL_NO, "sortBy", sortBy));
    logger.debug(getMessageInputParam(BL_NO, "sortDir", sortDir));
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        List<SalesDTO> salesDTOList;
        try {
            Page<Sales> sales = saleRepository.findAll(pageable);
            salesDTOList =
                    sales.getContent().stream().map(sale -> getMapper().toDto(sale, getCycleAvoidingMappingContext()))
                            .collect(Collectors.toList());
            logger.debug(getMessageOutputParam(BL_NO, "salesDTOList", salesDTOList));
            logger.info(getMessageEnd(BL_NO, methodName));
            return PaginationDTO.<SalesDTO>builder()
                    .items(salesDTOList)
                    .currentPage(pageNo)
                    .pageSize(pageSize)
                    .totalItems(sales.getTotalElements())
                    .itemCount(sales.getNumberOfElements())
                    .totalPages(sales.getTotalPages())
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException("Get all sales failed");
        }
    }

    @Override
    public SalesDTO updateSale(String id, SalesDTO salesDTO) {
        String methodName = "updateSale";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "id", id));
        logger.debug(getMessageInputParam(BL_NO, "salesDTO", salesDTO));
        try {
            SalesDTO sales = findById(id);
            if (Objects.isNull(sales)) {
                throw new SystemErrorException("Not found sale with id: " + id);
            }
            sales = save(salesDTO);
            logger.debug(getMessageOutputParam(BL_NO, "sales", sales));
            logger.info(getMessageEnd(BL_NO, methodName));
            return sales;
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException("Update sale failed");
        }
    }
}
