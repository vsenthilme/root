package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm003Att;

@Repository
public interface ITForm003AttRepository extends MongoRepository<ITForm003Att, String> {
	
	Optional<ITForm003Att> findById(String id);
}
