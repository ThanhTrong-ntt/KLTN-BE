package hcmute.nhom.kltn.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import hcmute.nhom.kltn.dto.AbstractDTO;

/**
 * Class CategoryDTO.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO extends AbstractDTO {
    private String id;
    private String categoryName;
    private Integer level;
    private String locale;
    private CategoryDTO parentCategory;

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "id='" + id + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", level=" + level +
                ", locale='" + locale + '\'' +
                ", parentCategory=" + parentCategory +
                '}';
    }
}
