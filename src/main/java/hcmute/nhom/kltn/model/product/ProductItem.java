package hcmute.nhom.kltn.model.product;

import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import hcmute.nhom.kltn.model.AbstractAuditModel;
import hcmute.nhom.kltn.model.Image;

/**
 * Class ProductItem.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Entity
@Table(name = "t_product_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem extends AbstractAuditModel {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "color_id", referencedColumnName = "id")
    private Color color;

    @Column(name = "size")
    private String size;
    @Column(name = "stock")
    private Integer stock;
    @OneToOne
    @JoinColumn(name = "main_image_id")
    private Image mainImage;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
