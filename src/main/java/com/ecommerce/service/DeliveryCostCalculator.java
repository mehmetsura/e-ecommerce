package com.ecommerce.service;

import com.ecommerce.model.product.Product;
import com.ecommerce.model.shopping.ShoppingCart;
import com.ecommerce.model.shopping.ShoppingCartItem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryCostCalculator {
	
	private Double costPerDelivery;
	
	private Double costPerProduct;
	
	private Double fixedCost;
	
    private Long calculateNumberOfDeliveries(ShoppingCart cart) {
       return cart.getCartItems().stream().map(ShoppingCartItem::getProduct).map(Product::getCategory).distinct().count();
    }
    
    private Long calculateNumberOfProduct(ShoppingCart cart) {
    	return cart.getCartItems().stream().map(ShoppingCartItem::getProduct).distinct().count();
    }

    public Double calculateFor(ShoppingCart cart) {
    	return (costPerDelivery * calculateNumberOfDeliveries(cart)) + (costPerProduct * calculateNumberOfProduct(cart)) + fixedCost;
    }
	
}
