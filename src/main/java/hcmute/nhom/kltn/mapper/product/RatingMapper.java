package hcmute.nhom.kltn.mapper.product;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import hcmute.nhom.kltn.dto.product.RatingDTO;
import hcmute.nhom.kltn.mapper.AbstractMapper;
import hcmute.nhom.kltn.model.product.Rating;

/**
 * Class RatingMapper.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Mapper()
public interface RatingMapper extends AbstractMapper<RatingDTO, Rating> {
    RatingMapper INSTANCE = Mappers.getMapper(RatingMapper.class);
}
