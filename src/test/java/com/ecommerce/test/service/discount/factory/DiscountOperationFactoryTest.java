package com.ecommerce.test.service.discount.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.ecommerce.model.enums.DiscountOperationType;
import com.ecommerce.service.discount.BaseDiscountOperation;
import com.ecommerce.service.discount.CampaignService;
import com.ecommerce.service.discount.CouponService;
import com.ecommerce.service.discount.factory.DiscountOperationFactory;

class DiscountOperationFactoryTest {

    private static DiscountOperationFactory discountOperationFactory;
    
    @BeforeAll
    public static void beforeEach() {
    	List<BaseDiscountOperation> discountOperations = new ArrayList<>();
    	discountOperations.add(new CampaignService());
    	discountOperations.add(new CouponService());
    	discountOperationFactory = new DiscountOperationFactory(discountOperations);
    	discountOperationFactory.init();
    }
    
    @Test
    public void test_getDiscountOperation_discount_type_campaign() {
    	Optional<BaseDiscountOperation> discountOperationOptional = discountOperationFactory.getDiscountOperation(DiscountOperationType.CAMPAIGN);
    	Assertions.assertTrue(discountOperationOptional.isPresent());
    	Assertions.assertTrue(discountOperationOptional.get() instanceof CampaignService);
    }
    
    @Test
    public void test_getDiscountOperation_discount_type_coupon() {
    	Optional<BaseDiscountOperation> discountOperationOptional = discountOperationFactory.getDiscountOperation(DiscountOperationType.COUPON);
    	Assertions.assertTrue(discountOperationOptional.isPresent());
    	Assertions.assertTrue(discountOperationOptional.get() instanceof CouponService);
    }
    
    @Test
    public void test_getDiscountOperation_when_discount_type_is_null() {
    	Optional<BaseDiscountOperation> discountOperationOptional = discountOperationFactory.getDiscountOperation(null);
    	Assertions.assertFalse(discountOperationOptional.isPresent());
    }

}
