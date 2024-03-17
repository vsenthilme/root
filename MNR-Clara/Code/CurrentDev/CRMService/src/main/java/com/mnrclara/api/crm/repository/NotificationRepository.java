package com.mnrclara.api.crm.repository;

import com.mnrclara.api.crm.model.Notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {

    List<Notification> findByUserIdOrUserIdIsNull(String userId);

    @Modifying
    @Query("update Notification n set n.status = true where n.userId = :userId")
    void updateStatus(@Param("userId") String userId);
    
    @Modifying
    @Query("update Notification n set n.status = true where n.userId = :userId and n.id = :id")
    void updateStatusById(@Param("userId") String userId, @Param("id") Long id);
}
