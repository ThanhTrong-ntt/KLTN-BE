package hcmute.nhom.kltn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class RoleDTO.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO extends AbstractDTO {
    private String id;
    private String roleName;
    private Boolean adminFlag;
    private Boolean removalFlag;

    @Override
    public String toString() {
        return "RoleDTO [id=" + id + ", roleName=" + roleName + ", adminFlag=" + adminFlag
                + ", removalFlag=" + removalFlag + "]";
    }
}
