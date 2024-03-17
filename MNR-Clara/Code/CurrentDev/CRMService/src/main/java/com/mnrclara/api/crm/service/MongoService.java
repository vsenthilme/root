package com.mnrclara.api.crm.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.crm.model.itform.ITForm000;
import com.mnrclara.api.crm.model.itform.ITForm001;
import com.mnrclara.api.crm.model.itform.ITForm002;
import com.mnrclara.api.crm.model.itform.ITForm003;
import com.mnrclara.api.crm.model.itform.ITForm004;
import com.mnrclara.api.crm.model.itform.ITForm005;
import com.mnrclara.api.crm.model.itform.ITForm006;
import com.mnrclara.api.crm.repository.ITForm000Repository;
import com.mnrclara.api.crm.repository.ITForm001Repository;
import com.mnrclara.api.crm.repository.ITForm002Repository;
import com.mnrclara.api.crm.repository.ITForm003Repository;
import com.mnrclara.api.crm.repository.ITForm004Repository;
import com.mnrclara.api.crm.repository.ITForm005Repository;
import com.mnrclara.api.crm.repository.ITForm006Repository;

@Service
public class MongoService {
	
	@Autowired
	ITForm000Repository itForm000Repository;
	
	@Autowired
	ITForm001Repository itForm001Repository;
	
	@Autowired
	ITForm002Repository itForm002Repository;
	
	@Autowired
	ITForm003Repository itForm003Repository;
	
	@Autowired
	ITForm004Repository itForm004Repository;
	
	@Autowired
	ITForm006Repository itForm006Repository;
	
	@Autowired
	ITForm005Repository itForm005Repository;
	
	public Optional<ITForm000> getITForm000 (String id) {
		return itForm000Repository.findById(id);
	}
	
	public ITForm000 addITForm000(ITForm000 form000) {
		return itForm000Repository.save(form000);
	}
	
	public ITForm001 addITFormA(ITForm001 form001) {
		return itForm001Repository.save(form001);
	}
	
	public ITForm002 addITFormB(ITForm002 form002) {
		return itForm002Repository.save(form002);
	}
	
	public ITForm003 addITForm003(ITForm003 form003) {
		return itForm003Repository.save(form003);
	}
	
	public ITForm004 addITForm004(ITForm004 form004) {
		return itForm004Repository.save(form004);
	}
	
	public ITForm006 addITForm006(ITForm006 form006) {
		return itForm006Repository.save(form006);
	}
	
	public ITForm005 addITForm005(ITForm005 form005) {
		return itForm005Repository.save(form005);
	}
}
