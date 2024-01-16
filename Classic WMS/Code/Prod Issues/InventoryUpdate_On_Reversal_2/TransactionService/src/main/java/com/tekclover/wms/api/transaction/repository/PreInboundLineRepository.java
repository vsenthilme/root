package com.tekclover.wms.api.transaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.transaction.model.inbound.preinbound.PreInboundLineEntity;

@Repository
@Transactional
public interface PreInboundLineRepository extends JpaRepository<PreInboundLineEntity,Long>, JpaSpecificationExecutor<PreInboundLineEntity> {
	
	public List<PreInboundLineEntity> findAll();
	
	public Optional<PreInboundLineEntity> 
		findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndLineNoAndItemCodeAndDeletionIndicator(
				String languageId, String companyCode, String plantId, String warehouseId, String preInboundNo, String refDocNumber, Long lineNo, String itemCode, Long deletionIndicator);
	public Optional<PreInboundLineEntity> findByLineNo(String lineNo);
	
	public List<PreInboundLineEntity> findByPreInboundNoAndDeletionIndicator(String preInboundNo, Long deletionIndicator);
	
	public List<PreInboundLineEntity> 
		findByPreInboundNoAndStatusIdAndDeletionIndicator(String preInboundNo, Long statusId, Long deletionIndicator);
	
	public List<PreInboundLineEntity> findByWarehouseIdAndPreInboundNoAndDeletionIndicator(
			String warehouseId, String preInboundNo, Long deletionIndicator);
	
	/**
	 * 
	 * @param rssFeedEntryId
	 * @param isRead
	 */
	@Modifying(clearAutomatically = true)
	@Query("UPDATE PreInboundLineEntity ib SET ib.statusId = :statusId WHERE ib.warehouseId = :warehouseId AND ib.refDocNumber = :refDocNumber")
	void updatePreInboundLineStatus(@Param ("warehouseId") String warehouseId,
			@Param ("refDocNumber") String refDocNumber, @Param ("statusId") Long statusId);
}