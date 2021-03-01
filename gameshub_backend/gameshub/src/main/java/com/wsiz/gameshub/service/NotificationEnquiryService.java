package com.wsiz.gameshub.service;

import com.wsiz.gameshub.exception.ObjectNotFoundException;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.entity.NotificationEnquiry;
import com.wsiz.gameshub.model.entity.NotificationEnquiryStatus;
import com.wsiz.gameshub.model.repository.GamesRepository;
import com.wsiz.gameshub.model.repository.NotificationEnquiryRepository;
import com.wsiz.gameshub.request.AddNotificationEnquiryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEnquiryService {

    private final NotificationEnquiryRepository notificationEnquiryRepository;
    private final GamesRepository gamesRepository;

    public void addNotificationEnquiry(AddNotificationEnquiryRequest request){

        Game game = gamesRepository.findById(request.getGameId()).orElseThrow(() -> new ObjectNotFoundException(request.getGameId(), "Requested game not found"));
        NotificationEnquiry notificationEnquiry = NotificationEnquiry.builder()
                .status(NotificationEnquiryStatus.NOT_SENT)
                .email(request.getEmail())
                .game(game)
                .priceGoal(request.getPriceGoal())
                .build();

        notificationEnquiryRepository.save(notificationEnquiry);
    }
}
