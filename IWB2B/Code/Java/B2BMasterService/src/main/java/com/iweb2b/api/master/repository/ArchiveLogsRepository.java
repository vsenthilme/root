package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.archivelogs.ArchiveLogs;

@Repository
public interface ArchiveLogsRepository extends JpaRepository<ArchiveLogs, Long>{

	public List<ArchiveLogs> findAll();

	public Optional<ArchiveLogs> findByPartnerIdAndDeletionIndicator(String partnerId, long l);
}