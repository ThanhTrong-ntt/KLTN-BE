package hcmute.nhom.kltn.common.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Class LoginRequest.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Data
public class LoginRequest {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
}
