package com.matheus.catalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.matheus.catalog.entities.Category;
import com.matheus.catalog.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;

	@Size(min = 5, max = 60, message = "Deve ter entre 5 e 60 caracteres")
	@NotBlank(message = "Campo requerido")
	private String name;

	@NotNull(message = "Campo requerido")
	private Boolean active;

	@NotBlank(message = "Campo requerido")
	private String sku;

	@Positive(message = "Preço de custo deve ser um valor positivo")
	private Double costPrice;

	private String imgUrl;

	@NotBlank(message = "Campo requerido")
	private String icms;

	@Positive(message = "Preço de venda deve ser um valor positivo")
	private Double salePrice;

	@NotNull(message = "Campo requerido")
	private Long amount;

	@PastOrPresent(message = "Data não pode ser futura")
	private Instant date;

	private List<CategoryDTO> categories = new ArrayList<>();

	private UserProductResponseDTO user;

	public ProductResponseDTO(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.active = entity.getActive();
		this.sku = entity.getSku();
		this.costPrice = entity.getCostPrice();
		this.imgUrl = entity.getImgUrl();
		this.icms = entity.getIcms();
		this.salePrice = entity.getSalePrice();
		this.amount = entity.getAmount();
		this.date = entity.getDate();
		this.user = new UserProductResponseDTO(entity.getUser());	
	}

	public ProductResponseDTO(Product entity, Set<Category> categories) {
		this(entity);
		categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
	}

}
