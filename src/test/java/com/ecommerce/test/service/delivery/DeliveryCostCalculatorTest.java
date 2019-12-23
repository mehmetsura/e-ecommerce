package com.ecommerce.test.service.delivery;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.ecommerce.model.category.Category;
import com.ecommerce.model.product.Product;
import com.ecommerce.model.shopping.ShoppingCart;
import com.ecommerce.model.shopping.ShoppingCartItem;
import com.ecommerce.service.DeliveryCostCalculator;

class DeliveryCostCalculatorTest {

	private static final Double COST_PER_DELIVERY = 1.0; 
	private static final Double COST_PER_PRODUCT = 1.0;
	private static final Double FIXED_COST = 1.0;
	
	private static DeliveryCostCalculator deliveryCostCalculator;
	
	private static ShoppingCart shoppingCart;
	
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		deliveryCostCalculator = new DeliveryCostCalculator(COST_PER_DELIVERY, COST_PER_PRODUCT, FIXED_COST);
		
		Category shoesCategory = new Category("Shoes"); 
		Product nikeProduct = new Product("Nike", 100.0, shoesCategory);
		Product adidasProduct = new Product("Adidas", 50.0, shoesCategory);
		shoppingCart = new ShoppingCart();
		shoppingCart.getCartItems().add(new ShoppingCartItem(nikeProduct, 1));
		shoppingCart.getCartItems().add(new ShoppingCartItem(adidasProduct, 2));
	}
	
	@Test
	void test_calculateFor() {
		Double calculatedDeliveryCost = deliveryCostCalculator.calculateFor(shoppingCart);
		Assertions.assertEquals(4, calculatedDeliveryCost);
	}
	
}
