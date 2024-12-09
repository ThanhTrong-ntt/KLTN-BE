package hcmute.nhom.kltn.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hcmute.nhom.kltn.common.payload.ChangePasswordRequest;
import hcmute.nhom.kltn.dto.ImageDTO;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.RoleDTO;
import hcmute.nhom.kltn.dto.UserDTO;
import hcmute.nhom.kltn.dto.UserProfileDTO;
import hcmute.nhom.kltn.email.EmailSender;
import hcmute.nhom.kltn.enums.RoleName;
import hcmute.nhom.kltn.exception.NotFoundException;
import hcmute.nhom.kltn.exception.SystemErrorException;
import hcmute.nhom.kltn.mapper.UserMapper;
import hcmute.nhom.kltn.model.User;
import hcmute.nhom.kltn.repository.UserRepository;
import hcmute.nhom.kltn.service.ClientService;
import hcmute.nhom.kltn.service.ImageService;
import hcmute.nhom.kltn.service.RoleService;
import hcmute.nhom.kltn.service.UserProfileService;
import hcmute.nhom.kltn.service.UserService;
import hcmute.nhom.kltn.util.Utilities;

/**
 * Class UserServiceImpl.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends AbstractServiceImpl<UserRepository, UserMapper, UserDTO, User>
        implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String SERVICE = "UserService";

    private final UserProfileService userProfileService;

    private final ImageService mediaFileService;

    private final UserRepository userRepository;

    private final ClientService clientService;

    private final EmailSender emailSender;

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Override
    public UserDTO findByUsername(String username) {
        String methodName = "findByUsername";
        logger.info(getMessageStart(SERVICE, methodName));
        logger.debug(getMessageInputParam(SERVICE, "username", username));
        try {
            User user = getRepository().findByUsername(username);
            if (Objects.isNull(user)) {
                logger.error("User not found");
                logger.info(getMessageEnd(SERVICE, methodName));
                throw new SystemErrorException("User not found");
            }
            UserDTO userDTO = getMapper().toDto(user, getCycleAvoidingMappingContext());
            logger.debug(getMessageOutputParam(SERVICE, "userDTO", userDTO));
            logger.info(getMessageEnd(SERVICE, methodName));
            return userDTO;
        } catch (Exception e) {
            logger.error("Error when find user by username", e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException("Error when find user by username");
        }
    }

    @Override
    @Transactional
    public Boolean registerUser(UserDTO userDTO) {
        String methodName = "registerUser";
        logger.info(getMessageStart(SERVICE, methodName));
        logger.debug(getMessageInputParam(SERVICE, "userDTO", userDTO));
        try {
            User user = getRepository().findByEmail(userDTO.getEmail());
            if (Objects.nonNull(user)) {
                logger.error("User already exists");
                logger.info(getMessageEnd(SERVICE, methodName));
                throw new SystemErrorException("User already exists");
            }
            UserProfileDTO userProfileDTO = new UserProfileDTO();
            userProfileDTO.setName(userDTO.getUserProfile().getName());
            userProfileDTO.setRemovalFlag(false);
            userProfileDTO.setAvatar(mediaFileService.findByFileName("default-avatar.png"));
            userProfileDTO = userProfileService.save(userProfileDTO);
            RoleDTO roleUser = roleService.findByRoleName(RoleName.USER.name());
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userDTO.setUserName(userDTO.getEmail());
            userDTO.setActiveFlag(false);
            userDTO.setRemovalFlag(false);
            userDTO.setCreatedBy(userDTO.getEmail());
            userDTO.setModifiedBy(userDTO.getEmail());
            userDTO.setRoles(Set.of(roleUser));
            userDTO.setUserProfile(userProfileDTO);
            save(userDTO);
            // Execute send email
            clientService.activeUser(userDTO.getEmail());
            logger.info(getMessageOutputParam(SERVICE, "result", true));
            logger.info(getMessageEnd(SERVICE, methodName));
            return true;
        } catch (Exception e) {
            logger.error("Error when register user:", e.getMessage());
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public UserDTO findByEmail(String email) {
        String methodName = "findByEmail";
        logger.info(getMessageStart(SERVICE, methodName));
        logger.debug(getMessageInputParam(SERVICE, "email", email));
        try {
            UserDTO userDTO = getMapper().toDto(getRepository().findByEmail(email), getCycleAvoidingMappingContext());
            if (Objects.isNull(userDTO)) {
                logger.debug(getMessageOutputParam(SERVICE, "userDTO", null));
                logger.info(getMessageEnd(SERVICE, methodName));
                return null;
            }
            logger.debug(getMessageOutputParam(SERVICE, "userDTO", userDTO));
            logger.info(getMessageEnd(SERVICE, methodName));
            return userDTO;
        } catch (Exception e) {
            logger.error("Error when find user by email", e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException("Error when find user by email");
        }
    }

    @Override
    @Transactional
    public Boolean saveUserProfile(UserDTO userDTO, String email) {
        String methodName = "saveUserProfile";
        logger.info(getMessageStart(SERVICE, methodName));
        logger.debug(getMessageInputParam(SERVICE, "userProfileDTO", userDTO.getUserProfile()));
        logger.debug(getMessageInputParam(SERVICE, "email", email));
        try {
            UserDTO user = findByEmail(email);
            if (Objects.isNull(user)) {
                logger.error("User not found");
                logger.info(getMessageEnd(SERVICE, methodName));
                return false;
            }
            user.getUserProfile().setName(userDTO.getUserProfile().getName());
            user.getUserProfile().setAvatar(userDTO.getUserProfile().getAvatar());
            save(user);
            logger.debug(getMessageOutputParam(SERVICE, "result", true));
            logger.info(getMessageEnd(SERVICE, methodName));
            return true;
        } catch (Exception e) {
            logger.error("Error when save user profile", e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException("Error when save user profile");
        }
    }

    @Override
    @Transactional
    public Boolean activeUser(String email) {
        String methodName = "activeUser";
        logger.info(getMessageStart(SERVICE, methodName));
        logger.debug(getMessageInputParam(SERVICE, "email", email));
        try {
            UserDTO user = findByEmail(email);
            if (Objects.isNull(user)) {
                logger.error("User not found");
                logger.info(getMessageEnd(SERVICE, methodName));
                return false;
            }
            user.setActiveFlag(true);
            save(user);
            logger.debug(getMessageOutputParam(SERVICE, "result", true));
            logger.info(getMessageEnd(SERVICE, methodName));
            return true;
        } catch (Exception e) {
            logger.error("Error when active user", e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException("Có lỗi khi kích hoạt tài khoản");
        }
    }

    @Override
    @Transactional
    public Boolean deactiveUser(String email) {
        String methodName = "deactiveUser";
        logger.info(getMessageStart(SERVICE, methodName));
        logger.debug(getMessageInputParam(SERVICE, "email", email));
        try {
            UserDTO user = findByEmail(email);
            if (Objects.isNull(user)) {
                logger.error("User not found");
                logger.info(getMessageEnd(SERVICE, methodName));
                return false;
            }
            user.setActiveFlag(false);
            save(user);
            logger.debug(getMessageOutputParam(SERVICE, "result", true));
            logger.info(getMessageEnd(SERVICE, methodName));
            return true;
        } catch (Exception e) {
            logger.error("Error when deactive user", e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException("Có lỗi khi vô hiệu hóa tài khoản");
        }
    }

    @Override
    public PaginationDTO<UserDTO> searchUser(String keyword, int pageNo, int pageSize, String sortBy, String sortDir) {
        String methodName = "searchUser";
        logger.info(getMessageStart(SERVICE, methodName));
        logger.debug(getMessageInputParam(SERVICE, "keyword", keyword));
        logger.debug(getMessageInputParam(SERVICE, "pageNo", pageNo));
        logger.debug(getMessageInputParam(SERVICE, "pageSize", pageSize));
        logger.debug(getMessageInputParam(SERVICE, "sortBy", sortBy));
        logger.debug(getMessageInputParam(SERVICE, "sortDir", sortDir));
        try {
            List<User> list = getRepository().searchUser(keyword);
            List<UserDTO> listDTO = list.stream()
                    .map(item -> getMapper().toDto(item, getCycleAvoidingMappingContext())).collect(Collectors.toList());
            Pageable pageRequest = Utilities.createPageRequestUsing(pageNo, pageSize);
            Page<UserDTO> pageDTO = new PageImpl<>(listDTO, pageRequest, listDTO.size());
            logger.debug(getMessageOutputParam(SERVICE, "pageDTO", listDTO));
            logger.info(getMessageEnd(SERVICE, methodName));
            return PaginationDTO.<UserDTO>builder()
                    .items(listDTO)
                    .currentPage(pageNo)
                    .pageSize(pageSize)
                    .totalItems(pageDTO.getTotalElements())
                    .itemCount(listDTO.size())
                    .totalPages(pageDTO.getTotalPages())
                    .build();
        } catch (Exception e) {
            logger.error("Error when search user", e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException("Error when search user");
        }
    }


    @Override
    public List<UserDTO> getAllUser() {
        String methodName = "getAllUser";
        logger.info(getMessageStart(SERVICE, methodName));
        try {
            List<User> list = getRepository().findAll();
            List<UserDTO> listDTO = list.stream()
                    .map(item -> getMapper().toDto(item, getCycleAvoidingMappingContext())).collect(Collectors.toList());
            logger.debug(getMessageOutputParam(SERVICE, "listDTO", listDTO));
            logger.info(getMessageEnd(SERVICE, methodName));
            return listDTO;
        } catch (Exception e) {
            logger.error("Error when get all user", e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException("Error when get all user");
        }
    }

    @Override
    @Transactional
    public Boolean updateAvatar(UserDTO userDTO, String email) {
        String methodName = "updateAvatar";
        logger.info(getMessageStart(SERVICE, methodName));
        logger.debug(getMessageInputParam(SERVICE, "avatar", userDTO.getUserProfile().getAvatar().getFileName()));
        logger.debug(getMessageInputParam(SERVICE, "email", email));
        try {
            if (Objects.nonNull(userDTO.getUserProfile().getAvatar())) {
                UserDTO user = findByEmail(email);
                if (Objects.isNull(user)) {
                    logger.error("User not found");
                    logger.info(getMessageEnd(SERVICE, methodName));
                    return false;
                }
                ImageDTO mediaFileDTO = mediaFileService.save(userDTO.getUserProfile().getAvatar());
                user.getUserProfile().setAvatar(mediaFileDTO);
                UserProfileDTO userProfileDTO = userProfileService.save(userDTO.getUserProfile());
                user.setUserProfile(userProfileDTO);
                save(user);
                logger.debug(getMessageOutputParam(SERVICE, "result", true));
                logger.info(getMessageEnd(SERVICE, methodName));
                return true;
            } else {
                logger.error("Avatar is null");
                logger.info(getMessageEnd(SERVICE, methodName));
                throw new SystemErrorException("Avatar is null");
            }
        } catch (Exception e) {
            logger.error("Error when update avatar", e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException("Error when update avatar");
        }
    }

    @Override
    public Boolean deleteAvatar(String email) {
        String methodName = "deleteAvatar";
        logger.info(getMessageStart(SERVICE, methodName));
        logger.debug(getMessageInputParam(SERVICE, "email", email));
        try {
            UserDTO user = findByEmail(email);
            if (Objects.isNull(user)) {
                logger.error("User not found");
                logger.info(getMessageEnd(SERVICE, methodName));
                return false;
            }
            mediaFileService.delete(user.getUserProfile().getAvatar());
            user.getUserProfile().setAvatar(null);
            save(user);
            logger.debug(getMessageOutputParam(SERVICE, "result", true));
            logger.info(getMessageEnd(SERVICE, methodName));
            return true;
        } catch (Exception e) {
            logger.error("Error when delete avatar", e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException("Error when delete avatar");
        }
    }

    @Override
    public Boolean checkActiveUser(String email) {
        String methodName = "checkActiveUser";
        logger.info(getMessageStart(SERVICE, methodName));
        logger.debug(getMessageInputParam(SERVICE, "email", email));
        try {
            UserDTO user = findByEmail(email);
            if (Objects.isNull(user)) {
                logger.error("User not found");
                logger.info(getMessageEnd(SERVICE, methodName));
                return false;
            }
            logger.debug(getMessageOutputParam(SERVICE, "result", user.getActiveFlag()));
            logger.info(getMessageEnd(SERVICE, methodName));
            return user.getActiveFlag();
        } catch (Exception e) {
            logger.error("Error when check active user", e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException("Error when check active user");
        }
    }

    @Override
    public Boolean forgotPassword(String email) {
        String methodName = "forgotPassword";
        logger.info(getMessageStart(SERVICE, methodName));
        logger.debug(getMessageInputParam(SERVICE, "email", email));
        try {
            // Find User
            UserDTO user = findByEmail(email);
            if (Objects.isNull(user)) {
                logger.error("Người dùng không có trong hệ thống");
                logger.info(getMessageEnd(SERVICE, methodName));
                throw new NotFoundException("Người dùng không có trong hệ thống");
            }
            String password = Utilities.generateTempPwd(8);
            user.setPassword(passwordEncoder.encode(password));
            save(user);
            // Execute send email
            clientService.forgotPassword(user, password);
            return true;
        } catch (NotFoundException e) {
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e) {
            logger.error("Error when forgot password", e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    public Boolean changePassword(ChangePasswordRequest changePasswordRequest) {
        String methodName = "changePassword";
        logger.info(getMessageStart(SERVICE, methodName));
        logger.debug(getMessageInputParam(SERVICE, "changePasswordRequest", changePasswordRequest));
        try {
            UserDTO user = findByEmail(changePasswordRequest.getEmail());
            if (Objects.isNull(user)) {
                logger.error("User not found");
                logger.info(getMessageEnd(SERVICE, methodName));
                return false;
            }
            if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
                logger.error("Old password is incorrect");
                logger.info(getMessageEnd(SERVICE, methodName));
                return false;
            }
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            save(user);
            logger.debug(getMessageOutputParam(SERVICE, "result", true));
            logger.info(getMessageEnd(SERVICE, methodName));
            return true;
        } catch (Exception e) {
            logger.error("Error when change password", e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException("Error when change password");
        }
    }

    @Override
    public UserDTO updateUserProfile(String email, UserDTO userDTO) {
        String methodName = "updateUserProfile";
        logger.info(getMessageStart(SERVICE, methodName));
        logger.debug(getMessageInputParam(SERVICE, "email", email));
        logger.debug(getMessageInputParam(SERVICE, "userDTO", userDTO));
        try {
            UserDTO user = findByEmail(email);
            if (Objects.isNull(user)) {
                logger.error("User not found");
                logger.info(getMessageEnd(SERVICE, methodName));
                throw new SystemErrorException("User not found");
            }
            UserProfileDTO userProfileDTO = userProfileService.save(userDTO.getUserProfile());
            user.setModifiedBy(email);
            user.setUserName(userDTO.getUserName());
            user.setUserProfile(userProfileDTO);
            save(user);
            logger.debug(getMessageOutputParam(SERVICE, "result", user));
            logger.info(getMessageEnd(SERVICE, methodName));
            return user;
        } catch (Exception e) {
            logger.error("Error when update user profile", e);
            logger.info(getMessageEnd(SERVICE, methodName));
            throw new SystemErrorException("Error when update user profile");
        }
    }

    @Override
    public UserDTO findUserByEmailAndProviderId(String email, String providerId) {
        String method = "findUserByEmailAndProviderId";
        logger.info(getMessageStart(SERVICE, method));
        logger.debug(getMessageInputParam(SERVICE, "email", email));
        logger.debug(getMessageInputParam(SERVICE, "providerId", providerId));
        try {
            User user = getRepository().findByEmailAndProviderId(email, providerId)
                    .orElse(null);
            UserDTO userDTO = getMapper().toDto(user, getCycleAvoidingMappingContext());
            logger.debug(getMessageOutputParam(SERVICE, "result", userDTO));
            logger.info(getMessageEnd(SERVICE, method));
            return userDTO;
        } catch (NotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        logger.info(getMessageStart("UserService", "Delete User"));
        logger.debug(getMessageInputParam("UserService", "user - id", id));
        UserDTO userDTO = findById(id);
        if (Objects.isNull(userDTO)) {
            throw new NotFoundException("User not found. Id: " + id);
        }
        try {
            getRepository().deleteRoleUserByUserId(id);
            getRepository().deleteOrderItemByUserId(id);
            getRepository().deleteOrderUserByUserId(id);
            //imageService.delete(userDTO.getUserProfile().getAvatar());
            userProfileService.delete(userDTO.getUserProfile());
            getRepository().deleteById(id);
            logger.info(getMessageEnd("UserService", "Delete DTO"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd("UserService", "Delete DTO"));
            throw new SystemErrorException("Delete not success. Error: " + e.getMessage());
        }
    }

    @Override
    public UserRepository getRepository() {
        return userRepository;
    }

    @Override
    public UserMapper getMapper() {
        return UserMapper.INSTANCE;
    }
}
