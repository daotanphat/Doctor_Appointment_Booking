package com.dtp.doctor_appointment_booking.exception;

import com.dtp.doctor_appointment_booking.dto.response.MessageResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<MessageResponse> handleResourceNotFoundException(EntityNotFoundException exception) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(messageResponse);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<MessageResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(messageResponse);
    }

    @ExceptionHandler({SlotUnavailableException.class})
    public ResponseEntity<MessageResponse> handleSlotUnavailableException(SlotUnavailableException exception) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(messageResponse);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<MessageResponse> handleException(Exception exception) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(messageResponse);
    }

    @ExceptionHandler({MessagingException.class})
    public ResponseEntity<MessageResponse> handleMessagingException(MessagingException exception) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(messageResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getErrorsMap(errors));
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
