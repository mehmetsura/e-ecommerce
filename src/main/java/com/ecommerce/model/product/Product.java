package com.ecommerce.model.product;

import com.ecommerce.model.category.Category;
import com.ecommerce.model.common.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Product extends BaseModel {
	
	private static final long serialVersionUID = 7676242199392532044L;

	private String title;
	
	@EqualsAndHashCode.Exclude 
	private double price;
	
	private Category category;

}
