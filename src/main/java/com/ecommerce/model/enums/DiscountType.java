package com.ecommerce.model.enums;

public enum DiscountType {

	RATE, AMOUNT;
	
	public static boolean isRateDiscount(DiscountType discountType) {
		return RATE.equals(discountType);
	}
	
	public static boolean isAmounDiscount(DiscountType discountType) {
		return AMOUNT.equals(discountType);
	}
	
}
