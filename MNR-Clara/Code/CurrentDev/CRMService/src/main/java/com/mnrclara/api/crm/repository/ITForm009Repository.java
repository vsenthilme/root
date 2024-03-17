package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm009;

@Repository
public interface ITForm009Repository extends MongoRepository<ITForm009, String> {
	
	Optional<ITForm009> findById(String id);
}
