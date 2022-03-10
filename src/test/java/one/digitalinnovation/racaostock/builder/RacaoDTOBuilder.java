package one.digitalinnovation.racaostock.builder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import one.digitalinnovation.racaostock.dto.RacaoDTO;
import one.digitalinnovation.racaostock.enums.RacaoType;

@Builder
public class RacaoDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Fisher";

    @Builder.Default
    private String brand = "Pediggre";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private RacaoType type = RacaoType.Gato;

    public RacaoDTO toBeerDTO() {
        return new RacaoDTO();
    }
}
