package com.ecommerce.test.service.shopping;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.ecommerce.model.category.Category;
import com.ecommerce.model.discount.Campaign;
import com.ecommerce.model.discount.Coupon;
import com.ecommerce.model.enums.DiscountOperationType;
import com.ecommerce.model.enums.DiscountType;
import com.ecommerce.model.product.Product;
import com.ecommerce.model.shopping.ShoppingCartItem;
import com.ecommerce.service.ShoppingCartOperation;
import com.ecommerce.service.discount.CampaignService;
import com.ecommerce.service.discount.CouponService;

class ShoppingCartOperationTest {
	
	private static ShoppingCartOperation shoppingCartOperation;
	
	@Mock
	CampaignService campaignService;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		shoppingCartOperation = new ShoppingCartOperation();
	}

	@Test
	void test_addItem() {
		Category phonesCategory = new Category("Phones");
		Product product = new Product("Apple XX", 1000.0, phonesCategory);
		shoppingCartOperation.addItem(product, 1);
		Assertions.assertEquals(1, shoppingCartOperation.getShoppingCart().getCartItems().size()); 
	}
	
	@Test
	void test_addItem_then_check_line_info_and_operation() {
		
		String categoryTitle = "Phones";
		String productTitle = "Apple XX";
		double productPrice = 1000.0;
		int quantity = 1;
		
		Category phonesCategory = new Category(categoryTitle);
		Product product = new Product(productTitle, productPrice, phonesCategory);
		shoppingCartOperation.addItem(product, quantity);
		
		Assertions.assertEquals(1, shoppingCartOperation.getShoppingCart().getCartItems().size());
		
		Optional<ShoppingCartItem> optionalItem = shoppingCartOperation.getShoppingCart().getCartItems().stream().findFirst();
		Assertions.assertTrue(optionalItem.isPresent());
		Assertions.assertNotNull(optionalItem.get());
		
		ShoppingCartItem cartItem = optionalItem.get();
		Assertions.assertEquals(quantity, cartItem.getQuantity());
		Assertions.assertEquals(0.0, cartItem.getAppliedDiscountPrice());
		Assertions.assertEquals(productPrice * quantity, cartItem.getTotalPriceWithoutDiscount());
		
		Assertions.assertNotNull(cartItem.getProduct());
		Assertions.assertEquals(productPrice, cartItem.getProduct().getPrice());
		Assertions.assertEquals(productTitle, cartItem.getProduct().getTitle());
		
		Assertions.assertNotNull(cartItem.getProduct().getCategory());
		Assertions.assertEquals(categoryTitle, cartItem.getProduct().getCategory().getTitle());
		
		Assertions.assertEquals(quantity + 1, cartItem.increaseQuantity(1));
		Assertions.assertEquals(1, cartItem.decreaseQuantity(1));
	}
	
	@Test
	void test_removeItem() {
		Category phonesCategory = new Category("Phones");
		Product product = new Product("Apple XX", 1000.0, phonesCategory);
		shoppingCartOperation.addItem(product, 1);
		
		shoppingCartOperation.removeItem(product, 1);
		Assertions.assertTrue(shoppingCartOperation.getShoppingCart().getCartItems().isEmpty());
		
		shoppingCartOperation.addItem(product, 3);
		shoppingCartOperation.removeItem(product, 1);
		Assertions.assertEquals(1, shoppingCartOperation.getShoppingCart().getCartItems().size());
		Assertions.assertEquals(2, shoppingCartOperation.getShoppingCart().getCartItems().stream().findFirst().get().getQuantity());
	}
	
	@Test
	void test_apply_discounts_with_discount_type_rate() {
		ShoppingCartOperation shoppingCartOperation = spy(ShoppingCartOperation.class);
		
		Category phonesCategory = new Category("Phones");
		Product product = new Product("Apple XX", 1000.0, phonesCategory);
		shoppingCartOperation.addItem(product, 3);
		
		Campaign campaign = new Campaign(phonesCategory, 10, 3, DiscountType.RATE);
		when(shoppingCartOperation.findDiscountOperationByOperationType(DiscountOperationType.CAMPAIGN)).thenReturn(Optional.of(new CampaignService()));
		
		shoppingCartOperation.applyDiscounts(campaign);
		
		Assertions.assertEquals(300.0, shoppingCartOperation.getShoppingCart().getCampaignDiscountPrice());
		Assertions.assertEquals(300.0, shoppingCartOperation.getShoppingCart().getCartItems().stream().findFirst().get().getAppliedDiscountPrice());
	}
	
	@Test
	void test_apply_discounts_with_discount_type_amount() {
		ShoppingCartOperation shoppingCartOperation = spy(ShoppingCartOperation.class);
		
		Category phonesCategory = new Category("Phones");
		Product product = new Product("Apple YY", 600.0, phonesCategory);
		shoppingCartOperation.addItem(product, 2);
		
		Campaign campaign = new Campaign(phonesCategory, 100, 2, DiscountType.AMOUNT);
		when(shoppingCartOperation.findDiscountOperationByOperationType(DiscountOperationType.CAMPAIGN)).thenReturn(Optional.of(new CampaignService()));
		
		shoppingCartOperation.applyDiscounts(campaign);
		
		Assertions.assertEquals(100, shoppingCartOperation.getShoppingCart().getCampaignDiscountPrice());
		Assertions.assertEquals(100, shoppingCartOperation.getShoppingCart().getCartItems().stream().findFirst().get().getAppliedDiscountPrice());
	}
	
	@Test
	void test_apply_multiple_discounts() {
		ShoppingCartOperation shoppingCartOperation = spy(ShoppingCartOperation.class);
		
		Category phonesCategory = new Category("Phones");
		Category tvCategory = new Category("TV");
		Product product = new Product("Apple ZZZ", 1000.0, phonesCategory);
		shoppingCartOperation.addItem(product, 3);
		
		Campaign campaign1 = new Campaign(phonesCategory, 10, 3, DiscountType.RATE);
		Campaign campaign2 = new Campaign(phonesCategory, 5, 3, DiscountType.RATE);
		Campaign campaign3 = new Campaign(phonesCategory, 400, 2, DiscountType.AMOUNT);
		Campaign campaign4 = new Campaign(tvCategory, 500, 2, DiscountType.AMOUNT);
		
		when(shoppingCartOperation.findDiscountOperationByOperationType(DiscountOperationType.CAMPAIGN)).thenReturn(Optional.of(new CampaignService()));
		shoppingCartOperation.applyDiscounts(campaign1, campaign2, campaign3, campaign4);
		
		Assertions.assertEquals(400, shoppingCartOperation.getShoppingCart().getCampaignDiscountPrice());
	}
	
	@Test
	void test_apply_discounts_with_discount_type_limit_not_match() {
		ShoppingCartOperation shoppingCartOperation = spy(ShoppingCartOperation.class);
		
		Category phonesCategory = new Category("Phones");
		Product product = new Product("Apple FF", 1000.0, phonesCategory);
		shoppingCartOperation.addItem(product, 1);
		
		Campaign campaign = new Campaign(phonesCategory, 10, 2, DiscountType.RATE);
		when(shoppingCartOperation.findDiscountOperationByOperationType(DiscountOperationType.CAMPAIGN)).thenReturn(Optional.of(new CampaignService()));
		
		shoppingCartOperation.applyDiscounts(campaign);
		
		Assertions.assertEquals(0.0, shoppingCartOperation.getShoppingCart().getCampaignDiscountPrice());
		Assertions.assertEquals(0.0, shoppingCartOperation.getShoppingCart().getCartItems().stream().findFirst().get().getAppliedDiscountPrice());
	}
	
	@Test
	void test_apply_coupons_with_discount_type_rate() {
		ShoppingCartOperation shoppingCartOperation = spy(ShoppingCartOperation.class);
		
		Category phonesCategory = new Category("Phones");
		Product product = new Product("Apple NN", 1000.0, phonesCategory);
		shoppingCartOperation.addItem(product, 3);
		
		Coupon coupon = new Coupon(500, 20, DiscountType.RATE);
		when(shoppingCartOperation.findDiscountOperationByOperationType(DiscountOperationType.COUPON)).thenReturn(Optional.of(new CouponService()));
		
		shoppingCartOperation.applyCoupons(coupon);
		
		Assertions.assertEquals(600.0, shoppingCartOperation.getShoppingCart().getCouponDiscountPrice());
	}
	
	@Test
	void test_apply_coupons_with_discount_type_amount() {
		ShoppingCartOperation shoppingCartOperation = spy(ShoppingCartOperation.class);
		
		Category phonesCategory = new Category("Phones");
		Product product = new Product("Apple PP", 1000.0, phonesCategory);
		shoppingCartOperation.addItem(product, 3);
		
		Coupon coupon = new Coupon(500, 150, DiscountType.AMOUNT);
		when(shoppingCartOperation.findDiscountOperationByOperationType(DiscountOperationType.COUPON)).thenReturn(Optional.of(new CouponService()));
		
		shoppingCartOperation.applyCoupons(coupon);
		
		Assertions.assertEquals(150, shoppingCartOperation.getShoppingCart().getCouponDiscountPrice());
	}
	
	@Test
	void test_apply_multiple_coupons() {
		ShoppingCartOperation shoppingCartOperation = spy(ShoppingCartOperation.class);
		
		Category phonesCategory = new Category("Phones");
		Product product = new Product("Apple LL", 1000.0, phonesCategory);
		shoppingCartOperation.addItem(product, 3);
		
		Coupon coupon1 = new Coupon(500, 150, DiscountType.AMOUNT);
		Coupon coupon2 = new Coupon(1000, 400, DiscountType.AMOUNT);
		Coupon coupon3 = new Coupon(400, 115, DiscountType.AMOUNT);
		when(shoppingCartOperation.findDiscountOperationByOperationType(DiscountOperationType.COUPON)).thenReturn(Optional.of(new CouponService()));
		
		shoppingCartOperation.applyCoupons(coupon1, coupon2, coupon3);
		
		Assertions.assertEquals(115, shoppingCartOperation.getShoppingCart().getCouponDiscountPrice());
	}
	
	@Test
	void test_apply_campaign_and_coupon() {
		ShoppingCartOperation shoppingCartOperation = spy(ShoppingCartOperation.class);
		
		Category phonesCategory = new Category("Phones");
		Product product = new Product("Apple UU", 1000.0, phonesCategory);
		shoppingCartOperation.addItem(product, 3);
		
		Campaign campaign = new Campaign(phonesCategory, 10, 2, DiscountType.RATE);
		Coupon coupon = new Coupon(500, 150, DiscountType.AMOUNT);
		
		when(shoppingCartOperation.findDiscountOperationByOperationType(DiscountOperationType.CAMPAIGN)).thenReturn(Optional.of(new CampaignService()));
		when(shoppingCartOperation.findDiscountOperationByOperationType(DiscountOperationType.COUPON)).thenReturn(Optional.of(new CouponService()));
		
		shoppingCartOperation.applyDiscounts(campaign);
		shoppingCartOperation.applyCoupons(coupon);
		
		Assertions.assertEquals(2550, shoppingCartOperation.getTotalAmountAfterDiscounts());
	}
	
	@Test
	void test_calculateDeliveryCost() {
		Category phonesCategory = new Category("Phones");
		Product product = new Product("Apple XX", 1000.0, phonesCategory);
		shoppingCartOperation.addItem(product, 2);
		
		shoppingCartOperation.calculateDeliveryCost(1, 1, 2);
		Assertions.assertEquals(4, shoppingCartOperation.getDeliveryCost()); 
	}
	
	@Test
	void test_total_prices() {
		ShoppingCartOperation shoppingCartOperation = spy(ShoppingCartOperation.class);
		
		Category phonesCategory = new Category("Phones");
		Product product = new Product("Apple UU", 1000.0, phonesCategory);
		shoppingCartOperation.addItem(product, 3);
		
		Campaign campaign = new Campaign(phonesCategory, 10, 2, DiscountType.RATE);
		Coupon coupon = new Coupon(500, 150, DiscountType.AMOUNT);
		
		when(shoppingCartOperation.findDiscountOperationByOperationType(DiscountOperationType.CAMPAIGN)).thenReturn(Optional.of(new CampaignService()));
		when(shoppingCartOperation.findDiscountOperationByOperationType(DiscountOperationType.COUPON)).thenReturn(Optional.of(new CouponService()));
		
		shoppingCartOperation.applyDiscounts(campaign);
		shoppingCartOperation.applyCoupons(coupon);
		shoppingCartOperation.calculateDeliveryCost(1, 1, 2);
		
		Assertions.assertEquals(300, shoppingCartOperation.getCampaignDiscount());
		Assertions.assertEquals(150, shoppingCartOperation.getCouponDiscount());
		Assertions.assertEquals(2550, shoppingCartOperation.getTotalAmountAfterDiscounts());
		Assertions.assertEquals(4, shoppingCartOperation.getDeliveryCost());
		
	}
	
}
