package hcmute.nhom.kltn.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * Class Role.
 *
 * @author: ThanhTrong
 **/
@Entity
@Table(name = "t_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AbstractAuditModel {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "role_name", unique = true, nullable = false)
    private String roleName;

    @Column(name = "admin_flag")
    private Boolean adminFlag;

    @Column(name = "removal_flag", nullable = false, length = 1)
    private Boolean removalFlag = false;

    @Override
    public String toString() {
        return "Role [id=" + id
                + ", roleName=" + roleName
                + ", adminFlag=" + adminFlag
                + ", removalFlag=" + removalFlag + "]";
    }
}
