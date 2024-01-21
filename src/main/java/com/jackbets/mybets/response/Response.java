package com.jackbets.mybets.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

public class Response {
    
    private @Getter @Setter Long wagerIdNumber;
    private @Getter @Setter String statusMessage;
    private @Getter @Setter LocalDateTime betTimeStamp;

    public Response(Long id, String statusMessage, LocalDateTime betTimeStamp) {
        this.wagerIdNumber= id;
        this.statusMessage = statusMessage;
        this.betTimeStamp = betTimeStamp;
    }

}
