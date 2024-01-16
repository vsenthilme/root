package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.matterdoclist.MatterDocList;

@Repository
@Transactional
public interface MatterDocListRepository extends JpaRepository<MatterDocList,Long>, JpaSpecificationExecutor<MatterDocList> {
	
	public List<MatterDocList> findAll();
	
	public Optional<MatterDocList>
		findByLanguageIdAndClassIdAndCheckListNoAndMatterNumberAndClientIdAndDeletionIndicator(
				String languageId, Long classId, Long checkListNo, String matterNumber, String clientId, 
				Long deletionIndicator);
	public List<MatterDocList>
		findByClassIdAndClientIdAndMatterNumberAndCheckListNoAndDeletionIndicator(
				Long classId,String clientId,String matterNumber,Long checkListNo, Long deletionIndicator);
	
	public MatterDocList findByMatterNumberAndClientIdAndCheckListNo(String matterNumber, String clientId, Long checkListNo);

	public MatterDocList findByMatterNumberAndClientIdAndCheckListNoAndSequenceNumber(String matterNumber,
			String clientId, Long checkListNo, Long sequenceNumber);
}