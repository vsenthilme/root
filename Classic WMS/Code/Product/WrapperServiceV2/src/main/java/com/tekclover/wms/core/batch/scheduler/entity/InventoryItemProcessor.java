package com.tekclover.wms.core.batch.scheduler.entity;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.tekclover.wms.core.batch.config.singleton.AccountService;
import com.tekclover.wms.core.batch.config.singleton.AppConfig;
import com.tekclover.wms.core.batch.dto.Inventory2;
import com.tekclover.wms.core.util.CommonUtils;

public class InventoryItemProcessor implements ItemProcessor<Inventory, Inventory2> {
	
	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	
    @Override
    public Inventory2 process(Inventory inventoryInput) throws Exception {
    	Inventory2 inventory2 = new Inventory2();
    	BeanUtils.copyProperties(inventoryInput, inventory2, CommonUtils.getNullPropertyNames(inventoryInput));
    	/*
    	 * private String description;			// ReferenceField8
    	 * private String mfrPartNumber;      	// ReferenceField9
    	 * private String storageSectionId;		// ReferenceField10 
    	 */
    	inventory2.setItemCode("\t" + inventoryInput.getItemCode() );
    	inventory2.setPackBarcodes("\t" + inventoryInput.getPackBarcodes());
    	inventory2.setDescription(CommonUtils.escapeComma(inventoryInput.getReferenceField8()));
    	inventory2.setMfrPartNumber(inventoryInput.getReferenceField9());
    	inventory2.setStorageSectionId(inventoryInput.getReferenceField10());
    	
    	inventory2.setInventoryQuantity(inventoryInput.getInventoryQuantity() != null ? inventoryInput.getInventoryQuantity() : 0);
    	inventory2.setAllocatedQuantity(inventoryInput.getAllocatedQuantity() != null ? inventoryInput.getAllocatedQuantity() : 0);
		
    	inventory2.setTotalQty(Double.sum(inventoryInput.getInventoryQuantity() != null ? inventoryInput.getInventoryQuantity() : 0,
    			inventoryInput.getAllocatedQuantity() != null ? inventoryInput.getAllocatedQuantity() : 0 ));
    	
//		AccountService service1 = context.getBean("accountService", AccountService.class);
//		service1.setInventory(inventory2);
        return inventory2;
    }
}