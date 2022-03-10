package one.digitalinnovation.racaostock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RacaoStockExceededException extends Exception {

    public RacaoStockExceededException(Long id, int quantityToIncrement) {
        super(String.format("Racaos with %s ID to increment informed exceeds the max stock capacity: %s", id, quantityToIncrement));
    }
}
