package com.mnrclara.api.cg.transaction.model.storepartnerlisting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessExactLogic {

    /**
     * @param result
     * @return
     */
    private static Long extractData(String result) {
        log.info("result-------> : " + result);
        if (result != null && !result.startsWith("null")) {
            result = result.substring(0, result.indexOf("-"));
            return Long.valueOf(result);
        }
        return 0L;
    }

    /**
     * @param findMatchResult
     * @param exactMatchGroupList
     * @return
     */
    private static Set<ExactMatchResultV2> extractResults(FindMatchResult findMatchResult, List<ExactMatchResultV2> exactMatchGroupList) {
        try {
            Set<ExactMatchResultV2> exactMatchResultList = new HashSet<>();
            int inputLength = inputParamsLength(findMatchResult);
            log.info("inputLength == >>> " + inputLength);
            exactMatchGroupList.stream().forEach(e -> {
                ExactMatchResultV2 s1 = findOut(e, findMatchResult.getCoOwnerId1());
                ExactMatchResultV2 s2 = findOut(e, findMatchResult.getCoOwnerId2());
                ExactMatchResultV2 s3 = findOut(e, findMatchResult.getCoOwnerId3());
                ExactMatchResultV2 s4 = findOut(e, findMatchResult.getCoOwnerId4());
                ExactMatchResultV2 s5 = findOut(e, findMatchResult.getCoOwnerId5());
                System.out.println("---ExactMatchResult---> : " + s1 + "," + s2 + "," + s3 + "," + s4 + "," + s5);

                exactMatchResultList.add(s1);
                exactMatchResultList.add(s2);
                exactMatchResultList.add(s3);
                exactMatchResultList.add(s4);
                exactMatchResultList.add(s5);
            });
            System.out.println("finalCollections ---> : " + exactMatchResultList);
            return exactMatchResultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param findMatchResult
     * @return
     */
    private static int inputParamsLength(FindMatchResult findMatchResult) {
        int counter = 0;
        counter = (findMatchResult.getCoOwnerId1() != null) ? (counter + 1) : counter;
        counter = (findMatchResult.getCoOwnerId2() != null) ? (counter + 1) : counter;
        counter = (findMatchResult.getCoOwnerId3() != null) ? (counter + 1) : counter;
        counter = (findMatchResult.getCoOwnerId4() != null) ? (counter + 1) : counter;
        counter = (findMatchResult.getCoOwnerId5() != null) ? (counter + 1) : counter;
        return counter;
    }

    /**
     * @param e
     * @param ip
     * @return
     */
    private static ExactMatchResultV2 findOut(ExactMatchResultV2 e, Long ip) {
//		return populateCoOwnerStoresPercentage(e);
        if (e.getCoOwnerId1() == ip) {
            return populateCoOwnerStoresPercentage(e);
        } else if (e.getCoOwnerId2() == ip) {
            return populateCoOwnerStoresPercentage(e);
        } else if (e.getCoOwnerId3() == ip) {
            return populateCoOwnerStoresPercentage(e);
        } else if (e.getCoOwnerId4() == ip) {
            return populateCoOwnerStoresPercentage(e);
        } else if (e.getCoOwnerId5() == ip) {
            return populateCoOwnerStoresPercentage(e);
        }
        return null;
    }

    /**
     * @param e
     * @return
     */
    private static ExactMatchResultV2 populateCoOwnerStoresPercentage(ExactMatchResultV2 e) {
        log.info("e--1-----> : " + e.getCoOwnerName1());
        log.info("e---2----> : " + e.getCoOwnerName2());
        ExactMatchResultV2 exactMatchResult = new ExactMatchResultV2();
        exactMatchResult.setCoOwnerId1(e.getCoOwnerId1());
        exactMatchResult.setCoOwnerId2(e.getCoOwnerId2());
        exactMatchResult.setCoOwnerId3(e.getCoOwnerId3());
        exactMatchResult.setCoOwnerId4(e.getCoOwnerId4());
        exactMatchResult.setCoOwnerId5(e.getCoOwnerId5());
        exactMatchResult.setCoOwnerPercentage1(e.getCoOwnerPercentage1());
        exactMatchResult.setCoOwnerPercentage2(e.getCoOwnerPercentage2());
        exactMatchResult.setCoOwnerPercentage3(e.getCoOwnerPercentage3());
        exactMatchResult.setCoOwnerPercentage4(e.getCoOwnerPercentage4());
        exactMatchResult.setCoOwnerPercentage5(e.getCoOwnerPercentage5());
        exactMatchResult.setCoOwnerName1(e.getCoOwnerId1() + "-" + e.getCoOwnerName1());
        exactMatchResult.setCoOwnerName2(e.getCoOwnerId2() + "-" + e.getCoOwnerName2());
        exactMatchResult.setCoOwnerName3(e.getCoOwnerId3() + "-" + e.getCoOwnerName3());
        exactMatchResult.setCoOwnerName4(e.getCoOwnerId4() + "-" + e.getCoOwnerName4());
        exactMatchResult.setCoOwnerName5(e.getCoOwnerId5() + "-" + e.getCoOwnerName5());
        exactMatchResult.setStoreId(e.getStoreId());
        exactMatchResult.setStoreName(e.getStoreName());
        log.info("" + exactMatchResult);
        return exactMatchResult;
    }

    private static ExactMatchResultV2 populateCoOwnerStoresPercentage2(ExactMatchResultV2 e) {
        ExactMatchResultV2 exactMatchResult = new ExactMatchResultV2();
        exactMatchResult.setCoOwnerId1(e.getCoOwnerId1());
        exactMatchResult.setCoOwnerId2(e.getCoOwnerId2());
        exactMatchResult.setCoOwnerId3(e.getCoOwnerId3());
        exactMatchResult.setCoOwnerId4(e.getCoOwnerId4());
        exactMatchResult.setCoOwnerId5(e.getCoOwnerId5());
        exactMatchResult.setCoOwnerPercentage1(e.getCoOwnerPercentage1());
        exactMatchResult.setCoOwnerPercentage2(e.getCoOwnerPercentage2());
        exactMatchResult.setCoOwnerPercentage3(e.getCoOwnerPercentage3());
        exactMatchResult.setCoOwnerPercentage4(e.getCoOwnerPercentage4());
        exactMatchResult.setCoOwnerPercentage5(e.getCoOwnerPercentage5());
        exactMatchResult.setCoOwnerName1(e.getCoOwnerId1() + "-" + e.getCoOwnerName1());
        exactMatchResult.setCoOwnerName2(e.getCoOwnerId2() + "-" + e.getCoOwnerName2());
        exactMatchResult.setCoOwnerName3(e.getCoOwnerId3() + "-" + e.getCoOwnerName3());
        exactMatchResult.setCoOwnerName4(e.getCoOwnerId4() + "-" + e.getCoOwnerName4());
        exactMatchResult.setCoOwnerName5(e.getCoOwnerId5() + "-" + e.getCoOwnerName5());
        exactMatchResult.setStoreId(e.getStoreId());
        exactMatchResult.setStoreName(e.getStoreName());
        log.info("" + exactMatchResult);
        return exactMatchResult;
    }

    /**
     * @param findMatchResult
     * @param list
     * @return
     */
    public List<ExactMatchResultV2> doProcessExactSearch(FindMatchResult findMatchResult, List<ExactMatchResultV2> list) {
        log.info("ExactMatchResult from DB : " + list);
        Set<ExactMatchResultV2> setExactMatchResult = extractResults(findMatchResult, list);
        List<ExactMatchResultV2> listExactMatchResult = new ArrayList<>(setExactMatchResult);
        return listExactMatchResult;
    }

    /**
     * @return
     */
    private static List<ExactMatchGroup> exactMatchGroups() {
        List<ExactMatchGroup> exactMatchGroups = new ArrayList<>();
        ExactMatchGroup exactMatchGroup1 = new ExactMatchGroup();
        ExactMatchGroup exactMatchGroup2 = new ExactMatchGroup();
        ExactMatchGroup exactMatchGroup3 = new ExactMatchGroup();
        List<Stores> stores = new ArrayList<>();
        List<Stores> stores2 = new ArrayList<>();
        List<Stores> stores3 = new ArrayList<>();

        /*
         * "storeId": "10", "storeName": "10-1", "coOwnerName1": "10012-CO1",
         * "coOwnerName2": "10017-CO2", "coOwnerName3": "10014-CO3", "coOwnerName4":
         * "null-null", "coOwnerName5": "null-null",
         */
        Stores store = new Stores();
        store.setStoreId("10");
        store.setStoreName("XXXX");
        store.setCoOwnerName1("10012-CO1");
        store.setCoOwnerName2("10017-CO2");
        store.setCoOwnerName3("10014-CO3");
        store.setCoOwnerName4("null-null");
        store.setCoOwnerName5("null-null");
        store.setCoOwnerPercentage1(45D);
        store.setCoOwnerPercentage2(56D);
        store.setCoOwnerPercentage3(66D);
        stores.add(store);
        exactMatchGroup1.setStores(stores);

        Stores store1 = new Stores();
        store1.setStoreId("101");
        store1.setStoreName("YYYY");
        store1.setCoOwnerName1("10012-CO4");
        store1.setCoOwnerName2("10017-CO5");
        store1.setCoOwnerName3("10014-CO6");
        store1.setCoOwnerName4("null-null");
        store1.setCoOwnerName5("null-null");
        store1.setCoOwnerPercentage1(22D);
        store1.setCoOwnerPercentage2(61D);
        store1.setCoOwnerPercentage3(87D);
        stores2.add(store1);
        exactMatchGroup2.setStores(stores2);

        Stores store2 = new Stores();
        store2.setStoreId("1011");
        store2.setStoreName("ZZZZ");
        store2.setCoOwnerName1("10012-10012-Salvador Ortega");
        store2.setCoOwnerName2("10014-10014-ROGA LLC");
        store2.setCoOwnerName3("null-null");
        store2.setCoOwnerName4("null-null");
        store2.setCoOwnerName5("null-null");
        store2.setCoOwnerPercentage1(82D);
        store2.setCoOwnerPercentage2(13D);
        store2.setCoOwnerPercentage3(54D);
        stores3.add(store2);
        exactMatchGroup3.setStores(stores3);

        exactMatchGroups.add(exactMatchGroup1);
        exactMatchGroups.add(exactMatchGroup2);
        exactMatchGroups.add(exactMatchGroup3);
        return exactMatchGroups;
    }


    public static void main(String[] args) {
//		extractResults();
        FindMatchResult findMatchResult = new FindMatchResult();
        findMatchResult.setCoOwnerId1(1001L);
        findMatchResult.setCoOwnerId2(1002L);
        findMatchResult.setCoOwnerId3(1003L);

        Long[] l1 = new Long[]{1002L, 1003L, 1001L};
        Long[] l2 = new Long[]{1003L, 1001L, 1002L};
        List<Long[]> l = new ArrayList<>();
        l.add(l1);
        l.add(l2);

        for (Long[] d : l) {
            if (findMatchResult.getCoOwnerId1().longValue() == d[0].longValue()) {
                log.info("#" + findMatchResult.getCoOwnerId1() + "-0");
            } else if (findMatchResult.getCoOwnerId1().longValue() == d[1].longValue()) {
                log.info("#" + findMatchResult.getCoOwnerId1() + "-1");
            } else if (findMatchResult.getCoOwnerId1().longValue() == d[2].longValue()) {
                log.info("#" + findMatchResult.getCoOwnerId1() + "-2");
            }

            if (findMatchResult.getCoOwnerId2().longValue() == d[0].longValue()) {
                log.info("$" + findMatchResult.getCoOwnerId2() + "-0");
            } else if (findMatchResult.getCoOwnerId2().longValue() == d[1].longValue()) {
                log.info("$" + findMatchResult.getCoOwnerId2() + "-1");
            } else if (findMatchResult.getCoOwnerId2().longValue() == d[2].longValue()) {
                log.info("$" + findMatchResult.getCoOwnerId2() + "-2");
            }

            if (findMatchResult.getCoOwnerId3().longValue() == d[0].longValue()) {
                log.info("*" + findMatchResult.getCoOwnerId3() + "-0");
            } else if (findMatchResult.getCoOwnerId3().longValue() == d[1].longValue()) {
                log.info("*" + findMatchResult.getCoOwnerId3() + "-1");
            } else if (findMatchResult.getCoOwnerId3().longValue() == d[2].longValue()) {
                log.info("*" + findMatchResult.getCoOwnerId3() + "-2");
            }
        }
    }
}
