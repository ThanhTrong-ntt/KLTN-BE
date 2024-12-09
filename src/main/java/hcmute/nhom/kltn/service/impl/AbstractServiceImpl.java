package hcmute.nhom.kltn.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import hcmute.nhom.kltn.common.AbstractMessage;
import hcmute.nhom.kltn.dto.AbstractNonAuditDTO;
import hcmute.nhom.kltn.exception.NotFoundException;
import hcmute.nhom.kltn.exception.SystemErrorException;
import hcmute.nhom.kltn.mapper.AbstractMapper;
import hcmute.nhom.kltn.mapper.helper.CycleAvoidingMappingContext;
import hcmute.nhom.kltn.model.AbstractModel;
import hcmute.nhom.kltn.service.AbstractService;
import hcmute.nhom.kltn.util.Utilities;

/**
 * Class AbstractServiceImpl.
 *
 * @author: ThanhTrong
 **/
@Setter
@Getter
public class AbstractServiceImpl<R extends JpaRepository<E, String>, M extends AbstractMapper<D, E>,
        D extends AbstractNonAuditDTO, E extends AbstractModel>
        extends AbstractMessage
        implements AbstractService<D, E>, MessageSourceAware {

    private static final Logger logger = LoggerFactory.getLogger(AbstractServiceImpl.class);

    protected R repository;
    protected D dto;
    protected E entity;
    protected M mapper;
    protected MessageSource messageSource;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public CycleAvoidingMappingContext getCycleAvoidingMappingContext() {
        return new CycleAvoidingMappingContext();
    }

    @Override
    @Transactional
    public D save(D dto) {
        logger.info(getMessageStart("Service", "Save DTO"));
        if (dto == null) {
            throw new SystemErrorException("Save not success. DTO is null");
        }
        E item = getMapper().toEntity(dto, getCycleAvoidingMappingContext());
        entity = getRepository().save(item);
        return getMapper().toDto(entity, getCycleAvoidingMappingContext());
    }

    @Override
    public E save(E entity) {
        logger.info(getMessageStart("AbstractService", "Save Entity"));
        if (entity == null) {
            throw new SystemErrorException("Save not success. Entity is null");
        }
        logger.info(getMessageEnd("AbstractService", "Save Entity"));
        return getRepository().save(entity);
    }

    @Override
    public List<D> save(List<D> dtos) {
        logger.info(getMessageStart("AbstractService", "Save DTOs"));
        if (Objects.isNull(dtos) || dtos.isEmpty()) {
            throw new SystemErrorException("Save not success. DTOs is null");
        }
        List<E> entities = dtos.stream().map(item -> getMapper()
                .toEntity(item, getCycleAvoidingMappingContext())).collect(Collectors.toList());

        entities = getRepository().saveAll(entities);

        logger.info(getMessageEnd("AbstractService", "Save DTOs"));
        return entities.stream().map(item -> getMapper()
                .toDto(item, getCycleAvoidingMappingContext())).collect(Collectors.toList());
    }

    @Override
    public D findById(String id) {
        logger.info(getMessageStart("AbstractService", "Find DTO by id"));
        logger.debug(getMessageInputParam("AbstractService", "id", id));
        String entityName = getEntityName();
        Optional<E> optional = getRepository().findById(id);
        if (optional.isEmpty()) {
            logger.error("Not found " + entityName + " with id: " + id);
            logger.info(getMessageEnd("AbstractService", "Find DTO by id"));
            throw new NotFoundException("Not found " + entityName + " with id: " + id);
        }
        logger.info(getMessageEnd("AbstractService", "Find DTO by id"));
        return getMapper().toDto(optional.get(), getCycleAvoidingMappingContext());
    }

    @Override
    public void delete(String id) {
        logger.info(getMessageStart("AbstractService", "Delete DTO"));
        logger.debug(getMessageInputParam("AbstractService", "dto - id", id));
        if (Objects.isNull(findById(id))) {
            throw new NotFoundException("DTO not found. Id: " + id);
        }
        try {
            getRepository().deleteById(id);
            logger.info(getMessageEnd("AbstractService", "Delete DTO"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd("AbstractService", "Delete DTO"));
            throw new SystemErrorException("Delete not success. Error: " + e.getMessage());
        }
    }

    @Override
    public void delete(D dto) {
        logger.info(getMessageStart("AbstractService", "Delete DTO"));
        logger.debug(getMessageInputParam("AbstractService", "dto", dto));
        if (dto == null) {
            throw new SystemErrorException("Delete not success. DTO is null");
        }
        try {
            getRepository().delete(getMapper().toEntity(dto, getCycleAvoidingMappingContext()));
            logger.info(getMessageEnd("AbstractService", "Delete DTO"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd("AbstractService", "Delete DTO"));
            throw new SystemErrorException("Delete not success. Error: " + e.getMessage());
        }
    }

    @Override
    public List<D> findAll() {
        logger.info(getMessageStart("AbstractService", "Find all DTO"));
        List<E> list = getRepository().findAll();
        logger.info(getMessageEnd("AbstractService", "Find all DTO"));
        return list.stream().map(item -> getMapper().toDto(item, getCycleAvoidingMappingContext())).collect(Collectors.toList());
    }

    @Override
    public Page<D> getPaging(int page, int size, String sortBy, String sortDir) {
        Pageable pageRequest = Utilities.getPageRequest(page, size, sortBy, sortDir);
        Page<E> entities = getRepository().findAll(pageRequest);
        return entities.map(item -> getMapper().toDto(item, getCycleAvoidingMappingContext()));
    }

    private String getEntityName() {
        Class<E> entityClass = getEntityClass();
        return entityClass.getSimpleName();  // Lấy tên ngắn gọn của class (không có package)
    }

    @SuppressWarnings("unchecked")
    private Class<E> getEntityClass() {
        // Tìm kiếm lớp `Entity` từ kiểu tổng quát của lớp `AbstractServiceImpl`
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<E>) type.getActualTypeArguments()[3];  // Chỉ mục thứ 3 là `E` trong khai báo generic
    }
}
