package nl.hva.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String message) {
        super(message);
    }

    public ResourceNotFound(long id) {
        super(String.format("Couldn't find item with id=%d", id));
    }
}
