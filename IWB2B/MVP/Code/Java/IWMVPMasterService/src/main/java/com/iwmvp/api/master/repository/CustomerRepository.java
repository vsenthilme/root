package com.iwmvp.api.master.repository;

import com.iwmvp.api.master.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer,String>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByCompanyIdAndCustomerIdAndLanguageIdAndDeletionIndicator(String companyId, Long customerId, String languageId,Long deletionIndicator);

    Optional<Customer> findByCustomerIdAndDeletionIndicator(Long customerId, long l);
}
