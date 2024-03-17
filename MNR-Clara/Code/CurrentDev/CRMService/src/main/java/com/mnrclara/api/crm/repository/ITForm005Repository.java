package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm005;

@Repository
public interface ITForm005Repository extends MongoRepository<ITForm005, String> {
	
	Optional<ITForm005> findById(String id);
}
