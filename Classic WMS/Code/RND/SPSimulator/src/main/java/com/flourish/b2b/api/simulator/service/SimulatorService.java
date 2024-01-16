package com.flourish.b2b.api.simulator.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flourish.b2b.api.simulator.model.ConsignmentOutbound;
import com.flourish.b2b.api.simulator.model.NewConsignmentOutbound;
import com.flourish.b2b.api.simulator.model.NewPickup;
import com.flourish.b2b.api.simulator.model.Pickup;
import com.flourish.b2b.api.simulator.repository.ConsignmentOutboundRepository;
import com.flourish.b2b.api.simulator.repository.PickupRepository;

@Service
public class SimulatorService {
	
	@Autowired
	PickupRepository pickupRepository;
	
	@Autowired
	ConsignmentOutboundRepository conOutboundRepository;
	
	/**
	 * getConsignments
	 * @return
	 */
	public List<Pickup> getConsignments () {
		return pickupRepository.findAll();
	}
	
	/**
	 * getConsignmentByShipmentOrderNo
	 * @param shipmentOrderNo
	 * @return
	 */
	public Pickup getConsignmentByShipmentOrderNo (String shipmentOrderNo) {
		return pickupRepository.findByShipmentOrderNo(shipmentOrderNo);
	}
	
	/**
	 * createConsignment
	 * @param newPickup
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Pickup createConsignment (NewPickup newPickup) 
			throws IllegalAccessException, InvocationTargetException {
		Pickup dbPickup = new Pickup();
		BeanUtils.copyProperties(dbPickup, newPickup);
		return pickupRepository.save(dbPickup);
	}
	
	/**
	 * createConsignmentOutbound
	 * @param newConsignmentOutbound
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ConsignmentOutbound createConsignmentOutbound (NewConsignmentOutbound newConsignmentOutbound) 
			throws IllegalAccessException, InvocationTargetException {
		ConsignmentOutbound dbConsignmentOutbound = new ConsignmentOutbound();
		BeanUtils.copyProperties(dbConsignmentOutbound, newConsignmentOutbound);
		return conOutboundRepository.save(dbConsignmentOutbound);
	}
	
	/**
	 * getConsignmentOutbounds
	 * @return
	 */
	public List<ConsignmentOutbound> getConsignmentOutbounds () {
		return conOutboundRepository.findAll();
	}
}
