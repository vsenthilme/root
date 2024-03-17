package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.config.PropertiesConfig;
import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.auth.AuthToken;
import com.mnrclara.api.setup.model.clientuser.ClientUser;
import com.mnrclara.api.setup.model.commonservice.EMail;
import com.mnrclara.api.setup.model.commonservice.SMS;
import com.mnrclara.api.setup.model.notification.TimeTicketNotification;
import com.mnrclara.api.setup.model.userprofile.AddUserProfile;
import com.mnrclara.api.setup.model.userprofile.UpdateUserProfile;
import com.mnrclara.api.setup.model.userprofile.UserOTP;
import com.mnrclara.api.setup.model.userprofile.UserProfile;
import com.mnrclara.api.setup.repository.TimeTicketNotificationRepository;
import com.mnrclara.api.setup.repository.UserOtpRepository;
import com.mnrclara.api.setup.repository.UserProfileRepository;
import com.mnrclara.api.setup.util.CommonUtils;
import com.mnrclara.api.setup.util.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserProfileService {
	
	@Autowired
	UserProfileRepository userProfileRepository;
	
	@Autowired
	UserOtpRepository userOtpRepository;
	
	@Autowired
	TimeTicketNotificationRepository ttNotificationRepository;
	
	@Autowired
	CommonCommunicationService commonCommunicationService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	ClientUserService clientUserService;
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	private static final String EMAIL_ID_TO_SKIP_OTP_VALIDATION = "claraitadmin@montyramirezlaw.com";
	
	/**
	 * 
	 * @return
	 */
	public List<UserProfile> getUserProfiles () {
		List<UserProfile> userProfileList = userProfileRepository.findAll();
		return userProfileList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getUserProfile
	 * @param userProfileId
	 * @return
	 */
	public UserProfile getUserProfile (String userProfileId) {
		UserProfile userProfile = userProfileRepository.findByUserId(userProfileId).orElse(null);
		log.info("userProfile : " + userProfile);
		if (userProfile != null && userProfile.getDeletionIndicator() == 0) {
			return userProfile;
		} else {
			throw new BadRequestException("The given UserProfile ID : " + userProfileId + " doesn't exist.");
		}
	}
	
	/**
	 * 
	 * @param userProfileId
	 * @param emailAddress
	 * @return
	 */
	public UserProfile getUserProfile (String userProfileId, String emailAddress) {
		UserProfile userProfile = userProfileRepository.findByUserIdAndEmailId(userProfileId, emailAddress);
		if (userProfile != null && userProfile.getDeletionIndicator() != 0) {
			return userProfile;
		} else {
			throw new BadRequestException("The given ID : " + userProfileId + " or " + emailAddress + " doesn't exist.");
		}
	}
	
	/**
	 * Pass the selected CLASS_ID in USERPROFILE table and 
	 * fetch USR_ID/USR_STATUS = ACTIVE values and display in dropdown
	 * @param userProfileId
	 * @return
	 */
	public UserProfile getUserProfile (Long classId) {
		UserProfile userProfile = userProfileRepository.findByClassId(classId).orElse(null);
		if (userProfile != null && 
				userProfile.getDeletionIndicator() == 0 && 
				userProfile.getUserStatus().equalsIgnoreCase("ACTIVE")) {
			return userProfile;
		} else {
			throw new BadRequestException("The given UserProfile ID : " + classId + " doesn't exist.");
		}
	}
	
	/**
	 * validateUser
	 * @param userId
	 * @param password
	 * @param isOTPRequired
	 * @return
	 */
	public UserProfile validateUser (String userId, String password) {
		UserProfile userProfile = getUserProfile(userId);
		if (userProfile == null) {
    		throw new BadRequestException("Invalid UserId : " + userId);
    	}
		
		boolean isValidUser = passwordEncoder.matches(password, userProfile.getPassword());
		
		if (isValidUser) {
			//if (userId.equalsIgnoreCase("JI") || userId.equalsIgnoreCase("MW")) {	// JI and MW
				doSendEmailOtp (userProfile.getUserId(), userProfile.getFirstName(), userProfile.getEmailId());
			//}
			
			return userProfile;
		} else {
			throw new BadRequestException("Password wrong. Please enter correct password.");
		}
	}
	
	/**
	 * 
	 * @param userName 
	 * @param userProfile
	 * @return 
	 * @return 
	 */
	private boolean doSendOtp (String contactNumber, String userName) {
		contactNumber = contactNumber.replaceAll("-", "");
		Long cNumber = Long.valueOf(contactNumber);
		
		SMS sms = new SMS();
		sms.setToNumber(cNumber);
		String msg = "To log into CLARA Client Portal please enter this 6-digit code: ";
		
		// Generate 6 digit OTP
		String otp = CommonUtils.generateOTP(6);
		msg += otp;
		sms.setTextMessage(msg);
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getCommonServiceAuthToken();
		Boolean isSMSSent = commonCommunicationService.sendOtp(authTokenForSetupService.getAccess_token(), sms);
		if (isSMSSent == Boolean.TRUE) {
			Date sentTime = new Date();
			UserOTP userOtp = new UserOTP();
			userOtp.setUserId(contactNumber);
			userOtp.setOtpCode(Long.valueOf(otp));
			userOtp.setOtpSentTime(sentTime);
			userOtp.setStatus("OTP_SENT");
			userOtp.setCreatedOn(sentTime);
			userOtp.setCreatedBy(userName);
			userOtp.setUpdatedOn(sentTime);
			userOtp.setUpdatedBy(userName);
			userOtpRepository.save(userOtp);
		}
		return isSMSSent;
	}
	
	/**
	 * 
	 * @param firstName
	 * @param emailId
	 * @param string 
	 * @return
	 */
	private boolean doSendEmailOtp(String userIdOrEmailId, String firstName, String emailId) {
		try {
			EMail email = new EMail();
			String subject = "Your One-Time Password (OTP)";
			String msg = "Dear " + firstName + ", <br/><br/> "
					+ "Your One-Time Password (OTP) to verify your CLARA log in attempt is: ";
			
			// Generate 6 digit OTP
			String otp = CommonUtils.generateOTP(4);
			msg += otp + ".<br/><br/>";
			msg += "Please note that this OTP is valid for a limited time only and can be used only once. Please do not share this OTP with anyone. <br/>";
			msg += "We take your security very seriously, and sharing your OTP with anyone may compromise your account.<br/><br/>";
			msg += "If you did not request an OTP, please contact CLARA Tech team immediately.<br/><br/>";
			msg += "Thank you.";
			
			log.info("Email Message:" + msg);
			
			email.setFromAddress(propertiesConfig.getLoginEmailFromAddress());
			email.setSubject(subject);
			email.setBodyText(msg);
			email.setToAddress(emailId);
			email.setSenderName("ClaraITAdmin");
			
			// Get AuthToken for SetupService
			AuthToken authTokenForSetupService = authTokenService.getCommonServiceAuthToken();
			boolean isEMailSent = commonCommunicationService.sendEmail(email, authTokenForSetupService.getAccess_token());
			log.info("isEMailSent:" + isEMailSent);
			if (isEMailSent == Boolean.TRUE) {
				Date sentTime = new Date();
				UserOTP userOtp = new UserOTP();
				userOtp.setUserId(userIdOrEmailId);
				userOtp.setOtpCode(Long.valueOf(otp));
				userOtp.setOtpSentTime(sentTime);
				userOtp.setStatus("OTP_SENT");
				userOtp.setCreatedOn(sentTime);
				userOtp.setCreatedBy("ClaraITAdmin");
				userOtp.setUpdatedOn(sentTime);
				userOtp.setUpdatedBy("ClaraITAdmin");
				userOtp = userOtpRepository.save(userOtp);
				log.info("UserOTP:" + userOtp);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param userIdOrEmailId
	 * @param firstName
	 * @param emailId
	 * @return
	 */
	private boolean doSendEmailOtpForClientUser (String userIdOrEmailId, String firstName, String emailId) {
		try {
			log.info("firstName:" + firstName);
			
			ClientUser clientUser = clientUserService.getClientUserByEmailId(emailId);
			EMail email = new EMail();
			String subject = "Your One-Time Password (OTP)";
			String msg = "Dear " + clientUser.getFirstName() + ", <br/><br/> "
					+ "Your One-Time Password (OTP) to verify your CLARA log in attempt is: ";
			
			// Generate 6 digit OTP
			String otp = CommonUtils.generateOTP(4);
			msg += otp + ".<br/><br/>";
			msg += "Please note that this OTP is valid for a limited time only and can be used only once. Please do not share this OTP with anyone. <br/>";
			msg += "We take your security very seriously, and sharing your OTP with anyone may compromise your account.<br/><br/>";
			msg += "If you did not request an OTP, please contact CLARA Tech team immediately.<br/><br/>";
			msg += "Thank you.";
			
			log.info("Email Message:" + msg);
			
			email.setFromAddress(propertiesConfig.getLoginEmailFromAddress());
			email.setSubject(subject);
			email.setBodyText(msg);
			email.setToAddress(emailId);
			email.setSenderName("ClaraITAdmin");
			
			// Get AuthToken for SetupService
			AuthToken authTokenForSetupService = authTokenService.getCommonServiceAuthToken();
			boolean isEMailSent = commonCommunicationService.sendEmail(email, authTokenForSetupService.getAccess_token());
			log.info("isEMailSent:" + isEMailSent);
			if (isEMailSent == Boolean.TRUE) {
				Date sentTime = new Date();
				UserOTP userOtp = new UserOTP();
				userOtp.setUserId(userIdOrEmailId);
				userOtp.setOtpCode(Long.valueOf(otp));
				userOtp.setOtpSentTime(sentTime);
				userOtp.setStatus("OTP_SENT");
				userOtp.setCreatedOn(sentTime);
				userOtp.setCreatedBy("ClaraITAdmin");
				userOtp.setUpdatedOn(sentTime);
				userOtp.setUpdatedBy("ClaraITAdmin");
				userOtp = userOtpRepository.save(userOtp);
				log.info("UserOTP:" + userOtp);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param userId
	 * @param enteredOtp
	 * @return
	 */
	public ClientUser validateOtp(String contactNumber, Long enteredOtp) {
		String otpNumber = contactNumber.replaceAll("-", "");
		Optional<UserOTP> objUserOtp = userOtpRepository.findByUserId(otpNumber);
		if (!objUserOtp.isEmpty()) {
			UserOTP userOtp = objUserOtp.get();
			long sentMin = userOtp.getOtpSentTime().getTime()/60000;
			long currentMin = new Date().getTime()/60000;
			long minDiff = currentMin - sentMin;
			
			log.info("MIn : " + sentMin + "," + currentMin + "," + minDiff);
			if (minDiff > 3) {
				throw new BadRequestException("OTP Time is elapsed. Please relogin.");
			}
			
			log.info("OTP: " + userOtp.getOtpCode() + "," + enteredOtp);
			if (minDiff < 3 && userOtp.getOtpCode().longValue() == enteredOtp.longValue()) {
				log.info("OTP matched");
				
				// Get Client User
				ClientUser clientUser = clientUserService.getClientUserByContactNumber (contactNumber);
				return clientUser;
			} 
			throw new BadRequestException("OTP mismatched. Please try again.");
		}
		return null;
	}
	
	/**
	 * API for Dashboard
	 * Fetch CLASS_ID values from USERPROFILE by passing Logged in USR_ID
	 * @return
	 */
	public List<Long> findClassByUserId(String userProfileId) {
		List<Long> classIds = userProfileRepository.findClassByUserId(userProfileId);
		return classIds;
	}
	
	/**
	 * createUserProfile
	 * @param newUserProfile
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UserProfile createUserProfile (AddUserProfile newUserProfile, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		UserProfile userProfile = userProfileRepository.findByUserId(newUserProfile.getUserId()).orElse(null);
		log.info("userProfile : " + userProfile);
		if (userProfile != null && userProfile.getDeletionIndicator() != null && userProfile.getDeletionIndicator() == 0) {
			throw new BadRequestException("The given UserProfile ID : " + userProfile.getUserId() + " already exists.");
		}
		
		UserProfile dbUserProfile = new UserProfile();
		BeanUtils.copyProperties(newUserProfile, dbUserProfile, CommonUtils.getNullPropertyNames(newUserProfile));
		dbUserProfile.setPassword(PasswordEncoder.encodePassword(newUserProfile.getPassword()));
		dbUserProfile.setDeletionIndicator(0L);
		dbUserProfile.setCreatedBy(loginUserID);
		dbUserProfile.setUpdatedBy(loginUserID);
		dbUserProfile.setCreatedOn(new Date());
		dbUserProfile.setUpdatedOn(new Date());
		return userProfileRepository.save(dbUserProfile);
	}
	
	/**
	 * updateUserProfile
	 * @param userProfileId
	 * @param loginUserId 
	 * @param updateUserProfile
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UserProfile updateUserProfile (String userProfileId, String loginUserID, UpdateUserProfile updateUserProfile) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("Before save -----> 1: " + updateUserProfile.getPassword());
		UserProfile dbUserProfile = getUserProfile(userProfileId);
		String dbUserProfilePassword = dbUserProfile.getPassword();
		BeanUtils.copyProperties(updateUserProfile, dbUserProfile, CommonUtils.getNullPropertyNames(updateUserProfile));
		if(updateUserProfile.getPassword() != null){
			boolean isPasswordSame = passwordEncoder.matches(updateUserProfile.getPassword(), dbUserProfilePassword);
			//if (!updateUserProfile.getPassword().equalsIgnoreCase(dbUserProfilePassword)) {
			if(!isPasswordSame){
				dbUserProfile.setPassword(PasswordEncoder.encodePassword(updateUserProfile.getPassword()));
				log.info("--Password reset-----> : " + dbUserProfile);
			} else {
				dbUserProfile.setPassword(PasswordEncoder.encodePassword(dbUserProfile.getPassword()));
				log.info("--Old---Password--maintains---> : " + dbUserProfile);
			}
		}
		dbUserProfile.setUpdatedBy(loginUserID);
		dbUserProfile.setUpdatedOn(new Date());
		
		log.info("save -----> 2: " + dbUserProfile);
		return userProfileRepository.save(dbUserProfile);
	}
	
	/**
	 * deleteUserProfile
	 * @param loginUserID 
	 * @param userProfileCode
	 */
	public void deleteUserProfile (String userProfileId, String loginUserID) {
		UserProfile userProfile = getUserProfile(userProfileId);
		if (userProfile != null) {
			userProfile.setDeletionIndicator(1L);
			userProfile.setUpdatedBy(loginUserID);
			userProfile.setUpdatedOn(new Date());
			userProfileRepository.save(userProfile);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + userProfileId);
		}
	}
	
	/**
	 * sendOTP
	 * @param contactNumber
	 * @return
	 */
	public boolean sendOTP (String contactNumber) {
		ClientUser clientUser = clientUserService.getClientUserByContactNumber(contactNumber);
		if (clientUser != null) {
			// do Send OTP to Client
			return doSendOtp(contactNumber, clientUser.getFirstNameLastName());
		}
		return false;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public boolean clientUserEmailOTP (String emailId) {
		try {
			ClientUser clientUser = clientUserService.getClientUserByEmailId(emailId);
			if (clientUser == null) {
				throw new BadRequestException("Invalid EmailId : " + emailId);
			}
			
			log.info("clientUser : " + clientUser.getFirstName());
			boolean isEmailSent = doSendEmailOtpForClientUser(emailId, clientUser.getFirstName(), emailId);
			return isEmailSent;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public boolean emailOTP (String userId) {
		UserProfile userProfile = getUserProfile(userId);
		if (userProfile == null) {
    		throw new BadRequestException("Invalid UserId : " + userId);
    	}
		
		boolean isEmailSent = doSendEmailOtp(userId, userProfile.getFirstName(), userProfile.getEmailId());
		if (isEmailSent) {
			userProfile.setReferenceField10("OTP SENT");
		}
		return isEmailSent;
	}

	/**
	 * 
	 * @param userId
	 * @param otp
	 * @param isFromClientUser 
	 * @return
	 */
	public UserProfile verifyEmailOTP(String userIdOrEmailId, Long otp) {
		Optional<UserOTP> objUserOtp = userOtpRepository.findByUserId(userIdOrEmailId);
		Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(userIdOrEmailId);
		UserProfile userProfile = userProfileOpt.get();

		if (userProfile.getEmailId().equalsIgnoreCase(EMAIL_ID_TO_SKIP_OTP_VALIDATION)) {
			return userProfile;
		}
		
		if (!objUserOtp.isEmpty()) {
			UserOTP userOtp = objUserOtp.get();
			log.info("OTP Sent: " + userOtp);
			
			long sentMin = userOtp.getOtpSentTime().getTime() / 60000;
			long currentMin = new Date().getTime() / 60000;
			long minDiff = currentMin - sentMin;
			
			log.info("MIn : " + sentMin + "," + currentMin + "," + minDiff);
			if (minDiff > 20) {
				userOtpRepository.delete(userOtp);
				throw new BadRequestException("OTP Time is elapsed. Please relogin.");
			}
			
			if (userOtp.getOtpCode().longValue() == otp.longValue()) {
				log.info("OTP matched");
				
				if (skipUserId (userIdOrEmailId)) {
					userProfile.setReferenceField8("-1");
					userProfile.setReferenceField9(null);
					userProfile.setReferenceField10("0");
					return userProfile;
				} 
				
				
				Long amPM = getDayAndAMPM(); 				//ref_field_9
				Long weekNumber = getWeekOfTheYear ();
				TimeTicketNotification timeTicketNotification = 
						ttNotificationRepository.findByTimeKeeperCodeAndWeekOfYear(userIdOrEmailId, weekNumber);
				log.info("----------> timeTicketNotification : " + timeTicketNotification);
				if (!userProfileOpt.isEmpty() && timeTicketNotification != null) {
					// EKD / MA
//					if (userIdOrEmailId.equalsIgnoreCase("EKD") || userIdOrEmailId.equalsIgnoreCase("MA")) {
						if (timeTicketNotification.getTimeKeeperHours() != null) {
							userProfile.setReferenceField8(String.valueOf(timeTicketNotification.getTimeKeeperHours()));
						}
						
						userProfile.setReferenceField9(String.valueOf(amPM));
						userProfile.setReferenceField10(String.valueOf(timeTicketNotification.getComplianceStatus()));
//					}
					return userProfile;
				} else {
					userProfile.setReferenceField8("0");
					userProfile.setReferenceField9(String.valueOf(amPM));
					userProfile.setReferenceField10("1");
					return userProfile;
				}
			} 
			throw new BadRequestException("OTP mismatched. Please try again.");
		}
		return userProfile;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	private boolean skipUserId (String userId) {
		List<String> userIds = Arrays.asList("JI","MW","CR","MEA","SV","SN","REC","DEC","SS","JD","JN","DD","GV");
		log.info(userId + "--->" + userIds.contains(userId.toUpperCase()));
		return userIds.contains(userId.toUpperCase());
	}
	
	/**
	 * verifyEmailOTPForAppUser
	 * @param emailId
	 * @param otp
	 * @return
	 */
	public ClientUser verifyEmailOTPForAppUser (String emailId, Long otp) {
		Optional<UserOTP> objUserOtp = userOtpRepository.findByUserId(emailId);
		if (!objUserOtp.isEmpty()) {
			UserOTP userOtp = objUserOtp.get();
			log.info("OTP Sent: " + userOtp);
			
			long sentMin = userOtp.getOtpSentTime().getTime()/60000;
			long currentMin = new Date().getTime()/60000;
			long minDiff = currentMin - sentMin;
			
			log.info("MIn : " + sentMin + "," + currentMin + "," + minDiff);
			if (minDiff > 20) {
				userOtpRepository.delete(userOtp);
				throw new BadRequestException("OTP Time is elapsed. Please relogin.");
			}
			
			if (emailId.equalsIgnoreCase(EMAIL_ID_TO_SKIP_OTP_VALIDATION)) {
				ClientUser clientUser = clientUserService.getClientUserByEmailId(emailId);
				log.info("clientUser :" + clientUser);
				return clientUser;
			}
			if (userOtp.getOtpCode().longValue() == otp.longValue()) {
				log.info("OTP matched");
				
				// Get User
				ClientUser clientUser = clientUserService.getClientUserByEmailId(emailId);
				log.info("clientUser :" + clientUser);
				return clientUser;
			} 
			throw new BadRequestException("OTP mismatched. Please try again.");
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	private Long getDayAndAMPM () {
		Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        log.info ("Day of week in number:" + dayOfWeek);
        
        SimpleDateFormat formatTime = new SimpleDateFormat("hh.mm aa");
        String time = formatTime.format(date);
        
        String amOrPM = time.substring(time.indexOf('M') - 1, time.indexOf('M') + 1);
        log.info ("amOrPM : " + amOrPM);
        
        time = time.substring(0, time.indexOf(" "));
        double time1 = Double.valueOf(time).doubleValue();
        if (dayOfWeek == 2) {
        	 if (amOrPM.equalsIgnoreCase("AM")) {
             	if (time1 >= 5.00 || time1 <= 12.59) {
             		log.info ("AM---> : " + 0);
                 	return 0L;
                 } else {
                 	log.info ("AM---> : " + 1);
                 	return 1L;
                 }
             } else  if (amOrPM.equalsIgnoreCase("PM")) {
             	if (time1 >= 1.00 || time1 <= 5.59) {
             		log.info ("PM : " + 0);
                 	return 0L;
                 } else {
                 	log.info ("AM---> : " + 1);
                 	return 1L;
                 }
             } 
        } else {
        	log.info("It is NOT MONDAY---> : " + dayOfWeek);
        	return null;
        }
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	private Long getWeekOfTheYear() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        log.info("weekNumber---##----> " + weekNumber);
        long wkNumber = (weekNumber - 1);
        return wkNumber;
    }
}
