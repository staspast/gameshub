package com.wsiz.gameshub.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class AddNotificationEnquiryRequest {

    @NotNull(message = "Game id is required")
    private Long gameId;
    @Email(message = "Email should be valid")
    private String email;
    private BigDecimal priceGoal;
}
