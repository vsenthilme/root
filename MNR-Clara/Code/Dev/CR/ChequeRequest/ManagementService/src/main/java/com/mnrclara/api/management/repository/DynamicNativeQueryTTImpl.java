package com.mnrclara.api.management.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.extern.slf4j.Slf4j;

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
}
