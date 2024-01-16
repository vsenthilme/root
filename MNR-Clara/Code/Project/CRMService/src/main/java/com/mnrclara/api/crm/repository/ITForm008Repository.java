package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm008;

@Repository
public interface ITForm008Repository extends MongoRepository<ITForm008, String> {
	
	Optional<ITForm008> findById(String id);
}
