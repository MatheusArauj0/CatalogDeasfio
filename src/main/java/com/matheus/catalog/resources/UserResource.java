package com.matheus.catalog.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.matheus.catalog.dto.UserDTO;
import com.matheus.catalog.dto.UserInsertDTO;
import com.matheus.catalog.dto.UserUpdateDTO;
import com.matheus.catalog.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/users", produces = {"application/json"})
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Controlador de Users")
public class UserResource {
	
	@Autowired
	private UserService service;
	
	@Operation(summary = "Realiza a busca paginada de Users", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Busca de Users realizada com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "403", description = "Usuário não possui role para acessar a esse recurso"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados")
	})
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable){
		
		Page<UserDTO> list = service.findAllPaged(pageable);
		
		return ResponseEntity.ok().body(list);
	}
	
	@Operation(summary = "Realiza a busca de Users por ID", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Busca de User por ID realizada com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "403", description = "Usuário não possui role para acessar a esse recurso"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados")
	})
	@GetMapping(value="/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id){
		UserDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@Operation(summary = "Inserir User", method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							examples = @ExampleObject("{\r\n"
									+ "  \"firstName\": \"Bob\",\r\n"
									+ "  \"lastName\": \"Brown\",\r\n"
									+ "  \"email\": \"bob@gmail.com\",\r\n"
									+ "  \"password\": \"bob123\",\r\n"
									+ "  \"roles\": [\r\n"
									+ "    {\r\n"
									+ "      \"id\": 1\r\n"
									+ "    },\r\n"
									+ "    {\r\n"
									+ "      \"id\": 2\r\n"
									+ "    }\r\n"
									+ "  ]\r\n"
									+ "}"))))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Usuário inserido com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "403", description = "Usuário não possui role para acessar a esse recurso"),
			@ApiResponse(responseCode = "422", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados")
	})
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto){
		UserDTO  newDto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();
		return ResponseEntity.created(uri).body(newDto);
	}
	
	@Operation(summary = "Atualizar User", method = "PUT",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							examples = @ExampleObject("{\r\n"
									+ "  \"firstName\": \"Bob\",\r\n"
									+ "  \"lastName\": \"Brown\",\r\n"
									+ "  \"email\": \"bob1@gmail.com\",\r\n"
									+ "  \"password\": \"bob123\",\r\n"
									+ "  \"roles\": [\r\n"
									+ "    {\r\n"
									+ "      \"id\": 1\r\n"
									+ "    },\r\n"
									+ "    {\r\n"
									+ "      \"id\": 2\r\n"
									+ "    }\r\n"
									+ "  ]\r\n"
									+ "}"))))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário inserido com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "403", description = "Usuário não possúi role para acessar a esse recurso"),
			@ApiResponse(responseCode = "422", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados")
	})
	@PutMapping(value="/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto){
		UserDTO newDto = service.update(id, dto);
		return ResponseEntity.ok().body(newDto);
	}
	
	@Operation(summary = "Deletar User", method = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Usuário inserido com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "403", description = "Usuário não possui role para acessar a esse recurso"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados")
	})
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
	
}
