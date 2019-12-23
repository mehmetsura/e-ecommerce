package com.ecommerce.service.discount;

import java.util.List;

import com.ecommerce.model.discount.BaseDiscount;
import com.ecommerce.model.enums.DiscountOperationType;
import com.ecommerce.model.shopping.ShoppingCart;

public interface BaseDiscountOperation {

	double apply(ShoppingCart cart, List<BaseDiscount> baseDiscounts);
	
	DiscountOperationType getDiscountOperationType();
	
}
