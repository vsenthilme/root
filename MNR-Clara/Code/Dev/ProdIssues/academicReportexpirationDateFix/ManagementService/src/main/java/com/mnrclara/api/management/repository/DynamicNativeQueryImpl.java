package com.mnrclara.api.management.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mnrclara.api.management.model.mattergeneral.WIPAgedPBReportInput;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicNativeQueryImpl implements DynamicNativeQuery {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
    public List<String[]> getMatterNumbers (WIPAgedPBReportInput wipAgedPBReportInput) {
        StringBuilder sb = new StringBuilder();
        //sb.append(" SELECT DISTINCT m.MATTER_NO, m.MATTER_TEXT, m.CLIENT_ID, m.CLASS_ID, m.BILL_MODE_ID, c.FIRST_LAST_NM, b.BILL_MODE_TEXT, a.PARTNER, a.RESPONSIBLE_TK ");
        sb.append(" SELECT DISTINCT m.MATTER_NO ");
        sb.append(" FROM tblmattergenaccid m join tblclientgeneralid c on m.CLIENT_ID = c.CLIENT_ID ");
        sb.append(" left outer join tblmatterassignmentid a on a.MATTER_NO = m.MATTER_NO ");
        sb.append(" left outer join tblbillingmodeid b on b.bill_mode_id = m.bill_mode_id ");
        sb.append(" WHERE 1=1 ");

        if (wipAgedPBReportInput.getCaseCategoryId() != null) {
			 sb.append(" AND m.CASE_CATEGORY_ID IN (:caseCategoryId)");
		}
		
		if (wipAgedPBReportInput.getCaseSubCategoryId() != null) {
			 sb.append(" AND m.CASE_SUB_CATEGORY_ID IN (:caseSubCategoryId)");
		}
		
		if (wipAgedPBReportInput.getClassId() != null) {
			 sb.append(" AND m.CLASS_ID IN (:classId)");
		}
		
		if (wipAgedPBReportInput.getClientId() != null) {
			 sb.append(" AND m.CLIENT_ID IN (:clientId)");
		}
		
        if (wipAgedPBReportInput.getMatterNumber() != null) {
            sb.append(" AND m.MATTER_NO IN (:matterNumber) ");
        }
        
        if (wipAgedPBReportInput.getPartner() != null) {
			sb.append (" AND a.PARTNER IN (:partner)");
		}
        
        log.info("Query : " + sb.toString());
        
        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());

        if (wipAgedPBReportInput.getCaseCategoryId() != null) {
			 query.setParameter("caseCategoryId", wipAgedPBReportInput.getCaseCategoryId());
		}
		
		if (wipAgedPBReportInput.getCaseSubCategoryId() != null) {
			 query.setParameter("caseSubCategoryId", wipAgedPBReportInput.getCaseSubCategoryId());
		}
		
		if (wipAgedPBReportInput.getClassId() != null) {
			 query.setParameter("classId", wipAgedPBReportInput.getClassId());
		}
		
		if (wipAgedPBReportInput.getClientId() != null) {
			 query.setParameter("clientId", wipAgedPBReportInput.getClientId());
		}
		
        if (wipAgedPBReportInput.getMatterNumber() != null) {
        	query.setParameter("matterNumber", wipAgedPBReportInput.getMatterNumber());
        }
        
        if (wipAgedPBReportInput.getPartner() != null) {
        	query.setParameter("partner", wipAgedPBReportInput.getPartner());
		}
        
//        log.info("Query : " + sb.toString());
        query.setHint("org.hibernate.fetchSize", "50");
        
        return query.getResultList();
    }
	
    @SuppressWarnings("unchecked")
	@Override
    public List<String[]> getMatterNumbers (WIPAgedPBReportInput wipAgedPBReportInput, int pageIndex, int noOfRecords) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT DISTINCT m.MATTER_NO, m.MATTER_TEXT, m.CLIENT_ID, m.CLASS_ID, m.BILL_MODE_ID, c.FIRST_LAST_NM, b.BILL_MODE_TEXT, a.PARTNER, a.RESPONSIBLE_TK ");
        sb.append(" FROM tblmattergenaccid m join tblclientgeneralid c on m.CLIENT_ID = c.CLIENT_ID ");
        sb.append(" left outer join tblmatterassignmentid a on a.MATTER_NO = m.MATTER_NO ");
        sb.append(" left outer join tblbillingmodeid b on b.bill_mode_id = m.bill_mode_id ");
        sb.append(" WHERE 1=1 ");

        if (wipAgedPBReportInput.getCaseCategoryId() != null) {
			 sb.append(" AND m.CASE_CATEGORY_ID IN (:caseCategoryId)");
		}
		
		if (wipAgedPBReportInput.getCaseSubCategoryId() != null) {
			 sb.append(" AND m.CASE_SUB_CATEGORY_ID IN (:caseSubCategoryId)");
		}
		
		if (wipAgedPBReportInput.getClassId() != null) {
			 sb.append(" AND m.CLASS_ID IN (:classId)");
		}
		
		if (wipAgedPBReportInput.getClientId() != null) {
			 sb.append(" AND m.CLIENT_ID IN (:clientId)");
		}
		
        if (wipAgedPBReportInput.getMatterNumber() != null) {
            sb.append(" AND m.MATTER_NO IN (:matterNumber) ");
        }
        
        if (wipAgedPBReportInput.getPartner() != null) {
			sb.append (" AND a.PARTNER IN (:partner)");
		}
        
