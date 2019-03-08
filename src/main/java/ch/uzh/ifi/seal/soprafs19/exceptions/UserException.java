package ch.uzh.ifi.seal.soprafs19.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User Not Found")
public class UserException extends Exception {
    private static final long serialVersionUID = 1L;

    public UserException(String errorMessage) {
        super(errorMessage);
    }
}