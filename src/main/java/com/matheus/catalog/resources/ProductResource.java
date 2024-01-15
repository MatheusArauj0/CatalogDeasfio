package com.matheus.catalog.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.matheus.catalog.dto.ConfigFieldsProductDTO;
import com.matheus.catalog.dto.ProductAggregateDTO;
import com.matheus.catalog.dto.ProductDTO;
import com.matheus.catalog.dto.ProductResponseDTO;
import com.matheus.catalog.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/products", produces = {"application/json"})
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Controlador de Produtos")
public class ProductResource {
	
	@Autowired
	private ProductService service;
	
	@Operation(summary = "Realiza a busca paginada de Produtos agregados", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Busca de produtos realizada com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados")
	})
	@GetMapping("/aggregate-value")
	public ResponseEntity<Page<ProductAggregateDTO>> findAllProductsAggregate(Pageable pageable){
		
		Page<ProductAggregateDTO> list = service.findAllAggregatePaged(pageable);
		
		return ResponseEntity.ok().body(list);
	}
	
	@Operation(summary = "Realiza a busca paginada de Produtos", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Busca de produtos realizada com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados")
	})
	@GetMapping
	public ResponseEntity<Page<ProductResponseDTO>> findAll(Pageable pageable){
		
		Page<ProductResponseDTO> list = service.findAllPaged(pageable);
		
		return ResponseEntity.ok().body(list);
	}
	
	@Operation(summary = "Lista todos os campos da entitade Produto, mostrando se o campo está ativo para vizualização dos operadores ou não", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Busca de campos realizada com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "403", description = "Usuário não possúi role para acessar a esse recurso"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados")
	})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/config")
	public ResponseEntity<List<ConfigFieldsProductDTO>> findAllFields(){
		
		List<ConfigFieldsProductDTO> list= service.findAll();
		
		return ResponseEntity.ok().body(list);
	}
	
	@Operation(summary = "Busca o produto pelo ID", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Busca de produto pelo ID realizada com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados")
	})
	@GetMapping(value="/{id}")
	public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id){
		ProductResponseDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@Operation(summary = "Inserir produto", method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							examples = @ExampleObject("{\r\n"
									+ "    \"name\": \"A produto inserido\",\r\n"
									+ "    \"active\": true,\r\n"
									+ "    \"sku\": \"MACPR\",\r\n"
									+ "    \"costPrice\": 500.0,\r\n"
									+ "    \"imgUrl\": \"url da imagem\",\r\n"
									+ "    \"icms\": \"22%\",\r\n"
									+ "    \"salePrice\": 1250.0,\r\n"
									+ "    \"amount\": 15,\r\n"
									+ "    \"date\": \"2020-07-14T10:00:00Z\",\r\n"
									+ "    \"categories\": [\r\n"
									+ "        {\r\n"
									+ "            \"id\": 1\r\n"
									+ "        }\r\n"
									+ "    ]\r\n"
									+ "}"))))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Produto inserido com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "422", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro ao buscar dados")
			
	})
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResponseDTO> insert(@Valid @RequestBody ProductDTO dto){
		ProductResponseDTO response = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(response);
	}
	
	@Operation(summary = "Atualizar produto", method = "PUT",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							examples = @ExampleObject("{\r\n"
									+ "    \"name\": \"A produto inserido\",\r\n"
									+ "    \"active\": true,\r\n"
									+ "    \"sku\": \"MACPR\",\r\n"
									+ "    \"costPrice\": 500.0,\r\n"
									+ "    \"imgUrl\": \"url da imagem\",\r\n"
									+ "    \"icms\": \"22%\",\r\n"
									+ "    \"salePrice\": 1250.0,\r\n"
									+ "    \"amount\": 15,\r\n"
									+ "    \"date\": \"2020-07-14T10:00:00Z\",\r\n"
									+ "    \"categories\": [\r\n"
									+ "        {\r\n"
									+ "            \"id\": 1\r\n"
									+ "        }\r\n"
									+ "    ]\r\n"
									+ "}"))))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Produto atualizado com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "422", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro ao inserir dados")
			
	})
	@PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto){
		ProductResponseDTO response = service.update(id, dto);
		return ResponseEntity.ok().body(response);
	}
	
	@Operation(summary = "Atualizar campos visiveis do produto para o operador", method = "PATCH",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							examples = @ExampleObject("{\r\n"
									+ "  \"visible\": false\r\n"
									+ "}\r\n"
									+ ""))))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Campo do Produto atualizado com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "403", description = "Usuário não possúi role para acessar a esse recurso"),
			@ApiResponse(responseCode = "422", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro ao atualizar dados")
			
	})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping(value="/config/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ConfigFieldsProductDTO> updateFieldVisible(@PathVariable Long id, @Valid @RequestBody ConfigFieldsProductDTO dto){
		dto = service.updateFieldsVisible(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@Operation(summary = "Deletar produto", method = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "404", description = "Erro no banco de dados"),
			@ApiResponse(responseCode = "500", description = "Erro ao deletar dados")
			
	})
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
	
}
