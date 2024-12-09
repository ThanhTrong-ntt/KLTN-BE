package hcmute.nhom.kltn.model.product;

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

/**
 * Class Category.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Entity
@Table(name = "t_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends AbstractAuditModel {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;
    /*
    * Level 1: Gender - Xác định nhóm người dùng sản phẩm.
    * Level 2: Category - Xác định nhóm sản phẩm chi tiết hơn.
    * */
    @Column(name = "level", nullable = false)
    private Integer level; // Phân chia level theo cấp độ của danh mục
    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName; // Tên theo tiếng anh
    @Column(name = "locale")
    private String locale; // Tên theo ngôn ngữ
    @ManyToOne
    @JoinColumn(name = "parent_id") // Liên kết với Category cha
    private Category parentCategory; // Danh mục cha
    @Column(name = "removal_flag")
    private Boolean removalFlag;
}
