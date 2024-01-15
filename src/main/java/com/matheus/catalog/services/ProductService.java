package com.matheus.catalog.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matheus.catalog.dto.CategoryDTO;
import com.matheus.catalog.dto.ConfigFieldsProductDTO;
import com.matheus.catalog.dto.ProductAggregateDTO;
import com.matheus.catalog.dto.ProductDTO;
import com.matheus.catalog.dto.ProductResponseDTO;
import com.matheus.catalog.entities.Audit;
import com.matheus.catalog.entities.Category;
import com.matheus.catalog.entities.ConfigFieldsProduct;
import com.matheus.catalog.entities.AuditDetails;
import com.matheus.catalog.entities.Product;
import com.matheus.catalog.entities.enums.Action;
import com.matheus.catalog.repositories.AuditRepository;
import com.matheus.catalog.repositories.CategoryRepository;
import com.matheus.catalog.repositories.ConfigFieldsProductRepository;
import com.matheus.catalog.repositories.AuditDetaisRepository;
import com.matheus.catalog.repositories.ProductRepository;
import com.matheus.catalog.services.exceptions.DatabaseException;
import com.matheus.catalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ConfigFieldsProductRepository configFieldsProductRepository;

	@Autowired
	private AuthService authService;
	
	@Autowired
	private AuditRepository auditRepository;
	
	@Autowired
	private AuditDetaisRepository auditDetaisRepository;
	
	
	@Transactional(readOnly = true)
	public Page<ProductAggregateDTO> findAllAggregatePaged(Pageable pageable) {
		Page<Product> list = repository.findAll(pageable);
		return list.map(x -> new ProductAggregateDTO(x));
	}


	@Transactional(readOnly = true)
	public Page<ProductResponseDTO> findAllPaged(Pageable pageable) {
		Page<Product> list = repository.findAll(pageable);
		if (!authService.validateAdmin()) {
			List<ConfigFieldsProduct> listConfig = configFieldsProductRepository.findAll();
			listConfig.forEach(field -> {
				if (field.getVisible().equals(false)) {
					list.forEach(x -> {
						try {
							String aux = x.getClass().getDeclaredField(field.getName()).getName();
							if (aux.equals(field.getName())) {
								x.setFieldNull(aux);
							}

						} catch (NoSuchFieldException | SecurityException e) {
							e.printStackTrace();
						}
					});
				}
			});
		}

		return list.map(x -> new ProductResponseDTO(x, x.getCategories()));
	}

	@Transactional(readOnly = true)
	public List<ConfigFieldsProductDTO> findAll() {
		List<ConfigFieldsProduct> list = configFieldsProductRepository.findAll();
		List<ConfigFieldsProductDTO> listReturn = new ArrayList<>();
		list.forEach(x -> listReturn.add(new ConfigFieldsProductDTO(x)));
		return listReturn;
	}

	@Transactional(readOnly = true)
	public ProductResponseDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductResponseDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductResponseDTO insert(ProductDTO dto) {
		Product entity = new Product();
		Audit audit = new Audit();
		AuditDetails detailAudit = new AuditDetails();
		copyDtoToEntity(dto, entity);
		entity.setUser(authService.Authentcated());
		audit.setName(entity.getName());
		audit.setAction(Action.INCLUSAO.name());
		audit.setDateAt(Instant.now());
		audit.setUser(authService.Authentcated().getFirstName());
		entity = repository.save(entity);
		audit.setProduct(entity);
		audit = auditRepository.save(audit);
		detailAudit.setAudit(audit);
		detailAudit.setNamePreviousValue(null);
		detailAudit.setNameCurrentValue(dto.getName());
		auditDetaisRepository.save(detailAudit);
		return new ProductResponseDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductResponseDTO update(Long id, ProductDTO dto) {
		try {

			Audit audit = new Audit();
			AuditDetails detailAudit = new AuditDetails();
			Product entity = repository.getOne(id);
			detailAudit.setNamePreviousValue(entity.getName());
			detailAudit.setNameCurrentValue(dto.getName());
			audit.setName(dto.getName());
			audit.setAction(Action.ALTERACAO.name());
			audit.setDateAt(Instant.now());
			audit.setUser(authService.Authentcated().getFirstName());
			entity.setName(dto.getName());
			entity.setActive(dto.getActive());
			entity.setSku(dto.getSku());
			entity.setImgUrl(dto.getImgUrl());
			entity.setSalePrice(dto.getSalePrice());
			entity.setAmount(dto.getAmount());
			entity.setDate(dto.getDate());
			if(authService.validateAdmin()) {
				entity.setIcms(dto.getIcms());
				entity.setCostPrice(dto.getCostPrice());
				
			}
			entity.getCategories().clear();
			for (CategoryDTO catDTO : dto.getCategories()) {
				Category category = categoryRepository.getOne(catDTO.getId());
				entity.getCategories().add(category);
			}

			entity = repository.save(entity);
			audit.setProduct(entity);
			auditRepository.save(audit);
			detailAudit.setAudit(audit);
			auditDetaisRepository.save(detailAudit);
			return new ProductResponseDTO(entity, entity.getCategories());
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	@Transactional
	public ConfigFieldsProductDTO updateFieldsVisible(Long id, ConfigFieldsProductDTO dto) {
		try {
			ConfigFieldsProduct entity = configFieldsProductRepository.getOne(id);
			entity.setVisible(dto.getVisible());
			entity = configFieldsProductRepository.save(entity);
			return new ConfigFieldsProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	@Transactional
	public void delete(Long id) {
		try {
			
			Audit audit = new Audit();
			Product entity = repository.getOne(id);
			AuditDetails detailAudit = new AuditDetails();
			audit.setName(entity.getName());
			audit.setAction(Action.EXCLUSAO.name());
			audit.setDateAt(Instant.now());
			audit.setUser(authService.Authentcated().getFirstName());
			detailAudit.setAudit(audit);
			detailAudit.setNamePreviousValue(entity.getName());
			detailAudit.setNameCurrentValue(null);
			auditRepository.save(audit);
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setActive(dto.getActive());
		entity.setSku(dto.getSku());
		entity.setCostPrice(dto.getCostPrice());
		entity.setImgUrl(dto.getImgUrl());
		entity.setIcms(dto.getIcms());
		entity.setSalePrice(dto.getSalePrice());
		entity.setAmount(dto.getAmount());
		entity.setDate(dto.getDate());

		entity.getCategories().clear();
		for (CategoryDTO catDTO : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDTO.getId());
			entity.getCategories().add(category);
		}
	}
}
