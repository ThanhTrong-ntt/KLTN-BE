package hcmute.nhom.kltn.payment.momo;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import hcmute.nhom.kltn.dto.order.OrderDTO;
import hcmute.nhom.kltn.util.Utilities;

/**
 * Class MomoService.
 *
 * @author: ThanhTrong
 **/
@Service
public class MomoService {

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.redirectUrl}")
    private String redirectUrl;

    @Value("${momo.ipnUrl}")
    private String ipnUrl;

    @Value("${momo.apiUrl}")
    private String apiUrl;

    public String generateSignature(Map<String, String> params) {
        // Xây dựng chữ ký
        StringBuilder rawSignature = new StringBuilder();
        rawSignature.append("accessKey=").append(accessKey)
                .append("&amount=").append(params.get("amount"))
                .append("&extraData=").append(params.get("extraData"))
                .append("&ipnUrl=").append(ipnUrl)
                .append("&orderId=").append(params.get("orderId"))
                .append("&orderInfo=").append(params.get("orderInfo"))
                .append("&partnerCode=").append(partnerCode)
                .append("&redirectUrl=").append(redirectUrl)
                .append("&requestId=").append(params.get("requestId"))
                //.append("&signature=").append(secretKey)
                .append("&requestType=").append(params.get("requestType"));

        System.out.println("Raw signature: " + rawSignature.toString());

        return hmacSHA256(secretKey, rawSignature.toString());
    }

    private String hmacSHA256(String key, String data) {
        try {
            javax.crypto.Mac sha256_HMAC = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec secret_key = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] array = sha256_HMAC.doFinal(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error in creating signature", e);
        }
    }

    public String createMoMoPaymentRequest(OrderDTO orderDTO) {
        // Các tham số giao dịch
        String orderId = orderDTO.getId().replace("-", ":"); // Mã đơn hàng duy nhất
        String amount = Utilities.parseString(orderDTO.getTotal().longValue()); // Số tiền thanh toán (100,000 VND)
        String orderInfo = "Thanh toán cho đơn hàng 123";
        String requestId = String.valueOf(System.currentTimeMillis()); // Mã yêu cầu duy nhất
        String requestType = "payWithCC";
        Map<String, String> params = new HashMap<>();
        params.put("partnerCode", partnerCode);
        params.put("accessKey", accessKey);
        params.put("requestId", requestId);
        params.put("amount", amount);
        params.put("orderId", orderId);
        params.put("orderInfo", orderInfo);
        params.put("redirectUrl", redirectUrl);
        params.put("ipnUrl", ipnUrl);
        params.put("extraData", "");
        params.put("requestType", requestType);
        params.put("lang", "vi");

        // Tính toán chữ ký
        String signature = generateSignature(params);
        params.put("signature", signature);

        // Tạo yêu cầu gửi tới MoMo API
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);

        // Gửi yêu cầu tới MoMo API
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod
                .POST, entity, String.class);
        return response.getBody();
    }
}
