package com.flourish.b2b.api.gateway.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flourish.b2b.api.gateway.model.consignmentoutbound.Order;

@Repository
public interface ConsignmentOutboundRepository extends JpaRepository<Order, Long>{

	public List<Order> findAll();
	public Order findByShipmentOrderNo(String shipmentOrderNo);
}