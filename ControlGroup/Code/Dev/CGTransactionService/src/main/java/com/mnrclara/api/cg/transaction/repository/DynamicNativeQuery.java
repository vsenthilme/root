package com.mnrclara.api.cg.transaction.repository;

import java.util.List;

import com.mnrclara.api.cg.transaction.model.storepartnerlisting.FindMatchResult;

public interface DynamicNativeQuery {

	public List<String[]> findStorePartnerListingByCOOwnerId (FindMatchResult findMatchResult);

	List<String[]> findByExact(
			Long coOwnerId1, Long coOwnerId2, Long coOwnerId3, Long coOwnerId4, Long coOwnerId5,
			Long coOwnerId6, Long coOwnerId7, Long coOwnerId8, Long coOwnerId9, Long coOwnerId10);
}

