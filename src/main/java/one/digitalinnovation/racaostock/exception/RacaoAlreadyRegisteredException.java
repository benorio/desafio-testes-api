package one.digitalinnovation.racaostock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RacaoAlreadyRegisteredException extends Exception{

    public RacaoAlreadyRegisteredException(String racaoName) {
        super(String.format("Racao with name %s already registered in the system.", racaoName));
    }
}
