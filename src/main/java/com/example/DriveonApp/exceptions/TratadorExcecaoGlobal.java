package com.example.DriveonApp.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TratadorExcecaoGlobal {

    @ExceptionHandler(RecursoNaoEncontrado.class)
    public ResponseEntity<DetalhesErro> handleRecursoNaoEncontradoException(RecursoNaoEncontrado ex, WebRequest request) {
        DetalhesErro detalhesErro = new DetalhesErro(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(detalhesErro, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<DetalhesErro> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        String mensagem = "Violação de integridade de dados.";
        Throwable root = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause() : ex;
        String rootMsg = root.getMessage() != null ? root.getMessage().toLowerCase() : "";
        if (rootMsg.contains("cpf")) {
            mensagem = "CPF já cadastrado.";
        } else if (rootMsg.contains("placa")) {
            mensagem = "Placa já cadastrada.";
        }

        DetalhesErro detalhesErro = new DetalhesErro(
                LocalDateTime.now(),
                mensagem,
                request.getDescription(false),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(detalhesErro, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DetalhesErro> handleGlobalException(Exception ex, WebRequest request) {
        // Log do erro para debug
        ex.printStackTrace();
        
        DetalhesErro detalhesErro = new DetalhesErro(
                LocalDateTime.now(),
                "Ocorreu um erro interno no servidor: " + ex.getMessage(),
                request.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(detalhesErro, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public record DetalhesErro(
            LocalDateTime timestamp,
            String mensagem,
            String detalhes,
            int status
    ) {}
}
