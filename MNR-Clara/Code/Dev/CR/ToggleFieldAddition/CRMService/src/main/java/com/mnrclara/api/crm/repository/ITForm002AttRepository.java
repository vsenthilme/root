package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm002Att;

@Repository
public interface ITForm002AttRepository extends MongoRepository<ITForm002Att, String> {
	
	Optional<ITForm002Att> findById(String id);
}
