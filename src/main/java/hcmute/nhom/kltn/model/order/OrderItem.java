package hcmute.nhom.kltn.model.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import hcmute.nhom.kltn.model.AbstractAuditModel;
import hcmute.nhom.kltn.model.product.ProductItem;

/**
 * Class OrderItem.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Entity
@Table(name = "t_order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends AbstractAuditModel {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "quantity")
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "product_item_id")
    private ProductItem product;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @Column(name = "sub_total")
    private Double subTotal;
}
