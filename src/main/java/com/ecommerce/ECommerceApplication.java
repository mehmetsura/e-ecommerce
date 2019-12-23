package com.ecommerce;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ecommerce.model.category.Category;
import com.ecommerce.model.discount.Campaign;
import com.ecommerce.model.discount.Coupon;
import com.ecommerce.model.enums.DiscountType;
import com.ecommerce.model.product.Product;
import com.ecommerce.service.ShoppingCartOperation;

@SpringBootApplication
public class ECommerceApplication implements CommandLineRunner {
	
	@Override
	public void run(String... args) throws Exception {
		Category foodCategory = new Category("food");
		Category televisionCategory = new Category("electronics");
		Category phoneCategory = new Category("phone");
		
		Product apple = new Product("Apple", 100.0, foodCategory);
		Product almond = new Product("Almonds", 150.0, foodCategory);
		Product lg = new Product("Lg", 75.0, televisionCategory);
		Product samsung = new Product("Samsung", 100.0, phoneCategory);
		
		ShoppingCartOperation cartOperation = new ShoppingCartOperation();
		
		cartOperation.addItem(apple, 3);
		cartOperation.addItem(almond, 1);
		cartOperation.addItem(lg, 1);
		cartOperation.addItem(samsung, 1);
		
		Campaign rateCampaign1 = new Campaign(foodCategory, 20.0, 3, DiscountType.RATE);
		Campaign rateCampaign2 = new Campaign(foodCategory, 40.0, 2, DiscountType.RATE);
		Campaign rateCampaign3 = new Campaign(foodCategory, 50.0, 1, DiscountType.RATE);
		Campaign amountCampaign1 = new Campaign(foodCategory, 5.0, 5, DiscountType.AMOUNT);
		Campaign amountCampaign2 = new Campaign(televisionCategory, 5.0, 1, DiscountType.AMOUNT);
		Coupon coupon = new Coupon(289.0, 10.0, DiscountType.AMOUNT);
		
		cartOperation.applyDiscounts(rateCampaign1, rateCampaign2, rateCampaign3, amountCampaign1, amountCampaign2);
		cartOperation.applyCoupons(coupon);
		cartOperation.calculateDeliveryCost(1.0, 1.0, 2.99);
		cartOperation.print();
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}
	
}
