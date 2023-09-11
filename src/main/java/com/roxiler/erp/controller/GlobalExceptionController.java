// package com.roxiler.erp.controller;

// import com.roxiler.erp.model.ResponseObject;
// import jakarta.persistence.EntityExistsException;
// import jakarta.persistence.EntityNotFoundException;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.AuthorizationServiceException;
// import org.springframework.validation.FieldError;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
// import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// import java.sql.SQLException;
// import java.util.HashMap;
// import java.util.Map;

// @Slf4j
// @RestControllerAdvice
// public class GlobalExceptionController {
//     @ResponseStatus(HttpStatus.BAD_REQUEST)
//     @ExceptionHandler({
//             MethodArgumentNotValidException.class,
// //            AuthorizationServiceException.class,
// //            UnknownError.class,
// //            EntityExistsException.class,
// //            EntityNotFoundException.class,
// //            SQLException.class
//     })
//     public Map<String, String> handleValidationExceptions(
//             MethodArgumentNotValidException ex) {
//         Map<String, String> errors = new HashMap<>();
//         ex.getBindingResult().getAllErrors().forEach((error) -> {
//             String fieldName = ((FieldError) error).getField();
//             String errorMessage = error.getDefaultMessage();
//             errors.put(fieldName, errorMessage);
//         });
//         return errors;
//     }

//     @ExceptionHandler({
//             AuthorizationServiceException.class,
//     })
//     public ResponseEntity<ResponseObject> handleUnauthorizedExceptions(
//             AuthorizationServiceException ex) {

//         ResponseObject responseObject = new ResponseObject();
//         responseObject.setIs_success(false);
//         responseObject.setMessage(ex.getMessage());
//         ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);

//         return response;
//     }

//     //    @ResponseStatus(HttpStatus.BAD_REQUEST)
//     @ExceptionHandler({
//             EntityExistsException.class,
//     })
//     public ResponseEntity<ResponseObject> handleEntityExistsExceptions(
//             EntityExistsException ex) {

//         ResponseObject responseObject = new ResponseObject();
//         responseObject.setIs_success(false);
//         responseObject.setMessage(ex.getMessage());
//         ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);

//         return response;
//     }

//     @ExceptionHandler({
//             SQLException.class,
//     })
//     public ResponseEntity<ResponseObject> handleSQLExceptions(
//             SQLException ex) {

//         ResponseObject responseObject = new ResponseObject();
//         responseObject.setIs_success(false);
//         responseObject.setMessage(ex.getMessage().split("Detail")[1]);
//         ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);

//         return response;
//     }

//     @ExceptionHandler({
//             UnknownError.class,
//     })
//     public ResponseEntity<ResponseObject> handleValidationExceptions(
//             UnknownError ex) {

//         ResponseObject responseObject = new ResponseObject();
//         responseObject.setIs_success(false);
//         responseObject.setMessage(ex.getMessage());
//         ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);

//         return response;
//     }

//     @ExceptionHandler({
//             EntityNotFoundException.class,
//     })
//     public ResponseEntity<ResponseObject> handleEntityNotFoundExceptions(
//             EntityNotFoundException ex) {

//         ResponseObject responseObject = new ResponseObject();
//         responseObject.setIs_success(false);
//         responseObject.setMessage(ex.getMessage());
//         ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);

//         return response;
//     }
// }
