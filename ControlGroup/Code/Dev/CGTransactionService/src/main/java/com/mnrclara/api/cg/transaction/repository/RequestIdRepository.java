package com.mnrclara.api.cg.transaction.repository;

import com.mnrclara.api.cg.transaction.model.Requestid.RequestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RequestIdRepository extends JpaRepository<RequestId,Long>, JpaSpecificationExecutor<RequestId> {


    List<RequestId> findByRequestIdAndDeletionIndicator(Long requestId, Long deletionIndicator);
    RequestId findByIdAndRequestIdAndDeletionIndicator(Long id,Long requestId, Long deletionIndicator);


}

