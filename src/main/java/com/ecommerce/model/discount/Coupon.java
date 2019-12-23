package com.ecommerce.model.discount;

import com.ecommerce.model.enums.DiscountType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper=true)
public class Coupon extends BaseDiscount {
	
	private static final long serialVersionUID = 8336180566490840759L;

	private double couponLimit;
	
	public Coupon(double couponLimit, double value, DiscountType discountType) {
		super(discountType, value);
		this.couponLimit = couponLimit;
	}
	
}
