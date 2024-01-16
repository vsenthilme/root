package com.tekclover.wms.core.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tekclover.wms.core.batch.dto.BinLocation;
import com.tekclover.wms.core.batch.dto.BomHeader;
import com.tekclover.wms.core.batch.dto.BomLine;
import com.tekclover.wms.core.batch.dto.BusinessPartner;
import com.tekclover.wms.core.batch.dto.HandlingEquipment;
import com.tekclover.wms.core.batch.dto.IMPartner;
import com.tekclover.wms.core.batch.dto.ImBasicData1;
import com.tekclover.wms.core.batch.dto.Inventory;
import com.tekclover.wms.core.batch.mapper.BinLocationFieldSetMapper;
import com.tekclover.wms.core.batch.mapper.BomHeaderFieldSetMapper;
import com.tekclover.wms.core.batch.mapper.BomLineFieldSetMapper;
import com.tekclover.wms.core.batch.mapper.BusinessPartnerFieldSetMapper;
import com.tekclover.wms.core.batch.mapper.HandlingEquipmentFieldSetMapper;
import com.tekclover.wms.core.batch.mapper.IMPartnerFieldSetMapper;
import com.tekclover.wms.core.batch.mapper.ImBasicData1FieldSetMapper;
import com.tekclover.wms.core.batch.mapper.InventoryFieldSetMapper;
import com.tekclover.wms.core.config.PropertiesConfig;
import com.tekclover.wms.core.model.transaction.InventoryStock;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class JobConfiguration extends DefaultBatchConfigurer {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	/*-------------------------BOMHEADER----------------------------------------------------------------*/
	@Bean
	public FlatFileItemReader<BomHeader> bomHeaderItemReader() {
		FlatFileItemReader<BomHeader> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getBomheaderFileName()));
		
		DefaultLineMapper<BomHeader> customerLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "languageId", "companyCode", "plantId", "warehouseId", "parentItemCode",
				"bomNumber", "parentItemQuantity", "statusId", "deletionIndicator", "createdBy", "createdOn"});

		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new BomHeaderFieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public JdbcBatchItemWriter<BomHeader> bomHeaderItemWriter() {
		JdbcBatchItemWriter<BomHeader> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblbomheader (LANG_ID,C_ID,PLANT_ID,WH_ID,PAR_ITM_CODE, BOM_NO, PAR_ITM_QTY, "
				+ "STATUS_ID, Is_deleted, CTD_BY, CTD_ON, UTD_BY, UTD_ON) "
				+ "VALUES (:languageId, :companyCode, :plantId, :warehouseId, "
				+ ":parentItemCode, :bomNumber, :parentItemQuantity, :statusId,\r\n"
				+ ":deletionIndicator, :createdBy, convert(datetime, :createdOn), :createdBy, convert(datetime, :createdOn))");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<BomHeader, BomHeader>chunk(10).reader(bomHeaderItemReader())
				.writer(bomHeaderItemWriter()).build();
	}
	
	//---------------------------BOMLINE--------------------------------------------------------------------//
	
	@Bean
	public FlatFileItemReader<BomLine> bomLineItemReader() {
		FlatFileItemReader<BomLine> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getBomlineFileName()));
		
		DefaultLineMapper<BomLine> customerLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "languageId", "companyCodeId", "plantId", "warehouseId", "bomNumber", "childItemCode", "childItemQuantity", 
										"statusId", "deletionIndicator", "createdBy", "createdOn"});
		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new BomLineFieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public JdbcBatchItemWriter<BomLine> bomLineItemWriter() throws Exception {
		JdbcBatchItemWriter<BomLine> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblbomline "
				+ "(LANG_ID, C_ID, PLANT_ID, WH_ID, BOM_NO, CHL_ITEM_CODE, CHL_ITM_QTY, STATUS_ID, "
				+ "IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON) "
				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, "
				+ ":bomNumber, :childItemCode, :childItemQuantity, :statusId,\r\n"
				+ ":deletionIndicator, :createdBy, convert(datetime, :createdOn), :createdBy, convert(datetime, :createdOn))");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step2() throws Exception {
		return stepBuilderFactory.get("step2").<BomLine, BomLine>chunk(10).reader(bomLineItemReader())
				.writer(bomLineItemWriter()).build();
	}
	
	//---------------------------BINLOCATION--------------------------------------------------------------------//

	@Bean
	public FlatFileItemReader<BinLocation> storageBinItemReader() {
		FlatFileItemReader<BinLocation> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getBinlocationFileName()));

		DefaultLineMapper<BinLocation> customerLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"languageId", "companyCodeId", "plantId", "warehouseId", "storageBin", "floorId",
				"storageSectionId", "rowId", "aisleNumber", "spanId", "shelfId", "binSectionId", "storageTypeId", "binClassId",
				"description", "binBarcode", "putawayBlock", "pickingBlock", "blockReason", "statusId",
				"occupiedVolume", "occupiedWeight", "occupiedQuantity", "remainingVolume", "remainingWeight",
				"remainingQuantity", "totalVolume", "totalWeight", "totalQuantity", "capacityCheck", "allocatedVolume",
				"deletionIndicator", "dType", "createdBy"});
		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new BinLocationFieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Bean
	public JdbcBatchItemWriter<BinLocation> storageBinItemWriter() {
		JdbcBatchItemWriter<BinLocation> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblstoragebin "
				+ " (LANG_ID, C_ID, PLANT_ID, WH_ID, ST_BIN, FL_ID, ST_SEC_ID, ROW_ID, AISLE_ID, SPAN_ID, SHELF_ID, "
				+ " BIN_SECTION_ID, ST_TYP_ID, BIN_CL_ID, ST_BIN_TEXT, BIN_BAR, PUTAWAY_BLOCK, PICK_BLOCK, BLK_REASON, "
				+ " STATUS_ID, OCC_VOL, OCC_WT, OCC_QTY, REMAIN_VOL, REMAIN_WT, REMAIN_QTY, TOT_VOL, TOT_WT, "
				+ " TOT_QTY, CAP_CHECK, ALLOC_VOL, IS_DELETED, DTYPE, CTD_BY, CTD_ON, UTD_BY, UTD_ON) "
				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, :storageBin, :floorId, :storageSectionId, "
				+ ":rowId, :aisleNumber, :spanId, :shelfId, :binSectionId, :storageTypeId, :binClassId, :description,\r\n"
				+ ":binBarcode, :putawayBlock, :pickingBlock, :blockReason, :statusId,\r\n"
				+ ":occupiedVolume, :occupiedWeight, :occupiedQuantity, :remainingVolume, :remainingWeight, :remainingQuantity, \r\n"
				+ ":totalVolume, :totalWeight, :totalQuantity, :capacityCheck, :allocatedVolume,\r\n"
				+ ":deletionIndicator, :dType, :createdBy, GETDATE(), :createdBy, GETDATE())");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step3() {
		return stepBuilderFactory.get("step3").<BinLocation, BinLocation>chunk(10).reader(storageBinItemReader())
				.writer(storageBinItemWriter()).build();
	}
	
	//---------------------------BusinessPartner--------------------------------------------------------------------//
	@Bean
	public FlatFileItemReader<BusinessPartner> businessPartnerItemReader() {
		FlatFileItemReader<BusinessPartner> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getBusinesspartnerFileName()));
		
		DefaultLineMapper<BusinessPartner> customerLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "languageId", "companyCodeId", "plantId", "warehouseId", "businessParnterType", "partnerCode", 
										  "partnerName", "address1", "address2", "zone", "country", "state", "phoneNumber", "faxNumber", 
										  "emailId", "referenceText", "location", "lattitude", "longitude", "storageTypeId", "storageBin",
										  "statusId", "deletionIndicator", "createdBy"});
		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new BusinessPartnerFieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public JdbcBatchItemWriter<BusinessPartner> businessPartnerItemWriter() {
		JdbcBatchItemWriter<BusinessPartner> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblbusinesspartner (LANG_ID, C_ID, PLANT_ID, WH_ID, PARTNER_TYP, PARTNER_CODE, PARTNER_NM, "
				+ "ADD_1, ADD_2, Zone, COUNTRY, STATE, PH_NO, FX_NO, MAIL_ID, REF_TXT, LOCATION, LATITUDE, LONGITUDE, ST_TYP_ID, "
				+ "ST_BIN, STATUS_ID, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON) "
				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, :businessParnterType, :partnerCode, :partnerName, "
				+ ":address1, :address2, :zone, :country, :state, :phoneNumber, :faxNumber, :emailId, :referenceText, :location, "
				+ ":lattitude, :longitude, :storageTypeId, :storageBin, :statusId,\r\n"
				+ ":deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE())");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step4() {
		return stepBuilderFactory.get("step4").<BusinessPartner, BusinessPartner>chunk(10).reader(businessPartnerItemReader())
				.writer(businessPartnerItemWriter()).build();
	}
	
	//---------------------------HandlingEquipment--------------------------------------------------------------------//
	@Bean
	public FlatFileItemReader<HandlingEquipment> handlingEquipmentItemReader() {
		FlatFileItemReader<HandlingEquipment> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getHandlingequipmentFileName()));
		
		DefaultLineMapper<HandlingEquipment> customerLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] {"languageId", "companyCodeId", "plantId", "warehouseId", "handlingEquipmentId", "category", 
										 "handlingUnit", "acquistionDate", "acquistionValue", "currencyId", "modelNo", "manufacturerPartNo", 
										 "countryOfOrigin", "heBarcode", "statusId", "deletionIndicator", "createdBy"});
		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new HandlingEquipmentFieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public JdbcBatchItemWriter<HandlingEquipment> handlingEquipmentItemWriter() {
		JdbcBatchItemWriter<HandlingEquipment> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblhandlingequipment (LANG_ID, C_ID, PLANT_ID, WH_ID, HE_ID, CATEGORY, HU_UNIT, AQU_DT, "
				+ "AQU_VAL, CURR_ID, MOD_NO, MFR_PART, CTY_ORG, HE_BAR, STATUS_ID, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON) "
				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, :handlingEquipmentId, :category, :handlingUnit,"
				+ " :acquistionDate, :acquistionValue, :currencyId, :modelNo, :manufacturerPartNo, :countryOfOrigin, :heBarcode, :statusId,\r\n"
				+ " :deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE())");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step5() {
		return stepBuilderFactory.get("step5").<HandlingEquipment, HandlingEquipment>chunk(10).reader(handlingEquipmentItemReader())
				.writer(handlingEquipmentItemWriter()).build();
	}
	
	//---------------------------Inventory--------------------------------------------------------------------//
	@Bean
	public FlatFileItemReader<Inventory> inventoryItemReader() {
		FlatFileItemReader<Inventory> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getInventoryFileName()));

		DefaultLineMapper<Inventory> inventoryLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"languageId", "companyCodeId", "plantId", "warehouseId", "palletCode", "caseCode",
				"itemCode", "packBarcode", "variantCode", "variantSubCode", "batchSerialNumber", "storageBin",
				"stockTypeId", "specialStockIndicatorId", "referenceOrderNo", "storageMethod", "binClassId", "description",
				"allocatedQuantity", "inventoryQuantity", "inventoryUom", "manufacturerCode", "manufacturerDate", "expiryDate",
				"barcodeId", "cbm", "cbmUnit", "cbmPerQuantity", "manufacturerName", "origin", "brand", "referenceDocumentNo",
				"companyDescription", "plantDescription", "warehouseDescription", "statusDescription", "deletionIndicator", "dType", "createdBy"});
		inventoryLineMapper.setLineTokenizer(tokenizer);
		inventoryLineMapper.setFieldSetMapper(new InventoryFieldSetMapper());
		inventoryLineMapper.afterPropertiesSet();
		reader.setLineMapper(inventoryLineMapper);
		return reader;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Bean
	public JdbcBatchItemWriter<Inventory> inventoryItemWriter() {
		JdbcBatchItemWriter<Inventory> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblinventory (LANG_ID, C_ID, PLANT_ID, WH_ID, PAL_CODE, CASE_CODE, ITM_CODE, PACK_BARCODE, "
				+ "VAR_ID, VAR_SUB_ID, STR_NO, ST_BIN, STCK_TYP_ID, SP_ST_IND_ID, REF_ORD_NO, STR_MTD, BIN_CL_ID, TEXT, ALLOC_QTY, "
				+ "INV_QTY, INV_UOM, MFR_CODE, MFR_DATE, EXP_DATE, BARCODE_ID, CBM, CBM_UNIT, CBM_PER_QTY, MFR_NAME, ORIGIN, BRAND, "
				+ "REF_DOC_NO, C_TEXT, PLANT_TEXT, WH_TEXT, STATUS_TEXT, IS_DELETED, DTYPE, IU_CTD_BY, IU_CTD_ON) "
				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, :palletCode, :caseCode, :itemCode, :packBarcode, "
				+ " :variantCode, :variantSubCode, :batchSerialNumber, :storageBin, :stockTypeId, :specialStockIndicatorId,\r\n"
				+ " :referenceOrderNo, :storageMethod, :binClassId, :description, :allocatedQuantity, :inventoryQuantity, :inventoryUom,\r\n"
				+ " :manufacturerCode, :manufacturerDate, :expiryDate, :barcodeId, :cbm, :cbmUnit, :cbmPerQuantity, :manufacturerName,\r\n"
				+ " :origin, :brand, :referenceDocumentNo, :companyDescription, :plantDescription, :warehouseDescription,\r\n"
				+ " :statusDescription, :deletionIndicator, :dType, :createdBy, GETDATE())");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step6() {
		return stepBuilderFactory.get("step6").<Inventory, Inventory>chunk(10).reader(inventoryItemReader())
				.writer(inventoryItemWriter()).build();
	}
	
	//---------------------------ImBasicData1--110------------------------------------------------------------------// 
	@Bean
	public FlatFileItemReader<ImBasicData1> imBasicData1ItemReader() {
		FlatFileItemReader<ImBasicData1> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getImbasicdata1FileName()));

		DefaultLineMapper<ImBasicData1> customerLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"uomId", "languageId", "companyCodeId", "plantId", "warehouseId", "itemCode", "manufacturerPartNo",
				"description", "model", "specifications1", "specifications2", "eanUpcNo", "hsnCode", "itemType", "itemGroup", "subItemGroup",
				"storageSectionId", "totalStock", "minimumStock", "maximumStock", "reorderLevel", "capacityCheck", "replenishmentQty",
				"safetyStock", "capacityUnit", "capacityUom", "quantity", "weight", "shelfLifeIndicator", "statusId", "length", "width",
				"height", "dimensionUom", "volume", "manufacturerName", "manufacturerFullName", "manufacturerCode", "brand",
				"supplierPartNumber", "remarks", "dType", "deletionIndicator", "createdBy"});
		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new ImBasicData1FieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Bean
	public JdbcBatchItemWriter<ImBasicData1> imBasicData1ItemWriter() {
		JdbcBatchItemWriter<ImBasicData1> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblimbasicdata1 (UOM_ID, LANG_ID, C_ID, PLANT_ID, WH_ID, ITM_CODE, MFR_PART, TEXT, MODEL, "
				+ "SPEC_01, SPEC_02, EAN_UPC_NO, HSN_CODE, ITM_TYP_ID, ITM_GRP_ID, SUB_ITM_GRP_ID, ST_SEC_ID, TOT_STK, MIN_STK, MAX_STK, "
				+ "RE_ORD_LVL, CAP_CHK, REP_QTY, SAFTY_STCK, CAP_UNIT, CAP_UOM, QUANTITY, WEIGHT, SHELF_LIFE_IND, STATUS_ID, LENGTH, "
				+ "WIDTH, HEIGHT, DIM_UOM, VOLUME, MANUFACTURER_NAME, MANUFACTURER_FULL_NAME, MANUFACTURER_CODE, BRAND, "
				+ "SUPPLIER_PART_NUMBER, REMARKS, DTYPE, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON) "
				+ "VALUES (:uomId, :languageId, :companyCodeId, :plantId, :warehouseId, :itemCode, :manufacturerPartNo, :description, "
				+ " :model, :specifications1, :specifications2, :eanUpcNo, :hsnCode, :itemType, :itemGroup, :subItemGroup,\r\n"
				+ " :storageSectionId, :totalStock, :minimumStock, :maximumStock, :reorderLevel, :capacityCheck, :replenishmentQty,\r\n"
				+ " :safetyStock, :capacityUnit, :capacityUom, :quantity, :weight, :shelfLifeIndicator, :statusId, :length,\r\n"
				+ " :width, :height, :dimensionUom, :volume, :manufacturerName, :manufacturerFullName, :manufacturerCode,\r\n"
				+ " :brand, :supplierPartNumber, :remarks, :dType, :deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE())");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step7() {
		return stepBuilderFactory.get("step7").<ImBasicData1, ImBasicData1>chunk(10).reader(imBasicData1ItemReader())
				.writer(imBasicData1ItemWriter()).build();
	}
	
//	//---------------------------ImBasicData1--111------------------------------------------------------------------//
//	@Bean
//	public FlatFileItemReader<ImBasicData1> imBasicData1WhId111ItemReader() {
//		FlatFileItemReader<ImBasicData1> reader = new FlatFileItemReader<>();
//		reader.setLinesToSkip(1);
//		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getImbasicdata1111FileName()));
//
//		DefaultLineMapper<ImBasicData1> customerLineMapper = new DefaultLineMapper<>();
//		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//		tokenizer.setNames(new String[] { "languageId", "companyCodeId", "plantId", "warehouseId", "itemCode", "uomId", "description",
//										  "manufacturerPartNo", "itemType", "itemGroup", "subItemGroup", "storageSectionId", "statusId",
//										  "deletionIndicator", "createdBy"});
//		customerLineMapper.setLineTokenizer(tokenizer);
//		customerLineMapper.setFieldSetMapper(new ImBasicData1FieldSetMapper());
//		customerLineMapper.afterPropertiesSet();
//		reader.setLineMapper(customerLineMapper);
//		return reader;
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Bean
//	public JdbcBatchItemWriter<ImBasicData1> imBasicData1WhId111ItemWriter() {
//		JdbcBatchItemWriter<ImBasicData1> itemWriter = new JdbcBatchItemWriter<>();
//		itemWriter.setDataSource(this.dataSource);
//		itemWriter.setSql("INSERT INTO tblimbasicdata1 (LANG_ID, C_ID, PLANT_ID, WH_ID, ITM_CODE, UOM_ID, TEXT, MFR_PART, ITM_TYP_ID, "
//				+ "ITM_GRP_ID, SUB_ITM_GRP_ID, ST_SEC_ID, STATUS_ID, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON) "
//				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, :itemCode, :uomId, :description, :manufacturerPartNo,"
//				+ " :itemType, :itemGroup, :subItemGroup, :storageSectionId, :statusId,\r\n"
//				+ ":deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE())");
//		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
//		itemWriter.afterPropertiesSet();
//		return itemWriter;
//	}
//
//	@Bean
//	public Step step8() {
//		return stepBuilderFactory.get("step8").<ImBasicData1, ImBasicData1>chunk(10).reader(imBasicData1WhId111ItemReader())
//				.writer(imBasicData1WhId111ItemWriter()).build();
//	}
	
	//---------------------------IMPartner--110------------------------------------------------------------------// 
	@Bean
	public FlatFileItemReader<IMPartner> imPartnerItemReader() {
		FlatFileItemReader<IMPartner> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getImpartnerFileName()));

		DefaultLineMapper<IMPartner> customerLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"businessPartnerCode", "languageId", "companyCodeId", "plantId", "warehouseId", "itemCode",
				"businessPartnerType", "partnerItemBarcode", "manufacturerCode", "manufacturerName", "brandName", "partnerName",
				"partnerItemNo", "vendorItemBarcode", "mfrBarcode", "stock", "stockUom", "statusId", "referenceField1",
				"referenceField2", "referenceField3", "deletionIndicator", "createdBy"});
		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new IMPartnerFieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Bean
	public JdbcBatchItemWriter<IMPartner> imPartnerItemWriter() {
		JdbcBatchItemWriter<IMPartner> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblimpartner (PARTNER_CODE, LANG_ID, C_ID, PLANT_ID, WH_ID, ITM_CODE, PARTNER_TYP, "
				+ "PARTNER_ITM_BAR, MFR_CODE, MFR_NAME, BRND_NM, PARTNER_NAME, PARTNER_ITM_CODE, PARTNER_ITM_BAR_CODE, MFR_BAR, "
				+ "STK, STK_UOM, STATUS_ID, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON) "
				+ "VALUES (:businessPartnerCode, :languageId, :companyCodeId, :plantId, :warehouseId, :itemCode, :businessPartnerType, "
				+ ":partnerItemBarcode, :manufacturerCode, :manufacturerName, :brandName, :partnerName, :partnerItemNo,\r\n"
				+ ":vendorItemBarcode, :mfrBarcode, :stock, :stockUom, :statusId, :referenceField1, :referenceField2,\r\n"
				+ ":referenceField3, :deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE())");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step9() {
		return stepBuilderFactory.get("step9").<IMPartner, IMPartner>chunk(10).reader(imPartnerItemReader())
				.writer(imPartnerItemWriter()).build();
	}


	//---------------------------IMPartner--111------------------------------------------------------------------// 
//	@Bean
//	public FlatFileItemReader<IMPartner> imPartnerWhId111ItemReader() {
//		FlatFileItemReader<IMPartner> reader = new FlatFileItemReader<>();
//		reader.setLinesToSkip(1);
//		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getImpartner111FileName()));
//
//		DefaultLineMapper<IMPartner> customerLineMapper = new DefaultLineMapper<>();
//		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//		tokenizer.setNames(new String[] { "languageId", "companyCodeId", "plantId", "warehouseId", "itemCode", "businessParnterType",
//										  "businessPartnerCode", "partnerItemNo", "mfrBarcode", "brandName", "statusId", "referenceField1",
//										  "referenceField2", "referenceField3", "deletionIndicator", "createdBy"});
//		customerLineMapper.setLineTokenizer(tokenizer);
//		customerLineMapper.setFieldSetMapper(new IMPartnerFieldSetMapper());
//		customerLineMapper.afterPropertiesSet();
//		reader.setLineMapper(customerLineMapper);
//		return reader;
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Bean
//	public JdbcBatchItemWriter<IMPartner> imPartnerWhId111ItemWriter() {
//		JdbcBatchItemWriter<IMPartner> itemWriter = new JdbcBatchItemWriter<>();
//		itemWriter.setDataSource(this.dataSource);
//		itemWriter.setSql("INSERT INTO tblimpartner (LANG_ID, C_ID, PLANT_ID, WH_ID, ITM_CODE, PARTNER_TYP, PARTNER_CODE, "
//				+ "PARTNER_ITM_CODE, MFR_BAR, BRND_NM, STATUS_ID, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, IS_DELETED, CTD_BY, "
//				+ "CTD_ON, UTD_BY, UTD_ON) "
//				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, :itemCode, :businessParnterType, :businessPartnerCode,"
//				+ ":partnerItemNo, :mfrBarcode, :brandName, :statusId, :referenceField1, :referenceField2, :referenceField3,\r\n"
//				+ ":deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE())");
//		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
//		itemWriter.afterPropertiesSet();
//		return itemWriter;
//	}

//	@Bean
//	public Step step10() {
//		return stepBuilderFactory.get("step10").<IMPartner, IMPartner>chunk(10).reader(imPartnerWhId111ItemReader())
//				.writer(imPartnerWhId111ItemWriter()).build();
//	}
	
	//---------------------------Inventory--------------------------------------------------------------------//
	@Bean
	public FlatFileItemReader<Inventory> inventoryStockItemReader() {
		FlatFileItemReader<Inventory> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getInventoryFileName()));
		
		DefaultLineMapper<Inventory> customerLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "languageId", "companyCodeId", "plantId", "warehouseId", "itemCode", "packBarcode", "storageBin", 
										  "stockTypeId", "specialStockIndicatorId", "binClassId", "description", "inventoryQuantity", 
										  "inventoryUom", "deletionIndicator", "createdBy"});
		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new InventoryFieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public JdbcBatchItemWriter<Inventory> inventoryStockItemWriter() {
		JdbcBatchItemWriter<Inventory> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblinventorystock (LANG_ID, C_ID, PLANT_ID, WH_ID, ITM_CODE, PACK_BARCODE, ST_BIN, STCK_TYP_ID, "
				+ "SP_ST_IND_ID, BIN_CL_ID, TEXT, INV_QTY, INV_UOM, IS_DELETED, CTD_BY, CTD_ON) "
				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, :itemCode, :packBarcode, :storageBin, :stockTypeId,"
				+ ":specialStockIndicatorId, :binClassId, :description, :inventoryQuantity, :inventoryUom,\r\n"
				+ ":deletionIndicator, :createdBy, GETDATE())");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step11() {
		return stepBuilderFactory.get("step11").<Inventory, Inventory>chunk(10).reader(inventoryStockItemReader())
				.writer(inventoryStockItemWriter()).build();
	}
	
	/*-----------------------------------------------------------------------------------------*/
	@Bean
	public JobListener wmsListener() throws Exception {
		JobListener listener = new JobListener();
		return listener;
	}
	
	@Bean
	public Job jobBomHeader() throws Exception {
		return jobBuilderFactory.get("jobBomHeader")
				.listener(wmsListener())
				.start(step1())
				.build();
	}
	
	@Bean
	public Job jobBomLine() throws Exception {
		return jobBuilderFactory.get("jobBomLine")
				.listener(wmsListener())
				.start(step2())
				.build();
	}
	
	@Bean
	public Job jobBinLocation() throws Exception {
		return jobBuilderFactory.get("jobBinLocation")
				.listener(wmsListener())
				.start(step3())
				.build();
	}
	
	@Bean
	public Job jobBusinessPartner() throws Exception {
		return jobBuilderFactory.get("jobBusinessPartner")
				.listener(wmsListener())
				.start(step4())
				.build();
	}
	
	@Bean
	public Job jobHandlingEquipment() throws Exception {
		return jobBuilderFactory.get("jobHandlingEquipment")
				.listener(wmsListener())
				.start(step5())
				.build();
	}
	
	@Bean
	public Job jobInventory() throws Exception {
		return jobBuilderFactory.get("jobInventory")
				.listener(wmsListener())
				.start(step6())
				.build();
	}
	
	@Bean
	public Job jobInventoryStock() throws Exception {
		return jobBuilderFactory.get("jobInventoryStock")
				.listener(wmsListener())
				.start(step11())
				.build();
	}
	
	@Bean
	public Job jobImBasicData1() throws Exception{
		return jobBuilderFactory.get("jobImBasicData1")
				.listener(wmsListener())
				.start(step7())
				.build();
	}
	
//	@Bean
//	public Job jobImBasicData1WhId111() throws Exception{
//		return jobBuilderFactory.get("jobImBasicData1WhId111")
//				.listener(wmsListener())
//				.start(step8())
//				.build();
//	}
//
	@Bean
	public Job jobIMPartner() throws Exception{
		return jobBuilderFactory.get("jobIMPartner")
				.listener(wmsListener())
				.start(step9())
				.build();
	}
	
	@Bean
	public Job jobIMPartnerWhId111() throws Exception {
		return jobBuilderFactory.get("jobIMPartnerWhId111")
				.listener(wmsListener())
				.start(step9())
				.build();
	}
}