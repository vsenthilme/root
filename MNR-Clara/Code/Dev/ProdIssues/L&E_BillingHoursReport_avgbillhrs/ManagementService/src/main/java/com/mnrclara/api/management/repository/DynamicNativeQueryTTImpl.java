package com.mnrclara.api.management.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mnrclara.api.management.model.matterexpense.MatterExpense;
import com.mnrclara.api.management.model.matterexpense.SearchMatterExpense;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.SimpleTriggerContext;

@Slf4j
public class DynamicNativeQueryTTImpl implements DynamicNativeQueryTT {

	@PersistenceContext
    private EntityManager entityManager;
	
    //------------------------------Bill-By-Group---------------------------------------------------------------
    @Override
    public List<String[]> findCountAndSumOfTimeTicketsByGroup1 (List<String> matterNumber, Date startDate, Date feesCutoffDate, 
			List<String> originatingTimeKeeper, List<String> responsibleTimeKeeper, List<String> assignedTimeKeeper) {
    	
    	StringBuilder sb = new StringBuilder();
		sb.append (" SELECT COUNT(t.TIME_TICKET_NO) AS timeTicketCount, SUM(t.TIME_TICKET_AMOUNT) AS timeTicketAmount, t.MATTER_NO AS matterNumber ");
		sb.append (" FROM tblmattertimeticketid t LEFT OUTER JOIN tblmatterassignmentid a");
		sb.append (" ON t.MATTER_NO = a.MATTER_NO ");
		sb.append (" WHERE t.MATTER_NO IN (:matterNumber) ");
		sb.append (" AND t.TIME_TICKET_DATE BETWEEN :startDate AND :feesCutoffDate ");
		sb.append (" AND t.STATUS_ID = 33 AND t.bill_type = 'Billable'  ");
		sb.append (" AND t.IS_DELETED = 0 ");
		
		if (!originatingTimeKeeper.isEmpty()) {
			sb.append (" OR a.ORIGINATING_TK IN (:originatingTimeKeeper) ");
		}
		
		if (!responsibleTimeKeeper.isEmpty()) {
			sb.append (" OR a.RESPONSIBLE_TK IN (:responsibleTimeKeeper) ");
		}
		
		if (!assignedTimeKeeper.isEmpty()) {
			sb.append (" OR a.ASSIGNED_TK IN (:assignedTimeKeeper) ");
		}
		
		sb.append (" GROUP BY t.MATTER_NO ");
		log.info(" query---> : " + sb.toString());
		 
		javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("matterNumber", matterNumber);
		query.setParameter("startDate", startDate);
		query.setParameter("feesCutoffDate", feesCutoffDate);
		
		if (!originatingTimeKeeper.isEmpty()) {
			query.setParameter("originatingTimeKeeper", originatingTimeKeeper);
		}
		
		if (!responsibleTimeKeeper.isEmpty()) {
			query.setParameter("responsibleTimeKeeper", responsibleTimeKeeper);
		}
		
		if (!assignedTimeKeeper.isEmpty()) {
			query.setParameter("assignedTimeKeeper", assignedTimeKeeper);
		}
		log.info(" query : " + query);
		
		return query.getResultList();
    }
    
    @Override
    public List<String[]> findExpenseByGroup1(List<String> matterNumber, Date startDate, Date feesCutoffDate, 
			List<String> originatingTimeKeeper, List<String> responsibleTimeKeeper, List<String> assignedTimeKeeper) {
    			
    	StringBuilder sb = new StringBuilder();
		sb.append (" SELECT COUNT(e.MATTER_EXP_ID) AS expenseCount, SUM(e.EXP_AMOUNT) AS expenseAmount, e.MATTER_NO AS matterNumber,e.EXP_TYPE as expenseType");
		sb.append (" FROM tblmatterexpenseid e LEFT OUTER JOIN tblmatterassignmentid a ");
		sb.append (" ON e.MATTER_NO = a.MATTER_NO ");
		sb.append (" WHERE e.MATTER_NO IN (:matterNumber) ");
		sb.append (" AND e.CTD_ON BETWEEN :startDate AND :feesCutoffDate ");
		sb.append (" AND e.STATUS_ID = 37  ");
		
		if (!originatingTimeKeeper.isEmpty()) {
			sb.append (" OR a.ORIGINATING_TK IN (:originatingTimeKeeper) ");
		}
		
		if (!responsibleTimeKeeper.isEmpty()) {
			sb.append (" OR a.RESPONSIBLE_TK IN (:responsibleTimeKeeper) ");
		}
		
		if (!assignedTimeKeeper.isEmpty()) {
			sb.append (" OR a.ASSIGNED_TK IN (:assignedTimeKeeper) ");
		}
		
		sb.append (" GROUP BY e.MATTER_NO,e.EXP_TYPE ");
		 
		javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("matterNumber", matterNumber);
		query.setParameter("startDate", startDate);
		query.setParameter("feesCutoffDate", feesCutoffDate);
		
		if (!originatingTimeKeeper.isEmpty()) {
			query.setParameter("originatingTimeKeeper", originatingTimeKeeper);
		}
		
		if (!responsibleTimeKeeper.isEmpty()) {
			query.setParameter("responsibleTimeKeeper", responsibleTimeKeeper);
		}
		
		if (!assignedTimeKeeper.isEmpty()) {
			query.setParameter("assignedTimeKeeper", assignedTimeKeeper);
		}
		
		return query.getResultList();
    }

