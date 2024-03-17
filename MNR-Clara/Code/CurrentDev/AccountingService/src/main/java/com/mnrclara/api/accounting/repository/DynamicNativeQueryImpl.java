package com.mnrclara.api.accounting.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mnrclara.api.accounting.model.prebill.BillByGroup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicNativeQueryImpl implements DynamicNativeQuery {

	public enum Timekeepers {
		PARTNERS,
		ORIGINATINGTIMEKEEPERS,
		RESPONSIBLETIMEKEEPERS, 
		LEGALASSISTANTS
	}
	
	@PersistenceContext
    private EntityManager entityManager;
	
    @Override
    public List<String[]> getBilledIncome (Long classId, Date fromPostingDate, Date toPostingDate) {
        StringBuilder sb = new StringBuilder();
        sb.append ("SELECT pd.partner_assigned AS partner, SUM(i.INVOICE_AMT) AS invoiceAmount ");
        sb.append ("FROM tblinvoiceheader i JOIN tblprebilldetails pd  ON i.pre_bill_no = pd.pre_bill_no ");
        sb.append ("WHERE i.CLASS_ID = :classId AND i.IS_DELETED = 0 " );
        sb.append ("AND i.POSTING_DATE BETWEEN :fromPostingDate AND :toPostingDate group by pd.partner_assigned");

        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("classId", classId);
        query.setParameter("fromPostingDate", fromPostingDate);	
        query.setParameter("toPostingDate", toPostingDate);
        
        return query.getResultList();
    }
    
    @Override
    public List<String[]> getCaseAssignment (Long classId, String timeKeeper) {
    	StringBuilder sb = new StringBuilder();
       
        if (timeKeeper.equalsIgnoreCase(Timekeepers.PARTNERS.name())) {
        	 sb.append ("SELECT PARTNER, COUNT(MATTER_NO) AS matterNumber FROM tblmatterassignmentid ");
             sb.append ("WHERE CLASS_ID = :classId AND (STATUS_ID = 39 or STATUS_ID = 26) AND IS_DELETED = 0 AND PARTNER is not null ");
             sb.append ("GROUP BY PARTNER ");
        } else if (timeKeeper.equalsIgnoreCase(Timekeepers.ORIGINATINGTIMEKEEPERS.name())) {
        	sb.append ("SELECT ORIGINATING_TK, COUNT(MATTER_NO) AS matterNumber FROM tblmatterassignmentid ");
            sb.append ("WHERE CLASS_ID = :classId AND (STATUS_ID = 39 or STATUS_ID = 26) AND IS_DELETED = 0 AND ORIGINATING_TK is not null ");
            sb.append ("GROUP BY ORIGINATING_TK ");
        } else if (timeKeeper.equalsIgnoreCase(Timekeepers.LEGALASSISTANTS.name())) {
        	sb.append ("SELECT LEGAL_ASSIST, COUNT(MATTER_NO) AS matterNumber FROM tblmatterassignmentid ");
            sb.append ("WHERE CLASS_ID = :classId AND (STATUS_ID = 39 or STATUS_ID = 26) AND IS_DELETED = 0 AND LEGAL_ASSIST is not null ");
            sb.append ("GROUP BY LEGAL_ASSIST ");
        } else if (timeKeeper.equalsIgnoreCase(Timekeepers.RESPONSIBLETIMEKEEPERS.name())) {
        	sb.append ("SELECT RESPONSIBLE_TK, COUNT(MATTER_NO) AS matterNumber FROM tblmatterassignmentid ");
            sb.append ("WHERE CLASS_ID = :classId AND (STATUS_ID = 39 or STATUS_ID = 26) AND IS_DELETED = 0 AND RESPONSIBLE_TK is not null ");
            sb.append ("GROUP BY RESPONSIBLE_TK ");
        }
        
        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("classId", classId);
//        query.setParameter("timeKeeper", timeKeeper);
        
        return query.getResultList();
    }

    @Override
    public List<String[]> getBillableNonBillableTime (Long classId, Date fromPostingDate, Date toPostingDate) {
        StringBuilder sb = new StringBuilder();
        sb.append ("SELECT  BILL_TYPE as billType, sum(TIME_TICKET_HRS) as timTicketHours ");
        sb.append ("FROM tblmattertimeticketid as ticket ");
        sb.append ("WHERE ticket.CLASS_ID = :classId AND ticket.IS_DELETED = 0 ");
        sb.append ("AND ticket.TIME_TICKET_DATE BETWEEN :fromPostingDate AND :toPostingDate GROUP BY ticket.BILL_TYPE");

        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("classId", classId);
        query.setParameter("fromPostingDate", fromPostingDate);
        query.setParameter("toPostingDate", toPostingDate);

        return query.getResultList();
    }

    @Override
    public List<String[]> getClientReferral (Long classId, Date fromPostingDate, Date toPostingDate) {
        StringBuilder sb = new StringBuilder();
        sb.append ("select r.REFERRAL_ID as referralId , r.referral_text as referralText, ");
        sb.append ("count(r.REFERRAL_ID) as countGroupedReferral, ");
        sb.append ("CAST(((count(r.REFERRAL_ID) /  ");
        sb.append ("(select Count(cg.REFERRAL_ID) from tblclientgeneralid cg where cg.REFERRAL_ID != 0 AND cg.CLASS_ID = :classId AND cg.CTD_ON BETWEEN :fromPostingDate AND :toPostingDate) ");
        sb.append (") *100) AS DECIMAL (18,2)) as average ");
        sb.append ("from  tblclientgeneralid c ");
        sb.append ("JOIN tblreferralid r on c.REFERRAL_ID = r.REFERRAL_ID  ");
        sb.append ("WHERE c.CLASS_ID = :classId AND c.IS_DELETED = 0  ");
        sb.append ("AND c.CTD_ON BETWEEN :fromPostingDate AND :toPostingDate GROUP BY r.REFERRAL_ID");

        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("classId", classId);
        query.setParameter("fromPostingDate", fromPostingDate);
        query.setParameter("toPostingDate", toPostingDate);

        return query.getResultList();
    }

    @Override
    public List<String[]> getPracticeBreakDown (Long classId, Date fromPostingDate, Date toPostingDate) {
        StringBuilder sb = new StringBuilder();
        sb.append ("select m.case_category_id , c.case_category ,  ");
        sb.append ("sum(i.invoice_amt) as totalInvoiceAmount from tblinvoiceheader i ");
        sb.append ("JOIN tblmattergenaccid m  ON i.MATTER_NO = m.MATTER_NO  ");
        sb.append ("JOIN tblcasecategoryid c ON m.case_category_id = c.case_category_id ");
        sb.append ("WHERE i.CLASS_ID = :classId AND i.IS_DELETED = 0 ");
        sb.append ("AND i.POSTING_DATE BETWEEN :fromPostingDate AND :toPostingDate GROUP BY m.case_category_id ");

        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("classId", classId);
        query.setParameter("fromPostingDate", fromPostingDate);
        query.setParameter("toPostingDate", toPostingDate);

        return query.getResultList();
    }

    @Override
    public List<String[]> getTopClients (Date fromPostingDate, Date toPostingDate) {
        StringBuilder sb = new StringBuilder();
        sb.append ("select c.CLIENT_ID as clientId, c.FIRST_LAST_NM as clientName, ");
        sb.append ("sum(t.APP_BILL_TIME)as totalTimeTicketHours , sum(t.APP_BILL_AMOUNT) as totalTimeTicketAmount ");
        sb.append ("from  tblmattertimeticketid t ");
        sb.append ("JOIN tblclientgeneralid c on t.CLIENT_ID = c.CLIENT_ID  ");
        sb.append ("where t.BILL_TYPE = 'BILLABLE' AND t.STATUS_ID = 51 AND t.IS_DELETED = 0 AND t.TIME_TICKET_DATE BETWEEN :fromPostingDate AND :toPostingDate ");
        sb.append ("GROUP BY c.CLIENT_ID , c.FIRST_LAST_NM ORDER by totalTimeTicketHours desc LIMIT 10  ");
        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("fromPostingDate", fromPostingDate);
        query.setParameter("toPostingDate", toPostingDate);

        return query.getResultList();
    }

    @Override
    public List<String[]> getLeadConversion (Long classId, Date fromDate, Date toDate) {
        StringBuilder sb = new StringBuilder();
        sb.append ("select i.ASSIGNED_USR_ID as assignedUserId, count(i.ASSIGNED_USR_ID) as countAssignedUserId  ");
        sb.append ("from tblinquiryid i ");
        sb.append ("WHERE i.ASSIGNED_USR_ID is not null and ");
        sb.append ("i.CLASS_ID = :classId AND i.IS_DELETED = 0 ");
        sb.append ("AND i.CTD_ON BETWEEN :fromDate AND :toDate GROUP BY i.ASSIGNED_USR_ID ");

        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("classId", classId);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);

        return query.getResultList();
    }
    
    //----------------------------------------------------------------------------------------------------------------
    @Override
    public List<String> findMatterNumbersForBillGroup (BillByGroup billByGroup) {
        StringBuilder sb = new StringBuilder();
        //sb.append(" SELECT DISTINCT m.MATTER_NO, m.MATTER_TEXT, m.CLIENT_ID, m.CLASS_ID, m.BILL_MODE_ID, c.FIRST_LAST_NM, b.BILL_MODE_TEXT, a.PARTNER, a.RESPONSIBLE_TK ");
        sb.append(" SELECT DISTINCT m.MATTER_NO ");
        sb.append(" FROM tblmattergenaccid m join tblclientgeneralid c on m.CLIENT_ID = c.CLIENT_ID ");
        sb.append(" left outer join tblmatterassignmentid a on a.MATTER_NO = m.MATTER_NO ");
        sb.append(" left outer join tblbillingmodeid b on b.bill_mode_id = m.bill_mode_id ");
        sb.append(" WHERE m.IS_DELETED = 0 AND 1=1 ");

        if (billByGroup.getCaseCategory() != null) {
			 sb.append(" AND m.CASE_CATEGORY_ID IN (:caseCategoryId)");
		}
		
		if (billByGroup.getCaseSubCategory() != null) {
			 sb.append(" AND m.CASE_SUB_CATEGORY_ID IN (:caseSubCategoryId)");
		}
		
		if (billByGroup.getClassId() != null) {
			 sb.append(" AND m.CLASS_ID IN (:classId)");
		}
		
		if (billByGroup.getClientId() != null) {
			 sb.append(" AND m.CLIENT_ID IN (:clientId)");
		}
		
        if (billByGroup.getMatterNumber() != null) {
            sb.append(" AND m.MATTER_NO IN (:matterNumber) ");
        }
        
        log.info("Query : " + sb.toString());
        
        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());

        if (billByGroup.getCaseCategory() != null) {
			 query.setParameter("caseCategoryId", billByGroup.getCaseCategory());
		}
		
		if (billByGroup.getCaseSubCategory() != null) {
			 query.setParameter("caseSubCategoryId", billByGroup.getCaseSubCategory());
		}
		
		if (billByGroup.getClassId() != null) {
			 query.setParameter("classId", billByGroup.getClassId());
		}
		
		if (billByGroup.getClientId() != null) {
			 query.setParameter("clientId", billByGroup.getClientId());
		}
		
        if (billByGroup.getMatterNumber() != null) {
        	query.setParameter("matterNumber", billByGroup.getMatterNumber());
        }
        
        log.info("Query : " + sb.toString());
        query.setHint("org.hibernate.fetchSize", "100");
        
        return query.getResultList();
    }
}
