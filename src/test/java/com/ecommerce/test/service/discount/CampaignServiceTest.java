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
import com.ecommerce.model.discount.Campaign;
import com.ecommerce.model.enums.DiscountType;
import com.ecommerce.model.product.Product;
import com.ecommerce.service.ShoppingCartOperation;
import com.ecommerce.service.discount.CampaignService;

class CampaignServiceTest {
	
	private CampaignService campaignService;
	
	private static ShoppingCartOperation shoppingCartOpetation;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Category shoesCategory = new Category("Shoes"); 
		Product nikeProduct = new Product("Nike", 100.0, shoesCategory);
		Product adidasProduct = new Product("Adidas", 50.0, shoesCategory);
		shoppingCartOpetation = new ShoppingCartOperation();
		shoppingCartOpetation.addItem(nikeProduct, 1);
		shoppingCartOpetation.addItem(adidasProduct, 1);
	}

	@BeforeEach
	void setUp() throws Exception {
		campaignService = new CampaignService();
	}
	
	@Test
	void test_apply_when_campaign_discounts_are_null() {
		double appliedCampaignDiscountValue = campaignService.apply(shoppingCartOpetation.getShoppingCart(), null);
		Assertions.assertEquals(0.0, appliedCampaignDiscountValue);
	}

	@Test
	void test_apply_for_discount_type_rate_and_campaign_category_matched() {
		List<BaseDiscount> campaignDiscount = new ArrayList<>();
		Category shoesCategory = new Category("Shoes");
		campaignDiscount.add(new Campaign(shoesCategory, 10.0, 1, DiscountType.RATE));
		double appliedCampaignDiscountValue = campaignService.apply(shoppingCartOpetation.getShoppingCart(), campaignDiscount);
		Assertions.assertEquals(15.0, appliedCampaignDiscountValue);
	}
	
	@Test
	void test_apply_for_discount_type_rate_and_campaign_category_not_matched() {
		List<BaseDiscount> campaignDiscount = new ArrayList<>();
		Category televisionCategory = new Category("Television");
		campaignDiscount.add(new Campaign(televisionCategory, 10.0, 1, DiscountType.RATE));
		double appliedCampaignDiscountValue = campaignService.apply(shoppingCartOpetation.getShoppingCart(), campaignDiscount);
		Assertions.assertEquals(0.0, appliedCampaignDiscountValue);
	}
	
	@Test
	void test_apply_for_discount_type_amount_and_campaign_category_matched() {
		List<BaseDiscount> campaignDiscount = new ArrayList<>();
		Category shoesCategory = new Category("Shoes");
		campaignDiscount.add(new Campaign(shoesCategory, 10.0, 1, DiscountType.AMOUNT));
		double appliedCampaignDiscountValue = campaignService.apply(shoppingCartOpetation.getShoppingCart(), campaignDiscount);
		Assertions.assertEquals(20.0, appliedCampaignDiscountValue);
	}
	
	@Test
	void test_apply_for_discount_type_amount_and_campaign_category_not_matched() {
		List<BaseDiscount> campaignDiscount = new ArrayList<>();
		Category televisionCategory = new Category("Television");
		campaignDiscount.add(new Campaign(televisionCategory, 10.0, 1, DiscountType.RATE));
		double appliedCampaignDiscountValue = campaignService.apply(shoppingCartOpetation.getShoppingCart(), campaignDiscount);
		Assertions.assertEquals(0.0, appliedCampaignDiscountValue);
	}
	
}
