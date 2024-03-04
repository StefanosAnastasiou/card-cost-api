package com.etraveli.cardcostapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ClearingCostExceptionController {
    @ExceptionHandler(value = IllegalCardLengthException.class)
    public ResponseEntity<Object> exception(IllegalCardLengthException exception) {
        return new ResponseEntity<>("Illegal number of digits", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalCardFormatException.class)
    public ResponseEntity<Object> exception(IllegalCardFormatException exception) {
        return new ResponseEntity<>("Illegal card format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CountryExistsException.class)
    public ResponseEntity<Object> exception(CountryExistsException exception) {
        return new ResponseEntity<>("Country already exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CountryNotExistsException.class)
    public ResponseEntity<Object> exception(CountryNotExistsException exception) {
        return new ResponseEntity<>("Country does not exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DeleteDefaultValueException.class)
    public ResponseEntity<Object> exception(DeleteDefaultValueException exception) {
        return new ResponseEntity<>("Cannot delete default value", HttpStatus.BAD_REQUEST);
    }
}
