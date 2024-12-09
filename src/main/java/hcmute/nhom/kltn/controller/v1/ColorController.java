package hcmute.nhom.kltn.controller.v1;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
import hcmute.nhom.kltn.dto.product.ColorDTO;
import hcmute.nhom.kltn.service.product.ColorService;
import hcmute.nhom.kltn.util.Constants;

/**
 * Class ColorController.
 *
 * @author: ThanhTrong
 **/
@RestController
public class ColorController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(ColorController.class);
    private final ColorService colorService;

    public ColorController(final ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping("colors")
    public ResponseEntity<ApiResponse<PaginationDTO<ColorDTO>>> getAllColors(
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
        logger.info(getMessageStart(request.getRequestURL().toString(), "getAllColors"));
        PaginationDTO<ColorDTO> colorDTOS = colorService.getAllColorPagination(pageNo, pageSize, sortBy, sortDir);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getAllColors"));
        return ResponseEntity.ok(
                ApiResponse.<PaginationDTO<ColorDTO>>builder()
                        .result(true)
                        .data(colorDTOS)
                        .message("Get all colors successfully!")
                        .code(HttpStatus.OK.toString()).build());
    }

    @GetMapping("colors/list")
    public ResponseEntity<ApiResponse<List<ColorDTO>>> getAllColors(
            HttpServletRequest request
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getAllColors"));
        List<ColorDTO> colorDTOS = colorService.findAll();
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getAllColors"));
        return ResponseEntity.ok(
                ApiResponse.<List<ColorDTO>>builder()
                        .result(true)
                        .data(colorDTOS)
                        .message("Get all colors successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @GetMapping("colors/{id}")
    public ResponseEntity<ApiResponse<ColorDTO>> getColorById(
            HttpServletRequest request,
            @RequestParam("id") String id
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getColorById"));
        ColorDTO colorDTO = colorService.findById(id);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getColorById"));
        return ResponseEntity.ok(
                ApiResponse.<ColorDTO>builder()
                        .result(true)
                        .data(colorDTO)
                        .message("Get color by id successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @PostMapping("color")
    public ResponseEntity<ApiResponse<ColorDTO>> saveColor(
            HttpServletRequest request,
            @RequestBody ColorDTO colorDTO
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "saveColor"));
        ColorDTO color = colorService.save(colorDTO);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "saveColor"));
        return ResponseEntity.ok(
                ApiResponse.<ColorDTO>builder()
                        .result(true)
                        .data(color)
                        .message("Save color successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @PutMapping("color/{id}")
    public ResponseEntity<ApiResponse<ColorDTO>> updateColor(
            HttpServletRequest request,
            @PathVariable("id") String id,
            @RequestBody ColorDTO colorDTO
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "updateColor"));
        ColorDTO color = colorService.updateColor(id, colorDTO);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "updateColor"));
        return ResponseEntity.ok(
                ApiResponse.<ColorDTO>builder()
                        .result(true)
                        .data(color)
                        .message("Update color successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @DeleteMapping("color/{id}")
    public ResponseEntity<ApiResponse<ColorDTO>> deleteColor(
            HttpServletRequest request,
            @PathVariable("id") String id
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "deleteColor"));
        colorService.delete(id);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "deleteColor"));
        return ResponseEntity.ok(
                ApiResponse.<ColorDTO>builder()
                        .result(true)
                        .data(null)
                        .message("Delete color successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }
}
