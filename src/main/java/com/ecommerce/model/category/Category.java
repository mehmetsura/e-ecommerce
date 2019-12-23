package com.ecommerce.model.category;

import java.util.HashSet;
import java.util.Set;

import com.ecommerce.model.common.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Category extends BaseModel {

	private static final long serialVersionUID = -5238499317194982174L;

	@NonNull
	private String title;
	
	private Category parent;
	
	private Set<Category> children = new HashSet<>();

}
