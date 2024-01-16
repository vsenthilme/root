package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.tasktype.TaskType;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, Long>{

	public List<TaskType> findAll();
	Optional<TaskType> findByTaskTypeCode (Long taskTypeId);
	
	// `LANG_ID`, `CLASS_ID`, `TASK_TYP_CODE`, `IS_DELETED`
	Optional<TaskType>
		findByLanguageIdAndClassIdAndTaskTypeCodeAndDeletionIndicator 
		(String languageId, Long classId, String TaskTypeCode, Long deletionIndicator);
}