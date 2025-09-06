package org.example.web;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private OffsetDateTime timestamp;
    private String path;
    private int status;
    private String error;     // ex.: "Not Found"
    private String code;      // ex.: "ENTIDADE_NAO_ENCONTRADA"
    private String message;   // mensagem amigável
    private List<FieldError> fieldErrors; // validação de DTO (próximo passo)

    public ApiError() { }

    public ApiError(OffsetDateTime timestamp, String path, int status, String error, String code, String message) {
        this.timestamp = timestamp;
        this.path = path;
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
    }

    // GETTERS/SETTERS
    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<FieldError> getFieldErrors() { return fieldErrors; }
    public void setFieldErrors(List<FieldError> fieldErrors) { this.fieldErrors = fieldErrors; }

    public static class FieldError {
        private String field;
        private String message;

        public FieldError() { }
        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() { return field; }
        public void setField(String field) { this.field = field; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
