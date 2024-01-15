package com.matheus.catalog.dto;


import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.matheus.catalog.entities.ConfigFieldsProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfigFieldsProductDTO  implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	@NotNull(message = "Campo requerido")
	private Boolean visible;
	
	
	public ConfigFieldsProductDTO(ConfigFieldsProduct entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.visible = entity.getVisible();
	}
}
