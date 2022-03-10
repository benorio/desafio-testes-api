package one.digitalinnovation.racaostock.controller;

import one.digitalinnovation.racaostock.builder.RacaoDTOBuilder;
import one.digitalinnovation.racaostock.controller.RacaoController;
import one.digitalinnovation.racaostock.dto.RacaoDTO;
import one.digitalinnovation.racaostock.dto.QuantityDTO;
import one.digitalinnovation.racaostock.exception.RacaoNotFoundException;
import one.digitalinnovation.racaostock.exception.RacaoStockExceededException;
import one.digitalinnovation.racaostock.service.RacaoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static one.digitalinnovation.racaostock.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RacaoControllerTest {

    private static final String racao_API_URL_PATH = "/api/v1/racaos";
    private static final long VALID_racao_ID = 1L;
    private static final long INVALID_racao_ID = 2l;
    private static final String racao_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String racao_API_SUBPATH_DECREMENT_URL = "/decrement";

    private MockMvc mockMvc;

    @Mock
    private RacaoService racaoService;

    @InjectMocks
    private RacaoController racaoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(racaoController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenARacaoIsCreated() throws Exception {
        // given
        RacaoDTO racaoDTO = RacaoDTOBuilder.builder().build().toRacaoDTO();

        // when
        when(racaoService.createRacao(racaoDTO)).thenReturn(racaoDTO);

        // then
        mockMvc.perform(post(racao_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(racaoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(racaoDTO.getName())))
                .andExpect(jsonPath("$.brand", is(racaoDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(racaoDTO.getType().toString())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        RacaoDTO racaoDTO = RacaoDTOBuilder.builder().build().toRacaoDTO();
        racaoDTO.setBrand(null);

        // then
        mockMvc.perform(post(racao_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(racaoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        RacaoDTO racaoDTO = RacaoDTOBuilder.builder().build().toRacaoDTO();

        //when
        when(racaoService.findByName(racaoDTO.getName())).thenReturn(racaoDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(racao_API_URL_PATH + "/" + racaoDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(racaoDTO.getName())))
                .andExpect(jsonPath("$.brand", is(racaoDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(racaoDTO.getType().toString())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        RacaoDTO racaoDTO = RacaoDTOBuilder.builder().build().toRacaoDTO();

        //when
        when(racaoService.findByName(racaoDTO.getName())).thenThrow(RacaoNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(racao_API_URL_PATH + "/" + racaoDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithRacaosIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        RacaoDTO racaoDTO = RacaoDTOBuilder.builder().build().toRacaoDTO();

        //when
        when(racaoService.listAll()).thenReturn(Collections.singletonList(racaoDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(racao_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(racaoDTO.getName())))
                .andExpect(jsonPath("$[0].brand", is(racaoDTO.getBrand())))
                .andExpect(jsonPath("$[0].type", is(racaoDTO.getType().toString())));
    }

    @Test
    void whenGETListWithoutRacaosIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        RacaoDTO racaoDTO = RacaoDTOBuilder.builder().build().toRacaoDTO();

        //when
        when(racaoService.listAll()).thenReturn(Collections.singletonList(racaoDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(racao_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // given
        RacaoDTO racaoDTO = RacaoDTOBuilder.builder().build().toRacaoDTO();

        //when
        doNothing().when(racaoService).deleteById(racaoDTO.getId());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(racao_API_URL_PATH + "/" + racaoDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        //when
        doThrow(RacaoNotFoundException.class).when(racaoService).deleteById(INVALID_racao_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(racao_API_URL_PATH + "/" + INVALID_racao_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(10)
                .build();

        RacaoDTO racaoDTO = RacaoDTOBuilder.builder().build().toRacaoDTO();
        racaoDTO.setQuantity(racaoDTO.getQuantity() + quantityDTO.getQuantity());

        when(racaoService.increment(VALID_racao_ID, quantityDTO.getQuantity())).thenReturn(racaoDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(racao_API_URL_PATH + "/" + VALID_racao_ID + racao_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(racaoDTO.getName())))
                .andExpect(jsonPath("$.brand", is(racaoDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(racaoDTO.getType().toString())))
                .andExpect(jsonPath("$.quantity", is(racaoDTO.getQuantity())));
    }

//    @Test
//    void whenPATCHIsCalledToIncrementGreatherThanMaxThenBadRequestStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(30)
//                .build();
//
//        RacaoDTO racaoDTO = RacaoDTOBuilder.builder().build().toRacaoDTO();
//        racaoDTO.setQuantity(racaoDTO.getQuantity() + quantityDTO.getQuantity());
//
//        when(racaoService.increment(VALID_racao_ID, quantityDTO.getQuantity())).thenThrow(RacaoStockExceededException.class);
//
//        mockMvc.perform(patch(racao_API_URL_PATH + "/" + VALID_racao_ID + racao_API_SUBPATH_INCREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .con(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
//    }

//    @Test
//    void whenPATCHIsCalledWithInvalidRacaoIdToIncrementThenNotFoundStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(30)
//                .build();
//
//        when(racaoService.increment(INVALID_racao_ID, quantityDTO.getQuantity())).thenThrow(RacaoNotFoundException.class);
//        mockMvc.perform(patch(racao_API_URL_PATH + "/" + INVALID_racao_ID + racao_API_SUBPATH_INCREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO)))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void whenPATCHIsCalledToDecrementDiscountThenOKstatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(5)
//                .build();
//
//        RacaoDTO racaoDTO = RacaoDTOBuilder.builder().build().toRacaoDTO();
//        racaoDTO.setQuantity(racaoDTO.getQuantity() + quantityDTO.getQuantity());
//
//        when(racaoService.decrement(VALID_racao_ID, quantityDTO.getQuantity())).thenReturn(racaoDTO);
//
//        mockMvc.perform(patch(racao_API_URL_PATH + "/" + VALID_racao_ID + racao_API_SUBPATH_DECREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is(racaoDTO.getName())))
//                .andExpect(jsonPath("$.brand", is(racaoDTO.getBrand())))
//                .andExpect(jsonPath("$.type", is(racaoDTO.getType().toString())))
//                .andExpect(jsonPath("$.quantity", is(racaoDTO.getQuantity())));
//    }
//
//    @Test
//    void whenPATCHIsCalledToDEcrementLowerThanZeroThenBadRequestStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(60)
//                .build();
//
//        RacaoDTO racaoDTO = RacaoDTOBuilder.builder().build().toRacaoDTO();
//        racaoDTO.setQuantity(racaoDTO.getQuantity() + quantityDTO.getQuantity());
//
//        when(racaoService.decrement(VALID_racao_ID, quantityDTO.getQuantity())).thenThrow(RacaoStockExceededException.class);
//
//        mockMvc.perform(patch(racao_API_URL_PATH + "/" + VALID_racao_ID + racao_API_SUBPATH_DECREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void whenPATCHIsCalledWithInvalidRacaoIdToDecrementThenNotFoundStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(5)
//                .build();
//
//        when(racaoService.decrement(INVALID_racao_ID, quantityDTO.getQuantity())).thenThrow(RacaoNotFoundException.class);
//        mockMvc.perform(patch(racao_API_URL_PATH + "/" + INVALID_racao_ID + racao_API_SUBPATH_DECREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO)))
//                .andExpect(status().isNotFound());
//    }
}
