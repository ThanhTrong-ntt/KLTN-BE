package hcmute.nhom.kltn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class AddressDTO.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO extends AbstractDTO {
    private String id;
    private String address;
    private String ward;
    private String district;
    private String province;
    private String phone;
    private String email;
    private String lastName;
    private String firstName;
    private String isDefault;
    private UserDTO user;
}
