package hcmute.nhom.kltn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import hcmute.nhom.kltn.dto.UserProfileDTO;
import hcmute.nhom.kltn.model.UserProfile;

/**
 * Class UserProfileMapper.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Mapper()
public interface UserProfileMapper extends AbstractMapper<UserProfileDTO, UserProfile> {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);
}
