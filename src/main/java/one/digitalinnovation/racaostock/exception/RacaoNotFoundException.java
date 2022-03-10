package one.digitalinnovation.racaostock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RacaoNotFoundException extends Exception {

    public RacaoNotFoundException(String racaoName) {
        super(String.format("Racao with name %s not found in the system.", racaoName));
    }

    public RacaoNotFoundException(Long id) {
        super(String.format("Racao with id %s not found in the system.", id));
    }
}
