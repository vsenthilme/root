package com.flourish.b2b.api.simulator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flourish.b2b.api.simulator.model.ConsignmentOutbound;
import com.flourish.b2b.api.simulator.model.NewConsignmentOutbound;

@Repository
public interface ConsignmentOutboundRepository extends JpaRepository<ConsignmentOutbound, Long> {

	public List<ConsignmentOutbound> findAll();
	public NewConsignmentOutbound findByShipmentOrderNo(String soNumber);
}