package com.flourish.b2b.api.simulator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flourish.b2b.api.simulator.model.Pickup;

@Repository
public interface PickupRepository extends JpaRepository<Pickup, Long> {

	public List<Pickup> findAll();
	public Pickup findByShipmentOrderNo(String soNumber);
}