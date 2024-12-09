package hcmute.nhom.kltn.mapper.product;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import hcmute.nhom.kltn.dto.product.SalesDTO;
import hcmute.nhom.kltn.mapper.AbstractMapper;
import hcmute.nhom.kltn.model.product.Sales;

/**
 * Class SalesMapper.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Mapper()
public interface SalesMapper extends AbstractMapper<SalesDTO, Sales> {
    SalesMapper INSTANCE = Mappers.getMapper(SalesMapper.class);
}
