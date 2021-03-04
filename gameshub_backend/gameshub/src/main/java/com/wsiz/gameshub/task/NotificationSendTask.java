package com.wsiz.gameshub.task;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.entity.NotificationEnquiry;
import com.wsiz.gameshub.model.entity.NotificationEnquiryStatus;
import com.wsiz.gameshub.model.repository.NotificationEnquiryRepository;
import com.wsiz.gameshub.service.NotificationEnquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationSendTask {

    private final NotificationEnquiryService notificationEnquiryService;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @Scheduled(fixedDelay = 10000)
    public void sendGameNotifications(){
        notificationEnquiryService.sendNotificationEnquiries();
    }
}