//        log.info("Query : " + sb.toString());
        
        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());

        if (wipAgedPBReportInput.getCaseCategoryId() != null) {
			 query.setParameter("caseCategoryId", wipAgedPBReportInput.getCaseCategoryId());
		}
		
		if (wipAgedPBReportInput.getCaseSubCategoryId() != null) {
			 query.setParameter("caseSubCategoryId", wipAgedPBReportInput.getCaseSubCategoryId());
		}
		
		if (wipAgedPBReportInput.getClassId() != null) {
			 query.setParameter("classId", wipAgedPBReportInput.getClassId());
		}
		
		if (wipAgedPBReportInput.getClientId() != null) {
			 query.setParameter("clientId", wipAgedPBReportInput.getClientId());
		}
		
        if (wipAgedPBReportInput.getMatterNumber() != null) {
        	query.setParameter("matterNumber", wipAgedPBReportInput.getMatterNumber());
        }
        
        if (wipAgedPBReportInput.getPartner() != null) {
        	query.setParameter("partner", wipAgedPBReportInput.getPartner());
		}
        
        query.setFirstResult(pageIndex * noOfRecords);
        query.setMaxResults(noOfRecords);
        query.setHint("org.hibernate.fetchSize", "50");
//        log.info("Query : " + query);
//        log.info("Query 1 : " + pageIndex + "," + noOfRecords);
        
        return query.getResultList();
    }
    
    @Override
    public int getMatterNumbersCount (WIPAgedPBReportInput wipAgedPBReportInput) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT count(*) ");
        sb.append(" FROM tblmattergenaccid m join tblclientgeneralid c on m.CLIENT_ID = c.CLIENT_ID ");
        sb.append(" left outer join tblmatterassignmentid a on a.MATTER_NO = m.MATTER_NO ");
        sb.append(" left outer join tblbillingmodeid b on b.bill_mode_id = m.bill_mode_id ");
        sb.append(" WHERE 1=1 ");

        if (wipAgedPBReportInput.getCaseCategoryId() != null) {
			 sb.append(" AND m.CASE_CATEGORY_ID IN (:caseCategoryId)");
		}
		
		if (wipAgedPBReportInput.getCaseSubCategoryId() != null) {
			 sb.append(" AND m.CASE_SUB_CATEGORY_ID IN (:caseSubCategoryId)");
		}
		
		if (wipAgedPBReportInput.getClassId() != null) {
			 sb.append(" AND m.CLASS_ID IN (:classId)");
		}
		
		if (wipAgedPBReportInput.getClientId() != null) {
			 sb.append(" AND m.CLIENT_ID IN (:clientId)");
		}
		
        if (wipAgedPBReportInput.getMatterNumber() != null) {
            sb.append(" AND m.MATTER_NO IN (:matterNumber) ");
        }
        
        if (wipAgedPBReportInput.getPartner() != null) {
			sb.append (" AND a.PARTNER IN (:partner)");
		}
        
//        log.info("Query : " + sb.toString());
        
        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());

        if (wipAgedPBReportInput.getCaseCategoryId() != null) {
			 query.setParameter("caseCategoryId", wipAgedPBReportInput.getCaseCategoryId());
		}
		
		if (wipAgedPBReportInput.getCaseSubCategoryId() != null) {
			 query.setParameter("caseSubCategoryId", wipAgedPBReportInput.getCaseSubCategoryId());
		}
		
		if (wipAgedPBReportInput.getClassId() != null) {
			 query.setParameter("classId", wipAgedPBReportInput.getClassId());
		}
		
		if (wipAgedPBReportInput.getClientId() != null) {
			 query.setParameter("clientId", wipAgedPBReportInput.getClientId());
		}
		
        if (wipAgedPBReportInput.getMatterNumber() != null) {
        	query.setParameter("matterNumber", wipAgedPBReportInput.getMatterNumber());
        }
        
        if (wipAgedPBReportInput.getPartner() != null) {
        	query.setParameter("partner", wipAgedPBReportInput.getPartner());
		}
        
        List<BigInteger> count = query.getResultList();
        return count.get(0).intValue();
    }
    
    /**
     * 
     * @param partner
     * @param matterNumber
     * @return
     */
    public List<String[]> getMatterAssignment (List<String> partner, String matterNumber) {
		StringBuilder sb = new StringBuilder();
		sb.append ("SELECT p.PARTNER AS partner, p.RESPONSIBLE_TK FROM tblmatterassignmentid p WHERE 1=1 ");
		sb.append(" AND p.MATTER_NO IN (:matterNumber) ");
		
		if (partner != null) {
			sb.append (" AND p.PARTNER IN (:partner)");
		}
		 
		javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("matterNumber", matterNumber);
		
		if (partner != null) {
			query.setParameter("partner", partner);
		}
		
		return query.getResultList();
    }

	@Override
	public List<String[]> getMatterDetail (String matterNumber) {
		 StringBuilder sb = new StringBuilder();
	        sb.append(" SELECT DISTINCT m.MATTER_NO, m.MATTER_TEXT, m.CLIENT_ID, m.CLASS_ID, m.BILL_MODE_ID, c.FIRST_LAST_NM, b.BILL_MODE_TEXT, a.PARTNER, a.RESPONSIBLE_TK ");
	        sb.append(" FROM tblmattergenaccid m join tblclientgeneralid c on m.CLIENT_ID = c.CLIENT_ID ");
	        sb.append(" left outer join tblmatterassignmentid a on a.MATTER_NO = m.MATTER_NO ");
	        sb.append(" left outer join tblbillingmodeid b on b.bill_mode_id = m.bill_mode_id ");
	        sb.append(" WHERE 1=1 AND m.MATTER_NO IN (:matterNumber) ");
//	        log.info("Query : " + sb.toString());
	        
	        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());
        	query.setParameter("matterNumber", matterNumber);
	        query.setHint("org.hibernate.fetchSize", "50");
	        return query.getResultList();
	}
}
