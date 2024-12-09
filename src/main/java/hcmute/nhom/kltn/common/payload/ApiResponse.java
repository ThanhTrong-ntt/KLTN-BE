package hcmute.nhom.kltn.common.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Class ApiResponse.
 *
 * @author: ThanhTrong
 **/
@Getter
@Setter
@Builder
public class ApiResponse<T> {
    private Boolean result;
    private String message;
    private String code;
    private T data;
}
