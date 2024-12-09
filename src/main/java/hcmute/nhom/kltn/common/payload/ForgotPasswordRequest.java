package hcmute.nhom.kltn.common.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class ForgotPasswordRequest.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {
    private String email;
}
