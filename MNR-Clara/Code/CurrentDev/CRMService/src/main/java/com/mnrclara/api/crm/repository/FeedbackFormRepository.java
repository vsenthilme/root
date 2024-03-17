package com.mnrclara.api.crm.repository;

import com.mnrclara.api.crm.model.pcitform.FeedbackForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface FeedbackFormRepository extends JpaRepository<FeedbackForm, Long>, JpaSpecificationExecutor<FeedbackForm> {

    Optional<FeedbackForm> findByIntakeFormNumberAndDeletionIndicator(String intakeFormNumber, Long deletionIndicator);
}
