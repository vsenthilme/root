package com.mnrclara.api.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.potentialclient.ClientGeneral;

@Repository
public interface ClientGeneralRepository extends JpaRepository<ClientGeneral, Long> {
	
	public ClientGeneral findByPotentialClientIdAndClientCategoryId(String potentialClientId, 
			Long clientCategoryId);
	public ClientGeneral findByPotentialClientId(String potentialClientId);
	
	//  Pass the fetched CLIENT_ID in into CLIENTGENERAL table as REF_FIELD_2 and fetch the CLIENT_ID
	public ClientGeneral findByReferenceField2(String referenceField2);
}
