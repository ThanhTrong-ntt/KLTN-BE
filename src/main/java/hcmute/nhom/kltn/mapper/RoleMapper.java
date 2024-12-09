package hcmute.nhom.kltn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import hcmute.nhom.kltn.dto.RoleDTO;
import hcmute.nhom.kltn.model.Role;

/**
 * Class RoleMapper.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Mapper()
public interface RoleMapper extends AbstractMapper<RoleDTO, Role> {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
}
