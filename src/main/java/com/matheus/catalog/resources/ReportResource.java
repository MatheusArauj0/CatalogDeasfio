package com.matheus.catalog.resources;

import java.io.FileNotFoundException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.matheus.catalog.services.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.JRException;


@RestController
@RequestMapping(value = "/report")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Controlador de Relatórios")
public class ReportResource {
	
	@Autowired
	private ReportService service;
	
	@Operation(summary = "Realiza a montagem de relatório nos formatos: pdf, html, csv e xlsx", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "500", description = "Erro ao gerar relatório")
	})
	@GetMapping(value="{type}")
	public ResponseEntity<byte[]> findAllProductsAggregate(@PathVariable("type") String type) throws FileNotFoundException, JRException, JsonProcessingException{
		byte[] response = service.exportReport(type);
		 HttpHeaders headers = new HttpHeaders();
         headers.setContentType(MediaType.APPLICATION_PDF);
         headers.setContentDispositionFormData("attachment", "products." + type);
		
         return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}
	
}
