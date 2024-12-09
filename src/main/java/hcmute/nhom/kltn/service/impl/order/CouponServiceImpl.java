package hcmute.nhom.kltn.service.impl.order;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.order.CouponDTO;
import hcmute.nhom.kltn.exception.SystemErrorException;
import hcmute.nhom.kltn.mapper.order.CouponMapper;
import hcmute.nhom.kltn.model.product.Coupon;
import hcmute.nhom.kltn.repository.order.CouponRepository;
import hcmute.nhom.kltn.service.impl.AbstractServiceImpl;
import hcmute.nhom.kltn.service.order.CouponService;

/**
 * Class CouponServiceImpl.
 *
 * @author: ThanhTrong
 **/
@Service
@RequiredArgsConstructor
public class CouponServiceImpl
        extends AbstractServiceImpl<CouponRepository, CouponMapper, CouponDTO, Coupon>
        implements CouponService {
    private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);
    private static final String BL_NO = "CouponService";
    private final CouponRepository couponRepository;

    @Override
    public CouponRepository getRepository() {
        this.repository = couponRepository;
        return this.repository;
    }

    @Override
    public CouponMapper getMapper() {
        return CouponMapper.INSTANCE;
    }

    @Override
    public PaginationDTO<CouponDTO> getAllCouponPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
        String methodName = "getAllCouponPagination";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "pageNo", pageNo));
        logger.debug(getMessageInputParam(BL_NO, "pageSize", pageSize));
        logger.debug(getMessageInputParam(BL_NO, "sortBy", sortBy));
        logger.debug(getMessageInputParam(BL_NO, "sortDir", sortDir));
        Page<Coupon> couponPage;
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        try {
            couponPage = couponRepository.findAll(pageable);
            List<CouponDTO> couponDTOS = couponPage.getContent().stream()
                    .map(coupon -> getMapper().toDto(coupon, getCycleAvoidingMappingContext()))
                    .collect(Collectors.toList());
            logger.info(getMessageEnd(BL_NO, methodName));
            return PaginationDTO.<CouponDTO>builder()
                    .items(couponDTOS)
                    .currentPage(pageNo)
                    .pageSize(pageSize)
                    .totalItems(couponPage.getTotalElements())
                    .itemCount(couponPage.getNumberOfElements())
                    .totalPages(couponPage.getTotalPages())
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException("Get all coupon pagination failed");
        }
    }

    @Override
    public List<CouponDTO> getAllCoupon() {
        String methodName = "getAllCoupon";
        logger.info(getMessageStart(BL_NO, methodName));
        List<CouponDTO> couponDTOS;
        try {
            List<Coupon> coupons = couponRepository.findAll();
            couponDTOS = coupons.stream()
                    .map(coupon -> getMapper().toDto(coupon, getCycleAvoidingMappingContext()))
                    .collect(Collectors.toList());
            logger.info(getMessageEnd(BL_NO, methodName));
            return couponDTOS;
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException("Get all coupon failed");
        }
    }

    @Override
    @Transactional
    public CouponDTO updateCoupon(String id, CouponDTO couponDTO) {
        String methodName = "updateCoupon";
        logger.info(getMessageStart(BL_NO, methodName));
        logger.debug(getMessageInputParam(BL_NO, "id", id));
        logger.debug(getMessageInputParam(BL_NO, "couponDTO", couponDTO));

        CouponDTO coupon = findById(id);
        try {
            coupon.setCode(couponDTO.getCode());
            coupon.setDiscount(couponDTO.getDiscount());
            coupon.setStartDate(couponDTO.getStartDate());
            coupon.setEndDate(couponDTO.getEndDate());
            coupon.setDescription(couponDTO.getDescription());
            Coupon couponEntity = getRepository().save(getMapper().toEntity(coupon, getCycleAvoidingMappingContext()));
            coupon = getMapper().toDto(couponEntity, getCycleAvoidingMappingContext());
            logger.debug(getMessageOutputParam(BL_NO, "coupon", coupon));
            logger.info(getMessageEnd(BL_NO, methodName));
            return coupon;
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(BL_NO, methodName));
            throw new SystemErrorException("Update coupon failed");
        }
    }
}
