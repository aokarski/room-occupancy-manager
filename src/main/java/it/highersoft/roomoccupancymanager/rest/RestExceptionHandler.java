package it.highersoft.roomoccupancymanager.rest;

import it.highersoft.roomoccupancymanager.domain.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidInputException.class})
    public void handleBadRequest(InvalidInputException e) {
        log.error("Invalid input supplied in request: {}", e.getMessage());
    }
}
