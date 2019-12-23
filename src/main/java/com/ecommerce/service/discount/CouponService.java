package com.ecommerce.service.discount;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.ecommerce.model.discount.BaseDiscount;
import com.ecommerce.model.discount.Coupon;
import com.ecommerce.model.enums.DiscountOperationType;
import com.ecommerce.model.enums.DiscountType;
import com.ecommerce.model.shopping.ShoppingCart;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CouponService implements BaseDiscountOperation {

	@Override
	public double apply(ShoppingCart cart, List<BaseDiscount> baseDiscounts) {
		
		double totalAppliedCartDiscountPrice = 0.0;
		
		if(CollectionUtils.isNotEmpty(baseDiscounts)) {
			Coupon appliedCoupon = null;
			double totalPriceWithAppliedCampaignDiscount = cart.getTotalPriceWithoutDiscount() - cart.getCampaignDiscountPrice();
			
			for (BaseDiscount baseDiscount : baseDiscounts) {
				Coupon coupon = (Coupon) baseDiscount;
				double appliedCouponPrice = 0.0;
				if (coupon.getCouponLimit() <= totalPriceWithAppliedCampaignDiscount) {
					if (DiscountType.isRateDiscount(coupon.getDiscountType())) {
						appliedCouponPrice = totalPriceWithAppliedCampaignDiscount * coupon.getValue() / 100;
					} else if(DiscountType.isAmounDiscount(coupon.getDiscountType())) {
						appliedCouponPrice = coupon.getValue();
					}
				}
				if (totalAppliedCartDiscountPrice == 0.0 || appliedCouponPrice < totalAppliedCartDiscountPrice) {
					totalAppliedCartDiscountPrice = appliedCouponPrice;
					appliedCoupon = coupon;
				}
			}
			
			if (totalAppliedCartDiscountPrice > 0 && appliedCoupon != null) {
				log.info("coupon discount applied for cart = {} with coupon = {}", cart, appliedCoupon);
			}
		}
		
		return totalAppliedCartDiscountPrice;
	}
	
	@Override
	public DiscountOperationType getDiscountOperationType() {
		return DiscountOperationType.COUPON;
	}

}
