package com.mnrclara.wrapper.core.batch.config;

import javax.sql.DataSource;

import com.mnrclara.wrapper.core.batch.dto.*;
import com.mnrclara.wrapper.core.batch.mapper.*;
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
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.wrapper.core.config.PropertiesConfig;

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

    /*-------------------------ClientGeneral----------------------------------------------------------------*/
    @Bean
    public FlatFileItemReader<ClientGeneral> clientGeneralItemReader() {
        FlatFileItemReader<ClientGeneral> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getClientGeneralFile()));

        DefaultLineMapper<ClientGeneral> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"languageId", "classId", "clientId", "clientCategoryId", "transactionId", "potentialClientId",
                "inquiryNumber", "intakeFormId", "intakeFormNumber", "firstName", "lastName", "firstNameLastName", "lastNameFirstName",
                "corporationClientId", "referralId", "emailId", "contactNumber", "addressLine1", "addressLine2", "addressLine3", "city",
                "state", "country", "zipCode", "consultationDate", "socialSecurityNo", "mailingAddress", "occupation", "statusId",
                "suiteDoorNo", "workNo", "homeNo", "fax", "alternateEmailId", "isMailingAddressSame", "deletionIndicator",
                "createdBy", "createdOn", "UpdatedBy", "updatedOn", "referenceField21", "referenceField22",
                "referenceField23", "referenceField25", "referenceField26", "referenceField27", "referenceField28"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new ClientGeneralFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public JdbcBatchItemWriter<ClientGeneral> clientGeneralItemWriter() {
        JdbcBatchItemWriter<ClientGeneral> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblclientgeneralid (LANG_ID, CLASS_ID, CLIENT_ID, CLIENT_CAT_ID, TRANS_ID, POT_CLIENT_ID, \r\n"
                + "INQ_NO, IT_FORM_ID, IT_FORM_NO, FIRST_NM, LAST_NM, FIRST_LAST_NM, LAST_FIRST_NM, CORP_CLIENT_ID, REFERRAL_ID, \r\n"
                + "EMAIL_ID, CONT_NO, ADDRESS_LINE1, ADDRESS_LINE2, ADDRESS_LINE3, CITY, STATE, COUNTRY, ZIP_CODE, \r\n"
                + "CONSULT_DATE, SSN_ID, MAIL_ADDRESS, OCCUPATION, STATUS_ID, SUITE_NO, WORK, HOME, FAX, ALT_EMAIL_ID, \r\n"
                + "IS_MAIL_SAME, Is_deleted, CTD_BY, CTD_ON, UTD_BY, UTD_ON, REF_FIELD_21, \r\n"
                + "REF_FIELD_22, REF_FIELD_23, REF_FIELD_25, REF_FIELD_26, REF_FIELD_27, REF_FIELD_28)\r\n"
                + "VALUES (:languageId, :classId, :clientId, :clientCategoryId, :transactionId, :potentialClientId, \r\n"
                + ":inquiryNumber, :intakeFormId, :intakeFormNumber, :firstName, :lastName, :firstNameLastName, \r\n "
                + ":lastNameFirstName, :corporationClientId, :referralId, :emailId, :contactNumber, :addressLine1,\r\n "
                + ":addressLine2, :addressLine3, :city, :state, :country, :zipCode, :consultationDate, :socialSecurityNo,\r\n "
                + ":mailingAddress, :occupation, :statusId, :suiteDoorNo, :workNo, :homeNo, :fax, :alternateEmailId,\r\n "
                + ":isMailingAddressSame, :deletionIndicator, :createdBy, CAST(:createdOn AS DATETIME), :updatedBy, CAST(:updatedOn AS DATETIME), :referenceField21,\r\n"
                + ":referenceField22, :referenceField23, \r\n "
                + ":referenceField25, :referenceField26, :referenceField27, :referenceField28)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<ClientGeneral, ClientGeneral>chunk(10).reader(clientGeneralItemReader())
                .writer(clientGeneralItemWriter()).build();
    }

    /*-------------------------ClientNote----------------------------------------------------------------*/
    @Bean
    public FlatFileItemReader<ClientNote> clientNoteItemReader() {
        FlatFileItemReader<ClientNote> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getClientNoteFile()));

        DefaultLineMapper<ClientNote> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"notesNumber", "languageId", "classId", "clientId", "noteText", "matterNumber", "noteTypeId",
                "statusId", "deletionIndicator", "referenceField1", "referenceField2", "referenceField3", "referenceField4",
                "referenceField5", "referenceField6", "referenceField7", "referenceField8", "referenceField9", "referenceField10",
                "createdBy", "createdOn", "updatedBy", "updatedOn", "additionalFields1", "additionalFields2", "additionalFields3",
                "additionalFields4", "additionalFields5", "additionalFields6", "additionalFields7"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new ClientNoteFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public JdbcBatchItemWriter<ClientNote> clientNoteItemWriter() {
        JdbcBatchItemWriter<ClientNote> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblclientnote (NOTE_NO, LANG_ID, CLASS_ID, CLIENT_ID, NOTE_TEXT, MATTER_NO, \r\n"
                + "NOTE_TYP_ID, STATUS_ID, IS_DELETED, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5, \r\n"
                + "REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, CTD_BY, CTD_ON, UTD_BY, UTD_ON, \r\n"
                + "ADD_FIELDS_1, ADD_FIELDS_2, ADD_FIELDS_3, ADD_FIELDS_4, ADD_FIELDS_5, ADD_FIELDS_6, ADD_FIELDS_7) \r\n"
                + "VALUES (:notesNumber, :languageId, :classId, :clientId, :noteText, :matterNumber, :noteTypeId, \r\n"
                + ":statusId, :deletionIndicator, :referenceField1, :referenceField2, :referenceField3, :referenceField4, \r\n "
                + ":referenceField5, :referenceField6, :referenceField7, :referenceField8, :referenceField9, :referenceField10, \r\n "
                + ":createdBy, CURDATE(), :updatedBy, :CURDATE(), \r\n "
                + ":additionalFields1, :additionalFields2, :additionalFields3, :additionalFields4, \r\n "
                + ":additionalFields5, :additionalFields6, :additionalFields7)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2").<ClientNote, ClientNote>chunk(10).reader(clientNoteItemReader())
                .writer(clientNoteItemWriter()).build();
    }

    /*-------------------------MatterAssignment----------------------------------------------------------------*/
    @Bean
    public FlatFileItemReader<MatterAssignment> matterAssignmentItemReader() {
        FlatFileItemReader<MatterAssignment> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getMatterAssignmentFile()));
        DefaultLineMapper<MatterAssignment> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"languageId", "classId", "matterNumber", "caseInformationNo", "clientId",
                "caseOpenedDate", "partner", "originatingTimeKeeper", "responsibleTimeKeeper", "assignedTimeKeeper",
                "legalAssistant", "statusId", "deletionIndicator", "createdBy", "createdOn"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new MatterAssignmentFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public JdbcBatchItemWriter<MatterAssignment> matterAssignmentItemWriter() {
        JdbcBatchItemWriter<MatterAssignment> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblmatterassignmentid (LANG_ID, CLASS_ID, MATTER_NO, CASEINFO_NO, CLIENT_ID, \r\n"
                + "CASE_OPEN_DATE, PARTNER, ORIGINATING_TK, RESPONSIBLE_TK, ASSIGNED_TK, LEGAL_ASSIST, \r\n"
                + "STATUS_ID, IS_DELETED,CTD_BY, CTD_ON) \r\n"
                + "VALUES (:languageId, :classId, :matterNumber, :caseInformationNo, :clientId,\r\n"
                + " :caseOpenedDate, :partner, :originatingTimeKeeper, :responsibleTimeKeeper, :assignedTimeKeeper, :legalAssistant,\r\n"
                + " :statusId, :deletionIndicator, :createdBy, CAST(:createdOn AS DATETIME))");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3").<MatterAssignment, MatterAssignment>chunk(10).reader(matterAssignmentItemReader())
                .writer(matterAssignmentItemWriter()).build();
    }

    /*-------------------------MatterExpense----------------------------------------------------------------*/
    @Bean
    public FlatFileItemReader<MatterExpense> matterExpenseItemReader() {
        FlatFileItemReader<MatterExpense> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getMatterExpenseFile()));

        DefaultLineMapper<MatterExpense> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"languageId", "classId", "matterExpenseId", "clientId", "matterNumber", "expenseCode", "expenseAmount",
                "expenseDescription", "expenseType", "billType", "statusId", "deletionIndicator",
                "createdBy", "createdOn", "referenceField2"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new MatterExpenseFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public JdbcBatchItemWriter<MatterExpense> matterExpenseItemWriter() {
        JdbcBatchItemWriter<MatterExpense> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblmatterexpenseid (MATTER_EXP_ID, LANG_ID, CLASS_ID, CLIENT_ID, MATTER_NO, EXP_CODE, \r\n"
                + "EXP_AMOUNT, EXP_TEXT, EXP_TYPE, BILL_TYPE, STATUS_ID, IS_DELETED, CTD_BY, CTD_ON, REF_FIELD_2) \r\n"
                + "VALUES (:matterExpenseId, :languageId, :classId, :clientId, :matterNumber, :expenseCode, :expenseAmount, \r\n"
                + ":expenseDescription, :expenseType, :billType, :statusId, :deletionIndicator, \r\n "
                + ":createdBy, CAST(:createdOn AS DATETIME), :referenceField2)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4").<MatterExpense, MatterExpense>chunk(10).reader(matterExpenseItemReader())
                .writer(matterExpenseItemWriter()).build();
    }

    /*-------------------------MatterGenAcc----------------------------------------------------------------*/
    @Bean
    public FlatFileItemReader<MatterGenAcc> matterGenAccItemReader() {
        FlatFileItemReader<MatterGenAcc> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getMatterGenAccFile()));
        DefaultLineMapper<MatterGenAcc> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"languageId", "classId", "matterNumber", "clientId", "caseInformationNo", "transactionId",
                "caseCategoryId", "caseSubCategoryId", "billingModeId", "billingFormatId", "billingFrequencyId", "billingRemarks",
                "matterDescription", "caseOpenedDate", "arAccountNumber", "trustDepositNo", "flatFeeAmount", "administrativeCost",
                "statusId", "deletionIndicator", "createdBy", "createdOn", "UpdatedBy", "updatedOn", "referenceField25"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new MatterGenAccFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public JdbcBatchItemWriter<MatterGenAcc> matterGenAccItemWriter() {
        JdbcBatchItemWriter<MatterGenAcc> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblmattergenaccid (LANG_ID, CLASS_ID, MATTER_NO, CLIENT_ID, CASEINFO_NO, TRANS_ID, \r\n"
                + "CASE_CATEGORY_ID, CASE_SUB_CATEGORY_ID, BILL_MODE_ID, BILL_FORMAT_ID, BILL_FREQ_ID, BILL_REMARK, \r\n"
                + "MATTER_TEXT, CASE_OPEN_DATE, AR_ACCOUNT_NO, TRUST_DEPOSIT_NO, FLAT_FEE, ADMIN_COST, \r\n"
                + "STATUS_ID, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON,REF_FIELD_25) \r\n"
                + "VALUES (:languageId, :classId, :matterNumber, :clientId, :caseInformationNo, :transactionId, \r\n"
                + ":caseCategoryId, :caseSubCategoryId, :billingModeId,  :billingFormatId, :billingFrequencyId, :billingRemarks, \r\n"
                + ":matterDescription, CAST(:caseOpenedDate AS DATETIME), :arAccountNumber, :trustDepositNo, :flatFeeAmount, :administrativeCost, \r\n"
                + ":statusId, :deletionIndicator,  :createdBy, CAST(:createdOn AS DATETIME), :updatedBy, CURDATE(), :referenceField25)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step5() {
        return stepBuilderFactory.get("step5").<MatterGenAcc, MatterGenAcc>chunk(10).reader(matterGenAccItemReader())
                .writer(matterGenAccItemWriter()).build();
    }

    /*-------------------------MatterNote----------------------------------------------------------------*/
    @Bean
    public FlatFileItemReader<MatterNote> matterNoteItemReader() {
        FlatFileItemReader<MatterNote> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getMatterNoteFile()));

        DefaultLineMapper<MatterNote> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"languageId", "classId", "clientId", "matterNumber", "notesNumber", "notesDescription",
                "statusId", "noteTypeId", "deletionIndicator", "createdBy", "createdOn", "UpdatedBy", "updatedOn"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new MatterNoteFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public JdbcBatchItemWriter<MatterNote> matterNoteItemWriter() {
        JdbcBatchItemWriter<MatterNote> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblmatternoteid (LANG_ID, CLASS_ID, CLIENT_ID, MATTER_NO, \r\n"
                + "NOTE_NO, NOTE_TEXT, STATUS_ID, NOTE_TYP_ID, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON) \r\n"
                + "VALUES (:languageId, :classId, :clientId, :matterNumber, :notesNumber, :notesDescription, \r\n"
                + ":statusId, :noteTypeId, :deletionIndicator, :createdBy, :createdOn, :updatedBy, :updatedOn)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step6() {
        return stepBuilderFactory.get("step6").<MatterNote, MatterNote>chunk(10).reader(matterNoteItemReader())
                .writer(matterNoteItemWriter()).build();
    }

    /*-------------------------MatterRate----------------------------------------------------------------*/
    @Bean
    public FlatFileItemReader<MatterRate> matterRateItemReader() {
        FlatFileItemReader<MatterRate> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getMatterRateFile()));

        DefaultLineMapper<MatterRate> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"languageId", "classId", "clientId", "matterNumber", "caseInformationNo",
                "timeKeeperCode", "defaultRatePerHour", "assignedRatePerHour", "rateUnit", "statusId", "deletionIndicator",
                "createdBy", "createdOn"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new MatterRateFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public JdbcBatchItemWriter<MatterRate> matterRateItemWriter() {
        JdbcBatchItemWriter<MatterRate> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblmatterrateid (LANG_ID, CLASS_ID, CLIENT_ID, MATTER_NO, CASEINFO_NO, TK_CODE, \r\n"
                + "DEF_RATE, ASSIGNED_RATE, RATE_UNIT, STATUS_ID, IS_DELETED, CTD_BY, CTD_ON, UTD_BY, UTD_ON) \r\n"
                + "VALUES (:languageId, :classId, :clientId, :matterNumber, :caseInformationNo, :timeKeeperCode, \r\n"
                + ":defaultRatePerHour, :assignedRatePerHour, :rateUnit, :statusId, :deletionIndicator, \r\n "
                + ":createdBy, :createdOn, :createdBy, :createdOn)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step7() {
        return stepBuilderFactory.get("step7").<MatterRate, MatterRate>chunk(10).reader(matterRateItemReader())
                .writer(matterRateItemWriter()).build();
    }

    /*-------------------------MatterTimeTicket----------------------------------------------------------------*/
    @Bean
    public FlatFileItemReader<MatterTimeTicket> matterTimeTicketItemReader() {
        FlatFileItemReader<MatterTimeTicket> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getMatterTimeTicketFile()));

        DefaultLineMapper<MatterTimeTicket> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"languageId", "classId", "matterNumber", "clientId",
                "caseCategoryId", "caseSubCategoryId", "timeTicketNumber", "timeTicketHours", "timeTicketDate",
                "timeTicketAmount", "billType", "timeTicketDescription", "statusId", "deletionIndicator",
                "createdBy", "createdOn"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new MatterTimeTicketFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

