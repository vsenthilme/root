package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm000;

@Repository
public interface ITForm000Repository extends MongoRepository<ITForm000, String> {
	
	Optional<ITForm000> findById(String id);
}
