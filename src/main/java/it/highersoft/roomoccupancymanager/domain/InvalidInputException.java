package it.highersoft.roomoccupancymanager.domain;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}
