package com.wsiz.gameshub.service;

import com.wsiz.gameshub.exception.ObjectNotFoundException;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.entity.NotificationEnquiry;
import com.wsiz.gameshub.model.entity.NotificationEnquiryStatus;
import com.wsiz.gameshub.model.repository.GamesRepository;
import com.wsiz.gameshub.model.repository.NotificationEnquiryRepository;
import com.wsiz.gameshub.request.AddNotificationEnquiryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationEnquiryService {

    private final NotificationEnquiryRepository notificationEnquiryRepository;
    private final GamesRepository gamesRepository;
    private final MailSenderService mailSenderService;

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

    public void sendNotificationEnquiries(){
        List<NotificationEnquiry> notificationEnquiries = notificationEnquiryRepository.findByStatus(NotificationEnquiryStatus.NOT_SENT, PageRequest.of(0, 10));

        notificationEnquiries.forEach(notificationEnquiry -> {
            Game game = notificationEnquiry.getGame();

            if(game.getPriceFinal() != null && game.getPriceFinal().compareTo(notificationEnquiry.getPriceGoal()) <= 0){
                mailSenderService.sendEmail(notificationEnquiry.getEmail(), "Game discount", "Hello, game that you are interested in " + game.getName() + " is on discount currenct price " + game.getPriceFinal() + " at " + game.getMarketplaceName());
                notificationEnquiry.setStatus(NotificationEnquiryStatus.SENT);
            }
        });
    }
}
