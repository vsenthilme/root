package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm004;

@Repository
public interface ITForm004Repository extends MongoRepository<ITForm004, String> {
	
	Optional<ITForm004> findById(String id);
}
