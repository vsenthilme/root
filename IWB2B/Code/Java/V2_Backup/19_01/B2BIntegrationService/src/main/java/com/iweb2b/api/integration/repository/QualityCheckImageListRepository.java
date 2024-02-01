package com.iweb2b.api.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.integration.model.consignment.entity.QualityCheckImageListEntity;

@Repository
@Transactional
public interface QualityCheckImageListRepository extends JpaRepository<QualityCheckImageListEntity, Long> {

}