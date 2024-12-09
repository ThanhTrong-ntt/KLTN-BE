package hcmute.nhom.kltn.controller.v1;

import javax.servlet.http.HttpServletRequest;
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
import hcmute.nhom.kltn.common.payload.CreateOrderRequest;
import hcmute.nhom.kltn.dto.PaginationDTO;
import hcmute.nhom.kltn.dto.order.OrderDTO;
import hcmute.nhom.kltn.service.order.OrderService;
import hcmute.nhom.kltn.util.Constants;

/**
 * Class OrderController.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@RestController
public class OrderController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<PaginationDTO<OrderDTO>>> getAllOrder(
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
        logger.info(getMessageStart(request.getRequestURL().toString(), "getAllOrder"));
        PaginationDTO<OrderDTO> orderDTOPage = orderService.getAllOrderPagination(pageNo, pageSize, sortBy, sortDir);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getAllOrder"));
        return ResponseEntity.ok(
                ApiResponse.<PaginationDTO<OrderDTO>>builder()
                        .result(true)
                        .data(orderDTOPage)
                        .message("Get all order successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> searchOrder(
            HttpServletRequest request,
            @PathVariable("id") String id
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "searchOrder"));
        OrderDTO orderDTO = orderService.findById(id);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "searchOrder"));
        return ResponseEntity.ok(
                ApiResponse.<OrderDTO>builder()
                        .result(true)
                        .data(orderDTO)
                        .message("Get order successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @GetMapping("/orders/email/{email}")
    public ResponseEntity<ApiResponse<PaginationDTO<OrderDTO>>> getOrderByUser(
            HttpServletRequest request,
            @PathVariable("email") String email,
            @RequestParam(value = "pageNo", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false)
            int pageNo,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false)
            int pageSize,
            @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false)
            String sortBy,
            @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false)
            String sortDir
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "getOrderByUser"));
        PaginationDTO<OrderDTO> orderDTOPage = orderService.getOrderByUser(email, pageNo, pageSize, sortBy, sortDir);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "getOrderByUser"));
        return ResponseEntity.ok(
                ApiResponse.<PaginationDTO<OrderDTO>>builder()
                        .result(true)
                        .data(orderDTOPage)
                        .message("Get order by user successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @PostMapping("/order/{email}/add")
    public ResponseEntity<ApiResponse<OrderDTO>> addOrder(
            HttpServletRequest request,
            @PathVariable("email") String email,
            @RequestBody CreateOrderRequest orderRequest
            ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "createOrder"));
        OrderDTO orderDTO = orderService.createOrder(orderRequest.getOrder(), orderRequest.getOrderItems());
        logger.info(getMessageEnd(request.getRequestURL().toString(), "createOrder"));
        return ResponseEntity.ok(
                ApiResponse.<OrderDTO>builder()
                        .result(true)
                        .data(orderDTO)
                        .message("Create order successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteOrder(
            HttpServletRequest request,
            @PathVariable("id") String id
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "deleteOrder"));
        orderService.delete(id);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "deleteOrder"));
        return ResponseEntity.ok(
                ApiResponse.<Boolean>builder()
                        .result(true)
                        .data(true)
                        .message("Delete order successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @PutMapping("/order/{id}/paid")
    public ResponseEntity<ApiResponse<OrderDTO>> paidOrder(
            HttpServletRequest request,
            @PathVariable("id") String id
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "paidOrder"));
        OrderDTO orderDTO = orderService.findById(id);
        orderDTO.setIsPaid(true);
        orderService.save(orderDTO);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "paidOrder"));
        return ResponseEntity.ok(
                ApiResponse.<OrderDTO>builder()
                        .result(true)
                        .data(orderDTO)
                        .message("Paid order successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }

    @PutMapping("/order/{id}/status")
    public ResponseEntity<ApiResponse<OrderDTO>> changeStatusOrder(
            HttpServletRequest request,
            @PathVariable("id") String id,
            @RequestParam(value = "status", required = true) String status
    ) {
        logger.info(getMessageStart(request.getRequestURL().toString(), "paidOrder"));
        OrderDTO orderDTO = orderService.findById(id);
        if (status.equalsIgnoreCase("PAID") || status.equalsIgnoreCase("DELIVERED")) {
            orderDTO.setIsPaid(true);
        }
        orderDTO.setStatus(status);
        orderService.save(orderDTO);
        logger.info(getMessageEnd(request.getRequestURL().toString(), "paidOrder"));
        return ResponseEntity.ok(
                ApiResponse.<OrderDTO>builder()
                        .result(true)
                        .data(orderDTO)
                        .message("Update status successfully!")
                        .code(HttpStatus.OK.toString())
                        .build());
    }
}
