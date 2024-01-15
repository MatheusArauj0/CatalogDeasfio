package com.matheus.catalog.resources;

import java.net.URI;

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

import com.matheus.catalog.dto.CategoryDTO;
import com.matheus.catalog.services.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/categories", produces = {"application/json"})
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Controlador de Categorias")
public class CategoryResource {
	
	@Autowired
	private CategoryService service;
	
	@Operation(summary = "Realiza a busca paginada de Categorias", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Busca de produtos realizada com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados")
	})
	@GetMapping
	public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable){
		Page<CategoryDTO> list = service.findAllPaged(pageable);
		
		return ResponseEntity.ok().body(list);
	}
	
	@Operation(summary = "Realiza a busca da Catergoria por ID", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Busca de categoria realizada com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados")
	})
	@GetMapping(value="/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
		CategoryDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@Operation(summary = "Inserir Categoria", method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							examples = @ExampleObject("{\r\n"
									+ "    \"name\" : \"Garden\",\r\n"
									+ "    \"active\": true,\r\n"
									+ "    \"tipo\": \"PERSONALIZADO\"\r\n"
									+ "}"))))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Categoria inserida com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "422", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro ao inserir dos dados")
	})
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@Operation(summary = "Atualizar Categoria", method = "PUT",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							examples = @ExampleObject("{\r\n"
									+ "    \"name\" : \"Garden\",\r\n"
									+ "    \"active\": true,\r\n"
									+ "    \"tipo\": \"PERSONALIZADO\"\r\n"
									+ "}"))))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "422", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro ao inserir dos dados")
	})
	@PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto){
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	@Operation(summary = "Deletar Caretgoria", method = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "404", description = "Erro no banco de dados"),
			@ApiResponse(responseCode = "500", description = "Erro ao inserir dos dados")
	})
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
	
}
