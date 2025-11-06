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

    // 409 - conflitos de integridade
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiError> handleSqlIntegrity(SQLIntegrityConstraintViolationException ex, HttpServletRequest req) {
        return build(req, HttpStatus.CONFLICT, "VIOLACAO_INTEGRIDADE",
                "Operação viola restrições de integridade do banco de dados.");
    }

    // 400 - erro de sintaxe SQL
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ResponseEntity<ApiError> handleSqlSyntax(SQLSyntaxErrorException ex, HttpServletRequest req) {
        return build(req, HttpStatus.BAD_REQUEST, "ERRO_SINTAXE_SQL",
                "Consulta inválida. Verifique a sintaxe da operação.");
    }

    // 422 - validação de DTO
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

    @ExceptionHandler(java.sql.SQLException.class)
    public ResponseEntity<ApiError> handleSqlGeneric(java.sql.SQLException ex, HttpServletRequest req) {
        int vendorCode = ex.getErrorCode();
        String sqlState = ex.getSQLState();
        String msg = ex.getMessage();

        String detalhe = "SQL(" + vendorCode + (sqlState != null ? ", state=" + sqlState : "") + "): " + msg;

        ApiError body = new ApiError(
                java.time.OffsetDateTime.now(),
                req.getRequestURI(),
                org.springframework.http.HttpStatus.BAD_REQUEST.value(),
                org.springframework.http.HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "ERRO_SQL",
                detalhe
        );
        return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(body);
    }


    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex,
            jakarta.servlet.http.HttpServletRequest req) {
        return build(req, HttpStatus.BAD_REQUEST, "JSON_INVALIDO",
                "Corpo da requisição inválido ou tipos incorretos.");
    }

}
