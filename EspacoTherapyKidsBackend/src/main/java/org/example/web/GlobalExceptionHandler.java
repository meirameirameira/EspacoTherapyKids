package org.example.web;

import  org.example.exception.EntidadeNaoEncontradaException;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException; // opcional (casos de sintaxe SQL)
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 404 - entidades não encontradas
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<ApiError> handleNotFound(EntidadeNaoEncontradaException ex, HttpServletRequest req) {
        return build(req, HttpStatus.NOT_FOUND, "ENTIDADE_NAO_ENCONTRADA", ex.getMessage());
    }

    // 409 - conflitos de integridade (FK/UK etc.) - só Java SQL
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiError> handleSqlIntegrity(SQLIntegrityConstraintViolationException ex, HttpServletRequest req) {
        return build(req, HttpStatus.CONFLICT, "VIOLACAO_INTEGRIDADE",
                "Operação viola restrições de integridade do banco de dados.");
    }

    // 400 - erro de sintaxe SQL (opcional, útil durante dev com JDBC puro)
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ResponseEntity<ApiError> handleSqlSyntax(SQLSyntaxErrorException ex, HttpServletRequest req) {
        return build(req, HttpStatus.BAD_REQUEST, "ERRO_SINTAXE_SQL",
                "Consulta inválida. Verifique a sintaxe da operação.");
    }

    // 422 - validação de DTO (vamos aproveitar no próximo passo)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        ApiError body = new ApiError(
                OffsetDateTime.now(),
                req.getRequestURI(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                "ERRO_VALIDACAO",
                "Um ou mais campos estão inválidos."
        );

        var fields = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ApiError.FieldError(err.getField(), err.getDefaultMessage()))
                .collect(Collectors.toList());

        body.setFieldErrors(fields);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    // 500 - fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        return build(req, HttpStatus.INTERNAL_SERVER_ERROR, "ERRO_INTERNO",
                "Ocorreu um erro inesperado. Tente novamente.");
    }

    private ResponseEntity<ApiError> build(HttpServletRequest req, HttpStatus status, String code, String message) {
        ApiError body = new ApiError(
                OffsetDateTime.now(),
                req.getRequestURI(),
                status.value(),
                status.getReasonPhrase(),
                code,
                message
        );
        return ResponseEntity.status(status).body(body);
    }
}
