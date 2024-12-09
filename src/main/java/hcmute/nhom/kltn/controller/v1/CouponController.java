package hcmute.nhom.kltn.controller.v1;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import hcmute.nhom.kltn.common.payload.ApiResponse;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.order.CouponDTO;
import hcmute.nhom.kltn.service.order.CouponService;
import hcmute.nhom.kltn.util.Constants;

/**
 * Class CouponController.
 *
 * @author: ThanhTrong
 **/
@RestController
@RequiredArgsConstructor
public class CouponController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);

    private final CouponService couponService;

    @GetMapping("coupons")
    public ResponseEntity<ApiResponse<PaginationDTO<CouponDTO>>> getAllCoupons(
            HttpServletRequest request,
            @RequestParam(value = "pageNo", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false)
            int pageNo,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false)
            int pageSize,
            @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false)
            String sortBy,
            @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false)
            String sortDir
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getAllCoupons"));
        PaginationDTO<CouponDTO> couponDTOS = couponService.getAllCouponPagination(pageNo, pageSize, sortBy, sortDir);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getAllCoupons"));
        return ResponseEntity.ok(
                ApiResponse.<PaginationDTO<CouponDTO>>builder()
                        .result(true)
                        .data(couponDTOS)
                        .message("Get all coupons successfully!")
                        .code(String.valueOf(HttpStatus.OK.value()))
                        .build());
    }

    @GetMapping("coupons/list")
    public ResponseEntity<ApiResponse<List<CouponDTO>>> getAllCoupons(
            HttpServletRequest request
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getListCoupons"));
        List<CouponDTO> couponDTOS = couponService.getAllCoupon();
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getListCoupons"));
        return ResponseEntity.ok(
                ApiResponse.<List<CouponDTO>>builder()
                        .result(true)
                        .data(couponDTOS)
                        .message("Get all coupons successfully!")
                        .code(String.valueOf(HttpStatus.OK.value()))
                        .build());
    }

    @GetMapping("coupons/{id}")
    public ResponseEntity<ApiResponse<CouponDTO>> getCouponById(
            HttpServletRequest request,
            @PathVariable("id") String id
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getCouponById"));
        CouponDTO couponDTO = couponService.findById(id);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getCouponById"));
        return ResponseEntity.ok(
                ApiResponse.<CouponDTO>builder()
                        .result(true)
                        .data(couponDTO)
                        .message("Get coupon by id successfully!")
                        .code(String.valueOf(HttpStatus.OK.value()))
                        .build());
    }

    @PostMapping("coupon")
    public ResponseEntity<ApiResponse<CouponDTO>> createCoupon(
            HttpServletRequest request,
            @RequestBody CouponDTO couponDTO
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "createCoupon"));
        CouponDTO coupon = couponService.save(couponDTO);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "createCoupon"));
        return ResponseEntity.ok(
                ApiResponse.<CouponDTO>builder()
                        .result(true)
                        .data(coupon)
                        .message("Create coupon successfully!")
                        .code(String.valueOf(HttpStatus.OK.value()))
                        .build());
    }

    @PutMapping("coupon/{id}")
    public ResponseEntity<ApiResponse<CouponDTO>> updateCoupon(
            HttpServletRequest request,
            @PathVariable("id") String id,
            @RequestBody CouponDTO couponDTO
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "updateCoupon"));
        CouponDTO coupon = couponService.updateCoupon(id, couponDTO);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "updateCoupon"));
        return ResponseEntity.ok(
                ApiResponse.<CouponDTO>builder()
                        .result(true)
                        .data(coupon)
                        .message("Update coupon successfully!")
                        .code(String.valueOf(HttpStatus.OK.value()))
                        .build());
    }

    @DeleteMapping("coupon/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteCoupon(
            HttpServletRequest request,
            @PathVariable("id") String id
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "deleteCoupon"));
        couponService.delete(id);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "deleteCoupon"));
        return ResponseEntity.ok(
                ApiResponse.<Boolean>builder()
                        .result(true)
                        .message("Delete coupon successfully!")
                        .code(String.valueOf(HttpStatus.OK.value()))
                        .build());
    }
}
