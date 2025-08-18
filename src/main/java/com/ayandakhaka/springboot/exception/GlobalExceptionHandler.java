package com.ayandakhaka.springboot.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
	    // collect all field errors
	    List<String> errorMessages = ex.getBindingResult()
	                                   .getFieldErrors()
	                                   .stream()
	                                   .map(error -> error.getField() + ": " + error.getDefaultMessage())
	                                   .toList();

	    String combinedMessage = String.join(", ", errorMessages);

	    ErrorResponse error = new ErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            combinedMessage,
	            LocalDateTime.now()
	    );

	    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
    
    // Handle resource not found (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    // Handle ResponseStatusException (e.g. manual throws)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex) {
        ErrorResponse error = new ErrorResponse(ex.getStatusCode().value(), ex.getReason(), LocalDateTime.now());
        return new ResponseEntity<>(error, ex.getStatusCode());
    }
    
    // Handle all other exceptions (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                "Something went wrong: " + ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
//    @ExceptionHandler(EmployeeNotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handleEmployeeNotFound(EmployeeNotFoundException ex) {
//        Map<String, Object> error = new HashMap<>();
//        error.put("status", HttpStatus.NOT_FOUND.value());
//        error.put("error", "Not Found");
//        error.put("message", ex.getMessage());
//        error.put("timestamp", LocalDateTime.now().toString());
//
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
    
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}