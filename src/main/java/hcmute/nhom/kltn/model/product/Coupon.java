package hcmute.nhom.kltn.model.product;

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
import hcmute.nhom.kltn.model.AbstractAuditModel;

/**
 * Class Coupon.
 *
 * @author: ThanhTrong
 **/
@Entity
@Table(name = "t_coupon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon extends AbstractAuditModel {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "discount", nullable = false)
    private double discount;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private String startDate;

    @Column(name = "end_date", nullable = false)
    private String endDate;

    @Column(name = "removal_flag", nullable = false)
    private boolean removalFlag;
}
