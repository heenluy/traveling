package dev.henriqueluiz.travelling.exception;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.henriqueluiz.travelling.exception.entity.AbstractExceptionBody;
import dev.henriqueluiz.travelling.exception.entity.InvalidTokenException;
import dev.henriqueluiz.travelling.exception.entity.RoleNotFoundException;
import dev.henriqueluiz.travelling.exception.entity.ValidationErrorField;
import dev.henriqueluiz.travelling.exception.entity.ValidationExceptionBody;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Nullable
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
        HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        
        List<ValidationErrorField> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ValidationErrorField(err.getField(), err.getDefaultMessage()))
                .toList();
        
        ValidationExceptionBody body = new ValidationExceptionBody();
        body.setStatus(status.value());
        body.setTitle("Bad Request");
        body.setDetails("One or more fields has an error.");
        body.setFields(fields);

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Nullable
    @ExceptionHandler(value = RoleNotFoundException.class)
    protected ResponseEntity<Object> handleMethodEntityNotFound(RuntimeException ex, WebRequest request) {
        AbstractExceptionBody body = new AbstractExceptionBody();
        body.setStatus(404); 
        body.setTitle("Bad Request");
        body.setDetails("Role not found: " + request.getParameter("role"));      
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Nullable
    @ExceptionHandler(value = UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleMethodUsernameNotFound(RuntimeException ex, WebRequest request) {
        AbstractExceptionBody body = new AbstractExceptionBody();
        body.setStatus(404); 
        body.setTitle("Bad request");
        body.setDetails("Username not found: " + request.getParameter("email"));      
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Nullable
    @ExceptionHandler(value = BadCredentialsException.class)
    protected ResponseEntity<Object> handleMethodBadCrederntials(RuntimeException ex, WebRequest request) {
        AbstractExceptionBody body = new AbstractExceptionBody();
        body.setStatus(401); 
        body.setTitle("Bad credentials");
        body.setDetails("Username or password are not valid");      
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @Nullable
    @ExceptionHandler(value = InvalidTokenException.class)
    protected ResponseEntity<Object> handleMethodInvalidToken(RuntimeException ex, WebRequest request) {
        AbstractExceptionBody body = new AbstractExceptionBody();
        body.setStatus(401); 
        body.setTitle("Bad credentials");
        body.setDetails("Refresh token is not valid");      
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @Nullable
    @ExceptionHandler(value = BadJwtException.class)
    protected ResponseEntity<Object> handleMethodJwtRejected(RuntimeException ex, WebRequest request) {
        AbstractExceptionBody body = new AbstractExceptionBody();
        body.setStatus(401); 
        body.setTitle("JWT rejected");
        body.setDetails("An error occurred while attempting to decode the Jwt");      
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }
}
