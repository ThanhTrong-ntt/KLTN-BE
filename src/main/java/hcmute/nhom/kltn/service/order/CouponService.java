package hcmute.nhom.kltn.service.order;

import java.util.List;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.order.CouponDTO;
import hcmute.nhom.kltn.model.product.Coupon;
import hcmute.nhom.kltn.service.AbstractService;

/**
 * Class CouponService.
 *
 * @author: ThanhTrong
 **/
public interface CouponService extends AbstractService<CouponDTO, Coupon> {
    PaginationDTO<CouponDTO> getAllCouponPagination(int pageNo, int pageSize, String sortBy, String sortDir);
    List<CouponDTO> getAllCoupon();
    CouponDTO updateCoupon(String id, CouponDTO couponDTO);
}
