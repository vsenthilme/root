package com.mnrclara.api.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.management.model.clientemail.ClientEmail;

@Repository
public interface ClientEmailRepository extends JpaRepository<ClientEmail, Long> {

	public List<ClientEmail> findAll();

	public List<ClientEmail> findByEmailId(Long emailId);

	@Query(value = "SELECT * FROM tblclientemailid WHERE MAIL_TYP LIKE :mailType OR\r\n"
			+ " FROM_ADD LIKE :from OR TO_ADD LIKE :to OR\r\n"
			+ " EMAIL_DATE_TIME BETWEEN :sEmailDate AND :eEmailDate AND IS_DELETED = 0", nativeQuery = true)
	public List<ClientEmail> findByMultipleParams(@Param("mailType") String mailType,
			@Param("sEmailDate") String sEmailDate, @Param("eEmailDate") String eEmailDate, @Param("from") String from,
			@Param("to") String to);

}