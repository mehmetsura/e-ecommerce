package com.ecommerce.service.discount;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.ecommerce.model.category.Category;
import com.ecommerce.model.discount.BaseDiscount;
import com.ecommerce.model.discount.Campaign;
import com.ecommerce.model.enums.DiscountOperationType;
import com.ecommerce.model.enums.DiscountType;
import com.ecommerce.model.product.Product;
import com.ecommerce.model.shopping.ShoppingCart;
import com.ecommerce.model.shopping.ShoppingCartItem;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CampaignService implements BaseDiscountOperation {

	@Override
	public double apply(ShoppingCart cart, List<BaseDiscount> baseDiscounts) {
		double totalAppliedCartDiscountPrice = 0.0;
		
		if(CollectionUtils.isNotEmpty(baseDiscounts)) {
			Map<Category, List<Campaign>> campaignCategoryMap = baseDiscounts.stream().map(obj -> (Campaign) obj)
					.collect(Collectors.groupingBy(Campaign::getCategory));
			
			for (Map.Entry<Category, List<Campaign>> entry : campaignCategoryMap.entrySet()) {
				Category category = entry.getKey();
				
				for (ShoppingCartItem cartItem : cart.getCartItems()) {
					
					Campaign appliedCampaign = null;
					double appliedDiscountPrice = 0.0;
					
					for (Campaign campaign : entry.getValue()) {
						double discountPrice = 0.0;
						Product product = cartItem.getProduct();
						if (product.getCategory().equals(category)
								&& cartItem.getQuantity() >= campaign.getProductLimit()) {
							DiscountType discountType = campaign.getDiscountType();
							if (DiscountType.isRateDiscount(discountType)) {
								discountPrice = cartItem.getTotalPriceWithoutDiscount() * campaign.getValue() / 100;
							} else if (DiscountType.isAmounDiscount(discountType)) {
								discountPrice = campaign.getValue();
							}
							
							if (appliedCampaign == null || discountPrice > appliedDiscountPrice) {
								appliedCampaign = campaign;
								appliedDiscountPrice = discountPrice;
							}
						}
					}
					
					if (appliedCampaign != null && appliedDiscountPrice > 0) {
						log.info("campaign discount applied for product name = {}, category name = {} with discount info = {}",
								cartItem.getProduct().getTitle(), cartItem.getProduct().getCategory().getTitle(), appliedCampaign);
						cartItem.setAppliedDiscountPrice(appliedDiscountPrice);
						totalAppliedCartDiscountPrice += appliedDiscountPrice;
					}
				}
			}
		}

		return totalAppliedCartDiscountPrice;
	}

	@Override
	public DiscountOperationType getDiscountOperationType() {
		return DiscountOperationType.CAMPAIGN;
	}

}
