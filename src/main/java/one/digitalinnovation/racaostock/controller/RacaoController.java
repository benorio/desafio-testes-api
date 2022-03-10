package one.digitalinnovation.racaostock.controller;

import lombok.AllArgsConstructor;
import one.digitalinnovation.racaostock.dto.RacaoDTO;
import one.digitalinnovation.racaostock.dto.QuantityDTO;
import one.digitalinnovation.racaostock.exception.RacaoAlreadyRegisteredException;
import one.digitalinnovation.racaostock.exception.RacaoNotFoundException;
import one.digitalinnovation.racaostock.exception.RacaoStockExceededException;
import one.digitalinnovation.racaostock.service.RacaoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/racaos")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RacaoController implements RacaoControllerDocs {

    private final RacaoService racaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RacaoDTO createRacao(@RequestBody @Valid RacaoDTO racaoDTO) throws RacaoAlreadyRegisteredException {
        return racaoService.createRacao(racaoDTO);
    }

    @GetMapping("/{name}")
    public RacaoDTO findByName(@PathVariable String name) throws RacaoNotFoundException {
        return racaoService.findByName(name);
    }

    @GetMapping
    public List<RacaoDTO> listRacaos() {
        return racaoService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws RacaoNotFoundException {
        racaoService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public RacaoDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws RacaoNotFoundException, RacaoStockExceededException {
        return racaoService.increment(id, quantityDTO.getQuantity());
    }
}
