package nl.hva.backend.exceptions;

public class PreConditionFailedException extends RuntimeException {

    public PreConditionFailedException(String message) {
        super(message);
    }
}
