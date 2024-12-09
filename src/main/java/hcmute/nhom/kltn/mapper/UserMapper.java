package hcmute.nhom.kltn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import hcmute.nhom.kltn.dto.UserDTO;
import hcmute.nhom.kltn.model.User;

/**
 * Class UserMapper.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Mapper()
public interface UserMapper extends AbstractMapper<UserDTO, User> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