	@Override
	public List<String[]> findMatterExpense (SearchMatterExpense searchMatterExpense) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select m.MATTER_EXP_ID, m.LANG_ID, m.CLASS_ID, m.MATTER_NO, m.CASEINFO_NO, m.CLIENT_ID, m.EXP_CODE, ");
		sb.append(" m.CASE_CATEGORY_ID, m.CASE_SUB_CATEGORY_ID, m.COST_ITEM, m.NO_ITEMS, m.EXP_AMOUNT, m.RATE_UNIT, ");
		sb.append(" m.EXP_TEXT, m.EXP_TYPE, m.BILL_TYPE, m.WRITE_OFF, m.EXP_ACCOUNT_NO, m.STATUS_ID, m.IS_DELETED, ");
		sb.append(" m.REF_FIELD_1, m.REF_FIELD_2, m.REF_FIELD_3, m.REF_FIELD_4, m.REF_FIELD_5, m.REF_FIELD_6, ");
		sb.append(" m.REF_FIELD_7, m.REF_FIELD_8, m.REF_FIELD_9, m.REF_FIELD_10, m.CTD_BY, m.CTD_ON, m.UTD_BY, m.UTD_ON, ");
		sb.append(" m.billable_to_client, m.card_not_accepted, m.payable_to, m.payment_mode, m.required_date, ");
		sb.append(" m.vendor_fax, m.vendor_mailing_address, m.vendor_notes, m.vendor_phone, m.user_name, ");
		sb.append(" m.qb_dept, m.check_request_status, m.document_name, m.physical_card, m.credit_card, ");
		sb.append(" m.expiration_date, m.name_on_card_check, m.security_code, m.type_of_credit_card, ");
		sb.append(" m.check_request_ctd_by, m.check_request_ctd_on, mg.matter_text matterDescription ");
		sb.append(" from tblmatterexpenseid m left outer join tblmattergenaccid mg on mg.matter_no = m.matter_no ");
		sb.append(" WHERE 1=1 AND m.IS_DELETED = 0 ");

		if (searchMatterExpense.getExpenseCode() != null) {
			sb.append(" AND m.EXP_CODE IN (:expenseCode)");
		}

		if (searchMatterExpense.getMatterNumber() != null) {
			sb.append(" AND m.MATTER_NO IN (:matterNumber)");
		}

		if (searchMatterExpense.getCreatedBy() != null) {
			sb.append(" AND m.CTD_BY IN (:createdBy)");
		}

		if (searchMatterExpense.getExpenseType() != null) {
			sb.append(" AND m.EXP_TYPE IN (:expenseType)");
		}

		if (searchMatterExpense.getStatusId() != null) {
			sb.append(" AND m.STATUS_ID IN (:statusId) ");
		}

		if (searchMatterExpense.getStartCreatedOn() != null && searchMatterExpense.getEndCreatedOn() != null) {
			sb.append (" AND m.REF_FIELD_2 BETWEEN (:startCreatedOn and :endCreatedOn)");
		}

		javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());

		if (searchMatterExpense.getExpenseCode() != null) {
			query.setParameter("expenseCode", searchMatterExpense.getExpenseCode());
		}

		if (searchMatterExpense.getExpenseType() != null) {
			query.setParameter("expenseType", searchMatterExpense.getExpenseType());
		}

		if (searchMatterExpense.getMatterNumber() != null) {
			query.setParameter("matterNumber", searchMatterExpense.getMatterNumber());
		}

		if (searchMatterExpense.getStatusId() != null) {
			query.setParameter("statusId", searchMatterExpense.getStatusId());
		}

		if (searchMatterExpense.getCreatedBy() != null) {
			query.setParameter("createdBy", searchMatterExpense.getCreatedBy());
		}

		if (searchMatterExpense.getStartCreatedOn() != null && searchMatterExpense.getEndCreatedOn() != null) {
			query.setParameter("startCreatedOn", searchMatterExpense.getStartCreatedOn());
			query.setParameter("endCreatedOn" , searchMatterExpense.getEndCreatedOn());
		}

		query.setHint("org.hibernate.fetchSize", "50");

		return query.getResultList();
	}
}
