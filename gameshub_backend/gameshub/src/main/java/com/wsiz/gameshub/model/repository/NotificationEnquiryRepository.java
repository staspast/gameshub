package com.wsiz.gameshub.model.repository;

import com.wsiz.gameshub.model.entity.NotificationEnquiry;
import com.wsiz.gameshub.model.entity.NotificationEnquiryStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationEnquiryRepository extends JpaRepository<NotificationEnquiry, Long> {

    @Query("SELECT n FROM NOTIFICATION_ENQUIRY n WHERE n.status = :status and n.game.priceFinal <= n.priceGoal ")
    List<NotificationEnquiry> findToSendByStatus(@Param("status") NotificationEnquiryStatus status, Pageable pageable);
}
