package com.ecommerce.model.shopping;

import java.util.HashSet;
import java.util.Set;

import com.ecommerce.model.common.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ShoppingCart extends BaseModel {

	private static final long serialVersionUID = -3162763697729088258L;

	private Set<ShoppingCartItem> cartItems = new HashSet<>();
	
	private double campaignDiscountPrice;
	
	private double couponDiscountPrice;
	
	public double totalPriceWithoutDiscount;
	
	public double deliveryCost;
	
}
