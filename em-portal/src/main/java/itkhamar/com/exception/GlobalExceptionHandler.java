package itkhamar.com.exception;

import itkhamar.com.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<?>> handleNotFound(NotFoundException ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(BaseResponse.error(ex.getMessage()))
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<?>> handleBadRequest(BadRequestException ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error(ex.getMessage()))
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<BaseResponse<Object>>> handleGeneral(Exception ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(BaseResponse.error("Internal Error: " + ex.getMessage()))
        );
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<?>> handleBadRequest(WebExchangeBindException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        return Mono.just(
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.error("Validation Error", errors))
        );
    }
}
