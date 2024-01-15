package com.matheus.catalog.resources;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheus.catalog.dto.ProductResponseDTO;
import com.matheus.catalog.tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

	@Autowired
	private MockMvc mockMvc;

	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	@Autowired
	private ObjectMapper objectMapper;

	private String userWithRoleAdmin;
	private String userWithRoleOperator;
	private String password;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 5l;
		userWithRoleAdmin = "maria@gmail.com";
		userWithRoleOperator = "alex@gmail.com";
		password = "123456";
	}

	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
		
		String token = getToken(userWithRoleAdmin);
		ResultActions result = mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
				.header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		result.andExpect(jsonPath("$.content[2].name").value("Rails for Dummies"));
	}
	
	@Test
	public void findAllFieldsShouldReturnListOfFieldsWithRoleAdmin() throws Exception {
		
		String token = getToken(userWithRoleAdmin);
		ResultActions result = mockMvc.perform(get("/products/config")
				.header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.[0].id").exists());
		result.andExpect(jsonPath("$.[0].name").value("name"));
		result.andExpect(jsonPath("$.[1].name").value("active"));
	}
	
	
	@Test
	public void findAllFieldsShouldReturnForbiddenWithRoleOperator() throws Exception {
		
		String token = getToken(userWithRoleOperator);
		ResultActions result = mockMvc.perform(get("/products/config")
				.header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void updateVisibleFieldsShouldReturnFieldUpdatedWithRoleAdmin() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(Factory.createConfigFieldsProductDTO());
		
		String token = getToken(userWithRoleAdmin);
		ResultActions result = mockMvc.perform(patch("/products/config/{id}", existingId).content(jsonBody)
				.header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("name"));
	}
	
	@Test
	public void updateVisibleFieldsShouldReturnFieldUpdatedWithRoleOperator() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(Factory.createConfigFieldsProductDTO());
		
		String token = getToken(userWithRoleOperator);
		ResultActions result = mockMvc.perform(patch("/products/config/{id}", existingId).content(jsonBody)
				.header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isForbidden());
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		ProductResponseDTO productDTO = Factory.createProductDTO();
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		String expectedName = productDTO.getName();
		String expectedSku = productDTO.getSku();
		
		String token = getToken(userWithRoleAdmin);
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId).content(jsonBody).header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());

		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.sku").value(expectedSku));
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ProductResponseDTO productDTO = Factory.createProductDTO();
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		String token = getToken(userWithRoleAdmin);
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId).content(jsonBody).header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void insertShouldReturnProductDTOWhenIdExists() throws Exception {
		ProductResponseDTO productDTO = Factory.createProductDTO();
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		String expectedName = productDTO.getName();
		String expectedSku = productDTO.getSku();
		
		String token = getToken(userWithRoleAdmin);
		ResultActions result = mockMvc.perform(post("/products").content(jsonBody).header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isCreated());

		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.sku").value(expectedSku));
	}
	
	
	@Test
	public void deleteShouldReturNotFoundtWhenIdDoesNotExists() throws Exception {

		
		String token = getToken(userWithRoleAdmin);
		ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId).header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturNoContentWhenIdExists() throws Exception {

		
		String token = getToken(userWithRoleAdmin);
		ResultActions result = mockMvc.perform(delete("/products/{id}", existingId).header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
	}



	private String getToken(String user) throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("username", user);
		params.add("password", password);

		MvcResult auth = mockMvc.perform(MockMvcRequestBuilders.post("/oauth/token").params(params)
				.with(httpBasic("myclientid", "myclientsecret")).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		String response = auth.getResponse().getContentAsString();
		JSONObject jwt = new JSONObject(response);
		String token = jwt.getString("access_token");
		
		return token;
	}

}
