package com.matheus.catalog.dto;



import com.matheus.catalog.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductAggregateDTO {
	private String name;
	private Double cost;
	private Double totalCost;
	private Long amount;
	
	
	
	public ProductAggregateDTO(Product entity) {
		this.name = entity.getName();
		this.cost = entity.getSalePrice();
		this.amount = entity.getAmount();
		this.totalCost = this.cost * this.amount;
	}
}
