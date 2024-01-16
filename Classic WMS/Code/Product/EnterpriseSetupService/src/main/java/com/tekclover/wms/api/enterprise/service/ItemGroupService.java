package com.tekclover.wms.api.enterprise.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.enterprise.model.IkeyValuePair;
import com.tekclover.wms.api.enterprise.model.itemtype.ItemType;
import com.tekclover.wms.api.enterprise.model.itemtype.SearchItemType;
import com.tekclover.wms.api.enterprise.repository.*;
import com.tekclover.wms.api.enterprise.repository.specification.ItemTypeSpecification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.enterprise.controller.exception.BadRequestException;
import com.tekclover.wms.api.enterprise.model.itemgroup.AddItemGroup;
import com.tekclover.wms.api.enterprise.model.itemgroup.ItemGroup;
import com.tekclover.wms.api.enterprise.model.itemgroup.SearchItemGroup;
import com.tekclover.wms.api.enterprise.model.itemgroup.UpdateItemGroup;
import com.tekclover.wms.api.enterprise.repository.specification.ItemGroupSpecification;
import com.tekclover.wms.api.enterprise.util.CommonUtils;
import com.tekclover.wms.api.enterprise.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ItemGroupService {
	@Autowired
	private ItemTypeRepository itemTypeRepository;
	@Autowired
	private WarehouseRepository warehouseRepository;
	@Autowired
	private PlantRepository plantRepository;
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private ItemGroupRepository itemgroupRepository;

	/**
	 * getItemGroups
	 * @return
	 */
	public List<ItemGroup> getItemGroups () {
		List<ItemGroup> itemgroupList = itemgroupRepository.findAll();
		log.info("itemgroupList : " + itemgroupList);
		itemgroupList = itemgroupList.stream().filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0).collect(Collectors.toList());
		List<ItemGroup> newItemGroup=new ArrayList<>();
		for(ItemGroup dbItemGroup:itemgroupList) {
			if (dbItemGroup.getCompanyIdAndDescription() != null&&dbItemGroup.getPlantIdAndDescription()!=null&&dbItemGroup.getWarehouseIdAndDescription()!=null&&dbItemGroup.getItemTypeIdAndDescription()!=null) {
				IkeyValuePair iKeyValuePair = companyRepository.getCompanyIdAndDescription(dbItemGroup.getCompanyId(), dbItemGroup.getLanguageId());
				IkeyValuePair iKeyValuePair1 = plantRepository.getPlantIdAndDescription(dbItemGroup.getPlantId(), dbItemGroup.getLanguageId(), dbItemGroup.getCompanyId());
				IkeyValuePair iKeyValuePair2 = warehouseRepository.getWarehouseIdAndDescription(dbItemGroup.getWarehouseId(), dbItemGroup.getLanguageId(), dbItemGroup.getCompanyId(), dbItemGroup.getPlantId());
				IkeyValuePair ikeyValuePair3 = itemTypeRepository.getItemTypeIdAndDescription(dbItemGroup.getItemTypeId(), dbItemGroup.getLanguageId(), dbItemGroup.getCompanyId(), dbItemGroup.getPlantId(), dbItemGroup.getWarehouseId());
				IkeyValuePair ikeyValuePair4 = itemgroupRepository.getItemGroupIdAndDescription(dbItemGroup.getItemGroupId(), dbItemGroup.getLanguageId(), dbItemGroup.getCompanyId(), dbItemGroup.getPlantId(), dbItemGroup.getWarehouseId(), dbItemGroup.getItemTypeId());
				if (iKeyValuePair != null) {
					dbItemGroup.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
				}
				if (iKeyValuePair1 != null) {
					dbItemGroup.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
				}
				if (iKeyValuePair2 != null) {
					dbItemGroup.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
				}
				if (ikeyValuePair3 != null) {
					dbItemGroup.setItemTypeIdAndDescription(ikeyValuePair3.getItemTypeId() + "-" + ikeyValuePair3.getDescription());
				}
				if (ikeyValuePair4 != null) {
					dbItemGroup.setDescription(ikeyValuePair4.getDescription());
				}
			}
			newItemGroup.add(dbItemGroup);
		}
		return newItemGroup;

	}

	/**
	 * getItemGroup
	 * @param warehouseId
	 * @param itemGroupId
	 * @param itemTypeId
	 * @param subItemGroupId
	 * @return
	 */
	public ItemGroup getItemGroup (String companyId,String languageId,String plantId,String warehouseId, Long itemTypeId, Long itemGroupId, Long subItemGroupId) {
		Optional<ItemGroup> itemgroup =
				itemgroupRepository.findByLanguageIdAndCompanyIdAndPlantIdAndWarehouseIdAndItemTypeIdAndItemGroupIdAndSubItemGroupIdAndDeletionIndicator(
						languageId, companyId, plantId, warehouseId, itemTypeId, itemGroupId, subItemGroupId, 0L);
		if (itemgroup.isEmpty()) {
			throw new BadRequestException("The given ItemGroup Id : " + itemGroupId + " doesn't exist.");
		}
		ItemGroup newItemGroup = new ItemGroup();
		BeanUtils.copyProperties(itemgroup.get(),newItemGroup, CommonUtils.getNullPropertyNames(itemgroup));
		IkeyValuePair iKeyValuePair=companyRepository.getCompanyIdAndDescription(companyId,languageId);
		IkeyValuePair iKeyValuePair1=plantRepository.getPlantIdAndDescription(plantId,languageId,companyId);
		IkeyValuePair iKeyValuePair2=warehouseRepository.getWarehouseIdAndDescription(warehouseId,languageId,companyId,plantId);
		IkeyValuePair ikeyValuePair3= itemTypeRepository.getItemTypeIdAndDescription(itemTypeId,languageId,companyId,plantId,warehouseId);
		IkeyValuePair ikeyValuePair4=itemgroupRepository.getItemGroupIdAndDescription(itemGroupId,languageId,companyId,plantId,warehouseId, itemTypeId);
		if(iKeyValuePair!=null) {
			newItemGroup.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
		}
		if(iKeyValuePair1!=null) {
			newItemGroup.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
		}
		if(iKeyValuePair2!=null) {
			newItemGroup.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
		}
		if(ikeyValuePair3!=null) {
			newItemGroup.setItemTypeIdAndDescription(ikeyValuePair3.getItemTypeId() + "-" + ikeyValuePair3.getDescription());
		}
		if(ikeyValuePair4!=null) {
			newItemGroup.setDescription(ikeyValuePair4.getDescription());
		}
		return newItemGroup;
	}

	/**
	 * findItemGroup
	 * @param searchItemGroup
	 * @return
	 * @throws ParseException
	 */
	public List<ItemGroup> findItemGroup(SearchItemGroup searchItemGroup) throws Exception {
		if (searchItemGroup.getStartCreatedOn() != null && searchItemGroup.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchItemGroup.getStartCreatedOn(), searchItemGroup.getEndCreatedOn());
			searchItemGroup.setStartCreatedOn(dates[0]);
			searchItemGroup.setEndCreatedOn(dates[1]);
		}

		ItemGroupSpecification spec = new ItemGroupSpecification(searchItemGroup);
		List<ItemGroup> results = itemgroupRepository.findAll(spec);
		log.info("results: " + results);
		results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		List<ItemGroup> newItemGroup=new ArrayList<>();
		for(ItemGroup dbItemGroup:results) {
			if (dbItemGroup.getCompanyIdAndDescription() != null&&dbItemGroup.getPlantIdAndDescription()!=null&&dbItemGroup.getWarehouseIdAndDescription()!=null&&dbItemGroup.getItemTypeIdAndDescription()!=null) {
				IkeyValuePair iKeyValuePair=companyRepository.getCompanyIdAndDescription(dbItemGroup.getCompanyId(), dbItemGroup.getLanguageId());
				IkeyValuePair iKeyValuePair1=plantRepository.getPlantIdAndDescription(dbItemGroup.getPlantId(), dbItemGroup.getLanguageId(), dbItemGroup.getCompanyId());
				IkeyValuePair iKeyValuePair2=warehouseRepository.getWarehouseIdAndDescription(dbItemGroup.getWarehouseId(), dbItemGroup.getLanguageId(), dbItemGroup.getCompanyId(), dbItemGroup.getPlantId());
				IkeyValuePair ikeyValuePair3= itemTypeRepository.getItemTypeIdAndDescription(dbItemGroup.getItemTypeId(), dbItemGroup.getLanguageId(), dbItemGroup.getCompanyId(), dbItemGroup.getPlantId(), dbItemGroup.getWarehouseId());
				IkeyValuePair ikeyValuePair4=itemgroupRepository.getItemGroupIdAndDescription(dbItemGroup.getItemGroupId(), dbItemGroup.getLanguageId(), dbItemGroup.getCompanyId(), dbItemGroup.getPlantId(), dbItemGroup.getWarehouseId(), dbItemGroup.getItemTypeId());
				if (iKeyValuePair != null) {
					dbItemGroup.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
				}
				if (iKeyValuePair1 != null) {
					dbItemGroup.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
				}
				if (iKeyValuePair2 != null) {
					dbItemGroup.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
				}
				if (ikeyValuePair3 != null) {
					dbItemGroup.setItemTypeIdAndDescription(ikeyValuePair3.getItemTypeId() + "-" + ikeyValuePair3.getDescription());
				}
				if (ikeyValuePair4 != null) {
					dbItemGroup.setDescription(ikeyValuePair4.getDescription());
				}
			}
			newItemGroup.add(dbItemGroup);
		}
		return newItemGroup;
	}

	/**
	 * createItemGroup
	 * @param newItemGroup
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ItemGroup createItemGroup (AddItemGroup newItemGroup, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		Optional<ItemGroup> optItemGroup =
				itemgroupRepository.findByLanguageIdAndCompanyIdAndPlantIdAndWarehouseIdAndItemTypeIdAndItemGroupIdAndSubItemGroupIdAndDeletionIndicator(
						newItemGroup.getLanguageId(),
						newItemGroup.getCompanyId(),
						newItemGroup.getPlantId(),
						newItemGroup.getWarehouseId(),
						newItemGroup.getItemTypeId(),
						newItemGroup.getItemGroupId(),
						newItemGroup.getSubItemGroupId(),
						0L);
		if (!optItemGroup.isEmpty()) {
			throw new BadRequestException("The given values are getting duplicated.");
		}

		ItemGroup dbItemGroup = new ItemGroup();
		BeanUtils.copyProperties(newItemGroup, dbItemGroup, CommonUtils.getNullPropertyNames(newItemGroup));
		IkeyValuePair iKeyValuePair=companyRepository.getCompanyIdAndDescription(newItemGroup.getCompanyId(), newItemGroup.getLanguageId());
		IkeyValuePair iKeyValuePair1=plantRepository.getPlantIdAndDescription(newItemGroup.getPlantId(), newItemGroup.getLanguageId(), newItemGroup.getCompanyId());
		IkeyValuePair iKeyValuePair2=warehouseRepository.getWarehouseIdAndDescription(newItemGroup.getWarehouseId(), newItemGroup.getLanguageId(), newItemGroup.getCompanyId(), newItemGroup.getPlantId());
		IkeyValuePair ikeyValuePair3= itemTypeRepository.getItemTypeIdAndDescription(newItemGroup.getItemTypeId(), newItemGroup.getLanguageId(), newItemGroup.getCompanyId(), newItemGroup.getPlantId(), newItemGroup.getWarehouseId());
		IkeyValuePair ikeyValuePair4=itemgroupRepository.getItemGroupIdAndDescription(newItemGroup.getItemGroupId(), newItemGroup.getLanguageId(), newItemGroup.getCompanyId(), newItemGroup.getPlantId(), newItemGroup.getWarehouseId(), newItemGroup.getItemTypeId());

		if(iKeyValuePair != null && iKeyValuePair1 != null &&
				iKeyValuePair2 != null && ikeyValuePair3 != null && ikeyValuePair4 != null) {
			dbItemGroup.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
			dbItemGroup.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
			dbItemGroup.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
			dbItemGroup.setItemTypeIdAndDescription(ikeyValuePair3.getItemTypeId() + "-" + ikeyValuePair3.getDescription());
			dbItemGroup.setDescription(ikeyValuePair4.getDescription());
		}
		else{
			throw new BadRequestException("The given values of Company Id "
					+ newItemGroup.getCompanyId() + " Plant Id "
					+ newItemGroup.getPlantId() + " Warehouse Id "
					+ newItemGroup.getWarehouseId() + " Item Type Id "
					+ newItemGroup.getItemTypeId() + " Item Group Id "
					+ newItemGroup.getItemGroupId() + " doesn't exist");
		}
		dbItemGroup.setDeletionIndicator(0L);
		dbItemGroup.setCreatedBy(loginUserID);
		dbItemGroup.setUpdatedBy(loginUserID);
		dbItemGroup.setCreatedOn(new Date());
		dbItemGroup.setUpdatedOn(new Date());
		return itemgroupRepository.save(dbItemGroup);
	}

	/**
	 * updateItemGroup
	 * @param itemGroupId
	 * @param updateItemGroup
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ItemGroup updateItemGroup (String companyId,String languageId,String plantId,String warehouseId, Long itemTypeId, Long itemGroupId, Long subItemGroupId, UpdateItemGroup updateItemGroup, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ItemGroup dbItemGroup = getItemGroup(companyId,languageId,plantId,warehouseId, itemTypeId, itemGroupId, subItemGroupId);
		BeanUtils.copyProperties(updateItemGroup, dbItemGroup, CommonUtils.getNullPropertyNames(updateItemGroup));
		dbItemGroup.setUpdatedBy(loginUserID);
		dbItemGroup.setUpdatedOn(new Date());
		return itemgroupRepository.save(dbItemGroup);
	}

	/**
	 * deleteItemGroup
	 * @param itemGroupId
	 */
	public void deleteItemGroup (String companyId,String languageId,String plantId,String warehouseId, Long itemTypeId, Long itemGroupId, Long subItemGroupId, String loginUserID) {
		ItemGroup itemgroup = getItemGroup(companyId,languageId,plantId,warehouseId, itemTypeId, itemGroupId, subItemGroupId);
		if ( itemgroup != null) {
			itemgroup.setDeletionIndicator (1L);
			itemgroup.setUpdatedBy(loginUserID);
			itemgroup.setUpdatedOn(new Date());
			itemgroupRepository.save(itemgroup);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + itemGroupId);
		}
	}
}
