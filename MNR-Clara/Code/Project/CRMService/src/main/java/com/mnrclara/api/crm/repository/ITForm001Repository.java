package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm001;

@Repository
public interface ITForm001Repository extends MongoRepository<ITForm001, String> {
	
	Optional<ITForm001> findById(String id);
}
