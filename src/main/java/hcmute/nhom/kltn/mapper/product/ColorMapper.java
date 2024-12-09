package hcmute.nhom.kltn.mapper.product;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import hcmute.nhom.kltn.dto.product.ColorDTO;
import hcmute.nhom.kltn.mapper.AbstractMapper;
import hcmute.nhom.kltn.model.product.Color;

/**
 * Class ColorMapper.
 *
 * @author: ThanhTrong
 **/
@Mapper()
public interface ColorMapper extends AbstractMapper<ColorDTO, Color> {
    ColorMapper INSTANCE = Mappers.getMapper(ColorMapper.class);
}
