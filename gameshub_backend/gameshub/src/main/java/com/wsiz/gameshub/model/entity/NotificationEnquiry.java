package com.wsiz.gameshub.model.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "NOTIFICATION_ENQUIRY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEnquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_enq_seq")
    @SequenceGenerator(name = "notification_enq_seq", sequenceName = "notification_enq_seq", allocationSize = 1)
    private Long id;

    @Column
    private String email;

    @OneToOne
    private Game game;

    @Column
    private BigDecimal priceGoal;

    @Enumerated(EnumType.STRING)
    @Column
    private NotificationEnquiryStatus status;
}
