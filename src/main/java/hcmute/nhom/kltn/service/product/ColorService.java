package hcmute.nhom.kltn.service.product;

import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.product.ColorDTO;
import hcmute.nhom.kltn.model.product.Color;
import hcmute.nhom.kltn.service.AbstractService;

/**
 * Class ColorService.
 *
 * @author: ThanhTrong
 **/
public interface ColorService extends AbstractService<ColorDTO, Color> {
    PaginationDTO<ColorDTO> getAllColorPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    ColorDTO updateColor(String id, ColorDTO colorDTO);
}
