package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm010;

@Repository
public interface ITForm010Repository extends MongoRepository<ITForm010, String> {
	
	Optional<ITForm010> findById(String id);
}
