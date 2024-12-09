package hcmute.nhom.kltn.model.product;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import hcmute.nhom.kltn.model.AbstractAuditModel;

/**
 * Class Product.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Entity
@Table(name = "t_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AbstractAuditModel {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "free_information")
    private String freeInformation;
    @Column(name = "short_description")
    private String longDescription;
    @Column(name = "long_description")
    private String washingInformation;
    @Column(name = "product_name", nullable = false, unique = true)
    private String productName;
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;
    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;
    @Column(name = "promo_price", nullable = false)
    private BigDecimal promoPrice;
    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Category gender;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id")
    private Sales sales;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Rating> ratings;
    @Column(name = "removal_flag", nullable = false)
    private Boolean removalFlag;
}
