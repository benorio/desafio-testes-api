package one.digitalinnovation.racaostock.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.racaostock.dto.RacaoDTO;
import one.digitalinnovation.racaostock.entity.Racao;
import one.digitalinnovation.racaostock.exception.RacaoAlreadyRegisteredException;
import one.digitalinnovation.racaostock.exception.RacaoNotFoundException;
import one.digitalinnovation.racaostock.exception.RacaoStockExceededException;
import one.digitalinnovation.racaostock.mapper.RacaoMapper;
import one.digitalinnovation.racaostock.repository.RacaoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RacaoService {

    private final RacaoRepository racaoRepository;
    private final RacaoMapper racaoMapper = RacaoMapper.INSTANCE;

    public RacaoDTO createRacao(RacaoDTO racaoDTO) throws RacaoAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(racaoDTO.getName());
        Racao racao = racaoMapper.toModel(racaoDTO);
        Racao savedRacao = racaoRepository.save(racao);
        return racaoMapper.toDTO(savedRacao);
    }

    public RacaoDTO findByName(String name) throws RacaoNotFoundException {
        Racao foundRacao = racaoRepository.findByName(name)
                .orElseThrow(() -> new RacaoNotFoundException(name));
        return racaoMapper.toDTO(foundRacao);
    }

    public List<RacaoDTO> listAll() {
        return racaoRepository.findAll()
                .stream()
                .map(racaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws RacaoNotFoundException {
        verifyIfExists(id);
        racaoRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws RacaoAlreadyRegisteredException {
        Optional<Racao> optSavedRacao = racaoRepository.findByName(name);
        if (optSavedRacao.isPresent()) {
            throw new RacaoAlreadyRegisteredException(name);
        }
    }

    private Racao verifyIfExists(Long id) throws RacaoNotFoundException {
        return racaoRepository.findById(id)
                .orElseThrow(() -> new RacaoNotFoundException(id));
    }

    public RacaoDTO increment(Long id, int quantityToIncrement) throws RacaoNotFoundException, RacaoStockExceededException {
        Racao racaoToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + racaoToIncrementStock.getQuantity();
        if (quantityAfterIncrement <= racaoToIncrementStock.getMax()) {
            racaoToIncrementStock.setQuantity(racaoToIncrementStock.getQuantity() + quantityToIncrement);
            Racao incrementedRacaoStock = racaoRepository.save(racaoToIncrementStock);
            return racaoMapper.toDTO(incrementedRacaoStock);
        }
        throw new RacaoStockExceededException(id, quantityToIncrement);
    }
}
