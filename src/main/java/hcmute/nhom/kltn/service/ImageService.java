package hcmute.nhom.kltn.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import hcmute.nhom.kltn.dto.ImageDTO;
import hcmute.nhom.kltn.model.Image;

/**
 * Class MediaFileService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface ImageService extends AbstractService<ImageDTO, Image> {

    ImageDTO uploadFile(MultipartFile file);

    List<ImageDTO> uploadFiles(List<MultipartFile> files);

    List<ImageDTO> findByProductId(String productId);

    ImageDTO findByFileName(String fileName);

    ImageDTO getDefaultAvatar();

    Boolean areEqualsTwoList(List<ImageDTO> list1, List<ImageDTO> list2);
}
