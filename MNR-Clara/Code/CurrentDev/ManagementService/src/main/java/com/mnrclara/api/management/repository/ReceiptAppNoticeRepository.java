package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.receiptappnotice.ReceiptAppNotice;


@Repository
@Transactional
public interface ReceiptAppNoticeRepository extends JpaRepository<ReceiptAppNotice,Long>, JpaSpecificationExecutor<ReceiptAppNotice> {
	
	public List<ReceiptAppNotice> findAll();
	public Optional<ReceiptAppNotice> findByLanguageIdAndClassIdAndMatterNumberAndReceiptNoAndDeletionIndicator(
			String languageId, Long classId, String matterNumber, String receiptNo, Long deletionIndicator );
}