package com.tekclover.wms.core.batch.config;

import javax.sql.DataSource;

import com.tekclover.wms.core.batch.dto.*;
import com.tekclover.wms.core.batch.mapper.*;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tekclover.wms.core.config.PropertiesConfig;

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
		tokenizer.setNames(new String[] { "languageId", "companyCodeId", "plantId", "warehouseId", "storageBin", "floorId", 
										  "storageSectionId", "rowId", "aisleNumber", "spanId", "shelfId", "binSectionId", 
										  "storageTypeId", "binClassId", "description",	"binBarcode", "putawayBlock",
										  "pickingBlock", "blockReason", "statusId", "deletionIndicator", "createdBy", });
		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new BinLocationFieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public JdbcBatchItemWriter<BinLocation> storageBinItemWriter() {
		JdbcBatchItemWriter<BinLocation> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblstoragebin "
				+ " (LANG_ID, C_ID, PLANT_ID, WH_ID, ST_BIN, FL_ID, ST_SEC_ID, ROW_ID, AISLE_ID, SPAN_ID, SHELF_ID, "
				+ " BIN_SECTION_ID, ST_TYP_ID, BIN_CL_ID, ST_BIN_TEXT, BIN_BAR, PUTAWAY_BLOCK, PICK_BLOCK, BLK_REASON, "
				+ " STATUS_ID, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON) "
				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, :storageBin, :floorId, :storageSectionId,"
				+ ":rowId, :aisleNumber, :spanId, :shelfId, :binSectionId, :storageTypeId, :binClassId, :description, "
				+ ":binBarcode, :putawayBlock, :pickingBlock, :blockReason, :statusId,\r\n"
				+ ":deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE())");
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
	public JdbcBatchItemWriter<Inventory> inventoryItemWriter() {
		JdbcBatchItemWriter<Inventory> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblinventory (LANG_ID, C_ID, PLANT_ID, WH_ID, ITM_CODE, PACK_BARCODE, ST_BIN, STCK_TYP_ID, "
				+ "SP_ST_IND_ID, BIN_CL_ID, TEXT, INV_QTY, INV_UOM, IS_DELETED, IU_CTD_BY, IU_CTD_ON) "
				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, :itemCode, :packBarcode, :storageBin, :stockTypeId,"
				+ ":specialStockIndicatorId, :binClassId, :description, :inventoryQuantity, :inventoryUom,\r\n"
				+ ":deletionIndicator, :createdBy, GETDATE())");
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
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getImbasicdata1110FileName()));
		
		DefaultLineMapper<ImBasicData1> customerLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "languageId", "companyCodeId", "plantId", "warehouseId", "itemCode", "uomId", "description",
										  "manufacturerPartNo", "itemType", "itemGroup", "subItemGroup", "storageSectionId", "statusId", 
										  "deletionIndicator", "createdBy"});
		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new ImBasicData1FieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public JdbcBatchItemWriter<ImBasicData1> imBasicData1ItemWriter() {
		JdbcBatchItemWriter<ImBasicData1> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblimbasicdata1 (LANG_ID, C_ID, PLANT_ID, WH_ID, ITM_CODE, UOM_ID, TEXT, MFR_PART, ITM_TYP_ID, "
				+ "ITM_GRP_ID, SUB_ITM_GRP_ID, ST_SEC_ID, STATUS_ID, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON) "
				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, :itemCode, :uomId, :description, :manufacturerPartNo,"
				+ " :itemType, :itemGroup, :subItemGroup, :storageSectionId, :statusId,\r\n"
				+ ":deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE())");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step7() {
		return stepBuilderFactory.get("step7").<ImBasicData1, ImBasicData1>chunk(10).reader(imBasicData1ItemReader())
				.writer(imBasicData1ItemWriter()).build();
	}
	
	//---------------------------ImBasicData1--111------------------------------------------------------------------// 
	@Bean
	public FlatFileItemReader<ImBasicData1> imBasicData1WhId111ItemReader() {
		FlatFileItemReader<ImBasicData1> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getImbasicdata1111FileName()));
		
		DefaultLineMapper<ImBasicData1> customerLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "languageId", "companyCodeId", "plantId", "warehouseId", "itemCode", "uomId", "description",
										  "manufacturerPartNo", "itemType", "itemGroup", "subItemGroup", "storageSectionId", "statusId", 
										  "deletionIndicator", "createdBy"});
		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new ImBasicData1FieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public JdbcBatchItemWriter<ImBasicData1> imBasicData1WhId111ItemWriter() {
		JdbcBatchItemWriter<ImBasicData1> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblimbasicdata1 (LANG_ID, C_ID, PLANT_ID, WH_ID, ITM_CODE, UOM_ID, TEXT, MFR_PART, ITM_TYP_ID, "
				+ "ITM_GRP_ID, SUB_ITM_GRP_ID, ST_SEC_ID, STATUS_ID, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON) "
				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, :itemCode, :uomId, :description, :manufacturerPartNo,"
				+ " :itemType, :itemGroup, :subItemGroup, :storageSectionId, :statusId,\r\n"
				+ ":deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE())");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step8() {
		return stepBuilderFactory.get("step8").<ImBasicData1, ImBasicData1>chunk(10).reader(imBasicData1WhId111ItemReader())
				.writer(imBasicData1WhId111ItemWriter()).build();
	}
	
	//---------------------------IMPartner--110------------------------------------------------------------------// 
	@Bean
	public FlatFileItemReader<IMPartner> imPartnerItemReader() {
		FlatFileItemReader<IMPartner> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getImpartner110FileName()));
		
		DefaultLineMapper<IMPartner> customerLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "languageId", "companyCodeId", "plantId", "warehouseId", "itemCode", "businessParnterType", 
										  "businessPartnerCode", "partnerItemNo", "mfrBarcode", "brandName", "statusId", "referenceField1", 
										  "referenceField2", "referenceField3", "deletionIndicator", "createdBy"});
		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new IMPartnerFieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public JdbcBatchItemWriter<IMPartner> imPartnerItemWriter() {
		JdbcBatchItemWriter<IMPartner> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblimpartner (LANG_ID, C_ID, PLANT_ID, WH_ID, ITM_CODE, PARTNER_TYP, PARTNER_CODE, "
				+ "PARTNER_ITM_CODE, MFR_BAR, BRND_NM, STATUS_ID, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, IS_DELETED, CTD_BY, "
				+ "CTD_ON, UTD_BY, UTD_ON) "
				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, :itemCode, :businessParnterType, :businessPartnerCode,"
				+ ":partnerItemNo, :mfrBarcode, :brandName, :statusId, :referenceField1, :referenceField2, :referenceField3,\r\n"
				+ ":deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE())");
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
	@Bean
	public FlatFileItemReader<IMPartner> imPartnerWhId111ItemReader() {
		FlatFileItemReader<IMPartner> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getImpartner111FileName()));
		
		DefaultLineMapper<IMPartner> customerLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "languageId", "companyCodeId", "plantId", "warehouseId", "itemCode", "businessParnterType", 
										  "businessPartnerCode", "partnerItemNo", "mfrBarcode", "brandName", "statusId", "referenceField1", 
										  "referenceField2", "referenceField3", "deletionIndicator", "createdBy"});
		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new IMPartnerFieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public JdbcBatchItemWriter<IMPartner> imPartnerWhId111ItemWriter() {
		JdbcBatchItemWriter<IMPartner> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblimpartner (LANG_ID, C_ID, PLANT_ID, WH_ID, ITM_CODE, PARTNER_TYP, PARTNER_CODE, "
				+ "PARTNER_ITM_CODE, MFR_BAR, BRND_NM, STATUS_ID, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, IS_DELETED, CTD_BY, "
				+ "CTD_ON, UTD_BY, UTD_ON) "
				+ "VALUES (:languageId, :companyCodeId, :plantId, :warehouseId, :itemCode, :businessParnterType, :businessPartnerCode,"
				+ ":partnerItemNo, :mfrBarcode, :brandName, :statusId, :referenceField1, :referenceField2, :referenceField3,\r\n"
				+ ":deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE())");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step10() {
		return stepBuilderFactory.get("step10").<IMPartner, IMPartner>chunk(10).reader(imPartnerWhId111ItemReader())
				.writer(imPartnerWhId111ItemWriter()).build();
	}

	//InBound
	/*============================================PreInboundHeader===============================================================*/

	//Insert
	@Bean
	public FlatFileItemReader<PreInboundHeader> preInboundHeaderFlatFileItemReader() {
		FlatFileItemReader<PreInboundHeader> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getPreInboundHeaderFileName()));

		DefaultLineMapper<PreInboundHeader> preInboundHeaderDefaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"languageId", "companyCode", "plantId", "warehouseId", "preInboundNo", "refDocNumber", "inboundOrderTypeId",
				"referenceDocumentType", "statusId", "containerNo", "noOfContainers", "containerType", "refDocDate", "referenceField1",
				"referenceField2", "referenceField3", "referenceField4", "referenceField5", "referenceField6", "referenceField7", "referenceField8",
				"referenceField9", "referenceField10", "deletionIndicator", "createdBy", "dType", "companyDescription", "plantDescription", "warehouseDescription",
				"statusDescription"});
		preInboundHeaderDefaultLineMapper.setLineTokenizer(tokenizer);
		preInboundHeaderDefaultLineMapper.setFieldSetMapper(new PreInboundHeaderFieldSetMapper());
		preInboundHeaderDefaultLineMapper.afterPropertiesSet();
		reader.setLineMapper(preInboundHeaderDefaultLineMapper);
		return reader;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@Bean
	public JdbcBatchItemWriter<PreInboundHeader> preInboundHeaderJdbcBatchItemWriter() {
		JdbcBatchItemWriter<PreInboundHeader> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblpreinboundheader (LANG_ID, C_ID, PLANT_ID, WH_ID, PRE_IB_NO, REF_DOC_NO, IB_ORD_TYP_ID, REF_DOC_TYP, " +
				" STATUS_ID, CONT_NO, NO_CONTAINERS, CONT_TYP, REF_DOC_DATE, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, " +
				" REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON, " +
				" DTYPE, C_TEXT, PLANT_TEXT, WH_TEXT, STATUS_TEXT)" +
				" VALUES( :languageId, :companyCode, :plantId, :warehouseId, :preInboundNo, :refDocNumber, :inboundOrderTypeId, :referenceDocumentType," +
				" :statusId, :containerNo, :noOfContainers, :containerType, :refDocDate, :referenceField1, :referenceField2, :referenceField3, :referenceField4," +
				" :referenceField5, :referenceField6, :referenceField7, :referenceField8, :referenceField9, :referenceField10, :deletionIndicator, :createdBy, GETDATE()," +
				" :createdBy, GETDATE(), :dType, :companyDescription, :plantDescription, :warehouseDescription, :statusDescription)  ");

		itemWriter.setItemSqlParameterSourceProvider(
				new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step16() {
		return stepBuilderFactory.get("step16").<PreInboundHeader, PreInboundHeader>chunk(10).reader(preInboundHeaderFlatFileItemReader())
				.writer(preInboundHeaderJdbcBatchItemWriter()).build();
	}


	/*====================================================PreInboundLine========================================================================================*/
	//Insert

	@Bean
	FlatFileItemReader<com.tekclover.wms.core.batch.dto.PreInboundLine> preInboundLineFlatFileItemReader() {
		FlatFileItemReader<PreInboundLine> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getPreInboundLineFileName()));

		DefaultLineMapper<PreInboundLine> defaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"languageId", "companyCode", "plantId", "warehouseId", "preInboundNo", "refDocNumber", "lineNo", "itemCode",
				"inboundOrderTypeId", "variantCode", "variantSubCode", "statusId", "itemDescription", "containerNo", "invoiceNo", "businessPartnerCode", "partnerItemNo",
				"brandName", "manufacturerPartNo", "hsnCode", "expectedArrivalDate", "orderQty", "orderUom", "stockTypeId", "specialStockIndicatorId", "numberOfPallets",
				"numberOfCases", "itemPerPalletQty", "itemCaseQty", "referenceField1", "referenceField2", "referenceField3", "referenceField4", "referenceField5",
				"referenceField6", "referenceField7", "referenceField8", "referenceField9", "referenceField10", "referenceField11", "referenceField12", "referenceField13",
				"referenceField14", "referenceField15", "referenceField16", "referenceField17", "referenceField18", "referenceField19", "referenceField20",
				"deletionIndicator", "createdBy", "dType", "manufacturerCode", "manufacturerName", "origin", "companyDescription", "plantDescription", "warehouseDescription",
				"statusDescription"});

		defaultLineMapper.setLineTokenizer(tokenizer);
		defaultLineMapper.setFieldSetMapper(new PreInboundLineFieldSetMapper());
		defaultLineMapper.afterPropertiesSet();
		reader.setLineMapper(defaultLineMapper);
		return reader;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Bean
	public JdbcBatchItemWriter<PreInboundLine> preInboundLineJdbcBatchItemWriter() {
		JdbcBatchItemWriter<PreInboundLine> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblpreinboundline (LANG_ID, C_ID, PLANT_ID, WH_ID, PRE_IB_NO, REF_DOC_NO, IB_LINE_NO, ITM_CODE, IB_ORD_TYP_ID, VAR_ID, " +
				" VAR_SUB_ID, STATUS_ID, ITEM_TEXT, CONT_NO, INV_NO, PARTNER_CODE, PARTNER_ITM_CODE, BRND_NM, MFR_PART, HSN_CODE, EA_DATE, ORD_QTY, ORD_UOM, " +
				" STCK_TYP_ID, SP_ST_IND_ID, PAL_QTY, CASE_NO, ITM_PAL_QTY, ITM_CASE_QTY, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5," +
				" REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, REF_FIELD_11, REF_FIELD_12, REF_FIELD_13, REF_FIELD_14, REF_FIELD_15," +
				" REF_FIELD_16, REF_FIELD_17, REF_FIELD_18, REF_FIELD_19, REF_FIELD_20, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON, DTYPE, MFR_CODE, MFR_NAME, " +
				" ORIGIN, C_TEXT, PLANT_TEXT, WH_TEXT, STATUS_TEXT) " +
				" VALUES( :languageId, :companyCode, :plantId, :warehouseId, :preInboundNo, :refDocNumber, :lineNo, :itemCode, :inboundOrderTypeId, :variantCode, " +
				" :variantSubCode, :statusId, :itemDescription, :containerNo, :invoiceNo, :businessPartnerCode, :partnerItemNo, :brandName, :manufacturerPartNo, :hsnCode, :expectedArrivalDate, :orderQty, :orderUom, " +
				" :stockTypeId, :specialStockIndicatorId, :numberOfPallets, :numberOfCases, :itemPerPalletQty, :itemCaseQty, :referenceField1, :referenceField2, :referenceField3, :referenceField4, :referenceField5," +
				" :referenceField6, :referenceField7, :referenceField8, :referenceField9, :referenceField10, :referenceField11, :referenceField12, :referenceField13, :referenceField14, :referenceField15," +
				" :referenceField16, :referenceField17, :referenceField18, :referenceField19, :referenceField20, :deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE(), :dType, :manufacturerCode, " +
				" :manufacturerName, :origin, :companyDescription, :plantDescription, :warehouseDescription, :statusDescription )  ");

		itemWriter.setItemSqlParameterSourceProvider(
				new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step17() {
		return stepBuilderFactory.get("step17").<PreInboundLine, PreInboundLine>chunk(10).reader(preInboundLineFlatFileItemReader())
				.writer(preInboundLineJdbcBatchItemWriter()).build();
	}

	/*==================================================PreInboundHeader======================================================*/
	//Update

	@Bean
	public FlatFileItemReader<PreInboundHeader> preInboundHeaderFileItemReader() {
		FlatFileItemReader<PreInboundHeader> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getPreInboundHeaderPatchFileName()));

		DefaultLineMapper<PreInboundHeader> preInboundHeaderDefaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"languageId", "companyCode", "plantId", "warehouseId", "preInboundNo", "refDocNumber", "inboundOrderTypeId",
				"referenceDocumentType", "statusId", "containerNo", "noOfContainers", "containerType", "refDocDate", "referenceField1",
				"referenceField2", "referenceField3", "referenceField4", "referenceField5", "referenceField6", "referenceField7", "referenceField8",
				"referenceField9", "referenceField10", "deletionIndicator", "createdBy", "dType", "companyDescription", "plantDescription", "warehouseDescription",
				"statusDescription"});
		preInboundHeaderDefaultLineMapper.setLineTokenizer(tokenizer);
		preInboundHeaderDefaultLineMapper.setFieldSetMapper(new PreInboundHeaderFieldSetMapper());
		preInboundHeaderDefaultLineMapper.afterPropertiesSet();
		reader.setLineMapper(preInboundHeaderDefaultLineMapper);
		return reader;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@Bean
	public JdbcBatchItemWriter<PreInboundHeader> preInboundHeaderJdbcBatchWriter() {
		JdbcBatchItemWriter<PreInboundHeader> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("MERGE INTO tblpreinboundheader AS target "
				+ " USING (values( :languageId, :companyCode, :plantId, :warehouseId, :preInboundNo, :refDocNumber, :inboundOrderTypeId, :referenceDocumentType, "
				+ " :statusId, :containerNo, :noOfContainers, :containerType, :refDocDate, :referenceField1, :referenceField2, :referenceField3, :referenceField4, "
				+ " :referenceField5, :referenceField6, :referenceField7, :referenceField8, :referenceField9, :referenceField10, :deletionIndicator, :createdBy, GETDATE(), "
				+ " :createdBy, GETDATE(), :dType, :companyDescription, :plantDescription, :warehouseDescription, :statusDescription)) "
				+ " AS source (LANG_ID, C_ID, PLANT_ID, WH_ID, PRE_IB_NO, REF_DOC_NO, IB_ORD_TYP_ID, REF_DOC_TYP, "
				+ " STATUS_ID, CONT_NO, NO_CONTAINERS, CONT_TYP, REF_DOC_DATE, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, "
				+ " REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON, "
				+ " DTYPE, C_TEXT, PLANT_TEXT, WH_TEXT, STATUS_TEXT ) "
				+ " ON target.LANG_ID = source.LANG_ID "
				+ " AND target.C_ID = source.C_ID "
				+ " AND target.PLANT_ID = source.PLANT_ID "
				+ " AND target.WH_ID = source.WH_ID "
				+ " AND target.PRE_IB_NO = source.PRE_IB_NO "
				+ " AND target.REF_DOC_NO = source.REF_DOC_NO "
				+ " WHEN MATCHED THEN "
				+ " UPDATE SET "
				+ " IB_ORD_TYP_ID = source.IB_ORD_TYP_ID, "
				+ " REF_DOC_TYP = source.REF_DOC_TYP, "
				+ " STATUS_ID = source.STATUS_ID, "
				+ " CONT_NO = source.CONT_NO, "
				+ " NO_CONTAINERS = source.NO_CONTAINERS, "
				+ " CONT_TYP = source.CONT_TYP, "
				+ " REF_DOC_DATE = source.REF_DOC_DATE, "
				+ " REF_FIELD_1 = source.REF_FIELD_1, "
				+ " REF_FIELD_2 = source.REF_FIELD_2, "
				+ " REF_FIELD_3 = source.REF_FIELD_3, "
				+ " REF_FIELD_4 = source.REF_FIELD_4, "
				+ " REF_FIELD_5 = source.REF_FIELD_5, "
				+ " REF_FIELD_6 = source.REF_FIELD_6, "
				+ " REF_FIELD_7 = source.REF_FIELD_7, "
				+ " REF_FIELD_8 = source.REF_FIELD_8, "
				+ " REF_FIELD_9 = source.REF_FIELD_9, "
				+ " REF_FIELD_10 = source.REF_FIELD_10, "
				+ " IS_DELETED = source.IS_DELETED, "
				+ " CTD_BY = source.CTD_BY, "
				+ " CTD_ON = source.CTD_ON, "
				+ " UTD_BY = source.UTD_BY, "
				+ " UTD_ON = source.UTD_ON, "
				+ " DTYPE = source.DTYPE, "
				+ " C_TEXT = source.C_TEXT, "
				+ " PLANT_TEXT = source.PLANT_TEXT, "
				+ " WH_TEXT = source.WH_TEXT, "
				+ " STATUS_TEXT = source.STATUS_TEXT;");

		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step18() {
		return stepBuilderFactory.get("step18").<PreInboundHeader, PreInboundHeader>chunk(10).reader(preInboundHeaderFileItemReader())
				.writer(preInboundHeaderJdbcBatchWriter()).build();
	}

	/*====================================================PreInboundLine========================================================================================*/
	//Update

	@Bean
	FlatFileItemReader<PreInboundLine> preInboundLineFileItemReader() {
		FlatFileItemReader<PreInboundLine> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getPreInboundLinePatchFileName()));

		DefaultLineMapper<PreInboundLine> defaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"languageId", "companyCode", "plantId", "warehouseId", "preInboundNo", "refDocNumber", "lineNo", "itemCode",
				"inboundOrderTypeId", "variantCode", "variantSubCode", "statusId", "itemDescription", "containerNo", "invoiceNo", "businessPartnerCode", "partnerItemNo",
				"brandName", "manufacturerPartNo", "hsnCode", "expectedArrivalDate", "orderQty", "orderUom", "stockTypeId", "specialStockIndicatorId", "numberOfPallets",
				"numberOfCases", "itemPerPalletQty", "itemCaseQty", "referenceField1", "referenceField2", "referenceField3", "referenceField4", "referenceField5",
				"referenceField6", "referenceField7", "referenceField8", "referenceField9", "referenceField10", "referenceField11", "referenceField12", "referenceField13",
				"referenceField14", "referenceField15", "referenceField16", "referenceField17", "referenceField18", "referenceField19", "referenceField20",
				"deletionIndicator", "createdBy", "dType", "manufacturerCode", "manufacturerName", "origin", "companyDescription", "plantDescription", "warehouseDescription",
				"statusDescription"});

		defaultLineMapper.setLineTokenizer(tokenizer);
		defaultLineMapper.setFieldSetMapper(new PreInboundLineFieldSetMapper());
		defaultLineMapper.afterPropertiesSet();
		reader.setLineMapper(defaultLineMapper);
		return reader;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Bean
	public JdbcBatchItemWriter<PreInboundLine> preInboundLineJdbcBatchWriter() {
		JdbcBatchItemWriter<PreInboundLine> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("MERGE INTO tblpreinboundline AS target "
				+ " USING (values (:languageId, :companyCode, :plantId, :warehouseId, :preInboundNo, :refDocNumber, :lineNo, :itemCode, :inboundOrderTypeId, :variantCode, "
				+ " :variantSubCode, :statusId, :itemDescription, :containerNo, :invoiceNo, :businessPartnerCode, :partnerItemNo, :brandName, :manufacturerPartNo, :hsnCode, :expectedArrivalDate, :orderQty, :orderUom, "
				+ " :stockTypeId, :specialStockIndicatorId, :numberOfPallets, :numberOfCases, :itemPerPalletQty, :itemCaseQty, :referenceField1, :referenceField2, :referenceField3, :referenceField4, :referenceField5, "
				+ " :referenceField6, :referenceField7, :referenceField8, :referenceField9, :referenceField10, :referenceField11, :referenceField12, :referenceField13, :referenceField14, :referenceField15, "
				+ " :referenceField16, :referenceField17, :referenceField18, :referenceField19, :referenceField20, :deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE(), :dType, :manufacturerCode, "
				+ " :manufacturerName, :origin, :companyDescription, :plantDescription, :warehouseDescription, :statusDescription ))"
				+ " AS source (LANG_ID, C_ID, PLANT_ID, WH_ID, PRE_IB_NO, REF_DOC_NO, IB_LINE_NO, ITM_CODE, IB_ORD_TYP_ID, VAR_ID, "
				+ " VAR_SUB_ID, STATUS_ID, ITEM_TEXT, CONT_NO, INV_NO, PARTNER_CODE, PARTNER_ITM_CODE, BRND_NM, MFR_PART, HSN_CODE, EA_DATE, ORD_QTY, ORD_UOM, "
				+ " STCK_TYP_ID, SP_ST_IND_ID, PAL_QTY, CASE_NO, ITM_PAL_QTY, ITM_CASE_QTY, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5,"
				+ " REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, REF_FIELD_11, REF_FIELD_12, REF_FIELD_13, REF_FIELD_14, REF_FIELD_15, "
				+ " REF_FIELD_16, REF_FIELD_17, REF_FIELD_18, REF_FIELD_19, REF_FIELD_20, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON, DTYPE, MFR_CODE, MFR_NAME, "
				+ " ORIGIN, C_TEXT, PLANT_TEXT, WH_TEXT, STATUS_TEXT )"
				+ " ON target.LANG_ID = source.LANG_ID "
				+ " AND target.C_ID = source.C_ID "
				+ " AND target.PLANT_ID = source.PLANT_ID "
				+ " AND target.WH_ID = source.WH_ID "
				+ " AND target.PRE_IB_NO = source.PRE_IB_NO "
				+ " AND target.REF_DOC_NO = source.REF_DOC_NO "
				+ " AND target.IB_LINE_NO = source.IB_LINE_NO "
				+ " AND target.ITM_CODE = source.ITM_CODE "
				+ " WHEN MATCHED THEN "
				+ " UPDATE SET "
				+ " IB_ORD_TYP_ID = source.IB_ORD_TYP_ID, "
				+ " VAR_ID = source.VAR_ID, "
				+ " VAR_SUB_ID = source.VAR_SUB_ID, "
				+ " STATUS_ID = source.STATUS_ID, "
				+ " ITEM_TEXT = source.ITEM_TEXT, "
				+ " CONT_NO = source.CONT_NO, "
				+ " INV_NO = source.INV_NO, "
				+ " PARTNER_CODE = source.PARTNER_CODE, "
				+ " PARTNER_ITM_CODE = source.PARTNER_ITM_CODE, "
				+ " BRND_NM = source.BRND_NM, "
				+ " MFR_PART = source.MFR_PART, "
				+ " HSN_CODE = source.HSN_CODE, "
				+ " EA_DATE = source.EA_DATE, "
				+ " ORD_QTY = source.ORD_QTY, "
				+ " ORD_UOM = source.ORD_UOM, "
				+ " STCK_TYP_ID = source.STCK_TYP_ID, "
				+ " SP_ST_IND_ID = source.SP_ST_IND_ID, "
				+ " PAL_QTY = source.PAL_QTY, "
				+ " CASE_NO = source.CASE_NO, "
				+ " ITM_PAL_QTY = source.ITM_PAL_QTY, ITM_CASE_QTY = source.ITM_CASE_QTY, "
				+ " REF_FIELD_1 = source.REF_FIELD_1, REF_FIELD_2 = source.REF_FIELD_2, "
				+ " REF_FIELD_3 = source.REF_FIELD_3, REF_FIELD_4 = source.REF_FIELD_4, "
				+ " REF_FIELD_5 = source.REF_FIELD_5, REF_FIELD_6 = source.REF_FIELD_6, "
				+ " REF_FIELD_7 = source.REF_FIELD_7, REF_FIELD_8 = source.REF_FIELD_8, "
				+ " REF_FIELD_9 = source.REF_FIELD_9, REF_FIELD_10 = source.REF_FIELD_10, "
				+ " REF_FIELD_11 = source.REF_FIELD_11, REF_FIELD_12 = source.REF_FIELD_12, "
				+ " REF_FIELD_13 = source.REF_FIELD_13, REF_FIELD_14 = source.REF_FIELD_14, "
				+ " REF_FIELD_15 = source.REF_FIELD_15, REF_FIELD_16 = source.REF_FIELD_16, "
				+ " REF_FIELD_17 = source.REF_FIELD_17, REF_FIELD_18 = source.REF_FIELD_17, "
				+ " REF_FIELD_19 = source.REF_FIELD_19, REF_FIELD_20 = source.REF_FIELD_20, "
				+ " IS_DELETED = source.IS_DELETED, CTD_BY = source.CTD_BY, "
				+ " CTD_ON = source.CTD_ON, UTD_BY = source.UTD_BY, UTD_ON = source.UTD_ON, "
				+ " DTYPE = source.DTYPE, MFR_CODE = source.MFR_CODE, MFR_NAME = source.MFR_NAME, "
				+ " ORIGIN = source.ORIGIN, C_TEXT = source.C_TEXT, PLANT_TEXT = source.PLANT_TEXT, "
				+ " WH_TEXT = source.WH_TEXT, STATUS_TEXT = source.STATUS_TEXT;");

		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step19() {
		return stepBuilderFactory.get("step19").<PreInboundLine, PreInboundLine>chunk(10).reader(preInboundLineFileItemReader())
				.writer(preInboundLineJdbcBatchWriter()).build();
	}


	//OutBound
	/*============================================PreOutBoundHeader===============================================================*/

	//Insert
	@Bean
	public FlatFileItemReader<PreOutboundHeader> preOutboundHeaderFlatFileItemReader() {
		FlatFileItemReader<PreOutboundHeader> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getPreOutBoundHeaderFileName()));

		DefaultLineMapper<PreOutboundHeader> preInboundHeaderDefaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"languageId", "companyCodeId", "plantId", "warehouseId", "preInboundNo", "refDocNumber", "preOutboundNo",
				"partnerCode", "outboundOrderTypeId", "referenceDocumentType", "statusId", "refDocDate", "requiredDeliveryDate", "referenceField1",
				"referenceField2", "referenceField3", "referenceField4", "referenceField5", "referenceField6", "referenceField7", "referenceField8",
				"referenceField9", "referenceField10", "deletionIndicator", "remarks", "createdBy", "dType", "companyDescription", "plantDescription",
				"warehouseDescription", "statusDescription"});

		preInboundHeaderDefaultLineMapper.setLineTokenizer(tokenizer);
		preInboundHeaderDefaultLineMapper.setFieldSetMapper(new PreOutboundHeaderFieldSetMapper());
		preInboundHeaderDefaultLineMapper.afterPropertiesSet();
		reader.setLineMapper(preInboundHeaderDefaultLineMapper);
		return reader;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@Bean
	public JdbcBatchItemWriter<PreOutboundHeader> preOutboundHeaderJdbcBatchItemWriter() {
		JdbcBatchItemWriter<PreOutboundHeader> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblpreoutboundheader (LANG_ID, C_ID, PLANT_ID, WH_ID, REF_DOC_NO, PRE_OB_NO, "
				+ " PARTNER_CODE, OB_ORD_TYP_ID, REF_DOC_TYP, STATUS_ID, REF_DOC_DATE, REQ_DEL_DATE, REF_FIELD_1, "
				+ " REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, "
				+ " REF_FIELD_9, REF_FIELD_10, IS_DELETED, REMARK, PRE_OB_CTD_BY, PRE_OB_CTD_ON, PRE_OB_UTD_BY, PRE_OB_UTD_ON, "
				+ " DTYPE, C_TEXT, PLANT_TEXT, WH_TEXT, STATUS_TEXT)"
				+ " VALUES( :languageId, :companyCodeId, :plantId, :warehouseId, :refDocNumber, :preOutboundNo, :partnerCode, :outboundOrderTypeId, :referenceDocumentType,"
				+ " :statusId, :refDocDate, :requiredDeliveryDate, :referenceField1, :referenceField2, :referenceField3, :referenceField4,"
				+ " :referenceField5, :referenceField6, :referenceField7, :referenceField8, :referenceField9, :referenceField10, :deletionIndicator, :remarks, :createdBy, GETDATE(),"
				+ " :createdBy, GETDATE(), :dType, :companyDescription, :plantDescription, :warehouseDescription, :statusDescription)  ");

		itemWriter.setItemSqlParameterSourceProvider(
				new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step20() {
		return stepBuilderFactory.get("step20").<PreOutboundHeader, PreOutboundHeader>chunk(10).reader(preOutboundHeaderFlatFileItemReader())
				.writer(preOutboundHeaderJdbcBatchItemWriter()).build();
	}


	/*====================================================PreOutboundLine========================================================================================*/
	//Insert

	@Bean
	FlatFileItemReader<PreOutboundLine> preOutboundLineFlatFileItemReader() {
		FlatFileItemReader<PreOutboundLine> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getPreOutboundLineFileName()));

		DefaultLineMapper<PreOutboundLine> defaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"languageId", "companyCodeId", "plantId", "warehouseId", "refDocNumber", "preOutboundNo", "partnerCode", "lineNumber", "itemCode",
				"outboundOrderTypeId", "variantCode", "variantSubCode", "statusId", "stockTypeId", "specialStockIndicatorId", "description", "manufacturerPartNo",
				"hsnCode", "itemBarcode", "orderQty", "orderUom", "requiredDeliveryDate", "referenceField1", "referenceField2", "referenceField3", "referenceField4",
				"referenceField5", "referenceField6", "referenceField7", "referenceField8", "referenceField9", "referenceField10", "deletionIndicator", "createdBy",
				"dType", "manufacturerCode", "manufacturerName", "origin", "brand", "companyDescription", "plantDescription", "warehouseDescription", "statusDescription"});

		defaultLineMapper.setLineTokenizer(tokenizer);
		defaultLineMapper.setFieldSetMapper(new PreOutboundLineFieldSetMapper());
		defaultLineMapper.afterPropertiesSet();
		reader.setLineMapper(defaultLineMapper);
		return reader;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Bean
	public JdbcBatchItemWriter<PreOutboundLine> preOutboundLineJdbcBatchItemWriter() {
		JdbcBatchItemWriter<PreOutboundLine> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO tblpreoutboundline (LANG_ID, C_ID, PLANT_ID, WH_ID, REF_DOC_NO, PRE_OB_NO, PARTNER_CODE, OB_LINE_NO, ITM_CODE, "
				+ " OB_ORD_TYP_ID, VAR_ID, VAR_SUB_ID, STATUS_ID, STCK_TYP_ID, SP_ST_IND_ID, TEXT, MFR_PART, HSN_CODE, ITM_BARCODE, ORD_QTY, ORD_UOM, REQ_DEL_DATE, "
				+ " REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, IS_DELETED, "
				+ " PRE_OB_CTD_BY, PRE_OB_CTD_ON, PRE_OB_UTD_BY, PRE_OB_UTD_ON, DTYPE,  MFR_CODE, MFR_NAME, ORIGIN, BRAND, C_TEXT, PLANT_TEXT, WH_TEXT, STATUS_TEXT) "
				+ " VALUES( :languageId, :companyCodeId, :plantId, :warehouseId, :refDocNumber, :preOutboundNo, :partnerCode, :lineNumber, :itemCode, :outboundOrderTypeId, "
				+ " :variantCode, :variantSubCode, :statusId, :stockTypeId, :specialStockIndicatorId, :description, :manufacturerPartNo, :hsnCode, :itemBarcode, :orderQty, "
				+ " :orderUom, :requiredDeliveryDate, :referenceField1, :referenceField2, :referenceField3, :referenceField4, :referenceField5, :referenceField6, "
				+ " :referenceField7, :referenceField8, :referenceField9, :referenceField10, :deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE(), :dType, "
				+ " :manufacturerCode, :manufacturerName, :origin, :brand, :companyDescription, :plantDescription, :warehouseDescription, :statusDescription)");

		itemWriter.setItemSqlParameterSourceProvider(
				new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step21() {
		return stepBuilderFactory.get("step21").<PreOutboundLine, PreOutboundLine>chunk(10).reader(preOutboundLineFlatFileItemReader())
				.writer(preOutboundLineJdbcBatchItemWriter()).build();
	}


	//OutBound
	/*============================================PreOutBoundHeader===============================================================*/

	//Update
	@Bean
	public FlatFileItemReader<PreOutboundHeader> preOutboundHeaderFileItemReader() {
		FlatFileItemReader<PreOutboundHeader> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getPreOutboundHeaderPatchFileName()));

		DefaultLineMapper<PreOutboundHeader> preInboundHeaderDefaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"languageId", "companyCodeId", "plantId", "warehouseId", "refDocNumber", "preOutboundNo",
				"partnerCode", "outboundOrderTypeId", "referenceDocumentType", "statusId", "refDocDate", "requiredDeliveryDate", "referenceField1",
				"referenceField2", "referenceField3", "referenceField4", "referenceField5", "referenceField6", "referenceField7", "referenceField8",
				"referenceField9", "referenceField10", "deletionIndicator", "remarks", "createdBy", "dType", "companyDescription", "plantDescription",
				"warehouseDescription", "statusDescription"});

		preInboundHeaderDefaultLineMapper.setLineTokenizer(tokenizer);
		preInboundHeaderDefaultLineMapper.setFieldSetMapper(new PreOutboundHeaderFieldSetMapper());
		preInboundHeaderDefaultLineMapper.afterPropertiesSet();
		reader.setLineMapper(preInboundHeaderDefaultLineMapper);
		return reader;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@Bean
	public JdbcBatchItemWriter<PreOutboundHeader> preOutboundHeaderJdbctemWriter() {
		JdbcBatchItemWriter<PreOutboundHeader> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("MERGE tblpreoutboundheader AS target "
				+ " USING (values(:languageId, :companyCodeId, :plantId, :warehouseId, :refDocNumber, :preOutboundNo, :partnerCode, :outboundOrderTypeId, :referenceDocumentType, "
				+ " :statusId, :refDocDate, :requiredDeliveryDate, :referenceField1, :referenceField2, :referenceField3, :referenceField4, "
				+ " :referenceField5, :referenceField6, :referenceField7, :referenceField8, :referenceField9, :referenceField10, :deletionIndicator, :remarks, :createdBy, GETDATE(), "
				+ " :createdBy, GETDATE(), :dType, :companyDescription, :plantDescription, :warehouseDescription, :statusDescription ))"
				+ " AS source (LANG_ID, C_ID, PLANT_ID, WH_ID, REF_DOC_NO, PRE_OB_NO, "
				+ " PARTNER_CODE, OB_ORD_TYP_ID, REF_DOC_TYP, STATUS_ID, REF_DOC_DATE, REQ_DEL_DATE, REF_FIELD_1, "
				+ " REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, "
				+ " REF_FIELD_9, REF_FIELD_10, IS_DELETED, REMARK, PRE_OB_CTD_BY, PRE_OB_CTD_ON, PRE_OB_UTD_BY, PRE_OB_UTD_ON, "
				+ " DTYPE, C_TEXT, PLANT_TEXT, WH_TEXT, STATUS_TEXT )"
				+ " ON target.LANG_ID = source.LANG_ID "
				+ " AND target.C_ID = source.C_ID "
				+ " AND target.PLANT_ID = source.PLANT_ID "
				+ " AND target.WH_ID = source.WH_ID "
				+ " AND target.REF_DOC_NO = source.REF_DOC_NO "
				+ " AND target.PRE_OB_NO = source.PRE_OB_NO "
				+ " AND target.PARTNER_CODE = source.PARTNER_CODE "
				+ " WHEN MATCHED THEN "
				+ " UPDATE SET "
				+ " OB_ORD_TYP_ID = source.OB_ORD_TYP_ID, "
				+ " REF_DOC_TYP = source.REF_DOC_TYP, "
				+ " STATUS_ID = source.STATUS_ID, "
				+ " REF_DOC_DATE = source.REF_DOC_DATE, "
				+ " REQ_DEL_DATE = source.REQ_DEL_DATE, "
				+ " REF_FIELD_1 = source.REF_FIELD_1, "
				+ " REF_FIELD_2 = source.REF_FIELD_2, "
				+ " REF_FIELD_3 = source.REF_FIELD_3, "
				+ " REF_FIELD_4 = source.REF_FIELD_4, "
				+ " REF_FIELD_5 = source.REF_FIELD_5, "
				+ " REF_FIELD_6 = source.REF_FIELD_6, "
				+ " REF_FIELD_7 = source.REF_FIELD_7, "
				+ " REF_FIELD_8 = source.REF_FIELD_8, "
				+ " REF_FIELD_9 = source.REF_FIELD_9, "
				+ " REF_FIELD_10 = source.REF_FIELD_10, "
				+ " IS_DELETED = source.IS_DELETED, "
				+ " REMARK = source.REMARK, "
				+ " PRE_OB_CTD_BY = source.PRE_OB_CTD_BY, "
				+ " PRE_OB_CTD_ON = source.PRE_OB_CTD_ON, "
				+ " PRE_OB_UTD_BY = source.PRE_OB_UTD_BY, "
				+ " PRE_OB_UTD_ON = source.PRE_OB_UTD_ON, "
				+ " DTYPE = source.DTYPE, "
				+ " C_TEXT = source.C_TEXT, "
				+ " PLANT_TEXT = source.PLANT_TEXT, "
				+ " WH_TEXT = source.WH_TEXT, "
				+ " STATUS_TEXT = source.STATUS_TEXT; ");

		itemWriter.setItemSqlParameterSourceProvider(
				new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step22() {
		return stepBuilderFactory.get("step22").<PreOutboundHeader, PreOutboundHeader>chunk(10).reader(preOutboundHeaderFileItemReader())
				.writer(preOutboundHeaderJdbctemWriter()).build();
	}

	/*====================================================PreOutboundLine========================================================================================*/
	//Update

	@Bean
	FlatFileItemReader<PreOutboundLine> preOutboundLineFileItemReader() {
		FlatFileItemReader<PreOutboundLine> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getPreOutboundLinePatchFileName()));

		DefaultLineMapper<PreOutboundLine> defaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"languageId", "companyCodeId", "plantId", "warehouseId", "refDocNumber", "preOutboundNo", "partnerCode", "lineNumber", "itemCode",
				"outboundOrderTypeId", "variantCode", "variantSubCode", "statusId", "stockTypeId", "specialStockIndicatorId", "description", "manufacturerPartNo",
				"hsnCode", "itemBarcode", "orderQty", "orderUom", "requiredDeliveryDate", "referenceField1", "referenceField2", "referenceField3", "referenceField4",
				"referenceField5", "referenceField6", "referenceField7", "referenceField8", "referenceField9", "referenceField10", "deletionIndicator", "createdBy",
				"dType", "manufacturerCode", "manufacturerName", "origin", "brand", "companyDescription", "plantDescription", "warehouseDescription", "statusDescription"});

		defaultLineMapper.setLineTokenizer(tokenizer);
		defaultLineMapper.setFieldSetMapper(new PreOutboundLineFieldSetMapper());
		defaultLineMapper.afterPropertiesSet();
		reader.setLineMapper(defaultLineMapper);
		return reader;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Bean
	public JdbcBatchItemWriter<PreOutboundLine> preOutboundLineBatchItemWriter() {
		JdbcBatchItemWriter<PreOutboundLine> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("MERGE tblpreoutboundline AS target "
				+ " USING (values( :languageId, :companyCodeId, :plantId, :warehouseId, :refDocNumber, :preOutboundNo, :partnerCode, :lineNumber, :itemCode, :outboundOrderTypeId, "
				+ " :variantCode, :variantSubCode, :statusId, :stockTypeId, :specialStockIndicatorId, :description, :manufacturerPartNo, :hsnCode, :itemBarcode, :orderQty, "
				+ " :orderUom, :requiredDeliveryDate, :referenceField1, :referenceField2, :referenceField3, :referenceField4, :referenceField5, :referenceField6, "
				+ " :referenceField7, :referenceField8, :referenceField9, :referenceField10, :deletionIndicator, :createdBy, GETDATE(), :createdBy, GETDATE(), :dType, "
				+ " :manufacturerCode, :manufacturerName, :origin, :brand, :companyDescription, :plantDescription, :warehouseDescription, :statusDescription)) "
				+ " AS source (LANG_ID, C_ID, PLANT_ID, WH_ID, REF_DOC_NO, PRE_OB_NO, PARTNER_CODE, OB_LINE_NO, ITM_CODE, "
				+ " OB_ORD_TYP_ID, VAR_ID, VAR_SUB_ID, STATUS_ID, STCK_TYP_ID, SP_ST_IND_ID, TEXT, MFR_PART, HSN_CODE, ITM_BARCODE, ORD_QTY, ORD_UOM, REQ_DEL_DATE, "
				+ " REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, IS_DELETED, "
				+ " PRE_OB_CTD_BY, PRE_OB_CTD_ON, PRE_OB_UTD_BY, PRE_OB_UTD_ON, DTYPE, MFR_CODE, MFR_NAME, ORIGIN, BRAND, C_TEXT, PLANT_TEXT, WH_TEXT, STATUS_TEXT) "
				+ " ON target.LANG_ID = source.LANG_ID "
				+ " AND target.C_ID = source.C_ID "
				+ " AND target.PLANT_ID = source.PLANT_ID "
				+ " AND target.WH_ID = source.WH_ID "
				+ " AND target.REF_DOC_NO = source.REF_DOC_NO "
				+ " AND target.PRE_OB_NO = source.PRE_OB_NO "
				+ " AND target.PARTNER_CODE = source.PARTNER_CODE "
				+ " AND target.OB_LINE_NO = source.OB_LINE_NO "
				+ " AND target.ITM_CODE = source.ITM_CODE "
				+ " WHEN MATCHED THEN "
				+ " UPDATE SET "
				+ " OB_ORD_TYP_ID = source.OB_ORD_TYP_ID, "
				+ " VAR_ID = source.VAR_ID, "
				+ " VAR_SUB_ID = source.VAR_SUB_ID, "
				+ " STATUS_ID = source.STATUS_ID, "
				+ " STCK_TYP_ID = source.STCK_TYP_ID, "
				+ " SP_ST_IND_ID = source.SP_ST_IND_ID, "
				+ " TEXT = source.TEXT, "
				+ " MFR_PART = source.MFR_PART, "
				+ " HSN_CODE = source.HSN_CODE, "
				+ " ITM_BARCODE = source.ITM_BARCODE, "
				+ " ORD_QTY = source.ORD_QTY, "
				+ " ORD_UOM = source.ORD_UOM, "
				+ " REQ_DEL_DATE = source.REQ_DEL_DATE, "
				+ " REF_FIELD_1 = source.REF_FIELD_1, "
				+ " REF_FIELD_2 = source.REF_FIELD_2, "
				+ " REF_FIELD_3 = source.REF_FIELD_3, "
				+ " REF_FIELD_4 = source.REF_FIELD_4, "
				+ " REF_FIELD_5 = source.REF_FIELD_5, "
				+ " REF_FIELD_6 = source.REF_FIELD_6, "
				+ " REF_FIELD_7 = source.REF_FIELD_7, "
				+ " REF_FIELD_8 = source.REF_FIELD_8, "
				+ " REF_FIELD_9 = source.REF_FIELD_9, "
				+ " REF_FIELD_10 = source.REF_FIELD_10, "
				+ " IS_DELETED = source.IS_DELETED, "
				+ " PRE_OB_CTD_BY = source.PRE_OB_CTD_BY, "
				+ " PRE_OB_CTD_ON = source.PRE_OB_CTD_ON, "
				+ " PRE_OB_UTD_BY = source.PRE_OB_UTD_BY, "
				+ " PRE_OB_UTD_ON = source.PRE_OB_UTD_ON, "
				+ " DTYPE = source.DTYPE, "
				+ " MFR_CODE = source.MFR_CODE, "
				+ " MFR_NAME = source.MFR_NAME, "
				+ " ORIGIN = source.ORIGIN, "
				+ " BRAND = source.BRAND, "
				+ " C_TEXT = source.C_TEXT, "
				+ " PLANT_TEXT = source.PLANT_TEXT, "
				+ " WH_TEXT = source.WH_TEXT, "
				+ " STATUS_TEXT = source.STATUS_TEXT;");


		itemWriter.setItemSqlParameterSourceProvider(
				new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

	@Bean
	public Step step23() {
		return stepBuilderFactory.get("step23").<PreOutboundLine, PreOutboundLine>chunk(10).reader(preOutboundLineFileItemReader())
				.writer(preOutboundLineBatchItemWriter()).build();
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
	public Job jobImBasicData1() throws Exception{
		return jobBuilderFactory.get("jobImBasicData1")
				.listener(wmsListener())
				.start(step7())
				.build();
	}
	
	@Bean
	public Job jobImBasicData1WhId111() throws Exception{
		return jobBuilderFactory.get("jobImBasicData1WhId111")
				.listener(wmsListener())
				.start(step8())
				.build();
	}
	
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


	@Bean
	public Job jobPreInboundHeader() throws Exception {
		return jobBuilderFactory.get("jobPreInboundHeader")
				.listener(wmsListener())
				.start(step16())
				.build();
	}

	@Bean
	public Job jobPreInboundLine() throws Exception {
		return jobBuilderFactory.get("jobPreInboundLine")
				.listener(wmsListener())
				.start(step17())
				.build();
	}

	@Bean
	public Job jobPreInboundHeaderPatch() throws Exception {
		return jobBuilderFactory.get("jobPreInboundHeaderPatch")
				.listener(wmsListener())
				.start(step18())
				.build();
	}

	@Bean
	public Job jobPreInboundLinePatch() throws Exception {
		return jobBuilderFactory.get("jobPreInboundLinePatch")
				.listener(wmsListener())
				.start(step19())
				.build();
	}

	@Bean
	public Job jobPreOutboundHeader() throws Exception {
		return jobBuilderFactory.get("jobPreOutboundHeader")
				.listener(wmsListener())
				.start(step20())
				.build();
	}

	@Bean
	public Job jobPreOutboundLine() throws Exception {
		return jobBuilderFactory.get("jobPreOutboundLine")
				.listener(wmsListener())
				.start(step21())
				.build();
	}

	@Bean
	public Job jobPreOutboundHeaderPatch() throws Exception {
		return jobBuilderFactory.get("jobPreOutboundHeaderPatch")
				.listener(wmsListener())
				.start(step22())
				.build();
	}

	@Bean
	public Job jobPreOutboundLinePatch() throws Exception {
		return jobBuilderFactory.get("jobPreOutboundLinePatch")
				.listener(wmsListener())
				.start(step23())
				.build();
	}

	/*-----------------------------------------------------------------------------------------*/

//	@Bean
//	public FlatFileItemReader<Person> personItemReader() {
//		FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
//		reader.setLinesToSkip(1);
//		reader.setResource(new ClassPathResource("/Book1.csv"));
//		DefaultLineMapper<Person> customerLineMapper = new DefaultLineMapper<>();
//
//		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//		tokenizer.setNames(new String[] { "id", "firstName", "lastName" });
//
//		customerLineMapper.setLineTokenizer(tokenizer);
//		customerLineMapper.setFieldSetMapper(new PersonFieldSetMapper());
//		customerLineMapper.afterPropertiesSet();
//		reader.setLineMapper(customerLineMapper);
//		return reader;
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Bean
//	public JdbcBatchItemWriter<Person> personItemWriter() {
//		JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();
//
//		itemWriter.setDataSource(this.dataSource);
//		itemWriter.setSql("INSERT INTO PERSON VALUES (:id, :firstName, :lastName)");
//		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
//		itemWriter.afterPropertiesSet();
//		return itemWriter;
//	}
//
//	@Bean
//	public Step step1() {
//		return stepBuilderFactory.get("step1").<Person, Person>chunk(10).reader(personItemReader())
//				.writer(personItemWriter()).build();
//	}
//	
//	//------------------------------Student-------------------------------------------
//	@Bean
//	public FlatFileItemReader<Student> studentItemReader() {
//		FlatFileItemReader<Student> reader = new FlatFileItemReader<>();
//		reader.setLinesToSkip(1);
//		reader.setResource(new ClassPathResource("/Student.csv"));
//
//		DefaultLineMapper<Student> customerLineMapper = new DefaultLineMapper<>();
//		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//		tokenizer.setNames(new String[] { "id", "firstName", "lastName" });
//
//		customerLineMapper.setLineTokenizer(tokenizer);
//		customerLineMapper.setFieldSetMapper(new StudentFieldSetMapper());
//		customerLineMapper.afterPropertiesSet();
//		reader.setLineMapper(customerLineMapper);
//		return reader;
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Bean
//	public JdbcBatchItemWriter<Student> studentItemWriter() {
//		JdbcBatchItemWriter<Student> itemWriter = new JdbcBatchItemWriter<>();
//
//		itemWriter.setDataSource(this.dataSource);
//		itemWriter.setSql("INSERT INTO Student VALUES (:id, :firstName, :lastName)");
//		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
//		itemWriter.afterPropertiesSet();
//		return itemWriter;
//	}
//
//	@Bean
//	public Step step2() {
//		return stepBuilderFactory.get("step2").<Student, Student>chunk(10).reader(studentItemReader())
//				.writer(studentItemWriter()).build();
//	}
	
	/*----------------------------------------------------------------------------------------*/
	
//	@Bean
//	public Job job1() {
//		return jobBuilderFactory.get("job1")
////				.listener(wmsListener())
//				.start(step1())
//				.next(step2())
////				.next(step3())
//				.build();
//	}
}