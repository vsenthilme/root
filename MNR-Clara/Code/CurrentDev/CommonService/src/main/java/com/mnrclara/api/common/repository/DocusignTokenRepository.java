package com.mnrclara.api.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.common.model.docusign.DocusignToken;

@Repository
public interface DocusignTokenRepository extends JpaRepository<DocusignToken, Long>{

	Optional<DocusignToken> findByClientId(String clientId);
}