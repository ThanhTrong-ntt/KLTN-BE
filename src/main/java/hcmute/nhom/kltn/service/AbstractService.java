package hcmute.nhom.kltn.service;

import java.util.List;
import org.springframework.data.domain.Page;
import hcmute.nhom.kltn.dto.AbstractNonAuditDTO;
import hcmute.nhom.kltn.model.AbstractModel;

/**
 * Class AbstractService.
 *
 * @author: ThanhTrong
 **/
public interface AbstractService<D extends AbstractNonAuditDTO, E extends AbstractModel> {
    /**
     * Save DTO.
     * @param dto : D
     * @return D
     */
    D save(D dto);

    /**
     * Save Entity.
     * @param entity : E
     * @return E
     */
    E save(E entity);

    /**
     * Save List DTOs.
     * @param dtos : List<D>
     * @return List<D>
     */
    List<D> save(List<D> dtos);

    /**
     * Find DTO By id.
     * @param id : String
     * @return D
     */
    D findById(String id);

    /**
     * Delete By Id.
     * @param id : String
     */
    void delete(String id);

    /**
     * Delete By DTO.
     * @param dto : D
     */
    void delete(D dto);

    /**
     * Find All.
     * @return List<D>
     */
    List<D> findAll();

    /**
     * Get D paging.
     * @param
     * @return Page<D>
     */
    Page<D> getPaging(int page, int size, String sortBy, String sortDir);
}
