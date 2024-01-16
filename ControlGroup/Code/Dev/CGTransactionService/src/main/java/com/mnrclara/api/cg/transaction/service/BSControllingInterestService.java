package com.mnrclara.api.cg.transaction.service;

import com.mnrclara.api.cg.transaction.exception.BadRequestException;
import com.mnrclara.api.cg.transaction.model.bscontrollinginterest.AddBSControllingInterest;
import com.mnrclara.api.cg.transaction.model.bscontrollinginterest.BSControllingInterest;
import com.mnrclara.api.cg.transaction.model.bscontrollinginterest.FindBSControllingInterest;
import com.mnrclara.api.cg.transaction.model.bscontrollinginterest.UpdateBSControllingInterest;
import com.mnrclara.api.cg.transaction.repository.BSControllingInterestRepository;
import com.mnrclara.api.cg.transaction.repository.specification.BSControllingInterestSpecification;
import com.mnrclara.api.cg.transaction.util.CommonUtils;
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
public class BSControllingInterestService {

    @Autowired
    private BSControllingInterestRepository bsControllingInterestRepository;


    /**
     * getAllBsEffectiveControl
     *
     * @return
     */
    public List<BSControllingInterest> getAllBSControllingInterest() {
        List<BSControllingInterest> bsControllingInterestList = bsControllingInterestRepository.findAll();

        bsControllingInterestList = bsControllingInterestList.stream()
                .filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + bsControllingInterestList);
        return bsControllingInterestList;
    }


    /**
     * @param companyId
     * @param languageId
     * @param validationId
     * @return
     */
    public BSControllingInterest getBSControllingInterest(String companyId, String languageId, Long validationId) {

        Optional<BSControllingInterest> dbControllingInterest =
                bsControllingInterestRepository.findByCompanyIdAndLanguageIdAndValidationIdAndDeletionIndicator(
                        companyId, languageId, validationId, 0L);

        if (dbControllingInterest.isEmpty()) {
            throw new BadRequestException("The given values companyId - "
                    + companyId + " languageId " + languageId
                    + " validationId " + validationId + " doesn't exists");
        }
        return dbControllingInterest.get();
    }


    /**
     * @param addBSControllingInterest
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public BSControllingInterest createBSControllingInterest(AddBSControllingInterest addBSControllingInterest, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        Optional<BSControllingInterest> duplicateBSControllingInterest =
                bsControllingInterestRepository.findByCompanyIdAndLanguageIdAndValidationIdAndDeletionIndicator(
                        addBSControllingInterest.getCompanyId(),
                        addBSControllingInterest.getLanguageId(),
                        addBSControllingInterest.getValidationId(),
                        0L);

        if (!duplicateBSControllingInterest.isEmpty()) {
            throw new RuntimeException("Record is Getting duplicated");
        } else {
            BSControllingInterest bsControllingInterest = new BSControllingInterest();
            BeanUtils.copyProperties(addBSControllingInterest, bsControllingInterest, CommonUtils.getNullPropertyNames(addBSControllingInterest));
            bsControllingInterest.setDeletionIndicator(0L);
            bsControllingInterest.setCreatedBy(loginUserID);
            bsControllingInterest.setUpdatedBy(loginUserID);
            bsControllingInterest.setCreatedOn(new Date());
            bsControllingInterest.setUpdatedOn(new Date());
            return bsControllingInterestRepository.save(bsControllingInterest);
        }
    }

    /**
     * @param companyId
     * @param languageId
     * @param validationId
     * @param loginUserID
     * @param updateBSControllingInterest
     * @return
     */
    public BSControllingInterest updateBSControllingInterest(String companyId, String languageId, Long validationId,
                                                             String loginUserID, UpdateBSControllingInterest updateBSControllingInterest) {

        BSControllingInterest dbBSControllingInterest = getBSControllingInterest(companyId, languageId, validationId);

        BeanUtils.copyProperties(updateBSControllingInterest, dbBSControllingInterest,
                CommonUtils.getNullPropertyNames(updateBSControllingInterest));
        dbBSControllingInterest.setDeletionIndicator(0L);
        dbBSControllingInterest.setUpdatedBy(loginUserID);
        dbBSControllingInterest.setUpdatedOn(new Date());
        return bsControllingInterestRepository.save(dbBSControllingInterest);

    }

    /**
     * @param companyId
     * @param languageId
     * @param validationId
     * @param loginUserID
     * @return
     */
    public void deleteBSControllingInterest(String companyId, String languageId, Long validationId, String loginUserID) {

        BSControllingInterest dbBSControllingInterest = getBSControllingInterest(companyId, languageId, validationId);
        if (dbBSControllingInterest != null) {
            dbBSControllingInterest.setDeletionIndicator(1L);
            dbBSControllingInterest.setUpdatedBy(loginUserID);
            dbBSControllingInterest.setUpdatedOn(new Date());
            bsControllingInterestRepository.save(dbBSControllingInterest);
        } else {
            throw new EntityNotFoundException("Error in deleting Id:" + validationId);
        }
    }

    /**
     * @param findBSControllingInterest
     * @return
     * @throws ParseException
     */
    public List<BSControllingInterest> findBSControllingInterest(FindBSControllingInterest findBSControllingInterest)
            throws ParseException {

        BSControllingInterestSpecification spec = new BSControllingInterestSpecification(findBSControllingInterest);
        List<BSControllingInterest> results = bsControllingInterestRepository.findAll(spec);
        log.info("results: " + results);
        return results;
    }
}
