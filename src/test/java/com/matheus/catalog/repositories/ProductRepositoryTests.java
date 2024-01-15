package com.matheus.catalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.matheus.catalog.entities.Product;
import com.matheus.catalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	private long existingId;
	private long nonExistindId;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistindId = 1000L;
		countTotalProducts = 5L;
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {
			Optional<Product> result = repository.findById(nonExistindId);
			Assertions.assertTrue(result.isEmpty());
	
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptinalWhenIdExists() {
		Optional<Product> dto = repository.findById(existingId);
		Assertions.assertTrue(dto.isPresent());
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		Optional<Product> result = repository.findById(product.getId());
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
		Assertions.assertTrue(result.isPresent());
		Assertions.assertSame(result.get(), product);
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {

		repository.deleteById(existingId);
		
		
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
		
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, ()->{
			repository.deleteById(nonExistindId);
		});
	}
	
	@Test
	public void findByIdShouldReturnOptionalNotVoidlWhenIdisNotNull() {
		
		Optional<Product> result = repository.findById(existingId);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnOptionalVoidlWhenIdisNull() {
		
		Optional<Product> result = repository.findById(nonExistindId);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	
}
