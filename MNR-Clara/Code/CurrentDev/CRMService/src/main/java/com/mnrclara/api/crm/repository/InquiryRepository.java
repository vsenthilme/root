package com.mnrclara.api.crm.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mnrclara.api.crm.model.timeticket.TimeTicketImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.crm.model.inquiry.Inquiry;

@Repository
@Transactional
public interface InquiryRepository extends JpaRepository<Inquiry, Long>, JpaSpecificationExecutor<Inquiry> {

	Optional<Inquiry> findByInquiryNumber(@Param("inquiryNumber") String inquiryNumber);
	List<Inquiry> findByClassId(Long classId);
	List<Inquiry> findByInquiryDateBetween(Date date1, Date date2);
	
	@Query(value = "SELECT * FROM tblinquiryid\r\n"
			+ " WHERE \r\n"
			+ " INQ_DATE BETWEEN :inqDate1 AND :inqDate2", nativeQuery = true)
	List<Inquiry> findByMultipleParams1(@Param ("inqDate1") String inqDate1,
										@Param ("inqDate2") String inqDate2);
	
	@Query(value = "SELECT * FROM tblinquiryid\r\n"
			+ " WHERE EMAIL_ID LIKE :email", nativeQuery = true)
	List<Inquiry> findByEmail(@Param ("email") String email);

	@Query(value = "SELECT * FROM tblinquiryid\r\n"
			+ " WHERE INQ_NO LIKE :inquiryNumber OR\r\n"
			+ " INQ_DATE BETWEEN :inqDate1 AND :inqDate2 OR\r\n"
			+ " FIRST_NM LIKE :firstName OR\r\n"
			+ " LAST_NM LIKE :lastName OR\r\n"
			+ " EMAIL_ID LIKE :email OR\r\n"
			+ " CONT_NO LIKE :contactNumber AND IS_DELETED = 0", nativeQuery = true)
	List<Inquiry> findByMultipleParams(@Param ("inquiryNumber") String inquiryNumber,
										@Param ("inqDate1") String inqDate1,
										@Param ("inqDate2") String inqDate2,
										@Param ("firstName") String firstName,
										@Param ("lastName") String lastName,
										@Param ("email") String email,
										@Param ("contactNumber") String contactNumber);
	
	@Query(value = "SELECT * FROM tblinquiryid \r\n"
			+ " WHERE DATE(INQ_DATE) = :inqDate AND \r\n"
			+ " FIRST_NM = :firstName AND \r\n"
			+ " LAST_NM = :lastName AND \r\n"
			+ " EMAIL_ID = :email AND \r\n"
			+ " CONT_NO = :contactNumber "
			+ " AND IS_DELETED = 0", nativeQuery = true)
	public Inquiry findDuplicateEntry(@Param ("inqDate") String inqDate,
										@Param ("firstName") String firstName,
										@Param ("lastName") String lastName,
										@Param ("email") String email,
										@Param ("contactNumber") String contactNumber);

	@Query(value = "select mt.tk_code as tkCode, round(sum(mt.TIME_TICKET_HRS),2) as timeTicketHours,up.fst_nm as timeKeeperName \n" +
			" from tblmattertimeticketid mt \n" +
			" join tbluserprofileid up on mt.TK_CODE = up.usr_id \n" +
			"where mt.TIME_TICKET_DATE between :fromDate and :toDate AND mt.IS_DELETED = 0 \n" +
			"group by mt.TK_CODE,up.fst_nm", nativeQuery = true)
	public List<TimeTicketImpl> getTimeTicketFrNotification(@Param ("fromDate") Date fromDate,
															@Param ("toDate") Date toDate);

	@Query(value = "select ASSIGNED_USR_ID from tblinquiryid \n" +
			"where INQ_NO = :inquiryNumber ", nativeQuery = true)
	public String getAssignedUserId(@Param ("inquiryNumber") String inquiryNumber);
}
