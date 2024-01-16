package com.iwmvp.api.master.service;

import com.iwmvp.api.master.controller.exception.BadRequestException;
import com.iwmvp.api.master.model.loyaltycategory.*;
import com.iwmvp.api.master.repository.LoyaltyCategoryRepository;
import com.iwmvp.api.master.repository.Specification.LoyaltyCategorySpecification;
import com.iwmvp.api.master.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoyaltyCategoryService {
    @Autowired
    private LoyaltyCategoryRepository loyaltyCategoryRepository;

    /**
     * getAllLoyaltyCategory
     * @return
     */
    public List<LoyaltyCategory> getAllLoyaltyCategory(){
        List<LoyaltyCategory> LoyaltyCategoryList=loyaltyCategoryRepository.findAll();
        LoyaltyCategoryList = LoyaltyCategoryList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return LoyaltyCategoryList;
    }
    /**
     * getLoyaltyCategory
     * @param rangeId
     * @return
     */
    public LoyaltyCategory getLoyaltyCategory(Long rangeId){
        Optional<LoyaltyCategory> dbLoyaltyCategory=
                loyaltyCategoryRepository.findByRangeIdAndDeletionIndicator(
                        rangeId,
                        0l);
        if (dbLoyaltyCategory.isEmpty()) {
            throw new BadRequestException("The given values : " +
                    "rangeId - " + rangeId +
                    "doesn't exist.");
        }
        return dbLoyaltyCategory.get();
    }
    /**
     * createLoyaltyCategory
     * @param newLoyaltyCategory
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public LoyaltyCategory createLoyaltyCategory (AddLoyaltyCategory newLoyaltyCategory, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        LoyaltyCategory dbLoyaltyCategory = new LoyaltyCategory();
        Optional<LoyaltyCategory> checkDuplicate = loyaltyCategoryRepository.findByCompanyIdAndLanguageIdAndCategoryIdAndPointsFromAndPointsToAndDeletionIndicator(
                newLoyaltyCategory.getCompanyId(),
                newLoyaltyCategory.getLanguageId(),
                newLoyaltyCategory.getCategoryId(),
                newLoyaltyCategory.getPointsFrom(),
                newLoyaltyCategory.getPointsTo(),
                0L);
        if(!checkDuplicate.isEmpty()){
            throw new BadRequestException("Record getting Duplicated");
        }
        Integer checkLoyaltyCategoryDuplicate = loyaltyCategoryRepository.getExistingCount(
                newLoyaltyCategory.getCategoryId(),
                newLoyaltyCategory.getPointsFrom(),
                newLoyaltyCategory.getPointsTo());
        if(checkLoyaltyCategoryDuplicate==0){
            BeanUtils.copyProperties(newLoyaltyCategory, dbLoyaltyCategory, CommonUtils.getNullPropertyNames(newLoyaltyCategory));
            dbLoyaltyCategory.setDeletionIndicator(0L);
            dbLoyaltyCategory.setCreatedBy(loginUserID);
            dbLoyaltyCategory.setUpdatedBy(loginUserID);
            dbLoyaltyCategory.setCreatedOn(new Date());
            dbLoyaltyCategory.setUpdatedOn(new Date());
            return loyaltyCategoryRepository.save(dbLoyaltyCategory);
        }else{
            throw new BadRequestException("Record getting Duplicated");
        }
    }
    /**
     * updateLoyaltyCategory
     * @param loginUserID
     * @param rangeId
     * @param updateLoyaltyCategory
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public LoyaltyCategory updateLoyaltyCategory(Long rangeId,String loginUserID,
                                   UpdateLoyaltyCategory updateLoyaltyCategory)
            throws IllegalAccessException, InvocationTargetException {
        LoyaltyCategory dbLoyaltyCategory = getLoyaltyCategory(rangeId);
        BeanUtils.copyProperties(updateLoyaltyCategory, dbLoyaltyCategory, CommonUtils.getNullPropertyNames(updateLoyaltyCategory));
        dbLoyaltyCategory.setUpdatedBy(loginUserID);
        dbLoyaltyCategory.setUpdatedOn(new Date());
        return loyaltyCategoryRepository.save(dbLoyaltyCategory);
    }
    /**
     * deleteLoyaltyCategory
     * @param loginUserID
     * @param rangeId
     */
    public void deleteLoyaltyCategory(Long rangeId, String loginUserID) {
        LoyaltyCategory dbLoyaltyCategory= getLoyaltyCategory(rangeId);
        if ( dbLoyaltyCategory != null) {
            dbLoyaltyCategory.setDeletionIndicator(1L);
            dbLoyaltyCategory.setUpdatedBy(loginUserID);
            loyaltyCategoryRepository.save(dbLoyaltyCategory);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + rangeId);
        }
    }
    //Find LoyaltyCategory
    public List<LoyaltyCategory> findLoyaltyCategory(FindLoyaltyCategory findLoyaltyCategory) throws ParseException {

        LoyaltyCategorySpecification spec = new LoyaltyCategorySpecification(findLoyaltyCategory);
        List<LoyaltyCategory> results = loyaltyCategoryRepository.findAll(spec);
        return results;
    }
}
