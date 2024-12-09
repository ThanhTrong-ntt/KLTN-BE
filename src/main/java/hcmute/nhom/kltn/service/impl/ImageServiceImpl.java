package hcmute.nhom.kltn.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import hcmute.nhom.kltn.dto.ImageDTO;
import hcmute.nhom.kltn.exception.SystemErrorException;
import hcmute.nhom.kltn.mapper.ImageMapper;
import hcmute.nhom.kltn.model.Image;
import hcmute.nhom.kltn.repository.ImageRepository;
import hcmute.nhom.kltn.service.ImageService;
import hcmute.nhom.kltn.service.firebase.FirebaseStorageService;
import hcmute.nhom.kltn.util.Constants;

/**
 * Class MediaFileServiceImpl.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Service
@RequiredArgsConstructor
public class ImageServiceImpl
        extends AbstractServiceImpl<ImageRepository, ImageMapper, ImageDTO, Image>
        implements ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    private static final String SERVICE_NAME = "MediaFileService";

    private final ImageRepository mediaFileRepository;

    private final FirebaseStorageService firebaseStorageService;

    @Override
    public ImageRepository getRepository() {
        return mediaFileRepository;
    }

    @Override
    public ImageMapper getMapper() {
        return ImageMapper.INSTANCE;
    }

    @Override
    public ImageDTO uploadFile(MultipartFile file) {
        String method = "UploadFile";
        logger.info(getMessageStart(SERVICE_NAME, method));
        logger.debug(getMessageInputParam(SERVICE_NAME, "file", file));
        try {
            File fileConverted = convertToFile(file);
            String fileName = file.getOriginalFilename();
            ImageDTO mediaFileDTO = new ImageDTO();
            mediaFileDTO.setFileName(fileName);
            mediaFileDTO.setFileType(this.getExtension(fileName));
            String fileType = file.getContentType();
            mediaFileDTO.setUrl(firebaseStorageService.uploadFile(fileConverted, fileType));
            mediaFileDTO = save(mediaFileDTO);
            fileConverted.delete();
            return mediaFileDTO;
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(SERVICE_NAME, method));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    public List<ImageDTO> uploadFiles(List<MultipartFile> files) {
        String method = "UploadFiles";
        logger.info(getMessageStart(SERVICE_NAME, method));
        logger.debug(getMessageInputParam(SERVICE_NAME, "files", files));
        try {
            List<ImageDTO> mediaFileDTOs = new ArrayList<>();
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                String fileType = file.getContentType();
                File fileConverted = convertToFile(file);
                ImageDTO mediaFileDTO = new ImageDTO();
                mediaFileDTO.setFileName(fileName);
                mediaFileDTO.setFileType(this.getExtension(fileName));
                mediaFileDTO.setUrl(firebaseStorageService.uploadFile(fileConverted, fileType));
                mediaFileDTOs.add(save(mediaFileDTO));
                fileConverted.delete();
            }
            logger.debug(getMessageOutputParam(SERVICE_NAME, "mediaFileDTOs", mediaFileDTOs));
            logger.info(getMessageEnd(SERVICE_NAME, method));
            return mediaFileDTOs;
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(SERVICE_NAME, method));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    public List<ImageDTO> findByProductId(String productId) {
        String method = "FindByProductId";
        logger.info(getMessageStart(SERVICE_NAME, method));
        logger.debug(getMessageInputParam(SERVICE_NAME, "productId", productId));
        try {
            List<Image> mediaFiles = mediaFileRepository.findByProductId(productId);
            List<ImageDTO> mediaFileDTOs = mediaFiles.stream().map(
                    mediaFile -> getMapper().toDto(mediaFile, getCycleAvoidingMappingContext())
            ).collect(Collectors.toList());
            logger.debug(getMessageOutputParam(SERVICE_NAME, "mediaFileDTOs", mediaFileDTOs));
            logger.info(getMessageEnd(SERVICE_NAME, method));
            return mediaFileDTOs;
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(SERVICE_NAME, method));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    public ImageDTO findByFileName(String fileName) {
        String method = "FindByFileName";
        logger.info(getMessageStart(SERVICE_NAME, method));
        logger.debug(getMessageInputParam(SERVICE_NAME, "fileName", fileName));
        try {
            Image mediaFile = mediaFileRepository.findByFileName(fileName);
            if (mediaFile == null) {
                logger.debug(getMessageOutputParam(SERVICE_NAME, "mediaFile", mediaFile));
                logger.info(getMessageEnd(SERVICE_NAME, method));
                return null;
            }
            ImageDTO mediaFileDTO = getMapper().toDto(mediaFile, getCycleAvoidingMappingContext());
            logger.debug(getMessageOutputParam(SERVICE_NAME, "mediaFileDTO", mediaFileDTO));
            logger.info(getMessageEnd(SERVICE_NAME, method));
            return mediaFileDTO;
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(SERVICE_NAME, method));
            throw new SystemErrorException(e.getMessage());
        }
    }

    @Override
    public ImageDTO getDefaultAvatar() {
        String method = "GetDefaultAvatar";
        logger.info(getMessageStart(SERVICE_NAME, method));
        ImageDTO mediaFileDTO = findByFileName("default-avatar.png");
        if (Objects.isNull(mediaFileDTO)) {
            String fileName = "default-avatar.png";
            mediaFileDTO.setFileName(fileName);
            mediaFileDTO.setFileType(fileName.substring(fileName.lastIndexOf(".")));
            mediaFileDTO.setUrl(Constants.DEFAULT_AVATAR);
            mediaFileDTO = save(mediaFileDTO);
        }
        logger.debug(getMessageOutputParam(SERVICE_NAME, "mediaFileDTO", mediaFileDTO));
        logger.info(getMessageEnd(SERVICE_NAME, method));
        return mediaFileDTO;
    }

    @Override
    public Boolean areEqualsTwoList(List<ImageDTO> list1, List<ImageDTO> list2) {
        if (Objects.isNull(list1) || Objects.isNull(list2) || list1.size() != list2.size()) {
            return false;
        }

        for (int i = 0; i < list1.size(); i++) {
            ImageDTO dto1 = list1.get(i);
            ImageDTO dto2 = list2.get(i);
            if (!areMediaFileDTOsEqual(dto1, dto2)) {
                return false;
            }
        }

        return true;
    }

    private static boolean areMediaFileDTOsEqual(ImageDTO dto1, ImageDTO dto2) {
        return dto1.getId().equals(dto2.getId()) &&
                dto1.getFileName().equals(dto2.getFileName()) &&
                dto1.getFileType().equals(dto2.getFileType()) &&
                dto1.getUrl().equals(dto2.getUrl());
    }

    private File convertToFile(MultipartFile multipartFile) {
        String method = "ConvertToFile";
        logger.info(getMessageStart(SERVICE_NAME, method));
        logger.debug(getMessageInputParam(SERVICE_NAME, "multipartFile", multipartFile));
        File tempFile = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.info(getMessageEnd(SERVICE_NAME, method));
            throw new SystemErrorException(e.getMessage());
        }
        logger.debug(getMessageOutputParam(SERVICE_NAME, "tempFile", tempFile));
        logger.info(getMessageEnd(SERVICE_NAME, method));
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
