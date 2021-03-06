package com.wsiz.gameshub.controller;

import com.wsiz.gameshub.request.AddNotificationEnquiryRequest;
import com.wsiz.gameshub.response.ObjectCreatedResponse;
import com.wsiz.gameshub.service.NotificationEnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationEnquiryController {

    private final NotificationEnquiryService notificationEnquiryService;

    @PostMapping("/add")
    public ResponseEntity<ObjectCreatedResponse> addNotificationEnquiry(@Valid @RequestBody AddNotificationEnquiryRequest request){
        notificationEnquiryService.addNotificationEnquiry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ObjectCreatedResponse("OK"));
    }
}
