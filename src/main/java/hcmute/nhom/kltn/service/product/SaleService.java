package hcmute.nhom.kltn.service.product;

import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.product.SalesDTO;
import hcmute.nhom.kltn.model.product.Sales;
import hcmute.nhom.kltn.service.AbstractService;

/**
 * Class SaleService.
 *
 * @author: ThanhTrong
 **/
public interface SaleService extends AbstractService<SalesDTO, Sales> {
    PaginationDTO<SalesDTO> getAllSalePagination(int pageNo, int pageSize, String sortBy, String sortDir);
    SalesDTO updateSale(String id, SalesDTO salesDTO);
}
