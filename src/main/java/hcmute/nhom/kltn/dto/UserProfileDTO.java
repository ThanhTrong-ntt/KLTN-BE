package hcmute.nhom.kltn.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class UserProfileDTO.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO extends AbstractDTO {
    private String id;
    private String name;
    private Date birthDate;
    private ImageDTO avatar;
    private Boolean removalFlag;

    @Override
    public String toString() {
        return "UserProfileDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", removalFlag=" + removalFlag +
                '}';
    }
}
