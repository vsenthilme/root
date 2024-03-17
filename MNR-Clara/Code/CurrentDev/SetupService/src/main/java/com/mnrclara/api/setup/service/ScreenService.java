package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.screen.AddScreen;
import com.mnrclara.api.setup.model.screen.Screen;
import com.mnrclara.api.setup.model.screen.UpdateScreen;
import com.mnrclara.api.setup.repository.ScreenRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScreenService {
	
	@Autowired
	private ScreenRepository screenRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<Screen> getScreens () {
		List<Screen> screenList =  screenRepository.findAll();
		if (screenList != null) {
			screenList = screenList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
			return screenList;
		}
		return screenList;
	}
	
	/**O
	 * getScreen
	 * @param screenId
	 * @return
	 */
	public Screen getScreen (Long screenId) {
		Screen screen = screenRepository.findByScreenId(screenId);
		if (screen != null && screen.getDeletionIndicator() == 0) {
			return screen;
		} else {
			throw new BadRequestException("The given Screen ID : " + screenId + " doesn't exist.");
		}
	}
	
	/**
	 * createScreen
	 * @param newScreen
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Screen createScreen (AddScreen newScreen, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<Screen> screen = 
				screenRepository.findByLanguageIdAndScreenIdAndSubScreenIdAndDeletionIndicator (		
					newScreen.getLanguageId(),
					newScreen.getScreenId(),
					newScreen.getSubScreenId(),
					0L);
		if (!screen.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		Screen dbScreen = new Screen();
		BeanUtils.copyProperties(newScreen, dbScreen);
		dbScreen.setDeletionIndicator(0L);
		dbScreen.setCreatedBy(loginUserID);
		dbScreen.setUpdatedBy(loginUserID);
		dbScreen.setCreatedOn(new Date());
		dbScreen.setUpdatedOn(new Date());
		return screenRepository.save(dbScreen);
	}
	
	/**
	 * updateScreen
	 * @param screenId
	 * @param loginUserId 
	 * @param updateScreen
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Screen updateScreen (Long screenId, String loginUserID, UpdateScreen updateScreen) 
			throws IllegalAccessException, InvocationTargetException {
		Screen dbScreen = getScreen(screenId);
		BeanUtils.copyProperties(updateScreen, dbScreen, CommonUtils.getNullPropertyNames(updateScreen));
		dbScreen.setUpdatedBy(loginUserID);
		dbScreen.setUpdatedOn(new Date());
		return screenRepository.save(dbScreen);
	}
	
	/**
	 * deleteScreen
	 * @param loginUserID 
	 * @param screenCode
	 */
	public void deleteScreen (Long screenModuleId, String loginUserID) {
		Screen screen = getScreen(screenModuleId);
		if ( screen != null) {
			screen.setDeletionIndicator(1L);
			screen.setUpdatedBy(loginUserID);
			screenRepository.save(screen);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + screenModuleId);
		}
	}
}
