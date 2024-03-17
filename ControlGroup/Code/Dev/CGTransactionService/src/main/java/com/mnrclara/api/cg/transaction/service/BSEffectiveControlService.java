package com.mnrclara.api.cg.transaction.service;


import com.mnrclara.api.cg.transaction.exception.BadRequestException;
import com.mnrclara.api.cg.transaction.model.bseffectivecontrol.AddBSEffectiveControl;
import com.mnrclara.api.cg.transaction.model.bseffectivecontrol.BSEffectiveControl;
import com.mnrclara.api.cg.transaction.model.bseffectivecontrol.FindBSEffectiveControl;
import com.mnrclara.api.cg.transaction.model.bseffectivecontrol.UpdateBSEffectiveControl;
import com.mnrclara.api.cg.transaction.repository.BSEffectiveControlRepository;
import com.mnrclara.api.cg.transaction.repository.OwnerShipRequestRepository;
import com.mnrclara.api.cg.transaction.repository.specification.BSEffectiveControlSpecification;
import com.mnrclara.api.cg.transaction.util.CommonUtils;
import com.sun.jdi.InvocationException;
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
public class BSEffectiveControlService {
    @Autowired
    private OwnerShipRequestRepository ownerShipRequestRepository;

    @Autowired
    private BSEffectiveControlRepository bsEffectiveControlRepository;


    /**
     * getAllBsEffectiveControl
     *
     * @return
     */
    public List<BSEffectiveControl> getAllBsEffectiveControl() {
        List<BSEffectiveControl> bsEffectiveControlList = bsEffectiveControlRepository.findAll();
        bsEffectiveControlList = bsEffectiveControlList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + bsEffectiveControlList);
        return bsEffectiveControlList;
    }


    /**
     * @param companyId
     * @param languageId
     * @param validationId
     * @return
     */
    public BSEffectiveControl getBSEffectiveControl(String companyId, String languageId, Long validationId) {

        Optional<BSEffectiveControl> dbBSEffectiveControl =
                bsEffectiveControlRepository.findByCompanyIdAndLanguageIdAndValidationIdAndDeletionIndicator(
                        companyId, languageId, validationId, 0L);

        if (dbBSEffectiveControl.isEmpty()) {
            throw new BadRequestException("The given values companyId - "
                    + companyId + " languageId " + languageId + " validationId " + validationId + " doesn't exists");
        }
        return dbBSEffectiveControl.get();
    }


    /**
     * @param addBSEffectiveControl
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationException
     */
    public BSEffectiveControl createBSEffectiveControl(AddBSEffectiveControl addBSEffectiveControl, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        Optional<BSEffectiveControl> duplicateBSEffectiveControl =
                bsEffectiveControlRepository.findByCompanyIdAndLanguageIdAndValidationIdAndDeletionIndicator(
                        addBSEffectiveControl.getCompanyId(),
                        addBSEffectiveControl.getLanguageId(),
                        addBSEffectiveControl.getValidationId(),
                        0L);

        if (!duplicateBSEffectiveControl.isEmpty()) {
            throw new RuntimeException("Record is Getting duplicated");
        } else {
            BSEffectiveControl dbBSEffectiveControl = new BSEffectiveControl();
            BeanUtils.copyProperties(addBSEffectiveControl, dbBSEffectiveControl, CommonUtils.getNullPropertyNames(addBSEffectiveControl));
            dbBSEffectiveControl.setDeletionIndicator(0L);
            dbBSEffectiveControl.setCreatedBy(loginUserID);
            dbBSEffectiveControl.setUpdatedBy(loginUserID);
            dbBSEffectiveControl.setCreatedOn(new Date());
            dbBSEffectiveControl.setUpdatedOn(new Date());
            return bsEffectiveControlRepository.save(dbBSEffectiveControl);
        }
    }

    /**
     * @param companyId
     * @param languageId
     * @param validationId
     * @param loginUserID
     * @param updateBSEffectiveControl
     * @return
     */
    public BSEffectiveControl updateBSEffectiveControl(String companyId, String languageId, Long validationId,
                                                       String loginUserID, UpdateBSEffectiveControl updateBSEffectiveControl) {

        BSEffectiveControl dbBSEffectiveControl = getBSEffectiveControl(companyId, languageId, validationId);

        BeanUtils.copyProperties(updateBSEffectiveControl, dbBSEffectiveControl,
                CommonUtils.getNullPropertyNames(updateBSEffectiveControl));
        dbBSEffectiveControl.setDeletionIndicator(0L);
        dbBSEffectiveControl.setUpdatedBy(loginUserID);
        dbBSEffectiveControl.setUpdatedOn(new Date());
        return bsEffectiveControlRepository.save(dbBSEffectiveControl);

    }

    /**
     * @param companyId
     * @param languageId
     * @param validationId
     * @param loginUserID
     * @return
     */
    public void deleteBSEffectiveControl(String companyId, String languageId, Long validationId, String loginUserID) {

        BSEffectiveControl dbBsEffectiveControl = getBSEffectiveControl(companyId, languageId, validationId);
        if (dbBsEffectiveControl != null) {
            dbBsEffectiveControl.setDeletionIndicator(1L);
            dbBsEffectiveControl.setUpdatedBy(loginUserID);
            dbBsEffectiveControl.setUpdatedOn(new Date());
            bsEffectiveControlRepository.save(dbBsEffectiveControl);
        } else {
            throw new EntityNotFoundException("Error in deleting Id:" + validationId);
        }
    }

    /**
     * @param findBSEffectiveControl
     * @return
     * @throws ParseException
     */
    public List<BSEffectiveControl> findBSEffectiveControl(FindBSEffectiveControl findBSEffectiveControl)
            throws ParseException {

        BSEffectiveControlSpecification spec = new BSEffectiveControlSpecification(findBSEffectiveControl);
        List<BSEffectiveControl> results = bsEffectiveControlRepository.findAll(spec);
        log.info("results: " + results);
        return results;
    }
}
