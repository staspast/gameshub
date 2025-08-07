package com.wsiz.gameshub.task;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wsiz.gameshub.service.NotificationEnquiryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
