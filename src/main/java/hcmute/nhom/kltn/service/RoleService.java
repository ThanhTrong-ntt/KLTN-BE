package hcmute.nhom.kltn.service;

import hcmute.nhom.kltn.dto.RoleDTO;
import hcmute.nhom.kltn.model.Role;

/**
 * Class RoleService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface RoleService extends AbstractService<RoleDTO, Role> {
    /**
     * findByRoleName.
     * @param roleName roleName
     * @return RoleDTO
     */
    RoleDTO findByRoleName(String roleName);

    /**
     * findByName.
     * @param roleName roleName
     * @return Role
     */
    Role findByName(String roleName);
}
