package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm011;

@Repository
public interface ITForm011Repository extends MongoRepository<ITForm011, String> {
	
	Optional<ITForm011> findById(String id);
}
