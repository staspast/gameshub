package com.wsiz.gameshub.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class AddNotificationEnquiryRequest {

    @NotNull(message = "Game id is required")
    private Long gameId;
    @Email(message = "Email should be valid")
    private String email;
    private BigDecimal priceGoal;
}
