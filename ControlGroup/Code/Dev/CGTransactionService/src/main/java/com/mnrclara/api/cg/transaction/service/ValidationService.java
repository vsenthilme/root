package com.mnrclara.api.cg.transaction.service;


import com.mnrclara.api.cg.transaction.model.IkeyValuePair;
import com.mnrclara.api.cg.transaction.model.storepartnerlisting.*;
import com.mnrclara.api.cg.transaction.repository.StorePartnerListingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ValidationService {

    @Autowired
    StorePartnerListingRepository storePartnerListingRepository;


    //Match
    public Group findMatch(FindMatchResult findMatchResult) {
        Group match = new Group();
        match.setExactMatchGroups(new HashSet<>());
        match.setLikeMatchGroup(new HashSet<>());
        ExactResultInGroup exactResultInGroup = null;

        Map<String, ExactMatchGroup> exactGroupMap = new HashMap<>();
        Map<String, LikeMatchGroup> likeMatchGroupMap = new HashMap<>();
        Map<String, Integer> groupCount = new HashMap<>();
        Set<String> ExactStoreIds = new HashSet<>();
        Set<String> LikeStoreIds = new HashSet<>();

        int inputParamLength = 0;
        if(findMatchResult.getCoOwnerId1() != null) inputParamLength ++;
        if(findMatchResult.getCoOwnerId2() != null) inputParamLength ++;
        if(findMatchResult.getCoOwnerId3() != null) inputParamLength ++;
        if(findMatchResult.getCoOwnerId4() != null) inputParamLength ++;
        if(findMatchResult.getCoOwnerId5() != null) inputParamLength ++;
        if(findMatchResult.getCoOwnerId6() != null) inputParamLength ++;
        if(findMatchResult.getCoOwnerId7() != null) inputParamLength ++;
        if(findMatchResult.getCoOwnerId8() != null) inputParamLength ++;
        if(findMatchResult.getCoOwnerId9() != null) inputParamLength ++;
        if(findMatchResult.getCoOwnerId10() != null) inputParamLength ++;

        for (int i = 1; i <= 10; i++) {
            Long coOwnerId = getCoOwnerId(findMatchResult, i);
            List<String> entityIds = storePartnerListingRepository.getEntityIds(coOwnerId);

            for (String entityPair : entityIds) {
                exactResultInGroup = new ExactResultInGroup();
                for (int j = 1; j <= 10; j++) {
                    if (j == i) {
                        setCoOwner(exactResultInGroup, findMatchResult, j, entityPair);
                    } else {
                        setCoOwner(exactResultInGroup, findMatchResult, j, null);
                    }
                }

                int exactResultLength = 0;
                if(exactResultInGroup.getCoOwnerId1() != null) exactResultLength ++;
                if(exactResultInGroup.getCoOwnerId2() != null) exactResultLength ++;
                if(exactResultInGroup.getCoOwnerId3() != null) exactResultLength ++;
                if(exactResultInGroup.getCoOwnerId4() != null) exactResultLength ++;
                if(exactResultInGroup.getCoOwnerId5() != null) exactResultLength ++;
                if(exactResultInGroup.getCoOwnerId6() != null) exactResultLength ++;
                if(exactResultInGroup.getCoOwnerId7() != null) exactResultLength ++;
                if(exactResultInGroup.getCoOwnerId8() != null) exactResultLength ++;
                if(exactResultInGroup.getCoOwnerId9() != null) exactResultLength ++;
                if(exactResultInGroup.getCoOwnerId10() != null) exactResultLength ++;

                if(inputParamLength == exactResultLength) {
                List<String[]> storePartnerListingByExact = storePartnerListingRepository.findByExact(
                        exactResultInGroup.getCoOwnerId1(),
                        exactResultInGroup.getCoOwnerId2(),
                        exactResultInGroup.getCoOwnerId3(),
                        exactResultInGroup.getCoOwnerId4(),
                        exactResultInGroup.getCoOwnerId5(),
                        exactResultInGroup.getCoOwnerId6(),
                        exactResultInGroup.getCoOwnerId7(),
                        exactResultInGroup.getCoOwnerId8(),
                        exactResultInGroup.getCoOwnerId9(),
                        exactResultInGroup.getCoOwnerId10());


                if (!storePartnerListingByExact.isEmpty()) {
                    for (String[] ikeyValuePair1 : storePartnerListingByExact) {

                        String storeId = ikeyValuePair1[30];

                        if (!ExactStoreIds.contains(storeId)) {
                            String groupId = ikeyValuePair1[34];
                            groupCount.put(groupId, groupCount.getOrDefault(groupId, 0) + 1);
                            if (!exactGroupMap.containsKey(groupId)) {
                                ExactMatchGroup exactMatchGroup = new ExactMatchGroup();
                                exactMatchGroup.setGroupName(ikeyValuePair1[35]);
                                exactMatchGroup.setGroupId(groupId);
                                exactMatchGroup.setStores(new ArrayList<>());
                                exactGroupMap.put(groupId, exactMatchGroup);
                            }

                            Stores stores = new Stores();
                            stores.setStoreId(ikeyValuePair1[30]);
                            stores.setStoreName(ikeyValuePair1[30] + "-" + ikeyValuePair1[31]);
                            stores.setCoOwnerName1(ikeyValuePair1[10]);
                            stores.setCoOwnerName2(ikeyValuePair1[11]);
                            stores.setCoOwnerName3(ikeyValuePair1[12]);
                            stores.setCoOwnerName4(ikeyValuePair1[13]);
                            stores.setCoOwnerName5(ikeyValuePair1[14]);
                            stores.setCoOwnerName6(ikeyValuePair1[15]);
                            stores.setCoOwnerName7(ikeyValuePair1[16]);
                            stores.setCoOwnerName8(ikeyValuePair1[17]);
                            stores.setCoOwnerName9(ikeyValuePair1[18]);
                            stores.setCoOwnerName10(ikeyValuePair1[19]);
                            if (ikeyValuePair1[20] != null) {
                                stores.setCoOwnerPercentage1(Double.valueOf(ikeyValuePair1[20]));
                            }
                            if (ikeyValuePair1[21] != null) {
                                stores.setCoOwnerPercentage2(Double.valueOf(ikeyValuePair1[21]));
                            }
                            if (ikeyValuePair1[22] != null) {
                                stores.setCoOwnerPercentage3(Double.valueOf(ikeyValuePair1[22]));
                            }
                            if (ikeyValuePair1[23] != null) {
                                stores.setCoOwnerPercentage4(Double.valueOf(ikeyValuePair1[23]));
                            }
                            if (ikeyValuePair1[24] != null) {
                                stores.setCoOwnerPercentage5(Double.valueOf(ikeyValuePair1[24]));
                            }
                            if (ikeyValuePair1[25] != null) {
                                stores.setCoOwnerPercentage6(Double.valueOf(ikeyValuePair1[25]));
                            }
                            if (ikeyValuePair1[26] != null) {
                                stores.setCoOwnerPercentage7(Double.valueOf(ikeyValuePair1[26]));
                            }
                            if (ikeyValuePair1[27] != null) {
                                stores.setCoOwnerPercentage8(Double.valueOf(ikeyValuePair1[27]));
                            }
                            if (ikeyValuePair1[28] != null) {
                                stores.setCoOwnerPercentage9(Double.valueOf(ikeyValuePair1[28]));
                            }
                            if (ikeyValuePair1[29] != null) {
                                stores.setCoOwnerPercentage10(Double.valueOf(ikeyValuePair1[29]));
                            }
                            stores.setGroupTypeId(Long.valueOf(ikeyValuePair1[36]));
                            stores.setSubGroupTypeId(Long.valueOf(ikeyValuePair1[39]));
                            stores.setGroupType(ikeyValuePair1[36] + "-" + ikeyValuePair1[37]);
                            stores.setSubGroupType(ikeyValuePair1[39] + "-" + ikeyValuePair1[38]);
                            ExactStoreIds.add(storeId);
                            exactGroupMap.get(groupId).getStores().add(stores);
                        }
                    }
                    }
                }
                match.getExactMatchGroups().addAll(exactGroupMap.values());

                List<IkeyValuePair> ikeyValuePairList = storePartnerListingRepository.getLikeMatchResult(
                        findMatchResult.getCoOwnerId1(),
                        findMatchResult.getCoOwnerId2(),
                        findMatchResult.getCoOwnerId3(),
                        findMatchResult.getCoOwnerId4(),
                        findMatchResult.getCoOwnerId5(),
                        findMatchResult.getCoOwnerId6(),
                        findMatchResult.getCoOwnerId7(),
                        findMatchResult.getCoOwnerId8(),
                        findMatchResult.getCoOwnerId9(),
                        findMatchResult.getCoOwnerId10(),
                        findMatchResult.getGroupId());


                if (!ikeyValuePairList.isEmpty()) {
                    for (IkeyValuePair ikeyValuePair1 : ikeyValuePairList) {
                        String storeId = ikeyValuePair1.getStoreId();
                        String groupId = ikeyValuePair1.getGroupId();

                        if (!LikeStoreIds.contains(storeId)) {
                            if (!likeMatchGroupMap.containsKey(groupId)) {
                                LikeMatchGroup likeMatchGroup = new LikeMatchGroup();
                                likeMatchGroup.setGroupName(ikeyValuePair1.getGroupName());
                                likeMatchGroup.setGroupId(groupId);
                                likeMatchGroup.setStores(new ArrayList<>());
                                likeMatchGroupMap.put(groupId, likeMatchGroup);
                            }

                            Stores stores = new Stores();
                            stores.setStoreId(ikeyValuePair1.getStoreId());
                            stores.setStoreName(ikeyValuePair1.getStoreId() + "-" + ikeyValuePair1.getStoreName());
                            if (ikeyValuePair1.getCoOwnerId1() != null && ikeyValuePair1.getCoOwnerId1() != 0) {
                                stores.setCoOwnerName1(ikeyValuePair1.getCoOwnerId1() + "-" + ikeyValuePair1.getCoOwnerName1());
                            }
                            if (ikeyValuePair1.getCoOwnerId2() != null && ikeyValuePair1.getCoOwnerId2() != 0) {
                                stores.setCoOwnerName2(ikeyValuePair1.getCoOwnerId2() + "-" + ikeyValuePair1.getCoOwnerName2());
                            }
                            if (ikeyValuePair1.getCoOwnerId3() != null && ikeyValuePair1.getCoOwnerId3() != 0) {
                                stores.setCoOwnerName3(ikeyValuePair1.getCoOwnerId3() + "-" + ikeyValuePair1.getCoOwnerName3());
                            }
                            if (ikeyValuePair1.getCoOwnerId4() != null && ikeyValuePair1.getCoOwnerId4() != 0) {
                                stores.setCoOwnerName4(ikeyValuePair1.getCoOwnerId4() + "-" + ikeyValuePair1.getCoOwnerName4());
                            }
                            if (ikeyValuePair1.getCoOwnerId5() != null && ikeyValuePair1.getCoOwnerId5() != 0) {
                                stores.setCoOwnerName5(ikeyValuePair1.getCoOwnerId5() + "-" + ikeyValuePair1.getCoOwnerName5());
                            }
                            if (ikeyValuePair1.getCoOwnerId6() != null && ikeyValuePair1.getCoOwnerId6() != 0) {
                                stores.setCoOwnerName6(ikeyValuePair1.getCoOwnerId6() + "-" + ikeyValuePair1.getCoOwnerName6());
                            }
                            if (ikeyValuePair1.getCoOwnerId7() != null && ikeyValuePair1.getCoOwnerId7() != 0) {
                                stores.setCoOwnerName7(ikeyValuePair1.getCoOwnerId7() + "-" + ikeyValuePair1.getCoOwnerName7());
                            }
                            if (ikeyValuePair1.getCoOwnerId8() != null && ikeyValuePair1.getCoOwnerId8() != 0) {
                                stores.setCoOwnerName8(ikeyValuePair1.getCoOwnerId8() + "-" + ikeyValuePair1.getCoOwnerName8());
                            }
                            if (ikeyValuePair1.getCoOwnerId9() != null && ikeyValuePair1.getCoOwnerId9() != 0) {
                                stores.setCoOwnerName9(ikeyValuePair1.getCoOwnerId9() + "-" + ikeyValuePair1.getCoOwnerName9());
                            }
                            if (ikeyValuePair1.getCoOwnerId10() != null && ikeyValuePair1.getCoOwnerId10() != 0) {
                                stores.setCoOwnerName10(ikeyValuePair1.getCoOwnerId10() + "-" + ikeyValuePair1.getCoOwnerName10());
                            }
                            if (ikeyValuePair1.getCoOwnerPercentage1() != null) {
                                stores.setCoOwnerPercentage1(ikeyValuePair1.getCoOwnerPercentage1());
                            }
                            if (ikeyValuePair1.getCoOwnerPercentage2() != null) {
                                stores.setCoOwnerPercentage2(ikeyValuePair1.getCoOwnerPercentage2());
                            }
                            if (ikeyValuePair1.getCoOwnerPercentage3() != null) {
                                stores.setCoOwnerPercentage3(ikeyValuePair1.getCoOwnerPercentage3());
                            }
                            if (ikeyValuePair1.getCoOwnerPercentage4() != null) {
                                stores.setCoOwnerPercentage4(ikeyValuePair1.getCoOwnerPercentage4());
                            }
                            if (ikeyValuePair1.getCoOwnerPercentage5() != null) {
                                stores.setCoOwnerPercentage5(ikeyValuePair1.getCoOwnerPercentage5());
                            }
                            if (ikeyValuePair1.getCoOwnerPercentage6() != null) {
                                stores.setCoOwnerPercentage6(ikeyValuePair1.getCoOwnerPercentage6());
                            }
                            if (ikeyValuePair1.getCoOwnerPercentage7() != null) {
                                stores.setCoOwnerPercentage7(ikeyValuePair1.getCoOwnerPercentage7());
                            }
                            if (ikeyValuePair1.getCoOwnerPercentage8() != null) {
                                stores.setCoOwnerPercentage8(ikeyValuePair1.getCoOwnerPercentage8());
                            }
                            if (ikeyValuePair1.getCoOwnerPercentage9() != null) {
                                stores.setCoOwnerPercentage9(ikeyValuePair1.getCoOwnerPercentage9());
                            }
                            if (ikeyValuePair1.getCoOwnerPercentage10() != null) {
                                stores.setCoOwnerPercentage10(ikeyValuePair1.getCoOwnerPercentage10());
                            }
                            if (ikeyValuePair1.getGroupTypeId() != null) {
                                stores.setGroupTypeId(Long.valueOf(ikeyValuePair1.getGroupTypeId()));
                                stores.setSubGroupType(ikeyValuePair1.getSubGroupTypeId() + "-" + ikeyValuePair1.getSubGroupTypeName());
                            }
                            if (ikeyValuePair1.getSubGroupTypeId() != null) {
                                stores.setSubGroupTypeId(Long.valueOf(ikeyValuePair1.getSubGroupTypeId()));
                                stores.setGroupType(ikeyValuePair1.getGroupTypeId() + "-" + ikeyValuePair1.getGroupTypeName());
                            }
                            LikeStoreIds.add(storeId);
                            likeMatchGroupMap.get(groupId).getStores().add(stores);
                        }
                    }
                }
                match.setLikeMatchGroup(new HashSet<>(likeMatchGroupMap.values()));
            }
        }

//        int maximumGroupCount;
//        if(!groupCount.isEmpty() ){
//            maximumGroupCount = Collections.max(groupCount.values());
//        }else {
//            maximumGroupCount = 0;
//        }
//        Set<String> exactGroupIds = match.getExactMatchGroups()
//                .stream().map(ExactMatchGroup::getGroupId)
//                .collect(Collectors.toSet());

//        Set<String> maxGroupIds = groupCount.entrySet().stream().filter(entry -> entry.getValue() == maximumGroupCount).map(Map.Entry :: getKey).collect(Collectors.toSet());

//        match.getLikeMatchGroup().addAll(match.getLikeMatchGroup().stream().filter(likeMatchGroup -> !exactGroupIds.contains(likeMatchGroup.getGroupId()))
//                .filter(likeMatchGroup -> maxGroupIds.contains(likeMatchGroup.getGroupId()))
//                .collect(Collectors.toSet()));

        return match;
    }


    /**
     * @param findMatchResult
     * @param index
     * @return
     */
    private Long getCoOwnerId(FindMatchResult findMatchResult, int index) {
        switch (index) {
            case 1:
                return findMatchResult.getCoOwnerId1();
            case 2:
                return findMatchResult.getCoOwnerId2();
            case 3:
                return findMatchResult.getCoOwnerId3();
            case 4:
                return findMatchResult.getCoOwnerId4();
            case 5:
                return findMatchResult.getCoOwnerId5();
            case 6:
                return findMatchResult.getCoOwnerId6();
            case 7:
                return findMatchResult.getCoOwnerId7();
            case 8:
                return findMatchResult.getCoOwnerId8();
            case 9:
                return findMatchResult.getCoOwnerId9();
            case 10:
                return findMatchResult.getCoOwnerId10();
            default:
                throw new IllegalArgumentException("Invalid index for coOwnerId: " + index);
        }
    }

    /**
     * @param exactResultInGroup
     * @param findMatchResult
     * @param index
     */
    private void setCoOwnerId(ExactResultInGroup exactResultInGroup, FindMatchResult findMatchResult, int index, String entityId) {
        Long coOwnerId;

        if (entityId != null) {
            coOwnerId = Long.valueOf(entityId);
        } else {
            switch (index) {
                case 1:
                    coOwnerId = findMatchResult.getCoOwnerId1();
                    break;
                case 2:
                    coOwnerId = findMatchResult.getCoOwnerId2();
                    break;
                case 3:
                    coOwnerId = findMatchResult.getCoOwnerId3();
                    break;
                case 4:
                    coOwnerId = findMatchResult.getCoOwnerId4();
                    break;
                case 5:
                    coOwnerId = findMatchResult.getCoOwnerId5();
                    break;
                case 6:
                    coOwnerId = findMatchResult.getCoOwnerId6();
                    break;
                case 7:
                    coOwnerId = findMatchResult.getCoOwnerId7();
                    break;
                case 8:
                    coOwnerId = findMatchResult.getCoOwnerId8();
                    break;
                case 9:
                    coOwnerId = findMatchResult.getCoOwnerId9();
                    break;
                case 10:
                    coOwnerId = findMatchResult.getCoOwnerId10();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid index for coOwnerId: " + index);
            }
        }


        switch (index) {
            case 1:
                exactResultInGroup.setCoOwnerId1(coOwnerId);
                break;
            case 2:
                exactResultInGroup.setCoOwnerId2(coOwnerId);
                break;
            case 3:
                exactResultInGroup.setCoOwnerId3(coOwnerId);
                break;
            case 4:
                exactResultInGroup.setCoOwnerId4(coOwnerId);
                break;
            case 5:
                exactResultInGroup.setCoOwnerId5(coOwnerId);
                break;
            case 6:
                exactResultInGroup.setCoOwnerId6(coOwnerId);
                break;
            case 7:
                exactResultInGroup.setCoOwnerId7(coOwnerId);
                break;
            case 8:
                exactResultInGroup.setCoOwnerId8(coOwnerId);
                break;
            case 9:
                exactResultInGroup.setCoOwnerId9(coOwnerId);
                break;
            case 10:
                exactResultInGroup.setCoOwnerId10(coOwnerId);
                break;
            default:
                throw new IllegalArgumentException("Invalid index for coOwnerId: " + index);
        }
    }


    //SetCoOwner
    private void setCoOwner(ExactResultInGroup exactResultInGroup, FindMatchResult findMatchResult, int index, String entityId) {
        Long coOwnerId = null;


        if (entityId != null) {
            coOwnerId = Long.valueOf(entityId);
        } else {
            switch (index) {
                case 1:
                    List<String> entityId1 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId1());
                    if (entityId1 != null && !entityId1.isEmpty() && entityId1.get(0) != null) {
                        for (String entity : entityId1) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 2:
                    List<String> entityId2 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId2());
                    if (entityId2 != null && !entityId2.isEmpty() && entityId2.get(0) != null) {
                        for (String entity : entityId2) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 3:
                    List<String> entityId3 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId3());
                    if (entityId3 != null && !entityId3.isEmpty() && entityId3.get(0) != null) {
                        for (String entity : entityId3) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 4:
                    List<String> entityId4 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId4());
                    if (entityId4 != null && !entityId4.isEmpty() && entityId4.get(0) != null) {
                        for (String entity : entityId4) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 5:
                    List<String> entityId5 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId5());
                    if (entityId5 != null && !entityId5.isEmpty() && entityId5.get(0) != null) {
                        for (String entity : entityId5) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 6:
                    List<String> entityId6 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId6());
                    if (entityId6 != null && !entityId6.isEmpty() && entityId6.get(0) != null) {
                        for (String entity : entityId6) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 7:
                    List<String> entityId7 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId7());
                    if (entityId7 != null && !entityId7.isEmpty() && entityId7.get(0) != null) {
                        for (String entity : entityId7) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 8:
                    List<String> entityId8 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId8());
                    if (entityId8 != null && !entityId8.isEmpty() && entityId8.get(0) != null) {
                        for (String entity : entityId8) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 9:
                    List<String> entityId9 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId9());
                    if (entityId9 != null && !entityId9.isEmpty() && entityId9.get(0) != null) {
                        for (String entity : entityId9) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 10:
                    List<String> entityId10 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId10());
                    if (entityId10 != null && !entityId10.isEmpty() && entityId10.get(0) != null) {
                        for (String entity : entityId10) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid index for coOwnerId: " + index);
            }
        }

        switch (index) {
            case 1:
                exactResultInGroup.setCoOwnerId1(coOwnerId);
                break;
            case 2:
                exactResultInGroup.setCoOwnerId2(coOwnerId);
                break;
            case 3:
                exactResultInGroup.setCoOwnerId3(coOwnerId);
                break;
            case 4:
                exactResultInGroup.setCoOwnerId4(coOwnerId);
                break;
            case 5:
                exactResultInGroup.setCoOwnerId5(coOwnerId);
                break;
            case 6:
                exactResultInGroup.setCoOwnerId6(coOwnerId);
                break;
            case 7:
                exactResultInGroup.setCoOwnerId7(coOwnerId);
                break;
            case 8:
                exactResultInGroup.setCoOwnerId8(coOwnerId);
                break;
            case 9:
                exactResultInGroup.setCoOwnerId9(coOwnerId);
                break;
            case 10:
                exactResultInGroup.setCoOwnerId10(coOwnerId);
                break;
            default:
                throw new IllegalArgumentException("Invalid index for coOwnerId: " + index);
        }

    }


    //=========================================ResponseObject-Straight Pass StorePartnerListing=================================================
    public ResponceObject findResponseObject(FindMatchResult findMatchResult) throws Exception {

        ResponceObject responceObject = new ResponceObject();
        responceObject.setExactMatchResult(new HashSet<>());
        responceObject.setLikeMatchResult(new HashSet<>());

        List<String[]> ikeyValuePair = storePartnerListingRepository.findStorePartnerListingByCOOwnerId(findMatchResult);
        List<IkeyValuePair> ikeyValuePairList = storePartnerListingRepository.getLikeMatchResult(
                findMatchResult.getCoOwnerId1(),
                findMatchResult.getCoOwnerId2(),
                findMatchResult.getCoOwnerId3(),
                findMatchResult.getCoOwnerId4(),
                findMatchResult.getCoOwnerId5(),
                findMatchResult.getCoOwnerId6(),
                findMatchResult.getCoOwnerId7(),
                findMatchResult.getCoOwnerId8(),
                findMatchResult.getCoOwnerId9(),
                findMatchResult.getCoOwnerId10(),
                findMatchResult.getGroupId());

        Set<String> exactGroups = new HashSet<>();

        if (!ikeyValuePair.isEmpty()) {
            for (String[] ikeyValuePair1 : ikeyValuePair) {
                ExactMatchResultV2 exactMatchResult = new ExactMatchResultV2();

                if (Long.valueOf(ikeyValuePair1[32]) == 0 && Long.valueOf(ikeyValuePair1[33]) == 0) {

                    if (ikeyValuePair1[0] != null) {
                        exactMatchResult.setCoOwnerId1(Long.valueOf(ikeyValuePair1[0]));
                    }
                    if (ikeyValuePair1[1] != null) {
                        exactMatchResult.setCoOwnerId2(Long.valueOf(ikeyValuePair1[1]));
                    }
                    if (ikeyValuePair1[2] != null) {
                        exactMatchResult.setCoOwnerId3(Long.valueOf(ikeyValuePair1[2]));
                    }
                    if (ikeyValuePair1[3] != null) {
                        exactMatchResult.setCoOwnerId4(Long.valueOf(ikeyValuePair1[3]));
                    }
                    if (ikeyValuePair1[4] != null) {
                        exactMatchResult.setCoOwnerId5(Long.valueOf(ikeyValuePair1[4]));
                    }
                    if (ikeyValuePair1[5] != null) {
                        exactMatchResult.setCoOwnerId6(Long.valueOf(ikeyValuePair1[5]));
                    }
                    if (ikeyValuePair1[6] != null) {
                        exactMatchResult.setCoOwnerId7(Long.valueOf(ikeyValuePair1[6]));
                    }
                    if (ikeyValuePair1[7] != null) {
                        exactMatchResult.setCoOwnerId8(Long.valueOf(ikeyValuePair1[7]));
                    }
                    if (ikeyValuePair1[8] != null) {
                        exactMatchResult.setCoOwnerId9(Long.valueOf(ikeyValuePair1[8]));
                    }
                    if (ikeyValuePair1[9] != null) {
                        exactMatchResult.setCoOwnerId10(Long.valueOf(ikeyValuePair1[9]));
                    }


                    exactMatchResult.setCoOwnerName1(ikeyValuePair1[10]);
                    exactMatchResult.setCoOwnerName2(ikeyValuePair1[11]);
                    exactMatchResult.setCoOwnerName3(ikeyValuePair1[12]);
                    exactMatchResult.setCoOwnerName4(ikeyValuePair1[13]);
                    exactMatchResult.setCoOwnerName5(ikeyValuePair1[14]);
                    exactMatchResult.setCoOwnerName6(ikeyValuePair1[15]);
                    exactMatchResult.setCoOwnerName7(ikeyValuePair1[16]);
                    exactMatchResult.setCoOwnerName8(ikeyValuePair1[17]);
                    exactMatchResult.setCoOwnerName9(ikeyValuePair1[18]);
                    exactMatchResult.setCoOwnerName10(ikeyValuePair1[19]);

                    if (ikeyValuePair1[20] != null) {
                        exactMatchResult.setCoOwnerPercentage1(Double.valueOf(ikeyValuePair1[20]));
                    }
                    if (ikeyValuePair1[21] != null) {
                        exactMatchResult.setCoOwnerPercentage2(Double.valueOf(ikeyValuePair1[21]));
                    }
                    if (ikeyValuePair1[22] != null) {
                        exactMatchResult.setCoOwnerPercentage3(Double.valueOf(ikeyValuePair1[22]));
                    }
                    if (ikeyValuePair1[23] != null) {
                        exactMatchResult.setCoOwnerPercentage4(Double.valueOf(ikeyValuePair1[23]));
                    }
                    if (ikeyValuePair1[24] != null) {
                        exactMatchResult.setCoOwnerPercentage5(Double.valueOf(ikeyValuePair1[24]));
                    }
                    if (ikeyValuePair1[25] != null) {
                        exactMatchResult.setCoOwnerPercentage6(Double.valueOf(ikeyValuePair1[25]));
                    }
                    if (ikeyValuePair1[26] != null) {
                        exactMatchResult.setCoOwnerPercentage7(Double.valueOf(ikeyValuePair1[26]));
                    }
                    if (ikeyValuePair1[27] != null) {
                        exactMatchResult.setCoOwnerPercentage8(Double.valueOf(ikeyValuePair1[27]));
                    }
                    if (ikeyValuePair1[28] != null) {
                        exactMatchResult.setCoOwnerPercentage9(Double.valueOf(ikeyValuePair1[28]));
                    }
                    if (ikeyValuePair1[29] != null) {
                        exactMatchResult.setCoOwnerPercentage10(Double.valueOf(ikeyValuePair1[29]));
                    }
                    exactMatchResult.setStoreId(ikeyValuePair1[30]);
                    exactMatchResult.setStoreName(ikeyValuePair1[31]);
                    exactGroups.add(ikeyValuePair1[30]);
                    responceObject.getExactMatchResult().add(exactMatchResult);
                }
            }
        }
        Set<String> likeStoreId = new HashSet<>();
        if (!ikeyValuePairList.isEmpty()) {
            for (IkeyValuePair ikeyValuePair1 : ikeyValuePairList) {
                LikeMatchResultV2 likeMatchResult = new LikeMatchResultV2();
                String storeId = ikeyValuePair1.getStoreId();
                if(!likeStoreId.contains(storeId)) {
                    if (!exactGroups.contains(ikeyValuePair1.getStoreId())) {
                        likeMatchResult.setCoOwnerId1(ikeyValuePair1.getCoOwnerId1());
                        likeMatchResult.setCoOwnerId2(ikeyValuePair1.getCoOwnerId2());
                        likeMatchResult.setCoOwnerId3(ikeyValuePair1.getCoOwnerId3());
                        likeMatchResult.setCoOwnerId4(ikeyValuePair1.getCoOwnerId4());
                        likeMatchResult.setCoOwnerId5(ikeyValuePair1.getCoOwnerId5());
                        likeMatchResult.setCoOwnerId6(ikeyValuePair1.getCoOwnerId6());
                        likeMatchResult.setCoOwnerId7(ikeyValuePair1.getCoOwnerId7());
                        likeMatchResult.setCoOwnerId8(ikeyValuePair1.getCoOwnerId8());
                        likeMatchResult.setCoOwnerId9(ikeyValuePair1.getCoOwnerId9());
                        likeMatchResult.setCoOwnerId10(ikeyValuePair1.getCoOwnerId10());
                        likeMatchResult.setCoOwnerPercentage1(ikeyValuePair1.getCoOwnerPercentage1());
                        likeMatchResult.setCoOwnerPercentage2(ikeyValuePair1.getCoOwnerPercentage2());
                        likeMatchResult.setCoOwnerPercentage3(ikeyValuePair1.getCoOwnerPercentage3());
                        likeMatchResult.setCoOwnerPercentage4(ikeyValuePair1.getCoOwnerPercentage4());
                        likeMatchResult.setCoOwnerPercentage5(ikeyValuePair1.getCoOwnerPercentage5());
                        likeMatchResult.setCoOwnerPercentage6(ikeyValuePair1.getCoOwnerPercentage6());
                        likeMatchResult.setCoOwnerPercentage7(ikeyValuePair1.getCoOwnerPercentage7());
                        likeMatchResult.setCoOwnerPercentage8(ikeyValuePair1.getCoOwnerPercentage8());
                        likeMatchResult.setCoOwnerPercentage9(ikeyValuePair1.getCoOwnerPercentage9());
                        likeMatchResult.setCoOwnerPercentage10(ikeyValuePair1.getCoOwnerPercentage10());
                        likeMatchResult.setCoOwnerName1(ikeyValuePair1.getCoOwnerId1() + "-" + ikeyValuePair1.getCoOwnerName1());
                        likeMatchResult.setCoOwnerName2(ikeyValuePair1.getCoOwnerId2() + "-" + ikeyValuePair1.getCoOwnerName2());
                        likeMatchResult.setCoOwnerName3(ikeyValuePair1.getCoOwnerId3() + "-" + ikeyValuePair1.getCoOwnerName3());
                        likeMatchResult.setCoOwnerName4(ikeyValuePair1.getCoOwnerId4() + "-" + ikeyValuePair1.getCoOwnerName4());
                        likeMatchResult.setCoOwnerName5(ikeyValuePair1.getCoOwnerId5() + "-" + ikeyValuePair1.getCoOwnerName5());
                        likeMatchResult.setCoOwnerName6(ikeyValuePair1.getCoOwnerId6() + "-" + ikeyValuePair1.getCoOwnerName6());
                        likeMatchResult.setCoOwnerName7(ikeyValuePair1.getCoOwnerId7() + "-" + ikeyValuePair1.getCoOwnerName7());
                        likeMatchResult.setCoOwnerName8(ikeyValuePair1.getCoOwnerId8() + "-" + ikeyValuePair1.getCoOwnerName8());
                        likeMatchResult.setCoOwnerName9(ikeyValuePair1.getCoOwnerId9() + "-" + ikeyValuePair1.getCoOwnerName9());
                        likeMatchResult.setCoOwnerName10(ikeyValuePair1.getCoOwnerId10() + "-" + ikeyValuePair1.getCoOwnerName10());
                        likeMatchResult.setStoreId(ikeyValuePair1.getStoreId());
                        likeMatchResult.setStoreName(ikeyValuePair1.getStoreName());
                        likeStoreId.add(storeId);
                        responceObject.getLikeMatchResult().add(likeMatchResult);
                    }
                }
            }
        }
        return responceObject;
    }


    // All Entity Id Pass based on client_Id
    public ResponceObject findResponseObjectEntity(FindMatchResult findMatchResult) throws Exception {

        ResponceObject responceObject = new ResponceObject();
        ExactMatchResultV2 exactMatchResultV2 = null;
        responceObject.setExactMatchResult(new HashSet<>());
        responceObject.setLikeMatchResult(new HashSet<>());

        Set<String> exactGroups = new HashSet<>();
        Set<String> exactStoreId = new HashSet<>();

        int inputParamsLength = 0;
        if (findMatchResult.getCoOwnerId1() != null) inputParamsLength++;
        if (findMatchResult.getCoOwnerId2() != null) inputParamsLength++;
        if (findMatchResult.getCoOwnerId3() != null) inputParamsLength++;
        if (findMatchResult.getCoOwnerId4() != null) inputParamsLength++;
        if (findMatchResult.getCoOwnerId5() != null) inputParamsLength++;
        if (findMatchResult.getCoOwnerId6() != null) inputParamsLength++;
        if (findMatchResult.getCoOwnerId7() != null) inputParamsLength++;
        if (findMatchResult.getCoOwnerId8() != null) inputParamsLength++;
        if (findMatchResult.getCoOwnerId9() != null) inputParamsLength++;
        if (findMatchResult.getCoOwnerId10() != null) inputParamsLength++;

        for (int i = 1; i <= 10; i++) {
            Long coOwnerId = getCoOwnerId(findMatchResult, i);

            List<String> entityIds = storePartnerListingRepository.getEntityIds(coOwnerId);

            if (entityIds != null && !entityIds.isEmpty() && entityIds.get(0) != null) {
                for (String entityPair : entityIds) {
                    exactMatchResultV2 = new ExactMatchResultV2();
                    for (int j = 1; j <= 10; j++) {
                        if (j == i) {
                            setMatchResultV2(exactMatchResultV2, findMatchResult, j, entityPair);
                        } else {
                            setMatchResultV2(exactMatchResultV2, findMatchResult, j, null);
                        }
                    }

                    int exactMatchResultLength = 0;
                    if(exactMatchResultV2.getCoOwnerId1() != null) exactMatchResultLength ++;
                    if(exactMatchResultV2.getCoOwnerId2() != null) exactMatchResultLength ++;
                    if(exactMatchResultV2.getCoOwnerId3() != null) exactMatchResultLength ++;
                    if(exactMatchResultV2.getCoOwnerId4() != null) exactMatchResultLength ++;
                    if(exactMatchResultV2.getCoOwnerId5() != null) exactMatchResultLength ++;
                    if(exactMatchResultV2.getCoOwnerId6() != null) exactMatchResultLength ++;
                    if(exactMatchResultV2.getCoOwnerId7() != null) exactMatchResultLength ++;
                    if(exactMatchResultV2.getCoOwnerId8() != null) exactMatchResultLength ++;
                    if(exactMatchResultV2.getCoOwnerId9() != null) exactMatchResultLength ++;
                    if(exactMatchResultV2.getCoOwnerId10() != null) exactMatchResultLength ++;

                    if(inputParamsLength == exactMatchResultLength) {
                        List<String[]> ikeyValuePair = storePartnerListingRepository.findByExact(
                                exactMatchResultV2.getCoOwnerId1(),
                                exactMatchResultV2.getCoOwnerId2(),
                                exactMatchResultV2.getCoOwnerId3(),
                                exactMatchResultV2.getCoOwnerId4(),
                                exactMatchResultV2.getCoOwnerId5(),
                                exactMatchResultV2.getCoOwnerId6(),
                                exactMatchResultV2.getCoOwnerId7(),
                                exactMatchResultV2.getCoOwnerId8(),
                                exactMatchResultV2.getCoOwnerId9(),
                                exactMatchResultV2.getCoOwnerId10());

                    if (!ikeyValuePair.isEmpty()) {
                        for (String[] ikeyValuePair1 : ikeyValuePair) {
                            exactMatchResultV2 = new ExactMatchResultV2();

                            String storeId = ikeyValuePair1[30];
                            if(!exactStoreId.contains(storeId)) {

                                if (Long.valueOf(ikeyValuePair1[32]) == 0 && Long.valueOf(ikeyValuePair1[33]) == 0) {

                                    if (ikeyValuePair1[0] != null) {
                                        exactMatchResultV2.setCoOwnerId1(Long.valueOf(ikeyValuePair1[0]));
                                    }
                                    if (ikeyValuePair1[1] != null) {
                                        exactMatchResultV2.setCoOwnerId2(Long.valueOf(ikeyValuePair1[1]));
                                    }
                                    if (ikeyValuePair1[2] != null) {
                                        exactMatchResultV2.setCoOwnerId3(Long.valueOf(ikeyValuePair1[2]));
                                    }
                                    if (ikeyValuePair1[3] != null) {
                                        exactMatchResultV2.setCoOwnerId4(Long.valueOf(ikeyValuePair1[3]));
                                    }
                                    if (ikeyValuePair1[4] != null) {
                                        exactMatchResultV2.setCoOwnerId5(Long.valueOf(ikeyValuePair1[4]));
                                    }
                                    if (ikeyValuePair1[5] != null) {
                                        exactMatchResultV2.setCoOwnerId6(Long.valueOf(ikeyValuePair1[5]));
                                    }
                                    if (ikeyValuePair1[6] != null) {
                                        exactMatchResultV2.setCoOwnerId7(Long.valueOf(ikeyValuePair1[6]));
                                    }
                                    if (ikeyValuePair1[7] != null) {
                                        exactMatchResultV2.setCoOwnerId8(Long.valueOf(ikeyValuePair1[7]));
                                    }
                                    if (ikeyValuePair1[8] != null) {
                                        exactMatchResultV2.setCoOwnerId9(Long.valueOf(ikeyValuePair1[8]));
                                    }
                                    if (ikeyValuePair1[9] != null) {
                                        exactMatchResultV2.setCoOwnerId10(Long.valueOf(ikeyValuePair1[9]));
                                    }
                                    exactMatchResultV2.setCoOwnerName1(ikeyValuePair1[10]);
                                    exactMatchResultV2.setCoOwnerName2(ikeyValuePair1[11]);
                                    exactMatchResultV2.setCoOwnerName3(ikeyValuePair1[12]);
                                    exactMatchResultV2.setCoOwnerName4(ikeyValuePair1[13]);
                                    exactMatchResultV2.setCoOwnerName5(ikeyValuePair1[14]);
                                    exactMatchResultV2.setCoOwnerName6(ikeyValuePair1[15]);
                                    exactMatchResultV2.setCoOwnerName7(ikeyValuePair1[16]);
                                    exactMatchResultV2.setCoOwnerName8(ikeyValuePair1[17]);
                                    exactMatchResultV2.setCoOwnerName9(ikeyValuePair1[18]);
                                    exactMatchResultV2.setCoOwnerName10(ikeyValuePair1[19]);

                                    if (ikeyValuePair1[20] != null) {
                                        exactMatchResultV2.setCoOwnerPercentage1(Double.valueOf(ikeyValuePair1[20]));
                                    }
                                    if (ikeyValuePair1[21] != null) {
                                        exactMatchResultV2.setCoOwnerPercentage2(Double.valueOf(ikeyValuePair1[21]));
                                    }
                                    if (ikeyValuePair1[22] != null) {
                                        exactMatchResultV2.setCoOwnerPercentage3(Double.valueOf(ikeyValuePair1[22]));
                                    }
                                    if (ikeyValuePair1[23] != null) {
                                        exactMatchResultV2.setCoOwnerPercentage4(Double.valueOf(ikeyValuePair1[23]));
                                    }
                                    if (ikeyValuePair1[24] != null) {
                                        exactMatchResultV2.setCoOwnerPercentage5(Double.valueOf(ikeyValuePair1[24]));
                                    }
                                    if (ikeyValuePair1[25] != null) {
                                        exactMatchResultV2.setCoOwnerPercentage6(Double.valueOf(ikeyValuePair1[25]));
                                    }
                                    if (ikeyValuePair1[26] != null) {
                                        exactMatchResultV2.setCoOwnerPercentage7(Double.valueOf(ikeyValuePair1[26]));
                                    }
                                    if (ikeyValuePair1[27] != null) {
                                        exactMatchResultV2.setCoOwnerPercentage8(Double.valueOf(ikeyValuePair1[27]));
                                    }
                                    if (ikeyValuePair1[28] != null) {
                                        exactMatchResultV2.setCoOwnerPercentage9(Double.valueOf(ikeyValuePair1[28]));
                                    }
                                    if (ikeyValuePair1[29] != null) {
                                        exactMatchResultV2.setCoOwnerPercentage10(Double.valueOf(ikeyValuePair1[29]));
                                    }
                                    exactMatchResultV2.setStoreId(ikeyValuePair1[30]);
                                    exactMatchResultV2.setStoreName(ikeyValuePair1[31]);
                                    exactMatchResultV2.setGroupId(ikeyValuePair1[34]);
                                    exactGroups.add(ikeyValuePair1[30]);
                                    exactStoreId.add(ikeyValuePair1[30]);
                                    responceObject.getExactMatchResult().add(exactMatchResultV2);
                                }
                            }
                            }
                        }
                    }
                    Set<String> likeStoreId = new HashSet<>();
                    List<IkeyValuePair> ikeyValuePairList = storePartnerListingRepository.getLikeMatchResult(
                            exactMatchResultV2.getCoOwnerId1(),
                            exactMatchResultV2.getCoOwnerId2(),
                            exactMatchResultV2.getCoOwnerId3(),
                            exactMatchResultV2.getCoOwnerId4(),
                            exactMatchResultV2.getCoOwnerId5(),
                            exactMatchResultV2.getCoOwnerId6(),
                            exactMatchResultV2.getCoOwnerId7(),
                            exactMatchResultV2.getCoOwnerId8(),
                            exactMatchResultV2.getCoOwnerId9(),
                            exactMatchResultV2.getCoOwnerId10(),
                            exactMatchResultV2.getGroupId());

                    if (!ikeyValuePairList.isEmpty()) {
                        for (IkeyValuePair ikeyValuePair1 : ikeyValuePairList) {
                            String storeId = ikeyValuePair1.getStoreId();
                            if (!likeStoreId.contains(storeId)) {
                                if (!exactGroups.contains(ikeyValuePair1.getStoreId())) {
                                    LikeMatchResultV2 likeMatchResult = new LikeMatchResultV2();
                                    likeMatchResult.setCoOwnerId1(ikeyValuePair1.getCoOwnerId1());
                                    likeMatchResult.setCoOwnerId2(ikeyValuePair1.getCoOwnerId2());
                                    likeMatchResult.setCoOwnerId3(ikeyValuePair1.getCoOwnerId3());
                                    likeMatchResult.setCoOwnerId4(ikeyValuePair1.getCoOwnerId4());
                                    likeMatchResult.setCoOwnerId5(ikeyValuePair1.getCoOwnerId5());
                                    likeMatchResult.setCoOwnerId6(ikeyValuePair1.getCoOwnerId6());
                                    likeMatchResult.setCoOwnerId7(ikeyValuePair1.getCoOwnerId7());
                                    likeMatchResult.setCoOwnerId8(ikeyValuePair1.getCoOwnerId8());
                                    likeMatchResult.setCoOwnerId9(ikeyValuePair1.getCoOwnerId9());
                                    likeMatchResult.setCoOwnerId10(ikeyValuePair1.getCoOwnerId10());
                                    likeMatchResult.setCoOwnerPercentage1(ikeyValuePair1.getCoOwnerPercentage1());
                                    likeMatchResult.setCoOwnerPercentage2(ikeyValuePair1.getCoOwnerPercentage2());
                                    likeMatchResult.setCoOwnerPercentage3(ikeyValuePair1.getCoOwnerPercentage3());
                                    likeMatchResult.setCoOwnerPercentage4(ikeyValuePair1.getCoOwnerPercentage4());
                                    likeMatchResult.setCoOwnerPercentage5(ikeyValuePair1.getCoOwnerPercentage5());
                                    likeMatchResult.setCoOwnerPercentage6(ikeyValuePair1.getCoOwnerPercentage6());
                                    likeMatchResult.setCoOwnerPercentage7(ikeyValuePair1.getCoOwnerPercentage7());
                                    likeMatchResult.setCoOwnerPercentage8(ikeyValuePair1.getCoOwnerPercentage8());
                                    likeMatchResult.setCoOwnerPercentage9(ikeyValuePair1.getCoOwnerPercentage9());
                                    likeMatchResult.setCoOwnerPercentage10(ikeyValuePair1.getCoOwnerPercentage10());
                                    likeMatchResult.setCoOwnerName1(ikeyValuePair1.getCoOwnerId1() + "-" + ikeyValuePair1.getCoOwnerName1());
                                    likeMatchResult.setCoOwnerName2(ikeyValuePair1.getCoOwnerId2() + "-" + ikeyValuePair1.getCoOwnerName2());
                                    likeMatchResult.setCoOwnerName3(ikeyValuePair1.getCoOwnerId3() + "-" + ikeyValuePair1.getCoOwnerName3());
                                    likeMatchResult.setCoOwnerName4(ikeyValuePair1.getCoOwnerId4() + "-" + ikeyValuePair1.getCoOwnerName4());
                                    likeMatchResult.setCoOwnerName5(ikeyValuePair1.getCoOwnerId5() + "-" + ikeyValuePair1.getCoOwnerName5());
                                    likeMatchResult.setCoOwnerName6(ikeyValuePair1.getCoOwnerId6() + "-" + ikeyValuePair1.getCoOwnerName6());
                                    likeMatchResult.setCoOwnerName7(ikeyValuePair1.getCoOwnerId7() + "-" + ikeyValuePair1.getCoOwnerName7());
                                    likeMatchResult.setCoOwnerName8(ikeyValuePair1.getCoOwnerId8() + "-" + ikeyValuePair1.getCoOwnerName8());
                                    likeMatchResult.setCoOwnerName9(ikeyValuePair1.getCoOwnerId9() + "-" + ikeyValuePair1.getCoOwnerName9());
                                    likeMatchResult.setCoOwnerName10(ikeyValuePair1.getCoOwnerId10() + "-" + ikeyValuePair1.getCoOwnerName10());
                                    likeMatchResult.setStoreId(ikeyValuePair1.getStoreId());
                                    likeMatchResult.setStoreName(ikeyValuePair1.getStoreName());
                                    likeMatchResult.setGroupId(ikeyValuePair1.getGroupId());
                                    responceObject.getLikeMatchResult().add(likeMatchResult);
                                    likeStoreId.add(storeId);
//                            responceObject.getLikeMatchResult().;
                                }
                            }
                        }
                    }
                }
            }
        }
        return responceObject;
    }


    //SetCoOwner
    private void setMatchResultV2(ExactMatchResultV2 exactResultInGroup, FindMatchResult findMatchResult, int index, String entityId) {
        Long coOwnerId = null;


        if (entityId != null) {
            coOwnerId = Long.valueOf(entityId);
        } else {
            switch (index) {
                case 1:
                    List<String> entityId1 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId1());
                    if (entityId1 != null && !entityId1.isEmpty() && entityId1.get(0) != null) {
                        for (String entity : entityId1) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 2:
                    List<String> entityId2 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId2());
                    if (entityId2 != null && !entityId2.isEmpty() && entityId2.get(0) != null) {
                        for (String entity : entityId2) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 3:
                    List<String> entityId3 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId3());
                    if (entityId3 != null && !entityId3.isEmpty() && entityId3.get(0) != null) {
                        for (String entity : entityId3) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 4:
                    List<String> entityId4 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId4());
                    if (entityId4 != null && !entityId4.isEmpty() && entityId4.get(0) != null) {
                        for (String entity : entityId4) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 5:
                    List<String> entityId5 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId5());
                    if (entityId5 != null && !entityId5.isEmpty() && entityId5.get(0) != null) {
                        for (String entity : entityId5) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 6:
                    List<String> entityId6 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId6());
                    if (entityId6 != null && !entityId6.isEmpty() && entityId6.get(0) != null) {
                        for (String entity : entityId6) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 7:
                    List<String> entityId7 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId7());
                    if (entityId7 != null && !entityId7.isEmpty() && entityId7.get(0) != null) {
                        for (String entity : entityId7) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 8:
                    List<String> entityId8 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId8());
                    if (entityId8 != null && !entityId8.isEmpty() && entityId8.get(0) != null) {
                        for (String entity : entityId8) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 9:
                    List<String> entityId9 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId9());
                    if (entityId9 != null && !entityId9.isEmpty() && entityId9.get(0) != null) {
                        for (String entity : entityId9) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                case 10:
                    List<String> entityId10 = storePartnerListingRepository.getEntityIds(findMatchResult.getCoOwnerId10());
                    if (entityId10 != null && !entityId10.isEmpty() && entityId10.get(0) != null) {
                        for (String entity : entityId10) {
                            coOwnerId = Long.valueOf(entity);
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid index for coOwnerId: " + index);
            }
        }

        switch (index) {
            case 1:
                exactResultInGroup.setCoOwnerId1(coOwnerId);
                break;
            case 2:
                exactResultInGroup.setCoOwnerId2(coOwnerId);
                break;
            case 3:
                exactResultInGroup.setCoOwnerId3(coOwnerId);
                break;
            case 4:
                exactResultInGroup.setCoOwnerId4(coOwnerId);
                break;
            case 5:
                exactResultInGroup.setCoOwnerId5(coOwnerId);
                break;
            case 6:
                exactResultInGroup.setCoOwnerId6(coOwnerId);
                break;
            case 7:
                exactResultInGroup.setCoOwnerId7(coOwnerId);
                break;
            case 8:
                exactResultInGroup.setCoOwnerId8(coOwnerId);
                break;
            case 9:
                exactResultInGroup.setCoOwnerId9(coOwnerId);
                break;
            case 10:
                exactResultInGroup.setCoOwnerId10(coOwnerId);
                break;
            default:
                throw new IllegalArgumentException("Invalid index for coOwnerId: " + index);
        }

    }

}
