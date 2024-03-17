package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.model.qb.QBSync;
import com.mnrclara.api.management.model.qb.SearchQbSync;
import com.mnrclara.api.management.repository.QbSyncRepository;
import com.mnrclara.api.management.repository.specification.QbSyncSpecification;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QbSyncService {

	@Autowired
	private QbSyncRepository qbSyncRepository;

	/**
	 * getQBSyncs
	 * 
	 * @return
	 */
	public List<QBSync> getQBSyncs() {
		List<QBSync> qbSyncList = qbSyncRepository.findAll();
		return qbSyncList;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public QBSync getQBSync (String id) {
		QBSync billingMode = qbSyncRepository.findById(id);
		return billingMode;
	}
	
	/**
	 * 
	 * @param newQBSync
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public QBSync createQBSync (QBSync newQBSync) throws IllegalAccessException, InvocationTargetException {
		newQBSync.setCreatedOn(new Date());
		return qbSyncRepository.save(newQBSync);
	}
	
	/**
	 * 
	 * @param id
	 * @param updateQBSync
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public QBSync updateQBSync (String id, QBSync updateQBSync) 
			throws IllegalAccessException, InvocationTargetException {
		QBSync dbQBSync = getQBSync(id);
		BeanUtils.copyProperties(updateQBSync, dbQBSync, CommonUtils.getNullPropertyNames(updateQBSync));
		dbQBSync.setUpdatedOn(new Date());
		return qbSyncRepository.save(dbQBSync);
	}

	/**
	 * 
	 * @param searchQbSync
	 * @return
	 * @throws ParseException
	 */
	public List<QBSync> findQbSync(SearchQbSync searchQbSync) throws ParseException {
		if (searchQbSync.getStartCreatedOn() != null && searchQbSync.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchQbSync.getStartCreatedOn(),
					searchQbSync.getEndCreatedOn());
			searchQbSync.setStartCreatedOn(dates[0]);
			searchQbSync.setEndCreatedOn(dates[1]);
		}

		QbSyncSpecification spec = new QbSyncSpecification(searchQbSync);
		List<QBSync> qbSyncList = qbSyncRepository.findAll(spec);
		return qbSyncList;
	}
}
