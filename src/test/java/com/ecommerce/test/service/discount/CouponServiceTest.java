/**
 * 
 */
package com.ecommerce.test.service.discount;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ecommerce.model.category.Category;
import com.ecommerce.model.discount.BaseDiscount;
import com.ecommerce.model.discount.Coupon;
import com.ecommerce.model.enums.DiscountType;
import com.ecommerce.model.product.Product;
import com.ecommerce.service.ShoppingCartOperation;
import com.ecommerce.service.discount.CouponService;

class CouponServiceTest {
	
	private CouponService couponService;
	
	private static ShoppingCartOperation shoppingCartOperation;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Category shoesCategory = new Category("Shoes"); 
		Product nikeProduct = new Product("Nike", 100.0, shoesCategory);
		Product adidasProduct = new Product("Adidas", 50.0, shoesCategory);
		shoppingCartOperation = new ShoppingCartOperation();
		shoppingCartOperation.addItem(nikeProduct, 1);
		shoppingCartOperation.addItem(adidasProduct, 1);
	}

	@BeforeEach
	void setUp() throws Exception {
		couponService = new CouponService();
	}
	
	@Test
	void testApply_when_campaign_discounts_are_null() {
		double appliedCampaignDiscountValue = couponService.apply(shoppingCartOperation.getShoppingCart(), null);
		Assertions.assertEquals(0.0, appliedCampaignDiscountValue);
	}

	@Test
	void test_apply_for_discount_type_rate_and_coupon_limit_matched() {
		List<BaseDiscount> couponDiscount = new ArrayList<>();
		couponDiscount.add(new Coupon(100, 10, DiscountType.RATE));
		double appliedCouponDiscountValue = couponService.apply(shoppingCartOperation.getShoppingCart(), couponDiscount);
		Assertions.assertEquals(15.0, appliedCouponDiscountValue);
	}
	
	@Test
	void test_apply_for_discount_type_rate_and_coupon_limit_not_matched() {
		List<BaseDiscount> couponDiscount = new ArrayList<>();
		couponDiscount.add(new Coupon(200, 10, DiscountType.RATE));
		double appliedCouponDiscountValue = couponService.apply(shoppingCartOperation.getShoppingCart(), couponDiscount);
		Assertions.assertEquals(0.0, appliedCouponDiscountValue);
	}
	
	@Test
	void test_apply_for_discount_type_amount_and_coupon_limit_matched() {
		List<BaseDiscount> couponDiscount = new ArrayList<>();
		couponDiscount.add(new Coupon(100, 5, DiscountType.AMOUNT));
		double appliedCouponDiscountValue = couponService.apply(shoppingCartOperation.getShoppingCart(), couponDiscount);
		Assertions.assertEquals(5, appliedCouponDiscountValue);
	}
	
	@Test
	void test_apply_for_discount_type_amount_and_coupon_limit_not_matched() {
		List<BaseDiscount> couponDiscount = new ArrayList<>();
		couponDiscount.add(new Coupon(200, 10, DiscountType.AMOUNT));
		double appliedCouponDiscountValue = couponService.apply(shoppingCartOperation.getShoppingCart(), couponDiscount);
		Assertions.assertEquals(0.0, appliedCouponDiscountValue);
	}
	
}
