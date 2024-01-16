package com.iwmvp.api.master.service;

import com.iwmvp.api.master.controller.exception.BadRequestException;
import com.iwmvp.api.master.model.loyaltysetup.*;
import com.iwmvp.api.master.repository.LoyaltySetupRepository;
import com.iwmvp.api.master.repository.Specification.LoyaltySetupSpecification;
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
public class LoyaltySetupService {

    @Autowired
    private LoyaltySetupRepository loyaltySetupRepository;

    /**
     * getLoyaltySetups
     * @return
     */
    public List<LoyaltySetup> getLoyaltySetups(){
        List<LoyaltySetup> LoyaltySetupList=loyaltySetupRepository.findAll();
        LoyaltySetupList = LoyaltySetupList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return LoyaltySetupList;
    }
    /**
     * getLoyaltySetup
     * @param loyaltyId
     * @param categoryId
     * @return
     */
    public LoyaltySetup getLoyaltySetup(Long loyaltyId,String categoryId,String companyId,String languageId){
        Optional<LoyaltySetup> dbLoyaltySetup=
                loyaltySetupRepository.findByCompanyIdAndLoyaltyIdAndCategoryIdAndLanguageIdAndDeletionIndicator(
                        companyId,
                        loyaltyId,
                        categoryId,
                        languageId,
                        0l
                );
        if (dbLoyaltySetup.isEmpty()) {
            throw new BadRequestException("The given values : " +
                    "loyaltyId - " + loyaltyId +
                    "doesn't exist.");
        }
        return dbLoyaltySetup.get();
    }
    /**
     * createLoyaltySetup
     * @param newLoyaltySetup
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public LoyaltySetup createLoyaltySetup (AddLoyaltySetup newLoyaltySetup, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        LoyaltySetup dbLoyaltySetup = new LoyaltySetup();

        Integer checkLoyaltySetupDuplicate = loyaltySetupRepository.getExistingCount(
                newLoyaltySetup.getCategoryId(),
                newLoyaltySetup.getTransactionValueFrom(),
                newLoyaltySetup.getTransactionValueTo());
        if(checkLoyaltySetupDuplicate==0){
            BeanUtils.copyProperties(newLoyaltySetup, dbLoyaltySetup, CommonUtils.getNullPropertyNames(newLoyaltySetup));
            dbLoyaltySetup.setDeletionIndicator(0L);
            dbLoyaltySetup.setCreatedBy(loginUserID);
            dbLoyaltySetup.setUpdatedBy(loginUserID);
            dbLoyaltySetup.setCreatedOn(new Date());
            dbLoyaltySetup.setUpdatedOn(new Date());
            return loyaltySetupRepository.save(dbLoyaltySetup);
        }else{
            throw new BadRequestException("Record getting Duplicated");
        }

    }
    /**
     * updateLoyaltySetup
     * @param loginUserID
     * @param loyaltyId
     * @param categoryId
     * @param updateLoyaltySetup
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public LoyaltySetup updateLoyaltySetup(Long loyaltyId,String categoryId,String companyId,String languageId, String loginUserID,
                                   UpdateLoyaltySetup updateLoyaltySetup)
            throws IllegalAccessException, InvocationTargetException {
        LoyaltySetup dbLoyaltySetup = getLoyaltySetup(loyaltyId,categoryId,companyId,languageId);
        BeanUtils.copyProperties(updateLoyaltySetup, dbLoyaltySetup, CommonUtils.getNullPropertyNames(updateLoyaltySetup));
        dbLoyaltySetup.setUpdatedBy(loginUserID);
        dbLoyaltySetup.setUpdatedOn(new Date());
        return loyaltySetupRepository.save(dbLoyaltySetup);
    }
    /**
     * deleteLoyaltySetup
     * @param loginUserID
     * @param loyaltyId
     * @param categoryId
     */
    public void deleteLoyaltySetup(Long loyaltyId,String categoryId,String companyId,String languageId,String loginUserID) {
        LoyaltySetup dbLoyaltySetup = getLoyaltySetup(loyaltyId,categoryId,companyId,languageId);
        if ( dbLoyaltySetup != null) {
            dbLoyaltySetup.setDeletionIndicator(1L);
            dbLoyaltySetup.setUpdatedBy(loginUserID);
            loyaltySetupRepository.save(dbLoyaltySetup);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + loyaltyId);
        }
    }
    //Find LoyaltySetup
    public List<LoyaltySetup> findLoyaltySetup(FindLoyaltySetup findLoyaltySetup) throws ParseException {

        LoyaltySetupSpecification spec = new LoyaltySetupSpecification(findLoyaltySetup);
        List<LoyaltySetup> results = loyaltySetupRepository.findAll(spec);
        return results;
    }

}
