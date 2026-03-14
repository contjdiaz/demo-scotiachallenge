package main.java.com.scotiachallenge.demo.infraestructure.web.exception;

import main.java.com.scotiachallenge.demo.domain.exception.DuplicateIdException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateIdException.class)
    public Mono<ResponseEntity<String>> handleDuplicate(DuplicateIdException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<String>> handleValidation(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Mono.just(ResponseEntity.badRequest().body(mensaje));
    }
}
