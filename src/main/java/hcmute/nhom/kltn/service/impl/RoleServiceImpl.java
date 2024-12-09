package hcmute.nhom.kltn.service.impl;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import hcmute.nhom.kltn.dto.RoleDTO;
import hcmute.nhom.kltn.exception.NotFoundException;
import hcmute.nhom.kltn.mapper.RoleMapper;
import hcmute.nhom.kltn.model.Role;
import hcmute.nhom.kltn.repository.RoleRepository;
import hcmute.nhom.kltn.service.RoleService;

/**
 * Class RoleServiceImpl.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends AbstractServiceImpl<RoleRepository, RoleMapper, RoleDTO, Role>
        implements RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private static final String SERVICE_NAME = "RoleService";
    private final RoleRepository roleRepository;
    @Override
    public RoleDTO findByRoleName(String roleName) {
        String method = "FindByRoleName";
        logger.info(getMessageStart(SERVICE_NAME, method));
        Role role = getRepository().findByName(roleName).orElse(null);
//        if (Objects.isNull(role)) {
//            String message = "Role not found";
//            logger.error(message);
//            logger.info(getMessageEnd(SERVICE_NAME, method));
//            throw new NotFoundException(message);
//        }
        logger.debug(getMessageOutputParam(SERVICE_NAME, "role", role));
        logger.info(getMessageEnd(SERVICE_NAME, method));
        return getMapper().toDto(role, getCycleAvoidingMappingContext());
    }

    @Override
    public Role findByName(String roleName) {
        String method = "FindByName";
        logger.info(getMessageStart(SERVICE_NAME, method));
        Role role = getRepository().findByName(roleName).orElse(null);
        if (Objects.isNull(role)) {
            String message = "Role not found";
            logger.error(message);
            logger.info(getMessageEnd(SERVICE_NAME, method));
            throw new NotFoundException(message);
        }
        logger.debug(getMessageOutputParam(SERVICE_NAME, "role", role));
        logger.info(getMessageEnd(SERVICE_NAME, method));
        return role;
    }

    @Override
    public RoleRepository getRepository() {
        return roleRepository;
    }

    @Override
    public RoleMapper getMapper() {
        return RoleMapper.INSTANCE;
    }
}
