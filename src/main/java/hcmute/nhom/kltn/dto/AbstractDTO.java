package hcmute.nhom.kltn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class AbstractDTO.
 *
 * @author: ThanhTrong
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbstractDTO extends AbstractNonAuditDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String createdBy;
    //    @JsonSerialize(using = DateHandlerSerialize.class)
    //    @JsonDeserialize(using = DateHandler.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createdDate;
    protected String modifiedBy;
    //    @JsonSerialize(using = DateHandlerSerialize.class)
    //    @JsonDeserialize(using = DateHandler.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date modifiedDate;
}
