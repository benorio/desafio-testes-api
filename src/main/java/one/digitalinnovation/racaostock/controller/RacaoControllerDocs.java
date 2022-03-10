package one.digitalinnovation.racaostock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import one.digitalinnovation.racaostock.dto.RacaoDTO;
import one.digitalinnovation.racaostock.dto.QuantityDTO;
import one.digitalinnovation.racaostock.exception.RacaoAlreadyRegisteredException;
import one.digitalinnovation.racaostock.exception.RacaoNotFoundException;
import one.digitalinnovation.racaostock.exception.RacaoStockExceededException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Api("Manages racao stock")
public interface RacaoControllerDocs {

    @ApiOperation(value = "Racao creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success racao creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
    })
    RacaoDTO createRacao(RacaoDTO racaoDTO) throws RacaoAlreadyRegisteredException;

    @ApiOperation(value = "Returns racao found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success racao found in the system"),
            @ApiResponse(code = 404, message = "Racao with given name not found.")
    })
    RacaoDTO findByName(@PathVariable String name) throws RacaoNotFoundException;

    @ApiOperation(value = "Returns a list of all racaos registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all racaos registered in the system"),
    })
    List<RacaoDTO> listRacaos();

    @ApiOperation(value = "Delete a racao found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success racao deleted in the system"),
            @ApiResponse(code = 404, message = "Racao with given id not found.")
    })
    void deleteById(@PathVariable Long id) throws RacaoNotFoundException;
}
