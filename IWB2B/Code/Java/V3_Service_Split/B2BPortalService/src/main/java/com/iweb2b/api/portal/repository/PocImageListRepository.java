package com.iweb2b.api.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.portal.model.consignment.entity.PocImageListEntity;

@Repository
@Transactional
public interface PocImageListRepository extends JpaRepository<PocImageListEntity, Long> {

}