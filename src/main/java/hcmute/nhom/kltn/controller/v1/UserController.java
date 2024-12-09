package hcmute.nhom.kltn.controller.v1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
import hcmute.nhom.kltn.common.payload.ChangePasswordRequest;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.UserDTO;
import hcmute.nhom.kltn.exception.SystemErrorException;
import hcmute.nhom.kltn.service.UserService;
import hcmute.nhom.kltn.service.session.SessionManagementService;
import hcmute.nhom.kltn.util.Constants;
import hcmute.nhom.kltn.util.SessionConstants;

/**
 * Class UserController.
 *
 * @author: ThanhTrong
 **/
@RestController
@RequiredArgsConstructor
public class UserController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final SessionManagementService sessionService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<PaginationDTO<UserDTO>>> getAllUser(
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
        logger.info(getMessageStart(request.getRequestURL().toString(), "getAllUser"));
        Page<UserDTO> userDTOPage = userService.getPaging(pageNo, pageSize, sortBy, sortDir);
        PaginationDTO<UserDTO> userDTOPagination = PaginationDTO.<UserDTO>builder()
                .items(userDTOPage.getContent())
                .totalPages(userDTOPage.getTotalPages())
                .totalItems(userDTOPage.getTotalElements())
                .itemCount(userDTOPage.getNumberOfElements())
                .currentPage(userDTOPage.getNumber())
                .build();
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getAllUser"));
        return ResponseEntity.ok(
                ApiResponse.<PaginationDTO<UserDTO>>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(userDTOPagination)
                        .message("Get all user successfully!")
                        .build());
    }

    @GetMapping("/users/search-by-name")
    public ResponseEntity<ApiResponse<PaginationDTO<UserDTO>>> searchUser(
            HttpServletRequest request,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "pageNo", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false)
            int pageNo,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false)
            int pageSize,
            @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false)
            String sortBy,
            @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false)
            String sortDir
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "searchUser"));
        PaginationDTO<UserDTO> userDTOPage = userService.searchUser(keyword, pageNo, pageSize, sortBy, sortDir);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "searchUser"));
        return ResponseEntity.ok(
                ApiResponse.<PaginationDTO<UserDTO>>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(userDTOPage)
                        .message("Search user successfully!")
                        .build());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
            HttpServletRequest request,
            @PathVariable("id") String id
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getUserById"));
        UserDTO userDTO = userService.findById(id);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getUserById"));
        return ResponseEntity.ok(
                ApiResponse.<UserDTO>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(userDTO)
                        .message("Get user by id successfully!")
                        .build());
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByEmail(
            HttpServletRequest request,
            @PathVariable("email") String email
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getUserByEmail"));
        UserDTO userDTO = userService.findByEmail(email);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getUserByEmail"));
        return ResponseEntity.ok(
                ApiResponse.<UserDTO>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(userDTO)
                        .message("Get user by email successfully!")
                        .build());
    }

    @PutMapping("/user/{email}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            HttpServletRequest request,
            HttpSession session,
            @PathVariable("email") String email,
            @RequestBody UserDTO userDTO
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "updateUser"));
        UserDTO result = userService.updateUserProfile(email, userDTO);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "updateUser"));
        return ResponseEntity.ok(
                ApiResponse.<UserDTO>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(result)
                        .message("Update user successfully!")
                        .build());
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> deleteUser(
            HttpServletRequest request,
            @PathVariable("id") String id,
            @RequestParam("userId") String userId
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "deleteUser"));
        if (userId.equals(id)) {
            throw new SystemErrorException("Không thể xóa chính mình!");
        }
        userService.deleteUser(id);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "deleteUser"));
        return ResponseEntity.ok(
                ApiResponse.<UserDTO>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .message("Delete user successfully!")
                        .build());
    }

    @PostMapping("/user/change-password")
    public ResponseEntity<ApiResponse<Boolean>> changePassword(
            HttpServletRequest request,
            @RequestBody ChangePasswordRequest changePasswordRequest
            ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "changePassword"));
        Boolean result = userService.changePassword(changePasswordRequest);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "changePassword"));
        return ResponseEntity.ok(
                ApiResponse.<Boolean>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(result)
                        .message("Change password successfully!")
                        .build());
    }

    @PostMapping("/user/active/{email}")
    public ResponseEntity<ApiResponse<Boolean>> activeUser(
            HttpServletRequest request,
            @PathVariable("email") String email
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "activeUser"));
        Boolean result = userService.activeUser(email);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "activeUser"));
        return ResponseEntity.ok(
                ApiResponse.<Boolean>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(result)
                        .message("Kích hoạt thành công!")
                        .build());
    }

    @PostMapping("/user/deactive/{email}")
    public ResponseEntity<ApiResponse<Boolean>> deactiveUser(
            HttpServletRequest request,
            @PathVariable("email") String email
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "activeUser"));
        Boolean result = userService.deactiveUser(email);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "activeUser"));
        return ResponseEntity.ok(
                ApiResponse.<Boolean>builder()
                        .result(true)
                        .code(HttpStatus.OK.toString())
                        .data(result)
                        .message("Hủy kích hoạt thành công!")
                        .build());
    }
}
