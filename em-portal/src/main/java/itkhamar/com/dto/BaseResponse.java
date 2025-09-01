package itkhamar.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ResponseEntity<BaseResponse<T>> success(T data) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Success", data));
    }

    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(false, message, null);
    }
}
