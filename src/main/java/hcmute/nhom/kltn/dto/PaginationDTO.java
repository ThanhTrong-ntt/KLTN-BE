package hcmute.nhom.kltn.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Class PaginationDTO.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PaginationDTO<T> {

    private List<T> items;

    private Integer totalPages;

    private Long totalItems;

    private Integer itemCount;

    private Integer pageSize;

    private Integer currentPage;
}
