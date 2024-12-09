package hcmute.nhom.kltn.security.oauth2;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import hcmute.nhom.kltn.dto.ImageDTO;
import hcmute.nhom.kltn.dto.RoleDTO;
import hcmute.nhom.kltn.dto.UserDTO;
import hcmute.nhom.kltn.dto.UserProfileDTO;
import hcmute.nhom.kltn.enums.AuthProvider;
import hcmute.nhom.kltn.enums.RoleName;
import hcmute.nhom.kltn.exception.SystemErrorException;
import hcmute.nhom.kltn.mapper.UserMapper;
import hcmute.nhom.kltn.mapper.helper.CycleAvoidingMappingContext;
import hcmute.nhom.kltn.security.oauth2.dto.OAuth2UserDetail;
import hcmute.nhom.kltn.security.pricipal.UserPrincipal;
import hcmute.nhom.kltn.service.ImageService;
import hcmute.nhom.kltn.service.RoleService;
import hcmute.nhom.kltn.service.UserProfileService;
import hcmute.nhom.kltn.service.UserService;

/**
 * Class CustomOAuth2UserService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserProfileService userProfileService;
    private final UserService userService;
    private final RoleService roleService;
    private final ImageService mediaFileService;

    public CustomOAuth2UserService(@Lazy UserService userService, UserProfileService userProfileService, RoleService roleService, ImageService mediaFileService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.roleService = roleService;
        this.mediaFileService = mediaFileService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserDetail oAuth2UserInfo = OAuth2UserDetailFactory.getOAuth2UserDetail(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new SystemErrorException("Email not found from OAuth2 provider");
        }
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        UserDTO userDTO = userService.findUserByEmailAndProviderId(oAuth2UserInfo.getEmail(), registrationId);
        UserDTO user;
        if (!Objects.isNull(userDTO)) {
            user = userDTO;
            if (!user.getProviderId().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()).name())) {
                throw new SystemErrorException("Looks like you're signed up with " +
                        user.getProviderId() + " account. Please use your " + user.getProviderId() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(UserMapper.INSTANCE.toEntity(user, getCycleAvoidingMappingContext()),
                oAuth2User.getAttributes());
    }

    private UserDTO registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserDetail oAuth2UserDetail) {
        UserDTO user = new UserDTO();

        ImageDTO mediaFile = new ImageDTO();
        mediaFile.setFileName(oAuth2UserDetail.getName());
        mediaFile.setFileType("PNG");
        mediaFile.setUrl(oAuth2UserDetail.getAvatarUrl());
        mediaFile = mediaFileService.save(mediaFile);

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setRemovalFlag(false);
        userProfileDTO.setName(oAuth2UserDetail.getName());
        userProfileDTO.setAvatar(mediaFile);
        userProfileDTO = userProfileService.save(userProfileDTO);

        user.setProviderId(String.valueOf(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId())));
        user.setUserName(oAuth2UserDetail.getEmail());
        user.setEmail(oAuth2UserDetail.getEmail());
        user.setUserProfile(userProfileDTO);
        user.setActiveFlag(true);
        user.setRemovalFlag(false);
        Set<RoleDTO> roleDTOS = new HashSet<>();
        roleDTOS.add(roleService.findByRoleName(RoleName.USER.name()));
        user.setRoles(roleDTOS);
        return userService.save(user);
    }

    private UserDTO updateExistingUser(UserDTO existingUser, OAuth2UserDetail oAuth2UserInfo) {
        //existingUser.setName(oAuth2UserInfo.getName());
        //existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userService.save(existingUser);
    }

    public CycleAvoidingMappingContext getCycleAvoidingMappingContext() {
        return new CycleAvoidingMappingContext();
    }
}
