package hcmute.nhom.kltn.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import hcmute.nhom.kltn.dto.UserProfileDTO;
import hcmute.nhom.kltn.exception.SystemErrorException;
import hcmute.nhom.kltn.mapper.UserProfileMapper;
import hcmute.nhom.kltn.model.UserProfile;
import hcmute.nhom.kltn.repository.UserProfileRepository;
import hcmute.nhom.kltn.service.UserProfileService;

/**
 * Class UserProfileServiceImpl.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl
        extends AbstractServiceImpl<UserProfileRepository, UserProfileMapper, UserProfileDTO, UserProfile>
        implements UserProfileService {
    private static final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);
    private static final String SERVICE = "UserProfileService";
    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfileRepository getRepository() {
        return userProfileRepository;
    }

    @Override
    public UserProfileMapper getMapper() {
        return UserProfileMapper.INSTANCE;
    }

    @Override
    public UserProfileDTO findProfileByEmail(String email) {
        String method = "FindProfileByEmail";
        logger.info(getMessageStart(SERVICE, method));
        logger.debug(getMessageInputParam(SERVICE, "email", email));
        try {
            UserProfile userProfile = getRepository().findByEmail(email).orElse(null);
            logger.debug(getMessageOutputParam(SERVICE, "userProfile", userProfile));
            logger.info(getMessageEnd(SERVICE, method));
            return getMapper().toDto(userProfile, getCycleAvoidingMappingContext());
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(SERVICE, method));
            throw new SystemErrorException(e.getMessage());
        }
    }
}
