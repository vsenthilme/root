package com.mnrclara.api.cg.transaction.repository;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mnrclara.api.cg.transaction.model.storepartnerlisting.FindMatchResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicNativeQueryImpl implements DynamicNativeQuery {

    @PersistenceContext
    private EntityManager entityManager;

    javax.persistence.Query query = null;
    @Override
    public List<String[]> findStorePartnerListingByCOOwnerId(FindMatchResult findMatchResult) {
        int inputParamsLength = getInputParamsLength(findMatchResult);

        if (inputParamsLength > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT ");
            sb.append("tl.CO_OWNER_ID_1 AS coOwnerId1, tl.CO_OWNER_ID_2 AS coOwnerId2, ");
            sb.append("tl.CO_OWNER_ID_3 AS coOwnerId3, tl.CO_OWNER_ID_4 AS coOwnerId4, tl.CO_OWNER_ID_5 AS coOwnerId5, ");
            sb.append("tl.CO_OWNER_ID_6 AS coOwnerId6, tl.CO_OWNER_ID_7 AS coOwnerId7, ");
            sb.append("tl.CO_OWNER_ID_8 AS coOwnerId8, tl.CO_OWNER_ID_9 AS coOwnerId9, tl.CO_OWNER_ID_10 AS coOwnerId10, ");
            sb.append("tl.CO_OWNER_NAME_1 AS coOwnerName1, tl.CO_OWNER_NAME_2 AS coOwnerName2, tl.CO_OWNER_NAME_3 AS coOwnerName3, ");
            sb.append("tl.CO_OWNER_NAME_4 AS coOwnerName4, tl.CO_OWNER_NAME_5 AS coOwnerName5, ");
            sb.append("tl.CO_OWNER_NAME_6 AS coOwnerName6, tl.CO_OWNER_NAME_7 AS coOwnerName7, ");
            sb.append("tl.CO_OWNER_NAME_8 AS coOwnerName8, tl.CO_OWNER_NAME_9 AS coOwnerName9, tl.CO_OWNER_NAME_10 AS coOwnerName10, ");
            sb.append("tl.CO_OWNER_PER_1 AS coOwnerPercentage1, tl.CO_OWNER_PER_2 AS coOwnerPercentage2, ");
            sb.append("tl.CO_OWNER_PER_3 AS coOwnerPercentage3, tl.CO_OWNER_PER_4 AS coOwnerPercentage4, ");
            sb.append("tl.CO_OWNER_PER_5 AS coOwnerPercentage5, tl.CO_OWNER_PER_6 AS coOwnerPercentage6,");
            sb.append("tl.CO_OWNER_PER_7 AS coOwnerPercentage7, tl.CO_OWNER_PER_8 AS coOwnerPercentage8, ");
            sb.append("tl.CO_OWNER_PER_9 AS coOwnerPercentage9, tl.CO_OWNER_PER_10 AS coOwnerPercentage10,");
            sb.append("tl.STORE_ID AS storeId, tl.STORE_NM AS storeName,");
            sb.append("tl.STATUS_ID_2 AS statusId2, tl.IS_DELETED AS deletionIndicator,");
            sb.append("tl.GROUP_ID AS groupId,tl.GRP_NM AS groupName,");
            sb.append("tl.GRP_ID AS groupTypeId,tl.GRP_TYP_NM AS groupTypeName, ");
            sb.append("tl.SUB_GRP_NM AS subGroupTypeName, tl.SUB_GRP_ID AS subGroupTypeId ");
            sb.append(" FROM tblstorepartnerlisting tl ");
            sb.append(" WHERE 1=1 AND ");
            sb.append("tl.IS_DELETED = 0 AND tl.STATUS_ID_2 = 0 AND ");


        /*
    	 * (
			(10006 in (co_owner_id_1, co_owner_id_2 , co_owner_id_3)) and
			(10007 in (co_owner_id_1, co_owner_id_2 , co_owner_id_3)) and
			(10008 in (co_owner_id_1, co_owner_id_2 , co_owner_id_3))
			)
			and co_owner_id_4 is null and co_owner_id_5 is null
    	 */

//        if (inputParamsLength > 0) {
            if (inputParamsLength == 1 && findMatchResult.getCoOwnerId1() != null) {
                sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1)) ");
                sb.append(" AND (tl.CO_OWNER_ID_2 IS NULL AND tl.CO_OWNER_ID_3 IS NULL AND tl.CO_OWNER_ID_4 IS NULL AND tl.CO_OWNER_ID_5 IS NULL ");
                sb.append(" AND tl.CO_OWNER_ID_6 IS NULL AND tl.CO_OWNER_ID_7 IS NULL AND tl.CO_OWNER_ID_8 IS NULL AND tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL) ");
                log.info("Query : " + sb.toString());
            } else if (inputParamsLength == 2 &&
                    findMatchResult.getCoOwnerId1() != null &&
                    findMatchResult.getCoOwnerId2() != null) {
                sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2) AND  ");
                sb.append("  :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2) ) ");
                sb.append(" AND (tl.CO_OWNER_ID_3 IS NULL AND tl.CO_OWNER_ID_4 IS NULL AND tl.CO_OWNER_ID_5 IS NULL AND tl.CO_OWNER_ID_6 IS NULL ");
                sb.append(" AND tl.CO_OWNER_ID_7 IS NULL AND tl.CO_OWNER_ID_8 IS NULL AND tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL)");
                log.info("Query : " + sb.toString());
            } else if (inputParamsLength == 3 &&
                    findMatchResult.getCoOwnerId1() != null &&
                    findMatchResult.getCoOwnerId2() != null &&
                    findMatchResult.getCoOwnerId3() != null) {
                sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3) AND  ");
                sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3) AND  ");
                sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3) )  ");
                sb.append(" AND (tl.CO_OWNER_ID_4 IS NULL AND tl.CO_OWNER_ID_5 IS NULL AND tl.CO_OWNER_ID_6 IS NULL AND tl.CO_OWNER_ID_7 IS NULL ");
                sb.append(" AND tl.CO_OWNER_ID_8 IS NULL AND tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL) ");
                log.info("Query : " + sb.toString());
            } else if (inputParamsLength == 4 &&
                    findMatchResult.getCoOwnerId1() != null &&
                    findMatchResult.getCoOwnerId2() != null &&
                    findMatchResult.getCoOwnerId3() != null &&
                    findMatchResult.getCoOwnerId4() != null) {
                sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4) AND  ");
                sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4) AND  ");
                sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4) AND  ");
                sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4) )  ");
                sb.append(" AND (tl.CO_OWNER_ID_5 IS NULL AND tl.CO_OWNER_ID_6 IS NULL AND tl.CO_OWNER_ID_7 IS NULL AND tl.CO_OWNER_ID_8 IS NULL ");
                sb.append(" AND tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL)");
                log.info("Query : " + sb.toString());
            } else if (inputParamsLength == 5 &&
                    findMatchResult.getCoOwnerId1() != null &&
                    findMatchResult.getCoOwnerId2() != null &&
                    findMatchResult.getCoOwnerId3() != null &&
                    findMatchResult.getCoOwnerId4() != null &&
                    findMatchResult.getCoOwnerId5() != null) {
                sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5) AND  ");
                sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5) AND  ");
                sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5) AND  ");
                sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5) AND  ");
                sb.append("   :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5) )  ");
                sb.append(" AND (tl.CO_OWNER_ID_6 IS NULL AND tl.CO_OWNER_ID_6 IS NULL AND tl.CO_OWNER_ID_7 IS NULL AND tl.CO_OWNER_ID_8 IS NULL AND tl.CO_OWNER_ID_9 IS NULL )");
                log.info("Query : " + sb.toString());
            } else if (inputParamsLength == 6 &&
                    findMatchResult.getCoOwnerId1() != null &&
                    findMatchResult.getCoOwnerId2() != null &&
                    findMatchResult.getCoOwnerId3() != null &&
                    findMatchResult.getCoOwnerId4() != null &&
                    findMatchResult.getCoOwnerId5() != null &&
                    findMatchResult.getCoOwnerId6() != null) {
                sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6) AND  ");
                sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6) AND  ");
                sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6) AND  ");
                sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6) AND  ");
                sb.append("   :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6) AND  ");
                sb.append("   :co_owner_id_6 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6) )  ");
                sb.append(" AND (tl.CO_OWNER_ID_7 IS NULL AND tl.CO_OWNER_ID_8 IS NULL AND tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL) ");
                log.info("Query : " + sb.toString());
            } else if (inputParamsLength == 7 &&
                    findMatchResult.getCoOwnerId1() != null &&
                    findMatchResult.getCoOwnerId2() != null &&
                    findMatchResult.getCoOwnerId3() != null &&
                    findMatchResult.getCoOwnerId4() != null &&
                    findMatchResult.getCoOwnerId5() != null &&
                    findMatchResult.getCoOwnerId6() != null &&
                    findMatchResult.getCoOwnerId7() != null) {
                sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7) AND  ");
                sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7) AND  ");
                sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7) AND  ");
                sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7) AND  ");
                sb.append("   :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7) AND  ");
                sb.append("   :co_owner_id_6 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7) AND  ");
                sb.append("   :co_owner_id_7 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7) )  ");
                sb.append(" AND ( tl.CO_OWNER_ID_8 IS NULL AND tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL ) ");
                log.info("Query : " + sb.toString());
            } else if (inputParamsLength == 8 &&
                    findMatchResult.getCoOwnerId1() != null &&
                    findMatchResult.getCoOwnerId2() != null &&
                    findMatchResult.getCoOwnerId3() != null &&
                    findMatchResult.getCoOwnerId4() != null &&
                    findMatchResult.getCoOwnerId5() != null &&
                    findMatchResult.getCoOwnerId6() != null &&
                    findMatchResult.getCoOwnerId7() != null &&
                    findMatchResult.getCoOwnerId8() != null) {
                sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
                sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
                sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
                sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
                sb.append("   :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
                sb.append("   :co_owner_id_6 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
                sb.append("   :co_owner_id_7 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
                sb.append("   :co_owner_id_8 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) )  ");
                sb.append(" AND (tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL) ");
                log.info("Query : " + sb.toString());
            } else if (inputParamsLength == 9 &&
                    findMatchResult.getCoOwnerId1() != null &&
                    findMatchResult.getCoOwnerId2() != null &&
                    findMatchResult.getCoOwnerId3() != null &&
                    findMatchResult.getCoOwnerId4() != null &&
                    findMatchResult.getCoOwnerId5() != null &&
                    findMatchResult.getCoOwnerId6() != null &&
                    findMatchResult.getCoOwnerId7() != null &&
                    findMatchResult.getCoOwnerId8() != null &&
                    findMatchResult.getCoOwnerId9() != null) {
                sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
                sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
                sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
                sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
                sb.append("   :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
                sb.append("   :co_owner_id_6 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
                sb.append("   :co_owner_id_7 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
                sb.append("   :co_owner_id_8 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
                sb.append("   :co_owner_id_9 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) )  ");
                sb.append(" AND tl.CO_OWNER_ID_10 IS NULL ");
                log.info("Query : " + sb.toString());
            } else if (inputParamsLength == 10 &&
                    findMatchResult.getCoOwnerId1() != null &&
                    findMatchResult.getCoOwnerId2() != null &&
                    findMatchResult.getCoOwnerId3() != null &&
                    findMatchResult.getCoOwnerId4() != null &&
                    findMatchResult.getCoOwnerId5() != null &&
                    findMatchResult.getCoOwnerId6() != null &&
                    findMatchResult.getCoOwnerId7() != null &&
                    findMatchResult.getCoOwnerId8() != null &&
                    findMatchResult.getCoOwnerId9() != null &&
                    findMatchResult.getCoOwnerId10() != null) {
                sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
                sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
                sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
                sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
                sb.append("   :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
                sb.append("   :co_owner_id_6 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
                sb.append("   :co_owner_id_7 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
                sb.append("   :co_owner_id_8 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
                sb.append("   :co_owner_id_9 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
                sb.append("   :co_owner_id_10 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) )  ");
                log.info("Query : " + sb.toString());
            }


//        log.info("Query : " + sb.toString());

//        if (inputParamsLength > 0) {

            query = entityManager.createNativeQuery(sb.toString());

            if (findMatchResult.getCoOwnerId1() != null) {
                query.setParameter("co_owner_id_1", findMatchResult.getCoOwnerId1());
            }

            if (findMatchResult.getCoOwnerId2() != null) {
                query.setParameter("co_owner_id_2", findMatchResult.getCoOwnerId2());
            }

            if (findMatchResult.getCoOwnerId3() != null) {
                query.setParameter("co_owner_id_3", findMatchResult.getCoOwnerId3());
            }

            if (findMatchResult.getCoOwnerId4() != null) {
                query.setParameter("co_owner_id_4", findMatchResult.getCoOwnerId4());
            }

            if (findMatchResult.getCoOwnerId5() != null) {
                query.setParameter("co_owner_id_5", findMatchResult.getCoOwnerId5());
            }

            if (findMatchResult.getCoOwnerId6() != null) {
                query.setParameter("co_owner_id_6", findMatchResult.getCoOwnerId6());
            }

            if (findMatchResult.getCoOwnerId7() != null) {
                query.setParameter("co_owner_id_7", findMatchResult.getCoOwnerId7());
            }

            if (findMatchResult.getCoOwnerId8() != null) {
                query.setParameter("co_owner_id_8", findMatchResult.getCoOwnerId8());
            }

            if (findMatchResult.getCoOwnerId9() != null) {
                query.setParameter("co_owner_id_9", findMatchResult.getCoOwnerId9());
            }

            if (findMatchResult.getCoOwnerId10() != null) {
                query.setParameter("co_owner_id_10", findMatchResult.getCoOwnerId10());
            }
//        log.info("Query AFTER INPUT : " + query);
            query.setHint("org.hibernate.fetchSize", "50");
        }
        return query.getResultList();
    }

    /**
     * @param findMatchResult
     * @return
     */
    private int getInputParamsLength(FindMatchResult findMatchResult) {
        int counter = 0;
        counter = (findMatchResult.getCoOwnerId1() != null) ? (counter + 1) : counter;
        counter = (findMatchResult.getCoOwnerId2() != null) ? (counter + 1) : counter;
        counter = (findMatchResult.getCoOwnerId3() != null) ? (counter + 1) : counter;
        counter = (findMatchResult.getCoOwnerId4() != null) ? (counter + 1) : counter;
        counter = (findMatchResult.getCoOwnerId5() != null) ? (counter + 1) : counter;
        counter = (findMatchResult.getCoOwnerId6() != null) ? (counter + 1) : counter;
        counter = (findMatchResult.getCoOwnerId7() != null) ? (counter + 1) : counter;
        counter = (findMatchResult.getCoOwnerId8() != null) ? (counter + 1) : counter;
        counter = (findMatchResult.getCoOwnerId9() != null) ? (counter + 1) : counter;
        counter = (findMatchResult.getCoOwnerId10() != null) ? (counter + 1) : counter;
        return counter;
    }


    //	@Override
    //	public List<String[]> findStorePartnerListingByCOOwnerId (FindMatchResult findMatchResult) {
    //		int inputParamsLength = getInputParamsLength (findMatchResult);
    //
    //        StringBuilder sb = new StringBuilder();
    //        sb.append(" SELECT ");
    //        sb.append("tl.CO_OWNER_ID_1 AS coOwnerId1, tl.CO_OWNER_ID_2 AS coOwnerId2, ");
    //        sb.append("tl.CO_OWNER_ID_3 AS coOwnerId3, tl.CO_OWNER_ID_4 AS coOwnerId4, tl.CO_OWNER_ID_5 AS coOwnerId5, ");
    //        sb.append("tl.CO_OWNER_NAME_1 AS coOwnerName1, tl.CO_OWNER_NAME_2 AS coOwnerName2, tl.CO_OWNER_NAME_3 AS coOwnerName3, ");
    //        sb.append("tl.CO_OWNER_NAME_4 AS coOwnerName4, tl.CO_OWNER_NAME_5 AS coOwnerName5, ");
    //        sb.append("tl.CO_OWNER_PER_1 AS coOwnerPercentage1, tl.CO_OWNER_PER_2 AS coOwnerPercentage2, ");
    //        sb.append("tl.CO_OWNER_PER_3 AS coOwnerPercentage3, tl.CO_OWNER_PER_4 AS coOwnerPercentage4, ");
    //        sb.append("tl.CO_OWNER_PER_5 AS coOwnerPercentage5, tl.STORE_NM AS storeName ");
    //        sb.append("FROM tblstorepartnerlisting tl ");
    //        sb.append(" WHERE 1=1 AND ");
    //
    //        /*
    //    	 * (
    //			(10006 in (co_owner_id_1, co_owner_id_2 , co_owner_id_3)) and
    //			(10007 in (co_owner_id_1, co_owner_id_2 , co_owner_id_3)) and
    //			(10008 in (co_owner_id_1, co_owner_id_2 , co_owner_id_3))
    //			)
    //			and co_owner_id_4 is null and co_owner_id_5 is null
    //    	 */
    //
    //        if (inputParamsLength == 1 && findMatchResult.getCoOwnerId1() != null) {
    //			 sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1)) ");
    //			 sb.append(" AND tl.CO_OWNER_ID_2 IS NULL AND tl.CO_OWNER_ID_3 IS NULL AND tl.CO_OWNER_ID_4 IS NULL AND tl.CO_OWNER_ID_5 IS NULL");
    //			 log.info("Query : " + sb.toString());
    //		} else if (inputParamsLength == 2 &&
    //				findMatchResult.getCoOwnerId1() != null &&
    //				findMatchResult.getCoOwnerId2() != null) {
    //			 sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2) AND  ");
    //			 sb.append("  :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2) ) ");
    //			 sb.append(" AND tl.CO_OWNER_ID_3 IS NULL AND tl.CO_OWNER_ID_4 IS NULL AND tl.CO_OWNER_ID_5 IS NULL");
    //			 log.info("Query : " + sb.toString());
    //		} else if (inputParamsLength == 3 &&
    //				findMatchResult.getCoOwnerId1() != null &&
    //				findMatchResult.getCoOwnerId2() != null &&
    //				findMatchResult.getCoOwnerId3() != null) {
    //			 sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3) AND  ");
    //			 sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3) AND  ");
    //			 sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3) )  ");
    //			 sb.append(" AND tl.CO_OWNER_ID_4 IS NULL AND tl.CO_OWNER_ID_5 IS NULL");
    //			 log.info("Query : " + sb.toString());
    //		} else if (inputParamsLength == 4 &&
    //				findMatchResult.getCoOwnerId1() != null &&
    //				findMatchResult.getCoOwnerId2() != null &&
    //				findMatchResult.getCoOwnerId3() != null &&
    //				findMatchResult.getCoOwnerId4() != null) {
    //			 sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4) AND  ");
    //			 sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4) AND  ");
    //			 sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4) AND  ");
    //			 sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4) )  ");
    //			 sb.append(" AND t1.CO_OWNER_ID_5 IS NULL");
    //			 log.info("Query : " + sb.toString());
    //		} else if (inputParamsLength == 5 &&
    //				findMatchResult.getCoOwnerId1() != null &&
    //				findMatchResult.getCoOwnerId2() != null &&
    //				findMatchResult.getCoOwnerId3() != null &&
    //				findMatchResult.getCoOwnerId4() != null &&
    //				findMatchResult.getCoOwnerId5() != null) {
    //			 sb.append(" ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, t1.CO_OWNER_ID_5) AND  ");
    //			 sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, t1.CO_OWNER_ID_5) AND  ");
    //			 sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, t1.CO_OWNER_ID_5) AND  ");
    //			 sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, t1.CO_OWNER_ID_5) AND  ");
    //			 sb.append("   :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, t1.CO_OWNER_ID_5) )  ");
    //			 log.info("Query : " + sb.toString());
    //		}
    //
    ////        log.info("Query : " + sb.toString());
    //
    //        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());
    //
    //        if (findMatchResult.getCoOwnerId1()  != null) {
    //			 query.setParameter("co_owner_id_1", findMatchResult.getCoOwnerId1() );
    //		}
    //
    //        if (findMatchResult.getCoOwnerId2()  != null) {
    //			 query.setParameter("co_owner_id_2", findMatchResult.getCoOwnerId2() );
    //		}
    //
    //        if (findMatchResult.getCoOwnerId3()  != null) {
    //			 query.setParameter("co_owner_id_3", findMatchResult.getCoOwnerId3() );
    //		}
    //
    //        if (findMatchResult.getCoOwnerId4()  != null) {
    //			 query.setParameter("co_owner_id_4", findMatchResult.getCoOwnerId4() );
    //		}
    //
    //        if (findMatchResult.getCoOwnerId5()  != null) {
    //			 query.setParameter("co_owner_id_5", findMatchResult.getCoOwnerId5() );
    //		}
    //
    ////        log.info("Query AFTER INPUT : " + query);
    //        query.setHint("org.hibernate.fetchSize", "50");
    //        return query.getResultList();
    //    }
    //
    //	/**
    //	 *
    //	 * @param findMatchResult
    //	 * @return
    //	 */
    //	private int getInputParamsLength (FindMatchResult findMatchResult) {
    //		int counter = 0;
    //		counter = (findMatchResult.getCoOwnerId1() != null) ? (counter + 1) : counter;
    //		counter = (findMatchResult.getCoOwnerId2() != null) ? (counter + 1) : counter;
    //		counter = (findMatchResult.getCoOwnerId3() != null) ? (counter + 1) : counter;
    //		counter = (findMatchResult.getCoOwnerId4() != null) ? (counter + 1) : counter;
    //		counter = (findMatchResult.getCoOwnerId5() != null) ? (counter + 1) : counter;
    //		return counter;
    //	}
    //}

    @Override
    public List<String[]> findByExact(
            Long coOwnerId1, Long coOwnerId2, Long coOwnerId3, Long coOwnerId4, Long coOwnerId5,
            Long coOwnerId6, Long coOwnerId7, Long coOwnerId8, Long coOwnerId9, Long coOwnerId10) {

        int inputParamsLength = 0;
        if (coOwnerId1 != null) inputParamsLength++;
        if (coOwnerId2 != null) inputParamsLength++;
        if (coOwnerId3 != null) inputParamsLength++;
        if (coOwnerId4 != null) inputParamsLength++;
        if (coOwnerId5 != null) inputParamsLength++;
        if (coOwnerId6 != null) inputParamsLength++;
        if (coOwnerId7 != null) inputParamsLength++;
        if (coOwnerId8 != null) inputParamsLength++;
        if (coOwnerId9 != null) inputParamsLength++;
        if (coOwnerId10 != null) inputParamsLength++;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append("tl.CO_OWNER_ID_1 AS coOwnerId1, tl.CO_OWNER_ID_2 AS coOwnerId2, ");
        sb.append("tl.CO_OWNER_ID_3 AS coOwnerId3, tl.CO_OWNER_ID_4 AS coOwnerId4, tl.CO_OWNER_ID_5 AS coOwnerId5, ");
        sb.append("tl.CO_OWNER_ID_6 AS coOwnerId6, tl.CO_OWNER_ID_7 AS coOwnerId7, ");
        sb.append("tl.CO_OWNER_ID_8 AS coOwnerId8, tl.CO_OWNER_ID_9 AS coOwnerId9, tl.CO_OWNER_ID_10 AS coOwnerId10, ");
        sb.append("tl.CO_OWNER_NAME_1 AS coOwnerName1, tl.CO_OWNER_NAME_2 AS coOwnerName2, tl.CO_OWNER_NAME_3 AS coOwnerName3, ");
        sb.append("tl.CO_OWNER_NAME_4 AS coOwnerName4, tl.CO_OWNER_NAME_5 AS coOwnerName5, ");
        sb.append("tl.CO_OWNER_NAME_6 AS coOwnerName6, tl.CO_OWNER_NAME_7 AS coOwnerName7, ");
        sb.append("tl.CO_OWNER_NAME_8 AS coOwnerName8, tl.CO_OWNER_NAME_9 AS coOwnerName9, tl.CO_OWNER_NAME_10 AS coOwnerName10, ");
        sb.append("tl.CO_OWNER_PER_1 AS coOwnerPercentage1, tl.CO_OWNER_PER_2 AS coOwnerPercentage2, ");
        sb.append("tl.CO_OWNER_PER_3 AS coOwnerPercentage3, tl.CO_OWNER_PER_4 AS coOwnerPercentage4, ");
        sb.append("tl.CO_OWNER_PER_5 AS coOwnerPercentage5, tl.CO_OWNER_PER_6 AS coOwnerPercentage6,");
        sb.append("tl.CO_OWNER_PER_7 AS coOwnerPercentage7, tl.CO_OWNER_PER_8 AS coOwnerPercentage8, ");
        sb.append("tl.CO_OWNER_PER_9 AS coOwnerPercentage9, tl.CO_OWNER_PER_10 AS coOwnerPercentage10,");
        sb.append("tl.STORE_ID AS storeId, tl.STORE_NM AS storeName,");
        sb.append("tl.STATUS_ID_2 AS statusId2, tl.IS_DELETED AS deletionIndicator,");
        sb.append("tl.GROUP_ID AS groupId,tl.GRP_NM AS groupName,");
        sb.append("tl.GRP_ID AS groupTypeId,tl.GRP_TYP_NM AS groupTypeName, ");
        sb.append("tl.SUB_GRP_NM AS subGroupTypeName, tl.SUB_GRP_ID AS subGroupTypeId ");
        sb.append(" FROM tblstorepartnerlisting tl ");
        sb.append(" WHERE 1=1 AND ");
        sb.append("tl.IS_DELETED = 0 AND tl.STATUS_ID_2 = 0 ");

        if (inputParamsLength == 1 && coOwnerId1 != null) {
            sb.append(" AND :co_owner_id_1 IN (tl.CO_OWNER_ID_1) ");
            sb.append(" AND (tl.CO_OWNER_ID_2 IS NULL AND tl.CO_OWNER_ID_3 IS NULL AND tl.CO_OWNER_ID_4 IS NULL AND tl.CO_OWNER_ID_5 IS NULL ");
            sb.append(" AND tl.CO_OWNER_ID_6 IS NULL AND tl.CO_OWNER_ID_7 IS NULL AND tl.CO_OWNER_ID_8 IS NULL AND tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL) ");
            log.info("Query : " + sb.toString());
        } else if (inputParamsLength == 2 && coOwnerId1 != null && coOwnerId2 != null) {
            sb.append(" AND :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2) AND  ");
            sb.append("  :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2)  ");
            sb.append(" AND (tl.CO_OWNER_ID_3 IS NULL AND tl.CO_OWNER_ID_4 IS NULL AND tl.CO_OWNER_ID_5 IS NULL AND tl.CO_OWNER_ID_6 IS NULL ");
            sb.append(" AND tl.CO_OWNER_ID_7 IS NULL AND tl.CO_OWNER_ID_8 IS NULL AND tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL)");
            log.info("Query : " + sb.toString());
        } else if (inputParamsLength == 3 && coOwnerId1 != null && coOwnerId2 != null && coOwnerId3 != null) {
            sb.append(" AND :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3) AND  ");
            sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3) AND  ");
            sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3)   ");
            sb.append(" AND (tl.CO_OWNER_ID_4 IS NULL AND tl.CO_OWNER_ID_5 IS NULL AND tl.CO_OWNER_ID_6 IS NULL AND tl.CO_OWNER_ID_7 IS NULL ");
            sb.append(" AND tl.CO_OWNER_ID_8 IS NULL AND tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL) ");
            log.info("Query : " + sb.toString());
        } else if (inputParamsLength == 4 && coOwnerId1 != null && coOwnerId2 != null && coOwnerId3 != null && coOwnerId4 != null) {
            sb.append(" AND :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4) AND  ");
            sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4) AND  ");
            sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4) AND  ");
            sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4)  ");
            sb.append(" AND (tl.CO_OWNER_ID_5 IS NULL AND tl.CO_OWNER_ID_6 IS NULL AND tl.CO_OWNER_ID_7 IS NULL AND tl.CO_OWNER_ID_8 IS NULL ");
            sb.append(" AND tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL)");
            log.info("Query : " + sb.toString());
        } else if (inputParamsLength == 5 && coOwnerId1 != null && coOwnerId2 != null && coOwnerId3 != null && coOwnerId4 != null && coOwnerId5 != null) {
            sb.append(" AND :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5) AND  ");
            sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5) AND  ");
            sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5) AND  ");
            sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5) AND  ");
            sb.append("   :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5)   ");
            sb.append(" AND (tl.CO_OWNER_ID_6 IS NULL AND tl.CO_OWNER_ID_7 IS NULL AND tl.CO_OWNER_ID_8 IS NULL AND tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL )");
//            log.info("Query : " + sb.toString());
        } else if (inputParamsLength == 6 && coOwnerId1 != null && coOwnerId2 != null && coOwnerId3 != null && coOwnerId4 != null &&
                coOwnerId5 != null && coOwnerId6 != null) {
            sb.append(" AND :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6) AND  ");
            sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6) AND  ");
            sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6) AND  ");
            sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6) AND  ");
            sb.append("   :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6) AND  ");
            sb.append("   :co_owner_id_6 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6)   ");
            sb.append(" AND (tl.CO_OWNER_ID_7 IS NULL AND tl.CO_OWNER_ID_8 IS NULL AND tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL) ");
//            log.info("Query : " + sb.toString());
        } else if (inputParamsLength == 7 &&
                coOwnerId1 != null && coOwnerId2 != null && coOwnerId3 != null && coOwnerId4 != null &&
                coOwnerId5 != null && coOwnerId6 != null && coOwnerId7 != null) {
            sb.append(" AND :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7) AND  ");
            sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7) AND  ");
            sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7) AND  ");
            sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7) AND  ");
            sb.append("   :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7) AND  ");
            sb.append("   :co_owner_id_6 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7) AND  ");
            sb.append("   :co_owner_id_7 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7)   ");
            sb.append(" AND ( tl.CO_OWNER_ID_8 IS NULL AND tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL ) ");
