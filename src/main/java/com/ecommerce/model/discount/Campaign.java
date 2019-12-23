package com.ecommerce.model.discount;

import com.ecommerce.model.category.Category;
import com.ecommerce.model.enums.DiscountType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Campaign extends BaseDiscount {

	private static final long serialVersionUID = -4065373106595508934L;

	private Category category;

	private int productLimit;

	public Campaign(Category category, double value, int productLimit, DiscountType discountType) {
		super(discountType, value);
		this.category = category;
		this.productLimit = productLimit;
	}

}
