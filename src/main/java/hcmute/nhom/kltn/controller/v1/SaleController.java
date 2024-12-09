package hcmute.nhom.kltn.controller.v1;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import hcmute.nhom.kltn.dto.product.SalesDTO;
import hcmute.nhom.kltn.service.product.SaleService;
import hcmute.nhom.kltn.util.Constants;

/**
 * Class SaleController.
 *
 * @author: ThanhTrong
 **/
@RestController
@Validated
public class SaleController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(SaleController.class);
    private final SaleService saleService;

    public SaleController(final SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping("sales")
    public ResponseEntity<ApiResponse<PaginationDTO<SalesDTO>>> getAllSales(
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
        logger.info(getMessageStart(request.getRequestURL().toString(), "getAllSales"));
        PaginationDTO<SalesDTO> saleDTOS = saleService.getAllSalePagination(pageNo, pageSize, sortBy, sortDir);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getAllSales"));
        return ResponseEntity.ok(
                ApiResponse.<PaginationDTO<SalesDTO>>builder()
                        .result(true)
                        .data(saleDTOS)
                        .message("Get all sales successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @GetMapping("sales/list")
    public ResponseEntity<ApiResponse<List<SalesDTO>>> getAllSales(
            HttpServletRequest request
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getAllSales"));
        List<SalesDTO> saleDTOS = saleService.findAll();
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getAllSales"));
        return ResponseEntity.ok(
                ApiResponse.<List<SalesDTO>>builder()
                        .result(true)
                        .data(saleDTOS)
                        .message("Get all sales successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @GetMapping("sales/{id}")
    public ResponseEntity<ApiResponse<SalesDTO>> getSaleById(
            HttpServletRequest request,
            @PathVariable("id") String id
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getSaleById"));
        SalesDTO saleDTO = saleService.findById(id);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getSaleById"));
        return ResponseEntity.ok(
                ApiResponse.<SalesDTO>builder()
                        .result(true)
                        .data(saleDTO)
                        .message("Get sale by id successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @PostMapping("sale")
    public ResponseEntity<ApiResponse<SalesDTO>> createSale(
            HttpServletRequest request,
            @Valid @RequestBody SalesDTO saleDTO
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "createSale"));
        SalesDTO sale = saleService.save(saleDTO);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "createSale"));
        return ResponseEntity.ok(
                ApiResponse.<SalesDTO>builder()
                        .result(true)
                        .data(sale)
                        .message("Create sale successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @PutMapping("sale/{id}")
    public ResponseEntity<ApiResponse<SalesDTO>> updateSale(
            HttpServletRequest request,
            @PathVariable("id") String id,
            @RequestBody SalesDTO saleDTO
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "updateSale"));
        SalesDTO sale = saleService.updateSale(id, saleDTO);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "updateSale"));
        return ResponseEntity.ok(
                ApiResponse.<SalesDTO>builder()
                        .result(true)
                        .data(sale)
                        .message("Update sale successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @DeleteMapping("sale/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSale(
            HttpServletRequest request,
            @PathVariable("id") String id
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "deleteSale"));
        saleService.delete(id);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "deleteSale"));
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .result(true)
                        .message("Delete sale successfully!")
                        .code(String.valueOf(HttpStatus.OK.value()))
                        .build());
    }
}