//            log.info("Query : " + sb.toString());
        } else if (inputParamsLength == 8 && coOwnerId1 != null && coOwnerId2 != null && coOwnerId3 != null && coOwnerId4 != null &&
                coOwnerId5 != null && coOwnerId6 != null && coOwnerId7 != null && coOwnerId8 != null) {
            sb.append(" AND :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
            sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
            sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
            sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
            sb.append("   :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
            sb.append("   :co_owner_id_6 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
            sb.append("   :co_owner_id_7 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8) AND  ");
            sb.append("   :co_owner_id_8 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8)   ");
            sb.append(" AND (tl.CO_OWNER_ID_9 IS NULL AND tl.CO_OWNER_ID_10 IS NULL) ");
//            log.info("Query : " + sb.toString());
        } else if (inputParamsLength == 9 && coOwnerId1 != null && coOwnerId2 != null && coOwnerId3 != null && coOwnerId4 != null &&
                coOwnerId5 != null && coOwnerId6 != null && coOwnerId7 != null && coOwnerId8 != null && coOwnerId9 != null) {
            sb.append(" AND :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
            sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
            sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
            sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
            sb.append("   :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
            sb.append("   :co_owner_id_6 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
            sb.append("   :co_owner_id_7 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
            sb.append("   :co_owner_id_8 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9) AND  ");
            sb.append("   :co_owner_id_9 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9)   ");
            sb.append(" AND tl.CO_OWNER_ID_10 IS NULL ");
//            log.info("Query : " + sb.toString());
        } else if (inputParamsLength == 10 &&
                coOwnerId1 != null && coOwnerId2 != null && coOwnerId3 != null && coOwnerId4 != null && coOwnerId5 != null &&
                coOwnerId6 != null && coOwnerId7 != null && coOwnerId8 != null && coOwnerId9 != null && coOwnerId10 != null) {
            sb.append(" AND :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
            sb.append("   :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
            sb.append("   :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
            sb.append("   :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
            sb.append("   :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
            sb.append("   :co_owner_id_6 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
            sb.append("   :co_owner_id_7 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
            sb.append("   :co_owner_id_8 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
            sb.append("   :co_owner_id_9 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND  ");
            sb.append("   :co_owner_id_10 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)   ");
            log.info("Query : " + sb.toString());
        }

//        if(coOwnerId1 != null) {
//            sb.append(" AND :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)  ");
//        }
//        if(coOwnerId2 != null) {
//            sb.append("  AND :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        if(coOwnerId3 != null) {
//            sb.append("  AND :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        if(coOwnerId4 != null) {
//            sb.append(" AND :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        if(coOwnerId5 != null) {
//            sb.append("  AND :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        if(coOwnerId6 != null) {
//            sb.append(" AND :co_owner_id_6 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        if(coOwnerId7 != null) {
//            sb.append(" AND :co_owner_id_7 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//                sb.append("  :co_owner_id_8 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND ");
//        if (coOwnerId8 != null) {
//            sb.append(" AND :co_owner_id_8 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        if(coOwnerId9 != null) {
//            sb.append(" AND :co_owner_id_9 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        if(coOwnerId10 != null) {
//            sb.append("  AND :co_owner_id_10 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }

        query = entityManager.createNativeQuery(sb.toString());

        log.info("Query : " + sb.toString());

//        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());

        if (coOwnerId1 != null) {
            query.setParameter("co_owner_id_1", coOwnerId1);
        }

        if (coOwnerId2 != null) {
            query.setParameter("co_owner_id_2", coOwnerId2);
        }

        if (coOwnerId3 != null) {
            query.setParameter("co_owner_id_3", coOwnerId3);
        }

        if (coOwnerId4 != null) {
            query.setParameter("co_owner_id_4", coOwnerId4);
        }

        if (coOwnerId5 != null) {
            query.setParameter("co_owner_id_5", coOwnerId5);
        }

        if (coOwnerId6 != null) {
            query.setParameter("co_owner_id_6", coOwnerId6);
        }

        if (coOwnerId7 != null) {
            query.setParameter("co_owner_id_7", coOwnerId7);
        }

        if (coOwnerId8 != null) {
            query.setParameter("co_owner_id_8", coOwnerId8);
        }

        if (coOwnerId9 != null) {
            query.setParameter("co_owner_id_9", coOwnerId9);
        }

        if (coOwnerId10 != null) {
            query.setParameter("co_owner_id_10", coOwnerId10);
        }

        if (inputParamsLength == 0) {
            return Collections.emptyList();
        }


        log.info("Query AFTER INPUT : " + query);
        query.setHint("org.hibernate.fetchSize", "50");
        return query.getResultList();
    }


//    @Override
//    public List<String[]> findByExact(
//            List<String> coOwnerId1, List<String> coOwnerId2, List<String> coOwnerId3, List<String> coOwnerId4, List<String> coOwnerId5,
//            List<String> coOwnerId6, List<String> coOwnerId7, List<String> coOwnerId8, List<String> coOwnerId9, List<String> coOwnerId10) {
//        int inputParamsLength = 0;
//        if (coOwnerId1 != null) inputParamsLength++;
//        if (coOwnerId2 != null) inputParamsLength++;
//        if (coOwnerId3 != null) inputParamsLength++;
//        if (coOwnerId4 != null) inputParamsLength++;
//        if (coOwnerId5 != null) inputParamsLength++;
//        if (coOwnerId6 != null) inputParamsLength++;
//        if (coOwnerId7 != null) inputParamsLength++;
//        if (coOwnerId8 != null) inputParamsLength++;
//        if (coOwnerId9 != null) inputParamsLength++;
//        if (coOwnerId10 != null) inputParamsLength++;
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(" SELECT ");
//        sb.append("tl.CO_OWNER_ID_1 AS coOwnerId1, tl.CO_OWNER_ID_2 AS coOwnerId2, ");
//        sb.append("tl.CO_OWNER_ID_3 AS coOwnerId3, tl.CO_OWNER_ID_4 AS coOwnerId4, tl.CO_OWNER_ID_5 AS coOwnerId5, ");
//        sb.append("tl.CO_OWNER_ID_6 AS coOwnerId6, tl.CO_OWNER_ID_7 AS coOwnerId7, ");
//        sb.append("tl.CO_OWNER_ID_8 AS coOwnerId8, tl.CO_OWNER_ID_9 AS coOwnerId9, tl.CO_OWNER_ID_10 AS coOwnerId10, ");
//        sb.append("tl.CO_OWNER_NAME_1 AS coOwnerName1, tl.CO_OWNER_NAME_2 AS coOwnerName2, tl.CO_OWNER_NAME_3 AS coOwnerName3, ");
//        sb.append("tl.CO_OWNER_NAME_4 AS coOwnerName4, tl.CO_OWNER_NAME_5 AS coOwnerName5, ");
//        sb.append("tl.CO_OWNER_NAME_6 AS coOwnerName6, tl.CO_OWNER_NAME_7 AS coOwnerName7, ");
//        sb.append("tl.CO_OWNER_NAME_8 AS coOwnerName8, tl.CO_OWNER_NAME_9 AS coOwnerName9, tl.CO_OWNER_NAME_10 AS coOwnerName10, ");
//        sb.append("tl.CO_OWNER_PER_1 AS coOwnerPercentage1, tl.CO_OWNER_PER_2 AS coOwnerPercentage2, ");
//        sb.append("tl.CO_OWNER_PER_3 AS coOwnerPercentage3, tl.CO_OWNER_PER_4 AS coOwnerPercentage4, ");
//        sb.append("tl.CO_OWNER_PER_5 AS coOwnerPercentage5, tl.CO_OWNER_PER_6 AS coOwnerPercentage6,");
//        sb.append("tl.CO_OWNER_PER_7 AS coOwnerPercentage7, tl.CO_OWNER_PER_8 AS coOwnerPercentage8, ");
//        sb.append("tl.CO_OWNER_PER_9 AS coOwnerPercentage9, tl.CO_OWNER_PER_10 AS coOwnerPercentage10,");
//        sb.append("tl.STORE_ID AS storeId, tl.STORE_NM AS storeName,");
//        sb.append("tl.STATUS_ID_2 AS statusId2, tl.IS_DELETED AS deletionIndicator,");
//        sb.append("tl.GROUP_ID AS groupId,tl.GRP_NM AS groupName,");
//        sb.append("tl.GRP_ID AS groupTypeId,tl.GRP_TYP_NM AS groupTypeName, ");
//        sb.append("tl.SUB_GRP_NM AS subGroupTypeName, tl.SUB_GRP_ID AS subGroupTypeId ");
//        sb.append(" FROM tblstorepartnerlisting tl ");
//        sb.append(" WHERE 1=1 AND ");
//        sb.append("tl.IS_DELETED = 0 AND tl.STATUS_ID_2 = 0 ");
//
//
//        if (coOwnerId1 != null) {
//            sb.append(" AND (:co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//            sb.append(" OR :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)) ");
//        }
//
//        if (coOwnerId2 != null) {
//            sb.append(" AND (:co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//            sb.append(" OR :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)) ");
//        }
//
//// Repeat similar conditions for other coOwnerIds.
//
//        if(coOwnerId3 != null) {
//            sb.append("  AND :co_owner_id_3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        if(coOwnerId4 != null) {
//            sb.append(" AND :co_owner_id_4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        if(coOwnerId5 != null) {
//            sb.append("  AND :co_owner_id_5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        if(coOwnerId6 != null) {
//            sb.append(" AND :co_owner_id_6 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        if(coOwnerId7 != null) {
//            sb.append(" AND :co_owner_id_7 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        //        sb.append("  :co_owner_id_8 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) AND ");
//        if (coOwnerId8 != null) {
//            sb.append(" AND :co_owner_id_8 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        if(coOwnerId9 != null) {
//            sb.append(" AND :co_owner_id_9 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//        if(coOwnerId10 != null) {
//            sb.append("  AND :co_owner_id_10 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10) ");
//        }
//
//        query = entityManager.createNativeQuery(sb.toString());
//
//        log.info("Query : " + sb.toString());
//
////        javax.persistence.Query query = entityManager.createNativeQuery(sb.toString());
//
//        if (coOwnerId1 != null) {
//            query.setParameter("co_owner_id_1", coOwnerId1);
//        }
//
//        if (coOwnerId2 != null) {
//            query.setParameter("co_owner_id_2", coOwnerId2);
//        }
//
//        if (coOwnerId3 != null) {
//            query.setParameter("co_owner_id_3", coOwnerId3);
//        }
//
//        if (coOwnerId4 != null) {
//            query.setParameter("co_owner_id_4", coOwnerId4);
//        }
//
//        if (coOwnerId5 != null) {
//            query.setParameter("co_owner_id_5", coOwnerId5);
//        }
//
//        if (coOwnerId6 != null) {
//            query.setParameter("co_owner_id_6", coOwnerId6);
//        }
//
//        if (coOwnerId7 != null) {
//            query.setParameter("co_owner_id_7", coOwnerId7);
//        }
//
//        if (coOwnerId8 != null) {
//            query.setParameter("co_owner_id_8", coOwnerId8);
//        }
//
//        if (coOwnerId9 != null) {
//            query.setParameter("co_owner_id_9", coOwnerId9);
//        }
//
//        if (coOwnerId10 != null) {
//            query.setParameter("co_owner_id_10", coOwnerId10);
//        }
//
//        if (inputParamsLength == 0) {
//            return Collections.emptyList();
//        }
//
//
//        log.info("Query AFTER INPUT : " + query);
//        query.setHint("org.hibernate.fetchSize", "50");
//        return query.getResultList();
//    }


    }
