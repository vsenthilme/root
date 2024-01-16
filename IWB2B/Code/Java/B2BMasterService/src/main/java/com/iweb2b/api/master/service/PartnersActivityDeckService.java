package com.iweb2b.api.master.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iweb2b.api.master.model.partnersactivitydeck.AddPartnersActivityDeck;
import com.iweb2b.api.master.model.partnersactivitydeck.PartnersActivityDeck;
import com.iweb2b.api.master.model.partnersactivitydeck.UpdatePartnersActivityDeck;
import com.iweb2b.api.master.repository.PartnersActivityDeckRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PartnersActivityDeckService {
	
	@Autowired
	private PartnersActivityDeckRepository partnersActivityDeckRepository;
	
	public List<PartnersActivityDeck> getPartnersActivityDeck () {
		List<PartnersActivityDeck> partnersActivityDeckList =  partnersActivityDeckRepository.findAll();
		partnersActivityDeckList = partnersActivityDeckList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return partnersActivityDeckList;
	}
	
	/**
	 * getPartnersActivityDeck
	 * @param partnersActivityDeckId
	 * @return
	 */
	public PartnersActivityDeck getPartnersActivityDeck (String partnersActivityDeckId) {
		Optional<PartnersActivityDeck> partnersActivityDeck = 
				partnersActivityDeckRepository.findByPartnerIdAndDeletionIndicator(partnersActivityDeckId, 0L);
		if (partnersActivityDeck.isEmpty()) {
			return null;
		}
		return partnersActivityDeck.get();
	}
	
	/**
	 * createPartnersActivityDeck
	 * @param newPartnersActivityDeck
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PartnersActivityDeck createPartnersActivityDeck (AddPartnersActivityDeck newPartnersActivityDeck, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {

		PartnersActivityDeck dbPartnersActivityDeck = new PartnersActivityDeck();
		BeanUtils.copyProperties(newPartnersActivityDeck, dbPartnersActivityDeck, CommonUtils.getNullPropertyNames(newPartnersActivityDeck));
		dbPartnersActivityDeck.setDeletionIndicator(0L);
		dbPartnersActivityDeck.setCreatedBy(loginUserId);
		dbPartnersActivityDeck.setUpdatedBy(loginUserId);
		dbPartnersActivityDeck.setCreatedOn(new Date());
		dbPartnersActivityDeck.setUpdatedOn(new Date());
		return partnersActivityDeckRepository.save(dbPartnersActivityDeck);
	}
	
	/**
	 * updatePartnersActivityDeck
	 * @param partnersActivityDeckId
	 * @param loginUserId 
	 * @param updatePartnersActivityDeck
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PartnersActivityDeck updatePartnersActivityDeck (String partnerId, String loginUserId, UpdatePartnersActivityDeck updatePartnersActivityDeck)
			throws IllegalAccessException, InvocationTargetException {
		PartnersActivityDeck dbPartnersActivityDeck = getPartnersActivityDeck(partnerId);
		BeanUtils.copyProperties(updatePartnersActivityDeck, dbPartnersActivityDeck, CommonUtils.getNullPropertyNames(updatePartnersActivityDeck));
		dbPartnersActivityDeck.setUpdatedBy(loginUserId);
		dbPartnersActivityDeck.setUpdatedOn(new Date());
		return partnersActivityDeckRepository.save(dbPartnersActivityDeck);
	}
	
	/**
	 * deletePartnersActivityDeck
	 * @param loginUserID 
	 * @param partnersactivitydeckCode
	 */
	public void deletePartnersActivityDeck (String partnersactivitydeckModuleId, String loginUserID) {
		PartnersActivityDeck partnersactivitydeck = getPartnersActivityDeck(partnersactivitydeckModuleId);
		if (partnersactivitydeck != null) {
			partnersactivitydeck.setDeletionIndicator(1L);
			partnersactivitydeck.setUpdatedBy(loginUserID);
			partnersactivitydeck.setUpdatedOn(new Date());
			partnersActivityDeckRepository.save(partnersactivitydeck);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + partnersactivitydeckModuleId);
		}
	}
}
