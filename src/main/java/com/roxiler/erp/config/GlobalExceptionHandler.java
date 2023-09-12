package com.roxiler.erp.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.roxiler.erp.model.ResponseObject;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle specific exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObject> handleException(Exception e) {
        // Handle the exception
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(false);
        responseObject.setMessage(e.getMessage());
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseObject> handleRuntimeException(RuntimeException e) {
        // Handle the runtime exception
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(false);
        responseObject.setMessage(e.getMessage());
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseObject> handleIllegalArgumentException(IllegalArgumentException e) {
        // Handle illegal argument exception
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(false);
        responseObject.setMessage(e.getMessage());
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle HTTP-related exceptions
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseObject> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        // Handle HTTP method not supported exception
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(false);
        responseObject.setMessage(e.getMessage());
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseObject> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        // Handle HTTP media type not supported exception
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(false);
        responseObject.setMessage(e.getMessage());
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle validation-related exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject> handleValidationException(MethodArgumentNotValidException e) {
        // Handle validation exception
        Map<String, Object> exceptions = new HashMap<String, Object>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            exceptions.put(error.getField(), error.getDefaultMessage());
        }
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(false);
        responseObject.setMessage("Bad requests");
        responseObject.setData(exceptions);
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.BAD_REQUEST);

    }

    // Handle security-related exceptions
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseObject> handleAccessDeniedException(AccessDeniedException e) {
        // Handle access denied exception
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(false);
        responseObject.setMessage(e.getMessage());
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle database-related exceptions
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseObject> handleDataAccessException(DataAccessException e) {
        // Handle data access exception
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(false);
        responseObject.setMessage(e.getMessage());
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    // Handle specific Spring Security exceptions
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseObject> handleBadCredentialsException(BadCredentialsException e) {
        // Handle bad credentials exception
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(false);
        responseObject.setMessage(e.getMessage());
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.FORBIDDEN);

    }

    // Handle specific Spring Security exceptions
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseObject> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(false);
        responseObject.setMessage("Data is already exists.");
        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.FORBIDDEN);

    }

}
