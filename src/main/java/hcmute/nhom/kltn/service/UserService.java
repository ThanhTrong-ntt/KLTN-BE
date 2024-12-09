package hcmute.nhom.kltn.service;

import java.util.List;
import org.springframework.data.domain.Page;
import hcmute.nhom.kltn.common.payload.ChangePasswordRequest;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.UserDTO;
import hcmute.nhom.kltn.model.User;

/**
 * Class UserService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface UserService extends AbstractService<UserDTO, User> {

    /**
     * findByUsername.
     * @param username String
     * @return UserDTO
     */
    UserDTO findByUsername(String username);

    /**
     * registerUser.
     * @param userDTO UserDTO
     * @return Boolean
     */
    Boolean registerUser(UserDTO userDTO);

    /**
     * findByEmail.
     * @param email String
     * @return UserDTO
     */
    UserDTO findByEmail(String email);

    /**
     * changePassword.
     * @param userDTO UserDTO
     * @param email String
     * @return UserDTO
     */
    Boolean saveUserProfile(UserDTO userDTO, String email);

    /**
     * changePassword.
     * @param email String
     * @return UserDTO
     */
    Boolean activeUser(String email);

    Boolean deactiveUser(String email);

    /**
     * changePassword.
     * @param keyword String
     * @param pageNo int
     * @param pageSize int
     * @param sortBy String
     * @param sortDir String
     * @return Page<UserDTO>
     */
    PaginationDTO<UserDTO> searchUser(String keyword, int pageNo, int pageSize, String sortBy, String sortDir);

    /**
     * getAllUser.
     * @return List<UserDTO>
     */
    List<UserDTO> getAllUser();

    /**
     * changePassword.
     * @param userDTO UserDTO
     * @param email String
     * @return UserDTO
     */
    Boolean updateAvatar(UserDTO userDTO, String email);

    /**
     * deleteAvatar.
     * @param email String
     * @return Boolean
     */
    Boolean deleteAvatar(String email);

    /**
     * checkActiveUser.
     * @param email String
     * @return Boolean
     */
    Boolean checkActiveUser(String email);

    Boolean forgotPassword(String email);

    Boolean changePassword(ChangePasswordRequest changePasswordRequest);

    UserDTO updateUserProfile(String email, UserDTO userDTO);

    UserDTO findUserByEmailAndProviderId(String email, String providerId);

    void deleteUser(String id);
}
