package com.matheus.catalog.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_product")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Boolean active;
	private String sku;
	private Double costPrice;
	private String icms;
	private Double salePrice;
	private Long amount;

	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant date;
	private String imgUrl;

	@ManyToMany
	@JoinTable(name = "tb_product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	Set<Category> categories = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Product(Long id, String name, boolean active, String sku, Double costPrice, String imgUrl, String icms,
			Double salePrice, Long amount, Instant date) {
		this.id = id;
		this.name = name;
		this.active = active;
		this.sku = sku;
		this.costPrice = costPrice;
		this.imgUrl = imgUrl;
		this.icms = icms;
		this.salePrice = salePrice;
		this.amount = amount;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setFieldNull(String field) {
		switch (field) {
		case "name":
			this.setName(null);
			break;
		case "active":
			this.setActive(null);
			break;
		case "sku":
			this.setSku(null);
			break;
		case "costPrice":
			this.setCostPrice(null);
			break;
		case "imgUrl":
			this.setImgUrl(null);
			break;
		case "icms":
			this.setIcms(null);
			break;
		case "salePrice":
			this.setSalePrice(null);
			break;
		case "amount":
			this.setImgUrl(null);
			break;
		case "date":
			this.setDate(null);
			break;
		default:
			break;
		}
	}

}
