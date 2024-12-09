package hcmute.nhom.kltn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import hcmute.nhom.kltn.dto.ImageDTO;
import hcmute.nhom.kltn.model.Image;

/**
 * Class MediaFileMapper.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Mapper()
public interface ImageMapper extends AbstractMapper<ImageDTO, Image> {
    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);
}
