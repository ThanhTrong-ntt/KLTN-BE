package hcmute.nhom.kltn.mapper.order;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import hcmute.nhom.kltn.dto.order.CouponDTO;
import hcmute.nhom.kltn.mapper.AbstractMapper;
import hcmute.nhom.kltn.model.product.Coupon;

/**
 * Class CouponMapper.
 *
 * @author: ThanhTrong
 **/
@Mapper()
public interface CouponMapper extends AbstractMapper<CouponDTO, Coupon> {
    CouponMapper INSTANCE = Mappers.getMapper(CouponMapper.class);
}
