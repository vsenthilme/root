package com.iweb2b.api.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.portal.model.consignment.entity.PiecesDetailsEntity;

@Repository
@Transactional
public interface PiecesDetailRepository extends JpaRepository<PiecesDetailsEntity, Long>{

    public List<PiecesDetailsEntity> findAll();
    public List<PiecesDetailsEntity> findByConsignmentId(Long consignmentId);

    @Query(value = "SELECT \r\n" +
            "max(pieces_id)+1 piecesId \r\n" +
            "FROM tblpiecesdetail ", nativeQuery = true)
    public Long findPiecesId ();
}