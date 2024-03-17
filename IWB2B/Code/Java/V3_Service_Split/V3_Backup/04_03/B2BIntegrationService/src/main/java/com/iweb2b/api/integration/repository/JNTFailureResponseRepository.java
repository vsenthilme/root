package com.iweb2b.api.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.integration.model.consignment.entity.JNTFailureResponse;

@Repository
public interface JNTFailureResponseRepository extends JpaRepository<JNTFailureResponse, Long> {

    @Query(value = "SELECT top 1 \n" +
            "IW_FAILURE_RESPONSE_CODE \n" +
            "FROM tbljntfailureresponse \n" +
            "WHERE \n" +
            "JNT_FAILURE_RESPONSE_REASON = :jntFailureResponseDesc OR \n" +
            "JNT_FAILURE_RESPONSE_SUB_REASON = :jntFailureResponseDesc", nativeQuery = true)
    public String getFailureResponseCode(@Param("jntFailureResponseDesc") String jntFailureResponseDesc);

}

