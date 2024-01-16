package com.iwmvp.api.master.service;

import com.iwmvp.api.master.controller.exception.BadRequestException;

import com.iwmvp.api.master.model.auth.AuthToken;
import com.iwmvp.api.master.model.customer.*;
import com.iwmvp.api.master.model.user.User;
import com.iwmvp.api.master.repository.CustomerRepository;
import com.iwmvp.api.master.repository.Specification.CustomerSpecification;
import com.iwmvp.api.master.repository.UserRepository;
import com.iwmvp.api.master.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
//import com.iwmvp.api.master.service.AuthTokenService;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    AuthTokenService authTokenService;
    @Autowired
    private SetupService setupService;

    /**
     * getCustomers
     * @return
     */
    public List<Customer> getCustomers(){
      List<Customer> customerList=customerRepository.findAll();
      customerList = customerList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
      return customerList;
    }
    /**
     * getCustomer
     *
     * @param customerId
     * @return
     */
    public Customer getCustomer(Long customerId,String companyId,String languageId){
        Optional<Customer> dbCustomer=
                     customerRepository.findByCompanyIdAndCustomerIdAndLanguageIdAndDeletionIndicator(
                             companyId,
                             customerId,
                             languageId,
                             0l
                     );
        if (dbCustomer.isEmpty()) {
            throw new BadRequestException("The given values : " +
                      "customerId - " + customerId +
                    "doesn't exist.");
        }
        return dbCustomer.get();
    }

    public Customer getCustomerById(Long customerId){
        Optional<Customer> dbCustomer=
                customerRepository.findByCustomerIdAndDeletionIndicator(
                        customerId,
                        0l
                );
        if (dbCustomer.isEmpty()) {
            throw new BadRequestException("The given values : " +
                    "customerId - " + customerId +
                    "doesn't exist.");
        }
        return dbCustomer.get();
    }
    /**
     * createCustomer
     * @param newCustomer
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Customer createCustomer (AddCustomer newCustomer, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        Customer dbCustomer = new Customer();
        BeanUtils.copyProperties(newCustomer, dbCustomer, CommonUtils.getNullPropertyNames(newCustomer));
        dbCustomer.setCustomerType("LEAD");
        dbCustomer.setDeletionIndicator(0L);
        dbCustomer.setCreatedBy(loginUserID);
        dbCustomer.setUpdatedBy(loginUserID);
        dbCustomer.setCreatedOn(new Date());
        dbCustomer.setUpdatedOn(new Date());
        //New User Creation
        createLoginUser(dbCustomer,loginUserID);
        return customerRepository.save(dbCustomer);
    }
    /**
     * approveCustomer-B2B
     * @param loginUserID
     * @param customerId
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void approveCustomer(Long  customerId, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
        Customer dbCustomer = getCustomerById(customerId);
        Customer pushCustomer = setupService.createCustomer(dbCustomer,loginUserID, authTokenForSetupService.getAccess_token());
        dbCustomer.setApprovedBy(loginUserID);
        dbCustomer.setApprovedOn(new Date());
        customerRepository.save(dbCustomer);
    }
    /**
     * updateCustomer
     * @param loginUserID
     * @param customerId
     * @param updateCustomer
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Customer updateCustomer(Long  customerId,String companyId,String languageId, String loginUserID,
                                 UpdateCustomer updateCustomer)
            throws IllegalAccessException, InvocationTargetException {
        Customer dbCustomer = getCustomerById(customerId);
        BeanUtils.copyProperties(updateCustomer, dbCustomer, CommonUtils.getNullPropertyNames(updateCustomer));
        dbCustomer.setUpdatedBy(loginUserID);
        dbCustomer.setUpdatedOn(new Date());
        return customerRepository.save(dbCustomer);
    }
    /**
     * deleteCustomer
     * @param loginUserID
     * @param customerId
     */
    public void deleteCustomer(Long  customerId,String companyId,String languageId,String loginUserID) {
        Customer dbCustomer = getCustomerById(customerId);
        if ( dbCustomer != null) {
            dbCustomer.setDeletionIndicator(1L);
            dbCustomer.setUpdatedBy(loginUserID);
            customerRepository.save(dbCustomer);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + customerId);
        }
        //Delete LoginUser
        deleteLoginUser(dbCustomer,loginUserID);
    }
    //Find Customer
    public List<Customer> findCustomer(FindCustomer findCustomer) throws ParseException {

        CustomerSpecification spec = new CustomerSpecification(findCustomer);
        List<Customer> results = customerRepository.findAll(spec);
        return results;
    }
    //Create New Login USer
    public void createLoginUser(Customer dbCustomer,String loginUserID) throws BadRequestException{
        String dbUserName = userService.getUsr(dbCustomer.getUserName());
        if(dbUserName==null){
            User dbUser = new User();
            dbUser.setUsername(dbCustomer.getUserName());
            dbUser.setUserTypeId("Associate");
            if(dbCustomer.getPassword()!=null){
                dbUser.setPassword(dbCustomer.getPassword());
            }else{
                throw new BadRequestException("Password cannot be Null");
            }
            if(dbCustomer.getCustomerName()!=null){
                dbUser.setFirstname(dbCustomer.getCustomerName());
            }
            if(dbCustomer.getCity()!=null){
                dbUser.setCity(dbCustomer.getCity());
            }
            if(dbCustomer.getCountry()!=null){
                dbUser.setCountry(dbCustomer.getCountry());
            }
            if(dbCustomer.getEmailId()!=null){
                dbUser.setEmail(dbCustomer.getEmailId());
            }
            if(dbCustomer.getPhoneNo()!=null){
                dbUser.setPhoneNo(dbCustomer.getPhoneNo());
            }
            if(dbCustomer.getStatus()!=null){
                dbUser.setStatus(dbCustomer.getStatus());
            }
            dbUser.setDeletionIndicator(0L);
            dbUser.setCreatedOn(new Date());
            dbUser.setCreatedBy(loginUserID);
            dbUser.setUpdatedBy(loginUserID);
            dbUser.setUpdatedOn(new Date());
            userRepository.save(dbUser);
        }else{
            throw new BadRequestException("Username already Exist: " + dbCustomer.getUserName());
        }
    }
    public void deleteLoginUser(Customer dbCustomer, String loginUserID) throws EntityNotFoundException{
        User dbUser = userService.getUserName(dbCustomer.getUserName());
        if(dbUser !=null){
            userService.deleteUser(dbUser.getId(),loginUserID);
        }else {
            throw new EntityNotFoundException("Error in deleting Login User: " + dbCustomer.getUserName());
        }
    }
  }
