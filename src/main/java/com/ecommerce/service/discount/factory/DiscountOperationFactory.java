package com.ecommerce.service.discount.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.ecommerce.model.enums.DiscountOperationType;
import com.ecommerce.service.discount.BaseDiscountOperation;

@Component
public class DiscountOperationFactory {

	private List<BaseDiscountOperation> discountOperations;
	
	public DiscountOperationFactory(List<BaseDiscountOperation> discountOperations) {
		this.discountOperations = discountOperations;
	}

	private static final Map<DiscountOperationType, BaseDiscountOperation> discountOperationMap = new HashMap<>();

	@PostConstruct
	public void init() {
		discountOperations.stream().forEach(op -> discountOperationMap.put(op.getDiscountOperationType(), op));
	}

	public static Optional<BaseDiscountOperation> getDiscountOperation(DiscountOperationType discountOperationType) {
		return Optional.ofNullable(discountOperationMap.get(discountOperationType));
	}

}
