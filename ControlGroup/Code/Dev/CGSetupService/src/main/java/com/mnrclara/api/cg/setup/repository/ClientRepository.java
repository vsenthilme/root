package com.mnrclara.api.cg.setup.repository;


import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, String>, JpaSpecificationExecutor<Client> {

    Optional<Client> findByCompanyIdAndLanguageIdAndClientIdAndDeletionIndicator(
            String companyId, String languageId, Long clientId, Long deletionIndicator);

    @Query(value = "select tl.client_id AS clientId, tl.client_nm AS description \n" +
            "from tblclient tl \n" +
            "WHERE \n" +
            "tl.client_id IN (:clientId) and tl.c_id IN (:companyId) and tl.lang_id IN (:languageId) and \n " +
            "tl.is_deleted = 0 and tl.status_id = 0", nativeQuery = true)
    IKeyValuePair getClientIdAndDescription(@Param(value = "clientId") Long clientId,
                                            @Param(value = "companyId") String companyId,
                                            @Param(value = "languageId") String languageId);
}
