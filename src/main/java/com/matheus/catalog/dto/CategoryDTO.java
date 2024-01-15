package com.matheus.catalog.dto;

import java.io.Serializable;

import com.matheus.catalog.entities.Category;
import com.matheus.catalog.entities.enums.Tipo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private Boolean active;
	private Tipo  tipo;
	
	public CategoryDTO(Category entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.active = entity.getActive();
		this.tipo = entity.getTipo();
	}
	
}
