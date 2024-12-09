package hcmute.nhom.kltn.model;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * Class UserProfile.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Entity
@Table(name = "t_user_profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile extends AbstractAuditModel {

    /**
     * UserProfile entity.
     */

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "birth_date")
    private Date birthDate;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "avatar_id")
    private Image avatar;

    @Column(name = "removal_flag", nullable = false, length = 1)
    private Boolean removalFlag = false;

    @Override
    public String toString() {
        return "UserProfile [id=" + id
                + ", name=" + name
//                + ", avatar=" + avatar
                + ", removalFlag=" + removalFlag + "]";
    }
}
