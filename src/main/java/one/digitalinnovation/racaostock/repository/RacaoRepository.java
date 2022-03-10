package one.digitalinnovation.racaostock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import one.digitalinnovation.racaostock.entity.Racao;

import java.util.Optional;

public interface RacaoRepository extends JpaRepository<Racao, Long> {

    Optional<Racao> findByName(String name);
}
