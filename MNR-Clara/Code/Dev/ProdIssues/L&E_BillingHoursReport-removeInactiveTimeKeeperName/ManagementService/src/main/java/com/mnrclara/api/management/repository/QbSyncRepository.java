package com.mnrclara.api.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.management.model.qb.QBSync;

@Repository
public interface QbSyncRepository extends JpaRepository<QBSync, Long>, JpaSpecificationExecutor<QBSync> {

	QBSync findById(String id);

}