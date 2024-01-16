package com.iweb2b.api.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.portal.model.consignment.entity.OriginDetailsEntity;

@Repository
@Transactional
public interface OriginDetailRepository extends JpaRepository<OriginDetailsEntity, Long> {
	
    public List<OriginDetailsEntity> findAll();

    @Query(value = "SELECT \r\n" +
            "max(origin_id)+1 originId \r\n" +
            "FROM tblorigindetail ", nativeQuery = true)
    public Long findOriginId ();
    public OriginDetailsEntity findByConsignmentId(Long consignmentId);
}