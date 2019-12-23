package com.ecommerce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ecommerce.model.category.Category;
import com.ecommerce.model.discount.BaseDiscount;
import com.ecommerce.model.discount.Campaign;
import com.ecommerce.model.discount.Coupon;
import com.ecommerce.model.enums.DiscountOperationType;
import com.ecommerce.model.product.Product;
import com.ecommerce.model.shopping.ShoppingCart;
import com.ecommerce.model.shopping.ShoppingCartItem;
import com.ecommerce.service.discount.BaseDiscountOperation;
import com.ecommerce.service.discount.factory.DiscountOperationFactory;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
public class ShoppingCartOperation {
	
	public ShoppingCartOperation() {
		this.shoppingCart = new ShoppingCart();
	}

	private ShoppingCart shoppingCart;

	public void addItem(Product product, Integer quantity) {
		Optional<ShoppingCartItem> existingItemOptional = getExistingItem(product);
		if (existingItemOptional.isPresent()) {
			existingItemOptional.get().increaseQuantity(quantity);
		} else {
			getShoppingCart().getCartItems().add(new ShoppingCartItem(product, quantity));
		}
		updateTotalPriceWithoutDiscount();
		log.info(
				"addLineItem to cart. [product title = {}, category = {}, quantity = {}] totalCartPriceWithoutDiscount = {} TL",
				product.getTitle(), product.getCategory().getTitle(), quantity,
				this.shoppingCart.getTotalPriceWithoutDiscount());
	}

	public void removeItem(Product product, Integer quantity) {
		Optional<ShoppingCartItem> existingItemOptional = getExistingItem(product);
		if (existingItemOptional.isPresent()) {
			Integer decreaseQuantity = existingItemOptional.get().decreaseQuantity(quantity);
			if (decreaseQuantity <= 0) {
				removeItem(product);
			}
		}
		updateTotalPriceWithoutDiscount();
		log.info(
				"removeLineItem from cart. [product title = {}, category = {}, quantity = {}] totalCartPriceWithoutDiscount = {} TL",
				product.getTitle(), product.getCategory().getTitle(), quantity,
				this.shoppingCart.getTotalPriceWithoutDiscount());
	}

	public void removeItem(Product product) {
		Optional<ShoppingCartItem> existingItemOptional = getExistingItem(product);
		if (existingItemOptional.isPresent()) {
			getShoppingCart().getCartItems().remove(existingItemOptional.get());
		}
		updateTotalPriceWithoutDiscount();
	}

	public void applyDiscounts(Campaign... campaigns) {
		Optional<BaseDiscountOperation> discountOperationType = findDiscountOperationByOperationType(
				DiscountOperationType.CAMPAIGN);
		if (discountOperationType.isPresent()) {
			Double applyDiscountValue = applyDiscountOperation(discountOperationType.get(), campaigns);
			if (applyDiscountValue > 0) {
				getShoppingCart().setCampaignDiscountPrice(applyDiscountValue);
			}
		}
	}

	public void applyCoupons(Coupon... coupons) {
		Optional<BaseDiscountOperation> discountOperationType = findDiscountOperationByOperationType(
				DiscountOperationType.COUPON);
		if (discountOperationType.isPresent()) {
			Double applyDiscountValue = applyDiscountOperation(discountOperationType.get(), coupons);
			if (applyDiscountValue > 0) {
				getShoppingCart().setCouponDiscountPrice(applyDiscountValue);
			}
		}
	}

	public double calculateDeliveryCost(double costPerDelivery, double costPerProduct, double fixedCost) {
		DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculator(costPerDelivery, costPerProduct,
				fixedCost);
		getShoppingCart().setDeliveryCost(deliveryCostCalculator.calculateFor(getShoppingCart()));
		return getDeliveryCost();
	}

	public double getTotalAmountAfterDiscounts() {
		return getShoppingCart().getTotalPriceWithoutDiscount() - getShoppingCart().getCampaignDiscountPrice()
				- getShoppingCart().getCouponDiscountPrice();
	}

	public double getCampaignDiscount() {
		return getShoppingCart().getCampaignDiscountPrice();
	}

	public double getCouponDiscount() {
		return getShoppingCart().getCouponDiscountPrice();
	}

	public double getDeliveryCost() {
		return getShoppingCart().getDeliveryCost();
	}

	public void print() {

		log.info("CART INFORMATIONS");
		Map<Category, List<ShoppingCartItem>> categoryDeviceMap = new HashMap<>();
		for (ShoppingCartItem cartItem : getShoppingCart().getCartItems()) {
			Category category = cartItem.getProduct().getCategory();
			if (categoryDeviceMap.get(category) == null) {
				categoryDeviceMap.put(category, new ArrayList<>());
			}
			categoryDeviceMap.get(category).add(cartItem);
		}

		categoryDeviceMap.forEach((cat, lines) -> {
			lines.forEach(line -> {
				@NonNull
				Product product = line.getProduct();
				log.info(
						"category name = {}, productName = {}, quantity = {}, unitPrice = {}, totalPrice = {} TL, appliedCampaignDiscount = {} TL, totalPriceWithDiscount = {} TL",
						cat.getTitle(), product.getTitle(), line.getQuantity(), product.getPrice(),
						line.getTotalPriceWithoutDiscount(), line.getAppliedDiscountPrice(),
						(line.getTotalPriceWithoutDiscount() - line.getAppliedDiscountPrice()));
			});
		});

		log.info(
				"basketTotalPriceWithoutDiscount = {} TL, campaignDiscount = {} TL, couponDiscount = {} TL, totalAmountAfterDiscounts = {} TL deliveryCost = {} TL",
				this.shoppingCart.getTotalPriceWithoutDiscount(), getCampaignDiscount(), getCouponDiscount(),
				getTotalAmountAfterDiscounts(), getDeliveryCost());

	}

	private Double applyDiscountOperation(BaseDiscountOperation baseDiscountOperation, BaseDiscount... baseDiscounts) {
		return baseDiscountOperation.apply(getShoppingCart(),
				Stream.of(baseDiscounts).collect(Collectors.toCollection(ArrayList::new)));
	}

	public Optional<BaseDiscountOperation> findDiscountOperationByOperationType(
			DiscountOperationType discountOperationType) {
		Optional<BaseDiscountOperation> discountOperationOptional = DiscountOperationFactory
				.getDiscountOperation(discountOperationType);
		return discountOperationOptional;
	}

	private Optional<ShoppingCartItem> getExistingItem(Product product) {
		return getShoppingCart().getCartItems().stream().filter(ci -> ci.getProduct().equals(product)).findFirst();
	}

	private void updateTotalPriceWithoutDiscount() {
		getShoppingCart().setTotalPriceWithoutDiscount(getShoppingCart().getCartItems().stream()
				.mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum());
	}

}
