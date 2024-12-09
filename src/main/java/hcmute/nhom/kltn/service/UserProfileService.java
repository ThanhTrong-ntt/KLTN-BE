package hcmute.nhom.kltn.service;

import hcmute.nhom.kltn.dto.UserProfileDTO;
import hcmute.nhom.kltn.model.UserProfile;

/**
 * Class UserProfileService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface UserProfileService extends AbstractService<UserProfileDTO, UserProfile> {

    UserProfileDTO findProfileByEmail(String email);
}
