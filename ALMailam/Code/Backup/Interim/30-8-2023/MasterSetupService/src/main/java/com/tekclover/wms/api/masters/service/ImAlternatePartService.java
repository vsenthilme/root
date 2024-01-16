package com.tekclover.wms.api.masters.service;

import com.tekclover.wms.api.masters.exception.BadRequestException;
import com.tekclover.wms.api.masters.model.imalternateparts.AddImAlternatePart;
import com.tekclover.wms.api.masters.model.imalternateparts.ImAlternatePart;
import com.tekclover.wms.api.masters.model.imalternateparts.SearchImAlternateParts;
import com.tekclover.wms.api.masters.model.imalternateparts.UpdateImAlternatePart;
import com.tekclover.wms.api.masters.repository.ImAlternatePartRepository;
import com.tekclover.wms.api.masters.repository.specification.ImAlternatePartSpecification;
import com.tekclover.wms.api.masters.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImAlternatePartService {

    @Autowired
    private ImAlternatePartRepository imAlternatePartRepository;

    public List<ImAlternatePart>getAllImAlternateParts(){
        List<ImAlternatePart>imAlternatePartList=imAlternatePartRepository.findAll();
//        log.info("imalternateparts : " + imAlternatePartList);
        imAlternatePartList=imAlternatePartList.stream().filter(n->n.getDeletionIndicator()!=null && n.getDeletionIndicator()==0).collect(Collectors.toList());
        return imAlternatePartList;
    }

    public ImAlternatePart getImAlternatePart(String companyCodeId,String languageId,String warehouseId,String plantId,String itemCode,String altItemCode){
        Optional<ImAlternatePart>imAlternatePart=imAlternatePartRepository.findByCompanyCodeIdAndLanguageIdAndPlantIdAndWarehouseIdAndItemCodeAndAltItemCodeAndDeletionIndicator(
                companyCodeId,
                languageId,
                plantId,
                warehouseId,
                itemCode,
                altItemCode,
                0l
        );
        if(imAlternatePart.isEmpty()){
            throw new BadRequestException("The given values:"+
                    "companyCodeId"+companyCodeId+
                    "languageId"+languageId+
                    "plantId"+plantId+
                    "warehouseId"+warehouseId+
                    "itemCode"+itemCode+
                    "altItemCode"+altItemCode+"doesn't exists");
        }
        return imAlternatePart.get();
    }

    public ImAlternatePart createAlternatePart(AddImAlternatePart newAddImAlternatePart,String loginUserID)
            throws IllegalAccessException, InvocationTargetException{
        ImAlternatePart imAlternatePart=new ImAlternatePart();
        Optional<ImAlternatePart>duplicateImAlternatePart=imAlternatePartRepository.findByCompanyCodeIdAndLanguageIdAndPlantIdAndWarehouseIdAndItemCodeAndAltItemCodeAndDeletionIndicator(newAddImAlternatePart.getCompanyCodeId(), newAddImAlternatePart.getLanguageId(), newAddImAlternatePart.getPlantId(), newAddImAlternatePart.getWarehouseId(), newAddImAlternatePart.getItemCode(), newAddImAlternatePart.getAltItemCode(), 0L);
        if(!duplicateImAlternatePart.isEmpty()){
            throw new EntityNotFoundException("The Record is Getting Duplicate");
        }
        else{
            BeanUtils.copyProperties(newAddImAlternatePart,imAlternatePart, CommonUtils.getNullPropertyNames(newAddImAlternatePart));
            imAlternatePart.setDeletionIndicator(0L);
            imAlternatePart.setCreatedBy(loginUserID);
            imAlternatePart.setUpdatedBy(loginUserID);
            imAlternatePart.setCreatedOn(new Date());
            imAlternatePart.setUpdatedOn(new Date());
            return imAlternatePartRepository.save(imAlternatePart);
        }
    }
    /**
     * updateImAlternateUom
     * @param altItemCode
     * @param updateImAlternatePart
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ImAlternatePart updateImAlternatePart(String companyCodeId, String languageId, String plantId, String warehouseId, String itemCode, String altItemCode, UpdateImAlternatePart updateImAlternatePart,String loginUserID)
        throws  IllegalAccessException,InvocationTargetException{
            ImAlternatePart dbImAlternatePart = getImAlternatePart(companyCodeId,languageId,warehouseId,plantId,itemCode,altItemCode);
            BeanUtils.copyProperties(updateImAlternatePart, dbImAlternatePart, CommonUtils.getNullPropertyNames(updateImAlternatePart));
            dbImAlternatePart.setUpdatedBy(loginUserID);
            dbImAlternatePart.setUpdatedOn(new Date());
            return imAlternatePartRepository.save(dbImAlternatePart);
        }

    /**
     * deleteImAlternateUom
     * @param altItemCode
     */
    public void deleteImAlternateUom (String companyCodeId, String languageId, String plantId, String warehouseId, String itemCode, String altItemCode, String loginUserID) {
        ImAlternatePart imAlternatePart = getImAlternatePart(companyCodeId,languageId,warehouseId,plantId,itemCode,altItemCode);
        if ( imAlternatePart != null) {
            imAlternatePart.setDeletionIndicator (1L);
            imAlternatePart.setUpdatedBy(loginUserID);
            imAlternatePart.setUpdatedOn(new Date());
            imAlternatePartRepository.save(imAlternatePart);
        } else {
            throw new EntityNotFoundException("Error in deleting Id:" + altItemCode);
        }
    }
    public List<ImAlternatePart> findIAmAlternatePart(SearchImAlternateParts searchImAlternateParts)
            throws Exception {

        ImAlternatePartSpecification spec = new ImAlternatePartSpecification(searchImAlternateParts);
        List<ImAlternatePart> results = imAlternatePartRepository.findAll(spec);
        log.info("results: " + results);
        return results;
    }

}
