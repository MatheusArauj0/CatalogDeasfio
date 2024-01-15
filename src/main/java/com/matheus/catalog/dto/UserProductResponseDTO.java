package com.matheus.catalog.dto;

import java.io.Serializable;
import javax.validation.constraints.Email;

import com.matheus.catalog.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProductResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@Email(message = "Favor entrar um email válido")
	private String email;

	
	public UserProductResponseDTO(User entity) {
		id = entity.getId();
		email = entity.getEmail();
	}


	
}
