package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm004Att;

@Repository
public interface ITForm004AttRepository extends MongoRepository<ITForm004Att, String> {
	
	Optional<ITForm004Att> findById(String id);
}
