package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm003;

@Repository
public interface ITForm003Repository extends MongoRepository<ITForm003, String> {
	
	Optional<ITForm003> findById(String id);
}
