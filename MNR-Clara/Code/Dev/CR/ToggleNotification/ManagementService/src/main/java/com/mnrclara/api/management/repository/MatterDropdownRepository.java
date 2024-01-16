package com.mnrclara.api.management.repository;

import com.mnrclara.api.management.model.dto.MatterNumberDropDown;
import com.mnrclara.api.management.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface MatterDropdownRepository extends PagingAndSortingRepository<MatterNumberDropDown, Long>,
        JpaSpecificationExecutor<MatterNumberDropDown>,
        DynamicNativeQuery, StreamableJpaSpecificationRepository<MatterNumberDropDown> {


}