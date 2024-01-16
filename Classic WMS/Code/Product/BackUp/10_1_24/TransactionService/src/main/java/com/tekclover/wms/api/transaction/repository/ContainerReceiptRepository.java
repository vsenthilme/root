package com.tekclover.wms.api.transaction.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.tekclover.wms.api.transaction.model.IkeyValuePair;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tekclover.wms.api.transaction.model.inbound.containerreceipt.ContainerReceipt;

@Repository
@Transactional
public interface ContainerReceiptRepository extends JpaRepository<ContainerReceipt,Long>,
		JpaSpecificationExecutor<ContainerReceipt>, StreamableJpaSpecificationRepository<ContainerReceipt> {
	
	/**
	 * 
	 */
	public List<ContainerReceipt> findAll();
	
	/**
	 * 
	 * @param languageId
	 * @param companyCodeId
	 * @param plantId
	 * @param warehouseId
	 * @param preInboundNo
	 * @param refDocNumber
	 * @param containerReceiptNo
	 * @param deletionIndicator
	 * @return
	 */
	public Optional<ContainerReceipt> 
		findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndContainerReceiptNoAndDeletionIndicator(
				String languageId, String companyCodeId, String plantId, String warehouseId, 
				String preInboundNo, String refDocNumber, String containerReceiptNo, Long deletionIndicator);

	public Optional<ContainerReceipt> findByPreInboundNo(String containerReceiptNo);

	public Optional<ContainerReceipt> findByContainerReceiptNoAndDeletionIndicator(String containerReceiptNo, Long deletionIndicator);

	long countByWarehouseIdAndContainerReceivedDateBetweenAndRefDocNumberIsNull(String warehouseId, Date fromDate, Date toDate);

	@Query(value = "Select \n" +
			"tc.c_text as companyDesc,\n" +
			"tp.plant_text as plantDesc,\n" +
			"tw.wh_text as warehouseDesc from \n" +
			"tblcompanyid tc\n" +
			"join tblplantid tp on tp.c_id = tc.c_id and tp.lang_id = tc.lang_id \n" +
			"join tblwarehouseid tw on tw.c_id = tc.c_id and tw.lang_id = tc.lang_id and tw.plant_id = tp.plant_id \n" +
			"where\n" +
			"tc.lang_id IN (:languageId) and \n" +
			"tc.c_id IN (:companyCodeId) and \n" +
			"tp.plant_id IN(:plantId) and \n" +
			"tw.wh_id IN (:warehouseId) and \n" +
			"tc.is_deleted = 0 and \n" +
			"tp.is_deleted = 0 and \n" +
			"tw.is_deleted = 0 ", nativeQuery = true)
	IkeyValuePair getDescription(@Param(value = "languageId") String languageId,
								 @Param(value = "companyCodeId") String companyCodeId,
								 @Param(value = "plantId") String plantId,
								 @Param(value = "warehouseId") String warehouseId);


}