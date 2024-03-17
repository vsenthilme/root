package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import com.mnrclara.api.management.model.clientemail.ClientEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.mattertask.MatterTask;

@Repository
@Transactional
public interface MatterTaskRepository extends JpaRepository<MatterTask,Long>,JpaSpecificationExecutor<MatterTask> {

	public List<MatterTask> findAll();
	public Optional<MatterTask> findByTaskNumber(String taskNumber);


}