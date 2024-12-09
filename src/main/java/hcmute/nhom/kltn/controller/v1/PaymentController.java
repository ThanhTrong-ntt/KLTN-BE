package hcmute.nhom.kltn.controller.v1;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import hcmute.nhom.kltn.common.payload.ApiResponse;
import hcmute.nhom.kltn.dto.UserDTO;
import hcmute.nhom.kltn.dto.order.OrderDTO;
import hcmute.nhom.kltn.payment.momo.MomoService;
import hcmute.nhom.kltn.payment.vnpay.Config;

/**
 * Class PaymentController.
 *
 * @author: ThanhTrong
 **/
@RestController
public class PaymentController extends AbstractController {

    @Autowired
    private MomoService momoService;

    @PostMapping("/momo")
    public ResponseEntity<ApiResponse<String>> initiateMoMoPayment(
            HttpServletRequest request,
            @RequestBody OrderDTO orderDTO
            ) {
        String paymentUrl = momoService.createMoMoPaymentRequest(orderDTO);
        return ResponseEntity.ok( ApiResponse.<String>builder()
                .result(true)
                .code(HttpStatus.OK.toString())
                .data(paymentUrl)
                .message("Payment url momo!")
                .build());
    }

    @PostMapping("/momo-ipn")
    public ResponseEntity<String> handleMoMoIpn(HttpServletRequest request) {
        // Lấy các tham số từ MoMo gửi về (thông qua request)
        String signature = request.getParameter("signature");
        String orderId = request.getParameter("orderId");
        String paymentStatus = request.getParameter("paymentStatus");

        // Kiểm tra chữ ký và kết quả giao dịch
        // Xử lý kết quả giao dịch ở đây (Cập nhật trạng thái đơn hàng, gửi email thông báo cho người dùng,...)

        return ResponseEntity.ok("OK");
    }


}
