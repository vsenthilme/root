package com.mnrclara.api.management.repository;

import com.mnrclara.api.management.model.matterdoclist.AddMatterDocListHeader;
import com.mnrclara.api.management.model.matterdoclist.MatterDocList;
import com.mnrclara.api.management.model.matterdoclist.MatterDocListHeader;
import com.mnrclara.api.management.model.matterdoclist.MatterDocListLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface MatterDocListHeaderRepository extends JpaRepository<MatterDocListHeader,Long>, JpaSpecificationExecutor<MatterDocListHeader> {
	
	public List<MatterDocListHeader> findAll();
	
	public MatterDocListHeader findByMatterHeaderId(Long matterHeaderId);
	
	public Optional<MatterDocListHeader>
		findByLanguageIdAndClassIdAndCheckListNoAndMatterNumberAndClientIdAndDeletionIndicator(
				String languageId, Long classId, Long checkListNo, String matterNumber, String clientId, 
				Long deletionIndicator);

	public Optional<MatterDocListHeader>
		findByMatterHeaderIdAndDeletionIndicator(Long matterHeaderId, Long deletionIndicator);

	
	public MatterDocListHeader findByMatterNumberAndClientIdAndCheckListNo(String matterNumber, String clientId, Long checkListNo);

}