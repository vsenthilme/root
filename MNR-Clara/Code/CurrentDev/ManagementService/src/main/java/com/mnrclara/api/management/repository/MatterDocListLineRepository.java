package com.mnrclara.api.management.repository;

import com.mnrclara.api.management.model.matterdoclist.MatterDocListLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface MatterDocListLineRepository extends JpaRepository<MatterDocListLine,Long>, JpaSpecificationExecutor<MatterDocListLine> {
	
	public List<MatterDocListLine> findAll();
	
	public Optional<MatterDocListLine>
		findByMatterNumberAndSequenceNumberAndDeletionIndicator(
				String matterNumber, Long sequenceNumber,
				Long deletionIndicator);

	public List<MatterDocListLine>
	findByMatterNumberAndCheckListNoAndDeletionIndicator(
			String matterNumber, Long checkListNo,
			Long deletionIndicator);

	public List<MatterDocListLine>
		findByMatterHeaderIdAndDeletionIndicator(Long matterHeaderId,Long deletionIndicator);

	public List<MatterDocListLine>
		findByClassIdAndClientIdAndMatterNumberAndCheckListNoAndDeletionIndicator(
				Long classId,String clientId,String matterNumber,Long checkListNo, Long deletionIndicator);

	public Optional<MatterDocListLine>
	findBySequenceNumberAndDeletionIndicator(Long sequenceNumber, Long deletionIndicator);
	public Optional<MatterDocListLine>
	findByMatterHeaderIdAndSequenceNumberAndDeletionIndicator(Long matterHeaderId, Long sequenceNumber, Long deletionIndicator);
	
	public MatterDocListLine findByMatterNumberAndClientIdAndCheckListNo(String matterNumber, String clientId, Long checkListNo);

	public MatterDocListLine findByMatterNumberAndClientIdAndCheckListNoAndSequenceNumber(String matterNumber,
			String clientId, Long checkListNo, Long sequenceNumber);
}