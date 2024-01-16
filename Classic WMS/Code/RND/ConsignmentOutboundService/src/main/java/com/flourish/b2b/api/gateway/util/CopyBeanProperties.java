package com.flourish.b2b.api.gateway.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.flourish.b2b.api.gateway.model.consignmentoutbound.NewOrder;
import com.flourish.b2b.api.gateway.model.consignmentoutbound.Order;

public class CopyBeanProperties {

	/**
	 * copyProperties
	 * @param order
	 * @param newOrder
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Order copyProperties(Order order, NewOrder newOrder) 
			throws IllegalAccessException, InvocationTargetException {
		BeanUtils.copyProperties(order, newOrder);
		return order;
	}
}
