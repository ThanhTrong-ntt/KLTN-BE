package hcmute.nhom.kltn.model.product;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import hcmute.nhom.kltn.model.AbstractAuditModel;

/**
 * Class Color.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Entity
@Table(name = "t_color")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Color extends AbstractAuditModel {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "display_code", nullable = false)
    private String displayCode;
    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public String toString() {
        return "Color{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", displayCode='" + displayCode + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
