package com.ecommerce.model.shopping;

import com.ecommerce.model.common.BaseModel;
import com.ecommerce.model.product.Product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class ShoppingCartItem extends BaseModel {
	
	private static final long serialVersionUID = -2653494200246532860L;

	@NonNull
	private Product product;
	
	@NonNull
	@EqualsAndHashCode.Exclude
	private Integer quantity;
	
	@EqualsAndHashCode.Exclude
	private Double appliedDiscountPrice = 0.0;
	
	public Integer increaseQuantity(Integer quantity) {
		return this.quantity += quantity;
	}
	
	public Integer decreaseQuantity(Integer quantity) {
		return this.quantity -= quantity;
	}
	
	public double getTotalPriceWithoutDiscount() {
		return quantity * product.getPrice();
	}

}
