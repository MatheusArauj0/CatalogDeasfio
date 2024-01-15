package com.matheus.catalog.tests;


import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.matheus.catalog.dto.ProductResponseDTO;
import com.matheus.catalog.entities.Audit;
import com.matheus.catalog.entities.Category;
import com.matheus.catalog.entities.Product;
import com.matheus.catalog.entities.User;

public class Factory {
	
	public static Product createProduct() {
		Product product = new Product(1L,"Phone", true, "IPH", 800.0,"https://img.com/img.png", "17%", 2000.0, 15L , Instant.parse("2020-10-20T03:00:00Z"));
		product.getCategories().add(new Category(2L,"Electronics"));
		product.setUser(new User(1L, "maria", "green", "maria@gmail.com", "123456"));
		return product;
	}
	
	public static ProductResponseDTO createProductDTO() {
		Product product = createProduct();
		return new ProductResponseDTO(product, product.getCategories());
	}
	
	public static Audit createAudit() {
		Audit audit = new Audit(1L, Instant.now(), "product A", "DELETE", "Maria", createProduct());
		return audit;
	} 
	
	public static Map<String, Boolean> createConfigFieldsProductDTO() {
		Map<String, Boolean> fields = new HashMap<>();
		fields.put("visible", false);
		return fields;
	}
	
}