//	TIME_TICKET_NO LANG_ID	CLASS_ID	MATTER_NO	CLIENT_ID	CASE_CATEGORY_ID	CASE_SUB_CATEGORY_ID	
//		TIME_TICKET_HRS	TIME_TICKET_DATE	TIME_TICKET_AMOUNT	BILL_TYPE	
//	TIME_TICKET_TEXT	STATUS_ID	Isdeleted	CTD_BY	CTD_ON

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public JdbcBatchItemWriter<MatterTimeTicket> matterTimeTicketItemWriter() {
        JdbcBatchItemWriter<MatterTimeTicket> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblmattertimeticketid (TIME_TICKET_NO, LANG_ID, CLASS_ID, MATTER_NO, CLIENT_ID, TK_CODE, \r\n"
                + "CASE_CATEGORY_ID, CASE_SUB_CATEGORY_ID, TIME_TICKET_HRS, TIME_TICKET_DATE, \r\n"
                + "TIME_TICKET_AMOUNT, BILL_TYPE, TIME_TICKET_TEXT, \r\n"
                + "STATUS_ID, IS_DELETED, CTD_BY, CTD_ON) \r\n"
                + "VALUES (:timeTicketNumber, :languageId, :classId, :matterNumber, :clientId, :createdBy, \r\n"
                + ":caseCategoryId, :caseSubCategoryId, :timeTicketHours, CAST(:timeTicketDate AS DATETIME), \r\n "
                + ":timeTicketAmount, :billType, :timeTicketDescription, \r\n "
                + ":statusId, :deletionIndicator, :createdBy, CAST(:createdOn AS DATETIME))");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step8() {
        return stepBuilderFactory.get("step8").<MatterTimeTicket, MatterTimeTicket>chunk(10).reader(matterTimeTicketItemReader())
                .writer(matterTimeTicketItemWriter()).build();
    }

    /*-------------------------InvoiceHeader----------------------------------------------------------------*/

    @Bean
    public FlatFileItemReader<InvoiceHeader> invoiceHeaderItemReader() {
        FlatFileItemReader<InvoiceHeader> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() +
                propertiesConfig.getInvoiceHeaderFile()));
        DefaultLineMapper<InvoiceHeader> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"languageId", "classId", "matterNumber", "clientId", "invoiceNumber", "invoiceFiscalYear",
                "invoicePeriod", "postingDate", "referenceText", "partnerAssigned", "invoiceDate", "invoiceAmount", "totalPaidAmount",
                "remainingBalance", "statusId", "deletionIndicator", "createdBy", "createdOn"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new InvoiceHeaderFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional
    @Bean
    public JdbcBatchItemWriter<InvoiceHeader> invoiceHeaderItemWriter() {
        JdbcBatchItemWriter<InvoiceHeader> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblinvoiceheader (LANG_ID, CLASS_ID, MATTER_NO, CLIENT_ID, INVOICE_NO, INVOICE_FISCAL_YEAR, INVOICE_PERIOD, POSTING_DATE,\r\n"
                + "INVOICE_REFERENCE, PARTNER_ASSIGNED, INVOICE_DATE, INVOICE_AMT, PAID_AMOUNT, REMAIN_BAL, STATUS_ID, IS_DELETED, CTD_BY, CTD_ON) \r\n"
                + "VALUES (:languageId, :classId, :matterNumber, :clientId, :invoiceNumber, :invoiceFiscalYear, :invoicePeriod, \r\n"
                + ":postingDate, :referenceText, :partnerAssigned, :invoiceDate, :invoiceAmount, :totalPaidAmount, :remainingBalance, :statusId, \r\n"
                + ":deletionIndicator, :createdBy, :createdOn)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step9() {
        return stepBuilderFactory.get("step9").<InvoiceHeader, InvoiceHeader>chunk(10).reader(invoiceHeaderItemReader())
                .writer(invoiceHeaderItemWriter()).build();
    }

    /*-------------------------InvoiceLine----------------------------------------------------------------*/
    @Bean
    public FlatFileItemReader<InvoiceLine> invoiceLineItemReader() {
        FlatFileItemReader<InvoiceLine> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getInvoiceLineFile()));
        DefaultLineMapper<InvoiceLine> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"languageId", "classId", "matterNumber", "clientId", "invoiceNumber", "serialNumber", "invoiceFiscalYear",
                "invoicePeriod", "itemNumber", "invoicedetailDescription", "billableAmount", "statusId", "deletionIndicator",
                "createdBy", "createdOn"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new InvoiceLineFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional
    @Bean
    public JdbcBatchItemWriter<InvoiceLine> invoiceLineItemWriter() {
        JdbcBatchItemWriter<InvoiceLine> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblinvoiceline (LANG_ID,CLASS_ID,MATTER_NO,CLIENT_ID,INVOICE_NO,SERIAL_NO,INVOICE_FISCAL_YEAR,\r\n"
                + "INVOICE_PERIOD,ITEM_NO,INVOICE_TEXT,BILL_AMOUNT,STATUS_ID,IS_DELETED,CTD_BY,CTD_ON) \r\n"
                + "VALUES (:languageId, :classId, :matterNumber, :clientId, :invoiceNumber,:serialNumber,:invoiceFiscalYear, \r\n"
                + ":invoicePeriod, :itemNumber, :invoicedetailDescription, :billableAmount, :statusId, :deletionIndicator, \r\n"
                + ":createdBy, :createdOn)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step10() {
        return stepBuilderFactory.get("step10").<InvoiceLine, InvoiceLine>chunk(10).reader(invoiceLineItemReader())
                .writer(invoiceLineItemWriter()).build();
    }

    /*-------------------------PaymentPlanHeader----------------------------------------------------------------*/
    @Bean
    public FlatFileItemReader<PaymentPlanHeader> paymentPlanHeaderItemReader() {
        FlatFileItemReader<PaymentPlanHeader> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() +
                propertiesConfig.getPaymentPlanHeaderFile()));

        DefaultLineMapper<PaymentPlanHeader> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"paymentPlanNumber", "paymentPlanRevisionNo", "languageId", "classId", "matterNumber",
                "clientId", "paymentPlanDate", "quotationNo", "paymentPlanStartDate", "endDate", "noOfInstallment",
                "paymentPlanTotalAmount", "dueAmount", "installmentAmount", "currency", "paymentCalculationDayDate",
                "paymentPlanText", "sentOn", "approvedOn", "statusId", "deletionIndicator", "referenceField1", "referenceField2",
                "referenceField3", "referenceField4", "referenceField5", "referenceField6", "referenceField7", "referenceField8",
                "referenceField9", "referenceField10", "createdBy"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new PaymentPlanHeaderFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public JdbcBatchItemWriter<PaymentPlanHeader> paymentPlanHeaderItemWriter() {
        JdbcBatchItemWriter<PaymentPlanHeader> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblPaymentplanheader (PAYMENT_PLAN_NO, PLAN_REV_NO, LANG_ID, CLASS_ID, MATTER_NO, CLIENT_ID, \r\n"
                + "PAYMENT_PLAN_DATE, QUOTE_NO, START_DATE, END_DATE, NO_OF_INSTAL, PAYMENT_PLAN_AMT, DUE_AMOUNT, INSTALL_AMT, \r\n"
                + "CURRENCY, PAYMENT_CAL_DAY, PAY_PLAN_TEXT, SENT_ON, APPROVED_ON, STATUS_ID, IS_DELETED, REF_FIELD_1, \r\n"
                + "REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, \r\n"
                + "REF_FIELD_10, CTD_BY, CTD_ON, UTD_BY, UTD_ON) \r\n"
                + "VALUES (:paymentPlanNumber, :paymentPlanRevisionNo, :languageId, :classId, :matterNumber, :clientId,\r\n"
                + ":paymentPlanDate, :quotationNo, :paymentPlanStartDate, :endDate, :noOfInstallment, :paymentPlanTotalAmount,\r\n "
                + ":dueAmount, :installmentAmount, :currency, :paymentCalculationDayDate, :paymentPlanText, :sentOn, :approvedOn,\r\n "
                + ":statusId, :deletionIndicator, :referenceField1, :referenceField2, :referenceField3, :referenceField4,\r\n "
                + ":referenceField5, :referenceField6, :referenceField7, :referenceField8, :referenceField9,\r\n "
                + ":referenceField10, :createdBy, CURDATE(), :createdBy, :CURDATE())");

        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step11() {
        return stepBuilderFactory.get("step11").<PaymentPlanHeader, PaymentPlanHeader>chunk(10).reader(paymentPlanHeaderItemReader())
                .writer(paymentPlanHeaderItemWriter()).build();
    }

    /*-------------------------PaymentPlanLine----------------------------------------------------------------*/
    @Bean
    public FlatFileItemReader<PaymentPlanLine> paymentPlanLineItemReader() {
        FlatFileItemReader<PaymentPlanLine> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getPaymentPlanLineFile()));

        DefaultLineMapper<PaymentPlanLine> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"languageId", "classId", "matterNumber", "clientId", "paymentPlanNumber",
                "paymentPlanRevisionNo", "itemNumber", "quotationNo", "installmentduedate", "dueDate", "dueAmount", "remainingDueNow",
                "currency", "paymentReminderDays", "reminderDate", "reminderStatus", "statusId", "deletionIndicator", "referenceField1",
                "referenceField2", "referenceField3", "referenceField4", "referenceField5", "referenceField6", "referenceField7",
                "referenceField8", "referenceField9", "referenceField10", "createdBy"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new PaymentPlanLineFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public JdbcBatchItemWriter<PaymentPlanLine> paymentPlanLineItemWriter() {
        JdbcBatchItemWriter<PaymentPlanLine> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblpaymentplanline (LANG_ID, CLASS_ID, MATTER_NO, CLIENT_ID, PAYMENT_PLAN_NO, PLAN_REV_NO, \r\n"
                + "ITEM_NO, QUOTE_NO, INSTALL_DUE_DATE, DUE_DATE, DUE_AMOUNT, REMAIN_DUE_NOW, CURRENCY, REMINDER_DAYS, REMINDER_DATE, \r\n"
                + "REMINDER_STATUS, STATUS_ID, IS_DELETED, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, \r\n"
                + "REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, CTD_BY, CTD_ON, UTD_BY, UTD_ON) \r\n"
                + "VALUES (:languageId, :classId, :matterNumber, :clientId, :paymentPlanNumber, \r\n"
                + ":paymentPlanRevisionNo, :itemNumber, :quotationNo, :installmentduedate, :dueDate, :dueAmount, :remainingDueNow,\r\n "
                + ":currency, :paymentReminderDays, :reminderDate, :reminderStatus, :statusId, :deletionIndicator, :referenceField1,\r\n "
                + ":referenceField2, :referenceField3, :referenceField4, :referenceField5, :referenceField6, :referenceField7,\r\n "
                + ":referenceField8, :referenceField9, :referenceField10, :createdBy, CURDATE(), :createdBy, :CURDATE())");

        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step12() {
        return stepBuilderFactory.get("step12").<PaymentPlanLine, PaymentPlanLine>chunk(10).reader(paymentPlanLineItemReader())
                .writer(paymentPlanLineItemWriter()).build();
    }

    /*-------------------------PaymentUpdate----------------------------------------------------------------*/
    @Bean
    public FlatFileItemReader<PaymentUpdate> paymentUpdateItemReader() {
        FlatFileItemReader<PaymentUpdate> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getPaymentUpdateFile()));

        DefaultLineMapper<PaymentUpdate> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"paymentId", "invoiceNumber", "classId", "clientId", "createdBy", "createdOn", "languageId", "matterNumber",
                "paymentAmount", "paymentDate", "paymentNumber", "paymentText", "postingDate", "statusId", "transactionType", "paymentCode"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new PaymentUpdateFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public JdbcBatchItemWriter<PaymentUpdate> paymentUpdateItemWriter() {
        JdbcBatchItemWriter<PaymentUpdate> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblpaymentupdate (PAYMENTID, INVOICE_NO, CLASS_ID, CLIENT_ID, CTD_BY, CTD_ON, LANG_ID, MATTER_NO, \r\n " +
                " PAYMENT_AMOUNT, PAYMENT_DATE, PAYMENT_NUMBER, PAYMENT_TEXT, POSTING_DATE, STATUS_ID, TRANSACTION_TYPE, PAYMENT_CODE) \r\n"
                + "VALUES (:paymentId, :invoiceNumber, :classId, :clientId, :createdBy, :createdOn, :languageId, :matterNumber, :paymentAmount, :paymentDate,\r\n"
                + ":paymentNumber, :paymentText, :postingDate, :statusId, :transactionType, :paymentCode)");

        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step13() {
        return stepBuilderFactory.get("step13").<PaymentUpdate, PaymentUpdate>chunk(10).reader(paymentUpdateItemReader())
                .writer(paymentUpdateItemWriter()).build();
    }

    //------------------------------------------------------------------------------------------------------

    @Bean
    public JobListener claraListener() throws Exception {
        JobListener listener = new JobListener();
        return listener;
    }

    @Bean
    public Job jobClientGeneral() throws Exception {
        return jobBuilderFactory.get("jobClientGeneral")
                .listener(claraListener())
                .start(step1())
                .build();
    }

    @Bean
    public Job jobClientNote() throws Exception {
        return jobBuilderFactory.get("jobClientNote")
                .listener(claraListener())
                .start(step2())
                .build();
    }

    @Bean
    public Job jobMatterAssignment() throws Exception {
        return jobBuilderFactory.get("jobMatterAssignment")
                .listener(claraListener())
                .start(step3())
                .build();
    }

    @Bean
    public Job jobMatterExpense() throws Exception {
        return jobBuilderFactory.get("jobMatterExpense")
                .listener(claraListener())
                .start(step4())
                .build();
    }

    @Bean
    public Job jobMatterGenAcc() throws Exception {
        return jobBuilderFactory.get("jobMatterGenAcc")
                .listener(claraListener())
                .start(step5())
                .build();
    }

    @Bean
    public Job jobMatterNote() throws Exception {
        return jobBuilderFactory.get("jobMatterNote")
                .listener(claraListener())
                .start(step6())
                .build();
    }

    @Bean
    public Job jobMatterRate() throws Exception {
        return jobBuilderFactory.get("jobMatterRate")
                .listener(claraListener())
                .start(step7())
                .build();
    }

    @Bean
    public Job jobMatterTimeTicket() throws Exception {
        return jobBuilderFactory.get("jobMatterTimeTicket")
                .listener(claraListener())
                .start(step8())
                .build();
    }

    //-----------------------------------------------------------------------------------------------------

    @Bean
    public Job jobStorePartnerListing() throws Exception {
        return jobBuilderFactory.get("jobStorePartnerListing")
                .listener(claraListener())
                .start(step14())
                .build();
    }
    
    //-----------------------------------------------------------------------------------------------------

    @Bean
    public Job jobInvoiceHeader() throws Exception {
        return jobBuilderFactory.get("jobInvoiceHeader")
                .listener(claraListener())
                .start(step9())
                .build();
    }

    @Bean
    public Job jobInvoiceLine() throws Exception {
        return jobBuilderFactory.get("jobInvoiceLine")
                .listener(claraListener())
                .start(step10())
                .build();
    }

    @Bean
    public Job jobPaymentPlanHeader() throws Exception {
        return jobBuilderFactory.get("jobPaymentPlanHeader")
                .listener(claraListener())
                .start(step11())
                .build();
    }

    @Bean
    public Job jobPaymentPlanLine() throws Exception {
        return jobBuilderFactory.get("jobPaymentPlanLine")
                .listener(claraListener())
                .start(step12())
                .build();
    }

    @Bean
    public Job jobPaymentUpdate() throws Exception {
        return jobBuilderFactory.get("jobPaymentUpdate")
                .listener(claraListener())
                .start(step13())
                .build();
    }
    //-------------------------------------------------------------------------------------------------------
//
//	@Bean
//	public FlatFileItemReader<Person> personItemReader() {
//		FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
//		reader.setLinesToSkip(1);
//		reader.setResource(new ClassPathResource("/Book1.csv"));
//
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
//	@Bean
//	public Job job() {
//		return jobBuilderFactory.get("job")
//				.start(step1())
//				.next(step2())
//				.build();
//	}

    //------------------------------Student-------------------------------------------
//	@Bean
//	public FlatFileItemReader<Student> studentItemReader() {
//		FlatFileItemReader<Student> reader = new FlatFileItemReader<>();
//		reader.setLinesToSkip(1);
//		reader.setResource(new ClassPathResource("/Student.csv"));
//
//		DefaultLineMapper<Student> customerLineMapper = new DefaultLineMapper<>();
//
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

    /*-------------------------Control Group----------------------------------------------------------------*/
    @Bean
    public FlatFileItemReader<StorePartnerListing> storePartnerListingItemReader() {
        FlatFileItemReader<StorePartnerListing> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(propertiesConfig.getFileUploadDir() + propertiesConfig.getStorePartnerListingFile()));

        DefaultLineMapper<StorePartnerListing> customerLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"languageId", "companyId", "storeId", "versionNumber", "storeName", "validityDateFrom", "validityDateTo", "groupTypeId",
                "groupTypeName", "subGroupId", "subGroupName", "groupId", "groupName", "coOwnerId1", "coOwnerId2", "coOwnerId3",
                "coOwnerId4", "coOwnerId5", "coOwnerName1", "coOwnerName2", "coOwnerName3", "coOwnerName4", "coOwnerName5",
                "coOwnerPercentage1", "coOwnerPercentage2", "coOwnerPercentage3", "coOwnerPercentage4", "coOwnerPercentage5",
                "statusId", "deletionIndicator", "referenceField1", "referenceField2", "referenceField3", "referenceField4",
                "referenceField5", "referenceField6", "referenceField7", "referenceField8", "referenceField9", "referenceField10",
                "createdBy", "createdOn", "updatedBy", "updatedOn"});
        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new StorePartnerListingFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        reader.setLineMapper(customerLineMapper);
        return reader;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public JdbcBatchItemWriter<StorePartnerListing> storePartnerListingItemWriter() {
        JdbcBatchItemWriter<StorePartnerListing> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("INSERT INTO tblstorepartnerlisting (LANG_ID, C_ID, STORE_ID, VERSION_NO, STORE_NM, VALID_DATE_FROM, VALID_DATE_TO, \r\n"
                + "GRP_ID, GRP_TYP_NM, SUB_GRP_ID, SUB_GRP_NM, GROUP_ID, GRP_NM, CO_OWNER_ID_1, CO_OWNER_ID_2, CO_OWNER_ID_3, \r\n"
                + "CO_OWNER_ID_4, CO_OWNER_ID_5, CO_OWNER_NAME_1, CO_OWNER_NAME_2, CO_OWNER_NAME_3, CO_OWNER_NAME_4, CO_OWNER_NAME_5, CO_OWNER_PER_1, \r\n"
                + "CO_OWNER_PER_2, CO_OWNER_PER_3, CO_OWNER_PER_4, CO_OWNER_PER_5, STATUS_ID, IS_DELETED, REF_FIELD_1, REF_FIELD_2, \r\n"
                + "REF_FIELD_3, REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, \r\n"
                + "REF_FIELD_9, REF_FIELD_10, CTD_BY, CTD_ON, UTD_BY, UTD_ON)\r\n"
                + "VALUES (:languageId, :companyId, :storeId, :versionNumber, :storeName, CAST(:validityDateFrom AS DATETIME), CAST(:validityDateTo AS DATETIME), :groupTypeId, \r\n"
                + ":groupTypeName, :subGroupId, :subGroupName, :groupId, :groupName, :coOwnerId1, :coOwnerId2, :coOwnerId3, \r\n"
                + ":coOwnerId4, :coOwnerId5, :coOwnerName1, :coOwnerName2, :coOwnerName3, :coOwnerName4, :coOwnerName5, \r\n "
                + ":coOwnerPercentage1, :coOwnerPercentage2, :coOwnerPercentage3, :coOwnerPercentage4, :coOwnerPercentage5, \r\n "
                + ":statusId, :deletionIndicator, :referenceField1, :referenceField2, :referenceField3, :referenceField4, \r\n "
                + ":referenceField5, :referenceField6, :referenceField7, :referenceField8, :referenceField9, :referenceField10, \r\n "
                + ":createdBy, CAST(:createdOn AS DATETIME), :updatedBy, CAST(:updatedOn AS DATETIME)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Step step14() {
        return stepBuilderFactory.get("step14").<StorePartnerListing, StorePartnerListing>chunk(10).reader(storePartnerListingItemReader())
                .writer(storePartnerListingItemWriter()).build();
    }
}