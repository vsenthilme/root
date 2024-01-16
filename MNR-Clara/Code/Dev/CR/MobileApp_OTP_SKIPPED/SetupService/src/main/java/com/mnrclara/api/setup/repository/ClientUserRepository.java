package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.setup.model.clientuser.ClientUser;
import com.mnrclara.api.setup.model.clientuser.ClientUserImpl;

@Repository
@Transactional
public interface ClientUserRepository extends JpaRepository<ClientUser, Long>, JpaSpecificationExecutor<ClientUser> {
		
	public Optional<ClientUser> findByClientUserId (String clientUserId);
	
	public Optional<ClientUser> findByLanguageIdAndClassIdAndClientUserIdAndClientIdAndDeletionIndicator(
			String languageId, Long classId, String clientUserId, String clientId, Long deletionIndicator);
	
	public ClientUser findByContactNumberAndDeletionIndicator (String contactNumber, Long deletionIndicator);
	public ClientUser findByEmailIdAndDeletionIndicator (String emailId, Long deletionIndicator);

	@Query(value ="select \n"
			+ "tcg.LANG_ID languageId, \n"
			+ "tcg.CLASS_ID classId, \n"
			+ "tcg.CLIENT_ID clientId, \n"
			+ "tcg.CONT_NO contactNumber, \n"
			+ "tcg.FIRST_NM firstName, \n"
			+ "tcg.LAST_NM lastName, \n"
			+ "tcg.EMAIL_ID emailId, \n"
			+ "tcg.IS_DELETED deletionIndicator, \n"
			+ "tcg.REF_FIELD_5 referenceField5, \n"
			+ "tcg.REF_FIELD_6 referenceField6, \n"
			+ "tcg.REF_FIELD_7 referenceField7, \n"
			+ "tcg.REF_FIELD_8 referenceField8, \n"
			+ "tcg.REF_FIELD_10 referenceField10, \n"
			+ "tcg.CTD_BY createdBy, \n"
			+ "tcg.CTD_ON createdOn, \n"
			+ "tcg.UTD_BY updatedBy, \n"
			+ "tcg.UTD_ON updatedOn, \n"
			+ "(select count(trn.receipt_no) from tblreceiptappnotice trn where trn.client_id=tcg.client_id and trn.IS_DELETED=0) receiptNumber, \n"
			+ "(select count(tqh.quote_no) from tblquotationheader tqh where tqh.client_id=tcg.client_id and tqh.IS_DELETED=0) quotation, \n"
			+ "(select count(tph.payment_plan_no) from tblpaymentplanheader tph where tph.client_id=tcg.client_id and tph.IS_DELETED=0) paymentPlan, \n"
			+ "(select count(tmg.client_id) from tblmattergenaccid tmg where tmg.client_id=tcg.client_id and tmg.IS_DELETED=0) matter, \n"
			+ "(select count(ti.invoice_no) from tblinvoiceheader ti where ti.client_id=tcg.client_id and ti.IS_DELETED=0) invoice, \n"
			+ "(select count(mdh.matter_header_id) from tblmatterdoclistheader mdh where mdh.client_id=tcg.client_id and mdh.IS_DELETED=0) documents, \n"
			+ "(select count(tmd.matter_doc_id) from tblmatterdocumentid tmd where tmd.client_id=tcg.client_id and tmd.IS_DELETED=0 and (tmd.ref_Field_2='CLIENTPORTAL' or tmd.ref_Field_5='Client Portal')) referenceField2, \n"
			+ "(select count(ta.pot_client_id) from tblagreementid ta left join tblclientgeneralid tc on tc.pot_client_id=ta.pot_client_id where tc.client_id=tcg.client_id and tc.is_deleted=0) agreement, \n"
			+ "(select tcu.client_usr_id from tblclientuser tcu where tcu.client_id=tcg.client_id and tcu.is_deleted=0) clientUserId \n"
			+ "from tblclientgeneralid tcg \n"
			+ "where \n"
			+ "(COALESCE(:classId) IS NULL OR (tcg.class_id IN (:classId))) and \n"
			+ "(COALESCE(:clientId) IS NULL OR (tcg.client_id IN (:clientId))) and \n"
			//+ "(COALESCE(:clientUserId) IS NULL OR (tcu.client_usr_id IN (:clientUserId))) and \n"
			+ "(COALESCE(:contactNumber) IS NULL OR (tcg.CONT_NO IN (:contactNumber))) and \n"
			+ "(COALESCE(:fullName) IS NULL OR (tcg.first_last_nm IN (:fullName))) and \n"
			+ "(COALESCE(:emailId) IS NULL OR (tcg.email_id IN (:emailId))) and \n"

			+ "tcg.IS_DELETED=0", nativeQuery = true)
	public List<ClientUserImpl> getClientUser (
			@Param(value = "classId") List<Long> classId,
			@Param(value = "clientId") List<String> clientId,
			//@Param(value = "clientUserId") List<String> clientUserId,
			@Param(value = "contactNumber") List<String> contactNumber,
			@Param(value = "fullName") List<String> fullName,
			@Param(value = "emailId") List<String> emailId);
}