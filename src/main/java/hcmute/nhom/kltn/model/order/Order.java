package hcmute.nhom.kltn.model.order;

import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import hcmute.nhom.kltn.model.AbstractAuditModel;
import hcmute.nhom.kltn.model.Address;
import hcmute.nhom.kltn.model.User;
import hcmute.nhom.kltn.model.product.Coupon;

/**
 * Class Order.
 *
 * @author: ThanhTrong
 **/
@Entity
@Table(name = "t_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends AbstractAuditModel {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "products_count")
    private int productsCount;
    @Column(name = "note")
    private String note;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "addressData", column = @Column(name = "addressData")),
            @AttributeOverride(name = "province", column = @Column(name = "province")),
            @AttributeOverride(name = "district", column = @Column(name = "district")),
            @AttributeOverride(name = "ward", column = @Column(name = "ward")),
            @AttributeOverride(name = "phone", column = @Column(name = "phone")),
            @AttributeOverride(name = "email", column = @Column(name = "email")),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name")),
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name"))
    })
    private Address address;

    @Column(name = "sub_total")
    private Double subTotal;
    @Column(name = "tax")
    private Double tax;
    @Column(name = "total")
    private Double total;
    @Column(name = "status")
    private String status;
    @Column(name = "payment_method")
    private String paymentMethod;
    @OneToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
    @Column(name = "is_paid")
    private Boolean isPaid;
}
