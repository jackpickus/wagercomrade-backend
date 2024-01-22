package com.jackbets.mybets.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

public class Response {
    
    private @Getter @Setter Long wagerIdNumber;
    private @Getter @Setter String statusMessage;
    private @Getter @Setter Instant betTimeStamp;

    public Response(Long id, String statusMessage, Instant betTimeStamp) {
        this.wagerIdNumber= id;
        this.statusMessage = statusMessage;
        this.betTimeStamp = betTimeStamp;
    }

}
