package com.ecommerce.model.discount;

import com.ecommerce.model.common.BaseModel;
import com.ecommerce.model.enums.DiscountType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseDiscount extends BaseModel {

	private static final long serialVersionUID = -5334618526444190342L;

	protected DiscountType discountType;
	
	protected double value;
	
}
