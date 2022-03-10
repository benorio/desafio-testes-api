package one.digitalinnovation.racaostock.mapper;

import one.digitalinnovation.racaostock.dto.RacaoDTO;
import one.digitalinnovation.racaostock.entity.Racao;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RacaoMapper {

    RacaoMapper INSTANCE = Mappers.getMapper(RacaoMapper.class);

    Racao toModel(RacaoDTO racaoDTO);

    RacaoDTO toDTO(Racao racao);
}
