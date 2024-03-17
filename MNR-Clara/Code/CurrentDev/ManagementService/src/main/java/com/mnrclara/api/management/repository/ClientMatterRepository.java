package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.clientmatter.ClientMatter;

@Repository
@Transactional
public interface ClientMatterRepository extends JpaRepository<ClientMatter, Long>, JpaSpecificationExecutor<ClientMatter> {

	public List<ClientMatter> findAll();

	Optional<ClientMatter> findByMatterNumber(Long matterNumber);
}