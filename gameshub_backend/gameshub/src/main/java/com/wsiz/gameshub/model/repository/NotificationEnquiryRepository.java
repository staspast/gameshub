package com.wsiz.gameshub.model.repository;

import com.wsiz.gameshub.model.entity.NotificationEnquiry;
import com.wsiz.gameshub.model.entity.NotificationEnquiryStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationEnquiryRepository extends JpaRepository<NotificationEnquiry, Long> {

    List<NotificationEnquiry> findByStatus(NotificationEnquiryStatus status, Pageable pageable);
}
