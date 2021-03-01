package com.wsiz.gameshub.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ObjectNotFoundException extends RuntimeException{

    private Long objectId;
    private String message;
}
