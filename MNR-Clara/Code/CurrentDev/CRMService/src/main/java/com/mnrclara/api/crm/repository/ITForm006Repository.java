package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm006;

@Repository
public interface ITForm006Repository extends MongoRepository<ITForm006, String> {
	
	Optional<ITForm006> findById(String id);
}
