package com.flourish.b2b.api.gateway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flourish.b2b.api.gateway.model.consignmentoutbound.ErrorOrder;

@Repository
public interface ConsignmentOutboundErrorLogRepository extends JpaRepository<ErrorOrder, Long>{

	public List<ErrorOrder> findAll();
	public ErrorOrder findByErrorId(Long errorId);
}