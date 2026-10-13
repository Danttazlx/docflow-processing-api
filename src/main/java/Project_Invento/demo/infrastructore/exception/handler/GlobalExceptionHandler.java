package Project_Invento.demo.infrastructore.exception.handler;

import Project_Invento.demo.infrastructore.exception.exceptions.EtlProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice   // annotation para nao usar try/catch
public class GlobalExceptionHandler {

    @ExceptionHandler(EtlProcessingException.class)
    public ResponseEntity<Map<String, Object>> handleEtlProcessingException(
            EtlProcessingException exception) {

        Map<String, Object> body = new HashMap<>();

        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}