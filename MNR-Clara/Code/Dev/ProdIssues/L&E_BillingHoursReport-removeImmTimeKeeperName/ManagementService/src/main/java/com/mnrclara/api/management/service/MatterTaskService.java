package com.mnrclara.api.management.service;

import com.mnrclara.api.management.config.PropertiesConfig;
import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.dto.CalendarEvent;
import com.mnrclara.api.management.model.dto.UserProfile;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.model.mattertask.AddMatterTask;
import com.mnrclara.api.management.model.mattertask.MatterTask;
import com.mnrclara.api.management.model.mattertask.SearchMatterTask;
import com.mnrclara.api.management.model.mattertask.UpdateMatterTask;
import com.mnrclara.api.management.repository.MatterGenAccRepository;
import com.mnrclara.api.management.repository.MatterTaskRepository;
import com.mnrclara.api.management.repository.specification.MatterTaskSpecification;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MatterTaskService {

	private static final String MATTERTASK = "MATTERTASK";

	@Autowired
	private MatterTaskRepository matterTaskRepository;

	@Autowired
	private MatterGenAccService matterGenAccService;

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private SetupService setupService;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private PropertiesConfig propertiesConfig;

	@Autowired
	CRMService crmService;

	@Autowired
	MatterGenAccRepository matterGenAccRepository;

	/**
	 * getMatterTasks
	 * 
	 * @return
	 */
	public List<MatterTask> getMatterTasks() {
		List<MatterTask> matterTaskList = matterTaskRepository.findAll();
		matterTaskList = matterTaskList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return matterTaskList;
	}

	/**
	 * getMatterTask
	 * 
	 * @param matterTaskId
	 * @return
	 */
	public MatterTask getMatterTask(String taskNumber) {
		MatterTask matterTask = matterTaskRepository.findByTaskNumber(taskNumber).orElse(null);
		if (matterTask != null && matterTask.getDeletionIndicator() == 0) {
			return matterTask;
		} else {
			throw new BadRequestException("The given MatterTask ID : " + taskNumber + " doesn't exist.");
		}
	}
	
	/**
	 * findMatterTasks
	 * @param searchMatterTask
	 * @return
	 * @throws ParseException 
	 */
	public List<MatterTask> findMatterTasks (SearchMatterTask searchMatterTask) throws ParseException {
		if (searchMatterTask.getSCreatedOn() != null && searchMatterTask.getECreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterTask.getSCreatedOn(), searchMatterTask.getECreatedOn());
			searchMatterTask.setSCreatedOn(dates[0]);
			searchMatterTask.setECreatedOn(dates[1]);
		}
		
		if (searchMatterTask.getSDeadlineDate() != null && searchMatterTask.getEDeadlineDate() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterTask.getSDeadlineDate(), searchMatterTask.getEDeadlineDate());
			searchMatterTask.setSDeadlineDate(dates[0]);
			searchMatterTask.setEDeadlineDate(dates[1]);
		}
		
		if (searchMatterTask.getSReminderDate() != null && searchMatterTask.getEReminderDate() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterTask.getSReminderDate(), searchMatterTask.getEReminderDate());
			searchMatterTask.setSReminderDate(dates[0]);
			searchMatterTask.setEReminderDate(dates[1]);
		}
		
		MatterTaskSpecification spec = new MatterTaskSpecification(searchMatterTask);
		List<MatterTask> searchResults = matterTaskRepository.findAll(spec);
		log.info("searchResults: " + searchResults);
		return searchResults;
	}

	/**
	 * createMatterTask
	 * 
	 * @param newMatterTask
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterTask createMatterTask(AddMatterTask newMatterTask, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		MatterTask dbMatterTask = new MatterTask();
		// TASK_ASSIGN - UI will send multiple values separated by comma
		dbMatterTask.setTaskAssignedTo(newMatterTask.getTaskAssignedTo());

		// TASK_EMAIL_ID <Multiple email addresses with comma>
		dbMatterTask.setTaskEmailId(newMatterTask.getTaskEmailId());

		// Copy all attributes from AddMatterTask to MatterTask
		BeanUtils.copyProperties(newMatterTask, dbMatterTask, CommonUtils.getNullPropertyNames(newMatterTask));

		MatterGenAcc matterGenAcc = matterGenAccService.getMatterGenAcc(newMatterTask.getMatterNumber());

		// LANG_ID
		dbMatterTask.setLanguageId(matterGenAcc.getLanguageId());

		// CLASS_ID
		dbMatterTask.setClassId(matterGenAcc.getClassId());

		// CLIENT_ID
		dbMatterTask.setClientId(matterGenAcc.getClientId());

		// TASK_NO
		/*
		 * "During Save, check the CLASS_ID values 1. If CLASS_ID=1, then pass CLASS_ID,
		 * NUM_RAN_CODE=10 in NUMBERRANGE table and Fetch NUM_RAN_CURRENT values and add
		 * +1 and then insert 2. If CLASS_ID=2, then pass CLASS_ID, NUM_RAN_CODE=11 in
		 * NUMBERRANGE table and Fetch NUM_RAN_CURRENT values and add +1 and then insert
		 */
		long NUM_RAN_CODE = 0;
		long classID = 0;
		String fromEmailAddress = null;
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
				
		if (matterGenAcc.getClassId() != null && matterGenAcc.getClassId().longValue() == 1) {
			NUM_RAN_CODE = 10;
		} else if (matterGenAcc.getClassId() != null && matterGenAcc.getClassId().longValue() == 2) {
			NUM_RAN_CODE = 11;
		}
		
		// Retrieve LoginUserID's email ID
		UserProfile userProfile = setupService.getUserProfile(loginUserID, authTokenForSetupService.getAccess_token());
		fromEmailAddress = userProfile.getEmailId();
		classID = matterGenAcc.getClassId().longValue();
		
		String TASK_NO = setupService.getNextNumberRange(classID, NUM_RAN_CODE,
				authTokenForSetupService.getAccess_token());
		log.info("nextVal from NumberRange for TASK_NO: " + TASK_NO);
		dbMatterTask.setTaskNumber(TASK_NO);
		dbMatterTask.setStatusId(31L); // Hard Coded Value - 31
		dbMatterTask.setDeletionIndicator(0L);
		dbMatterTask.setCreatedBy(loginUserID);
		dbMatterTask.setUpdatedBy(loginUserID);
		dbMatterTask.setCreatedOn(new Date());
		dbMatterTask.setUpdatedOn(new Date());
		MatterTask createdMatterTask = matterTaskRepository.save(dbMatterTask);
		
		createdMatterTask.setReferenceField10(fromEmailAddress);
		
		log.info("-----input---event--date : " + newMatterTask.getDeadlineDate());
		log.info("-----saved---event--date : " + createdMatterTask.getDeadlineDate());

		// DEADLINE_DATE - Calendar Event
		Boolean deadlineDateResponse = sendCalendarEvent(createdMatterTask, createdMatterTask.getDeadlineDate(),
				loginUserID, "DEADLINE: ", matterGenAcc.getMatterDescription());
		log.info("deadlineDateResponse : " + deadlineDateResponse);
		if (deadlineDateResponse) {
			// REMINDER_DATE - Calendar Event
			Boolean reminderDateresponse = sendCalendarEvent(createdMatterTask, createdMatterTask.getReminderDate(),
					loginUserID, "REMINDER: ", matterGenAcc.getMatterDescription());
			log.info("reminderDateresponse : " + reminderDateresponse);
			return createdMatterTask;
		}
		return createdMatterTask;
	}

	/**
	 * 
	 * @param mMatterTask
	 * @param eventDate
	 * @param loginUserID
	 * @param subjectPrefix 
	 * @param matterDescription() 
	 * @return
	 */
	public Boolean sendCalendarEvent(MatterTask matterTask, Date eventDate, String loginUserID, String subjectPrefix, String matterDescription) {
		// Send Calendar Event
		CalendarEvent calendarEvent = new CalendarEvent();

		// FROM
		if (matterTask.getReferenceField10() != null) {
			calendarEvent.setFromAddress(matterTask.getReferenceField10());  	// User Email ID
			log.info("User Email ID : " + matterTask.getReferenceField10());
		} else {
			calendarEvent.setFromAddress(propertiesConfig.getTaskEmailFromAddress()); 
		}
		
		calendarEvent.setSenderName(propertiesConfig.getTaskEmailFromAddress());
		
		// TO
		calendarEvent.setToAddress(matterTask.getTaskEmailId());

		// Subject - DEADLINE : MATTER_ID (MATTER_TEXT) / TASK_NO (TASK_NM)	
		// String subject = subjectPrefix + " MATTER ID (" + matterTask.getMatterNumber() + ") / TASK NO (" + matterTask.getTaskNumber() + ")";
		
		// Subject - DEADLINE : <Task Name> - <Priority Level> - <MatterNumber> - <MatterText>
		String subject = subjectPrefix + matterTask.getTaskName() + "-" + 
										matterTask.getPriority() + " Priority-" + 
										matterTask.getMatterNumber() + "-" +
										matterDescription;
										
		calendarEvent.setSubject(subject);

		// Start Date and Time
		LocalDateTime startDateTime = DateUtils.convertDateToLocalDateTime(eventDate, "START");
		log.info("startDateTime-------<> : " + startDateTime);
		calendarEvent.setMeetingStartTime(startDateTime);

		// End Date and Time
		LocalDateTime endDateTime = DateUtils.convertDateToLocalDateTime(eventDate, "END");
		log.info("endDateTime-------<> : " + endDateTime);
		calendarEvent.setMeetingEndTime(endDateTime);

		// BODY
		calendarEvent.setBodyText(matterTask.getTaskDescription());

		// Get AuthToken for CommonService
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		Boolean response = commonService.sendCalendar(calendarEvent, authTokenForCommonService.getAccess_token());
		log.info("Calendar response : " + response);
		return response;
	}

	/**
	 * updateMatterTask
	 * 
	 * @param mattertaskId
	 * @param updateMatterTask
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ParseException
	 */
	public MatterTask updateMatterTask(String taskNumber, UpdateMatterTask updateMatterTask, String loginUserID)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		MatterTask dbMatterTask = getMatterTask(taskNumber);
		/*
		 * When the record is updated with DEADLINE_DATE or REMINDER_DATE or
		 * TASK_EMAIL_ID field values in MATTERTASK table, then
		 */
		boolean isCalendarToBeResent = false;

		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();

		// TASK_NM
		if (updateMatterTask.getTaskName() != null
				&& !updateMatterTask.getTaskName().equalsIgnoreCase(dbMatterTask.getTaskName())) {
			log.info("Inserting Audit log for TASK_NM");
			setupService.createAuditLogRecord(loginUserID, taskNumber, 3L, MATTERTASK, "TASK_NM",
					dbMatterTask.getTaskName(), updateMatterTask.getTaskName(),
					authTokenForSetupService.getAccess_token());
		}

		// PRIORITY
		if (updateMatterTask.getPriority() != null
				&& !updateMatterTask.getPriority().equalsIgnoreCase(dbMatterTask.getPriority())) {
			log.info("Inserting Audit log for PRIORITY");
			setupService.createAuditLogRecord(loginUserID, taskNumber, 3L, MATTERTASK, "PRIORITY",
					dbMatterTask.getPriority(), updateMatterTask.getPriority(),
					authTokenForSetupService.getAccess_token());
		}

		// TASK_ASSIGN
		if (updateMatterTask.getTaskAssignedTo() != null
				&& !updateMatterTask.getTaskAssignedTo().equalsIgnoreCase(dbMatterTask.getTaskAssignedTo())) {
			log.info("Inserting Audit log for TASK_ASSIGN");
			setupService.createAuditLogRecord(loginUserID, taskNumber, 3L, MATTERTASK,
					"TASK_ASSIGN", dbMatterTask.getTaskAssignedTo(), updateMatterTask.getTaskAssignedTo(),
					authTokenForSetupService.getAccess_token());
		}

		// TASK_EMAIL_ID
		if (updateMatterTask.getTaskEmailId() != null
				&& !updateMatterTask.getTaskEmailId().equalsIgnoreCase(dbMatterTask.getTaskEmailId())) {
			log.info("Inserting Audit log for TASK_EMAIL_ID");
			setupService.createAuditLogRecord(loginUserID, taskNumber, 3L, MATTERTASK,
					"TASK_EMAIL_ID", dbMatterTask.getTaskEmailId(), updateMatterTask.getTaskEmailId(),
					authTokenForSetupService.getAccess_token());
			isCalendarToBeResent = true;
		}

		// TASK_TYP_CODE/TASK_TYP_CODE_TEXT
		if (updateMatterTask.getTaskTypeCode() != null
				&& !updateMatterTask.getTaskTypeCode().equalsIgnoreCase(dbMatterTask.getTaskTypeCode())) {
			log.info("Inserting Audit log for TASK_TYP_CODE");
			setupService.createAuditLogRecord(loginUserID, taskNumber, 3L, MATTERTASK,
					"TASK_TYP_CODE", dbMatterTask.getTaskTypeCode(), updateMatterTask.getTaskTypeCode(),
					authTokenForSetupService.getAccess_token());
		}

		// TASK_TEXT
		if (updateMatterTask.getTaskDescription() != null
				&& !updateMatterTask.getTaskDescription().equalsIgnoreCase(dbMatterTask.getTaskDescription())) {
			log.info("Inserting Audit log for TASK_TEXT");
			setupService.createAuditLogRecord(loginUserID, taskNumber, 3L, MATTERTASK,
					"TASK_TEXT", dbMatterTask.getTaskDescription(), updateMatterTask.getTaskDescription(),
					authTokenForSetupService.getAccess_token());
		}

		// DEADLINE_DAYS
		if (updateMatterTask.getDeadlineCalculationDays() != null
				&& updateMatterTask.getDeadlineCalculationDays() != dbMatterTask.getDeadlineCalculationDays()) {
			log.info("Inserting Audit log for DEADLINE_DAYS");
			setupService.createAuditLogRecord(loginUserID,
					taskNumber, 3L, MATTERTASK, "DEADLINE_DAYS",
					String.valueOf(dbMatterTask.getDeadlineCalculationDays()),
					String.valueOf(updateMatterTask.getDeadlineCalculationDays()),
					authTokenForSetupService.getAccess_token());
		}

		// DEADLINE_DATE
		if (updateMatterTask.getDeadlineDate() != null) {
			log.info("Inserting Audit log for DEADLINE_DATE");
			boolean isEqual = DateUtils.compareDates(updateMatterTask.getDeadlineDate(),
					dbMatterTask.getDeadlineDate());
			if (!isEqual) {
				setupService.createAuditLogRecord(loginUserID, taskNumber, 3L,
						MATTERTASK, "DEADLINE_DATE", String.valueOf(dbMatterTask.getDeadlineDate()),
						String.valueOf(updateMatterTask.getDeadlineDate()), authTokenForSetupService.getAccess_token());
				isCalendarToBeResent = true;
			}
		}

		// REMINDER_DAYS
		if (updateMatterTask.getReminderCalculationDays() != null
				&& updateMatterTask.getReminderCalculationDays() != dbMatterTask.getReminderCalculationDays()) {
			log.info("Inserting Audit log for REMINDER_DAYS");
			setupService.createAuditLogRecord(loginUserID,
					taskNumber, 3L, MATTERTASK, "REMINDER_DAYS",
					String.valueOf(dbMatterTask.getReminderCalculationDays()),
					String.valueOf(updateMatterTask.getReminderCalculationDays()),
					authTokenForSetupService.getAccess_token());
		}

		// REMINDER_DATE
		if (updateMatterTask.getReminderDate() != null) {
			log.info("Inserting Audit log for REMINDER_DAYS");
			boolean isEqual = DateUtils.compareDates(updateMatterTask.getReminderDate(),
					dbMatterTask.getReminderDate());
			if (!isEqual) {
				setupService.createAuditLogRecord(loginUserID, taskNumber, 3L,
						MATTERTASK, "REMINDER_DATE", String.valueOf(dbMatterTask.getReminderDate()),
						String.valueOf(updateMatterTask.getReminderDate()), authTokenForSetupService.getAccess_token());
				isCalendarToBeResent = true;
			}
		}

		BeanUtils.copyProperties(updateMatterTask, dbMatterTask, CommonUtils.getNullPropertyNames(updateMatterTask));
		dbMatterTask.setUpdatedBy(loginUserID);
		dbMatterTask.setUpdatedOn(new Date());
		MatterTask updatedMatterTask = matterTaskRepository.save(dbMatterTask);

		MatterGenAcc matterGenAcc = matterGenAccService.getMatterGenAcc(updateMatterTask.getMatterNumber());
		// Send Calendar when there are fields updated
		if (isCalendarToBeResent) {
			// DEADLINE_DATE - Calendar Event
			Boolean deadlineDateResponse = sendCalendarEvent(updatedMatterTask, updatedMatterTask.getDeadlineDate(),
					loginUserID, "DEADLINE:", matterGenAcc.getMatterDescription());
			log.info("deadlineDateResponse : " + deadlineDateResponse);

			// REMINDER_DATE - Calendar Event
			Boolean reminderDateresponse = sendCalendarEvent(updatedMatterTask, updatedMatterTask.getReminderDate(),
					loginUserID, "REMINDER:", matterGenAcc.getMatterDescription());
			log.info("reminderDateresponse : " + reminderDateresponse);
		}
		return updatedMatterTask;
	}

	/**
	 * deleteMatterTask
	 * 
	 * @param mattertaskCode
	 */
	public void deleteMatterTask(String taskNumber) {
		MatterTask mattertask = getMatterTask(taskNumber);
		if (mattertask != null) {
			mattertask.setDeletionIndicator(1L);
			matterTaskRepository.save(mattertask);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + taskNumber);
		}
	}

	@Async
	public void sendNotification(MatterTask matterTask, String topic){
		log.info("Send notification to CRM service for matter task creation : " + matterTask);
		String matterDescription = matterGenAccRepository.getMatterText(matterTask.getMatterNumber());
		AuthToken authTokenForCRMService = authTokenService.getCrmServiceAuthToken();
		String message = "A new task for matter " + matterDescription + " has been assigned by "+ matterTask.getCreatedBy() + " on ";
		Date createdOn = matterTask.getCreatedOn();
		String createdBy = matterTask.getCreatedBy();
		List<String> userId =  new ArrayList<>();
		if(matterTask.getTaskAssignedTo() != null) {
			userId.add(matterTask.getTaskAssignedTo());
		}
		if(matterTask.getCreatedBy() != null && !matterTask.getTaskAssignedTo().equals(matterTask.getCreatedBy())) {
			userId.add(matterTask.getCreatedBy());
		}
		if(topic.equals("Matter Task Update")){
			message = "A task has been completed by "+ matterTask.getTaskAssignedTo() +" for matter " + matterDescription  + " on ";
			createdOn = matterTask.getUpdatedOn();
			createdBy = matterTask.getUpdatedBy();
		}

		crmService.setNotificationMessage(
				topic,
				message,
				userId,
				null,
				createdOn,
				createdBy,
				authTokenForCRMService.getAccess_token());
	}
}
