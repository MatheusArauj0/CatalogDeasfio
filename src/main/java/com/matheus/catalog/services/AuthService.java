package com.matheus.catalog.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matheus.catalog.entities.User;
import com.matheus.catalog.repositories.UserRepository;
import com.matheus.catalog.services.exceptions.ResourceNotFoundException;
import com.matheus.catalog.services.exceptions.UnauthorizedException;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	
	@Transactional(readOnly = true)
	public User Authentcated() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User obj = userRepository.findByEmail(username);
			if(obj.equals(null)) {
				throw new ResourceNotFoundException("User not found");
			}
			return obj;
		}catch (Exception e) {
			throw new UnauthorizedException("Invalid user");
		}
	}
	
	public boolean validateAdmin() {
		User user = Authentcated();
		if(!user.hasRole("ROLE_ADMIN")){
			return false;
		}else {
			return true;
		}
	}
}
