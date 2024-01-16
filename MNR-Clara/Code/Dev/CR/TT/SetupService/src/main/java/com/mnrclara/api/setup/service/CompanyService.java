package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.company.AddCompany;
import com.mnrclara.api.setup.model.company.Company;
import com.mnrclara.api.setup.model.company.UpdateCompany;
import com.mnrclara.api.setup.repository.CompanyRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CompanyService {
	
	@Autowired
	private CompanyRepository companyRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<Company> getCompanies () {
		List<Company> companyList =  companyRepository.findAll();
		companyList = companyList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return companyList;
	}
	
	/**
	 * getCompany
	 * @param companyId
	 * @return
	 */
	public Company getCompany (String companyId) {
		Company company = companyRepository.findByCompanyId(companyId);
		if (company != null && company.getDeletionIndicator() == 0) {
			return company;
		} else {
			throw new BadRequestException("The given Company ID : " + companyId + " doesn't exist.");
		}
	}
	
	/**
	 * createCompany
	 * @param newCompany
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Company createCompany (AddCompany newCompany, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<Company> company = 
				companyRepository.findByCompanyIdAndLanguageIdAndDeletionIndicator (	
					newCompany.getCompanyId(), 
					newCompany.getLanguageId(), 
					0L);
		log.info(" company : " + company);
		if (!company.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		Company dbCompany = new Company();
		BeanUtils.copyProperties(newCompany, dbCompany);
		dbCompany.setDeletionIndicator(0L);
		dbCompany.setCreatedBy(loginUserID);
		dbCompany.setUpdatedBy(loginUserID);
		return companyRepository.save(dbCompany);
	}
	
	/**
	 * updateCompany
	 * @param companyId
	 * @param loginUserId 
	 * @param updateCompany
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Company updateCompany (String companyId, String loginUserID, UpdateCompany updateCompany) 
			throws IllegalAccessException, InvocationTargetException {
		Company dbCompany = getCompany(companyId);
		BeanUtils.copyProperties(updateCompany, dbCompany, CommonUtils.getNullPropertyNames(updateCompany));
		dbCompany.setUpdatedBy(loginUserID);
		dbCompany.setUpdatedOn(new Date());
		return companyRepository.save(dbCompany);
	}
	
	/**
	 * deleteCompany
	 * @param loginUserID 
	 * @param companyCode
	 */
	public void deleteCompany (String companyModuleId, String loginUserID) {
		Company company = getCompany(companyModuleId);
		if ( company != null) {
			company.setDeletionIndicator(1L);
			company.setUpdatedBy(loginUserID);
			companyRepository.save(company);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + companyModuleId);
		}
	}
}
