package hcmute.nhom.kltn.common.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class ChangePasswordRequest.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}
