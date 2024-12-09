package hcmute.nhom.kltn.service.impl.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.product.ColorDTO;
import hcmute.nhom.kltn.exception.NotFoundException;
import hcmute.nhom.kltn.exception.SystemErrorException;
import hcmute.nhom.kltn.mapper.product.ColorMapper;
import hcmute.nhom.kltn.model.product.Color;
import hcmute.nhom.kltn.repository.product.ColorRepository;
import hcmute.nhom.kltn.service.impl.AbstractServiceImpl;
import hcmute.nhom.kltn.service.product.ColorService;

/**
 * Class ColorServiceImpl.
 *
 * @author: ThanhTrong
 **/
@Service
@RequiredArgsConstructor
public class ColorServiceImpl extends AbstractServiceImpl<ColorRepository, ColorMapper, ColorDTO, Color> implements ColorService {
    private final static Logger logger = LoggerFactory.getLogger(ColorServiceImpl.class);
    private static final String BL_NO = "ColorService";
    private final ColorRepository colorRepository;

    @Override
    public ColorRepository getRepository() {
        return colorRepository;
    }

    @Override
    public ColorMapper getMapper() {
        return ColorMapper.INSTANCE;
    }

    @Override
    public PaginationDTO<ColorDTO> getAllColorPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
        String methodName = "getAllColorPagination";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "pageNo", pageNo));
        logger.debug(getMessageInputParam(BL_NO, "pageSize", pageSize));
        logger.debug(getMessageInputParam(BL_NO, "sortBy", sortBy));
        logger.debug(getMessageInputParam(BL_NO, "sortDir", sortDir));
        Page<Color> pageColor;
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        try {
            pageColor = getRepository().findAll(pageable);
            List<ColorDTO> colorDTOS = getMapper().toDtoList(pageColor.getContent(), getCycleAvoidingMappingContext());
            logger.debug(getMessageOutputParam(BL_NO, "colorDTOS", colorDTOS));
            logger.info(getMessageEnd(BL_NO, methodName));
            return PaginationDTO.<ColorDTO>builder()
                    .items(colorDTOS)
                    .totalPages(pageColor.getTotalPages())
                    .totalItems(pageColor.getTotalElements())
                    .itemCount(pageColor.getNumberOfElements())
                    .currentPage(pageColor.getNumber())
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd("ColorService", methodName));
            throw new SystemErrorException("Get all color pagination not success");
        }
    }

    @Override
    public ColorDTO updateColor(String id, ColorDTO colorDTO) {
        String methodName = "updateColor";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "id", id));
        logger.debug(getMessageInputParam(BL_NO, "colorDTO", colorDTO));
        ColorDTO colorCheck = findById(id);
        if (colorCheck == null) {
            throw new NotFoundException("Color not found. Id: " + id);
        }
        try {
            colorCheck.setCode(colorDTO.getCode());
            colorCheck.setName(colorDTO.getName());
            colorCheck.setDisplayCode(colorDTO.getDisplayCode());
            colorCheck = save(colorCheck);
            logger.debug(getMessageOutputParam(BL_NO, "colorDTO", colorCheck));
            logger.info(getMessageEnd(BL_NO, methodName));
            return colorCheck;
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException("Update color not success. Error: " + e.getMessage());
        }
    }
}
