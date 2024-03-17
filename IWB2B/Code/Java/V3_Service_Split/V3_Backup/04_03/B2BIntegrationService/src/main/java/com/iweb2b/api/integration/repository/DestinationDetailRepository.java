package com.iweb2b.api.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.integration.model.consignment.entity.DestinationDetailEntity;

@Repository
@Transactional
public interface DestinationDetailRepository extends JpaRepository<DestinationDetailEntity, Long>{

    public List<DestinationDetailEntity> findAll();
    public DestinationDetailEntity findByConsignmentId(Long consignmentId);

    @Query(value = "SELECT \r\n" +
            "max(destination_id)+1 destinationId \r\n" +
            "FROM tbldestinationdetail ", nativeQuery = true)
    public Long findDestinationId ();

}